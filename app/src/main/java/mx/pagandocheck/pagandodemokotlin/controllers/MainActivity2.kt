package mx.pagandocheck.pagandodemokotlin.controllers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import mx.pagando.check.services.GetCardBrandCallback
import mx.pagando.check.services.GetOperationsCallback
import mx.pagando.check.services.InitKeysCallback
import mx.pagando.check.services.LoginCallback
import mx.pagando.check.services.LogoutCallback
import mx.pagando.check.services.MakePaymentCallback
import mx.pagando.check.services.ReadCardCallback
import mx.pagando.check.services.SelectAppCallback
import mx.pagando.check.services.SignatureCallback
import mx.pagando.check.services.TokenStatusCallback
import mx.pagando.check.services.models.CardBrand
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagando.check.services.models.TokenStatusOrder
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.NipPagandoView
import mx.pagandocheck.pagandodemokotlin.R
import java.io.IOException
import java.util.Calendar

private lateinit var checkServices: CheckServices
private val loginRequiredCodes = arrayListOf("0001", "0002", "0003", "1002", "400", "4001")

private var loginRequired : Boolean = true
private var initKeysRequired : Boolean = true

class MainActivity2 : AppCompatActivity() {
    private var isSandbox: Boolean = false

    private lateinit var instructionsText: TextView
    private lateinit var operationText: TextView
    private lateinit var amountText: TextView
    private lateinit var acceptButton: Button
    private lateinit var cancelButton: Button
    private lateinit var finishButton: Button
    private lateinit var nipPagandoView: NipPagandoView


    private lateinit var loginKey: String
    private lateinit var loginCookie: String
    private lateinit var amount: String
    private lateinit var operation: String
    private lateinit var orderId: String
    private lateinit var stationUrl: String
    private lateinit var stationToken: String
    private lateinit var idempotencyToken: String

    private lateinit var transactionDescription: String

    private var paymentResponse: PaymentResponse? = null

    private val SELL = "SELL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        instructionsText = findViewById(R.id.instructionsText)
        operationText = findViewById(R.id.operationText)
        amountText = findViewById(R.id.amountText)
        acceptButton = findViewById(R.id.acceptButton)
        cancelButton = findViewById(R.id.cancelButton)
        finishButton = findViewById(R.id.finishButton)
        nipPagandoView = findViewById(R.id.nipPagandoView)
        nipPagandoView.setVisibility(false)
        nipPagandoView.setTitle("Ingresa tu NIP")
        nipPagandoView.setActivity(this)
        transactionDescription = ""

        checkServices = CheckServices.getInstance(this)


        val intent = intent
        val extras = intent.extras
        isSandbox = extras?.getBoolean("isSandbox") ?: false
        loginKey = extras?.getString("loginKey") ?: ""
        loginCookie = extras?.getString("loginCookie") ?: ""
        orderId = extras?.getString("orderId") ?: ""
        amount = extras?.getString("amount") ?: "1"
        operation = extras?.getString("operation") ?: SELL

        stationUrl = extras?.getString("stationUrl") ?: ""
        stationToken = extras?.getString("stationToken") ?: ""

        idempotencyToken = extras?.getString("idempotencyToken") ?: ""
        instructionsText.text = "Sigue las instrucciones para completar la operación"
        amountText.text = amount.toString()
        acceptButton.setOnClickListener {
            Log.i("CLICK ACCEPT", "CLCIECK")

            instructionsText.text = "ESTABLECIENDO CONEXION"
            checkServices.cancelCardRead()

            if (loginRequired) {
                loginApi(loginKey, loginCookie)
                return@setOnClickListener
            }

            if (initKeysRequired){
                initKeys()
                return@setOnClickListener
            }

            if (idempotencyToken.isNotEmpty()) {
                getTokenInPotency(idempotencyToken)
                return@setOnClickListener
            }

            readCard()
        }
        cancelButton.setOnClickListener {
            finishAndRemoveTask()
        }
        finishButton.setOnClickListener {
            checkServices.cancelCardRead()
            setResult(Activity.RESULT_OK, intent)
            finishAndRemoveTask()
        }
    }

private fun setErrorResponse(errorResponse: ErrorResponse?) {
    checkServices = CheckServices.getInstance(this)
    runOnUiThread {
        instructionsText.text = " ${errorResponse?.code}:\n${errorResponse?.message}"
        cancelButton.text = "Cerrar"
        cancelButton.isVisible = true

        if (errorResponse != null) {
            if (loginRequiredCodes.contains(errorResponse.code)) {
                loginRequired = true
                logout()
            }

        }
    }
}
    private fun setExceptionResponse(exception: Exception) {
        checkServices = CheckServices.getInstance(this)
        runOnUiThread {
            val exceptionResponse = ErrorResponse()
            exceptionResponse.message = exception.message
            setErrorResponse(exceptionResponse)
        }
    }
private fun loginApi(key: String, cookie: String) {
    transactionDescription += "loginApi . "

    try {
        checkServices.loginApi(key, cookie, isSandbox, object : LoginCallback.Stub() {
            override fun onSuccessful() {
                loginRequired = false
                getOperations()
            }

            override fun onError(errorResponse: ErrorResponse?) {
                setErrorResponse(errorResponse)
            }
        })
    } catch (ex: Exception) {
        setExceptionResponse(ex)
    }
}
    private fun getOperations() {

        try {
            checkServices.getOperations(object : GetOperationsCallback.Stub() {
                override fun onSuccessful(terminalOperations: MutableList<String>?) {
                    initKeys()
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    setErrorResponse(errorResponse)
                }
            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }

    private fun initKeys() {

        try {
            checkServices.initKeys(object : InitKeysCallback.Stub() {
                override fun onSuccessful() {
                    initKeysRequired = false
                    if (idempotencyToken.isNotEmpty()) {
                        getTokenInPotency(idempotencyToken)
                        return
                    }

                    readCard()
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    setErrorResponse(errorResponse)
                }
            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }

    private fun readCard() {

        try {
            checkServices.readCard(nipPagandoView, amount, operation, object : ReadCardCallback.Stub() {
                override fun onSuccessful(typeCard: Int) {
                    getCardBrand()
                }

                override fun onMessage(message: String?) {
                    runOnUiThread {
                        instructionsText.text = message?.uppercase() ?: "ESPERE POR FAVOR"
                    }
                }

                override fun onActionNip(message: String?) {
                    runOnUiThread {
                        cancelButton.isVisible = false
                        instructionsText.text = "PROCESANDO"
                        amountText.text = ""
                        operationText.text = ""
                        nipPagandoView.proccessMessage(message)
                    }
                }

                override fun onApplicationSelection(
                    message: Array<out String>?, callback: SelectAppCallback?
                ) {
                    runOnUiThread {
                        AlertDialog.Builder(this@MainActivity2)
                            .setTitle("Seleccione una Tarjeta")
                            .setCancelable(false)
                            .setNegativeButton(
                                "Cancelar"
                            ) { dialog, which ->
                                dialog.dismiss()
                                callback!!.cancel()
                                checkServices.cancelCardRead()
                            }
                            .setItems(message) { dialog, which ->
                                dialog.dismiss()
                                callback!!.selectApp(which)
                            }
                            .show()
                    }
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    setErrorResponse(errorResponse)
                }

            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }

    private fun getCardBrand() {

        try {
            checkServices.getCardBrand(object : GetCardBrandCallback.Stub() {
                override fun onSuccessful(cardBrand: CardBrand?) {
                    if (cardBrand != null) {
                        idempotencyToken = cardBrand.idempotencyToken
                        makePayment()
                    } else {
                        runOnUiThread {
                            val nullCardBrand = ErrorResponse()
                            nullCardBrand.code = "43"
                            nullCardBrand.message = "Error al obtener Token de Idempotencia (cardBrand null)."
                            setErrorResponse(nullCardBrand)
                        }
                    }
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    setErrorResponse(errorResponse)
                }

            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }

    private fun saveIdempotencyToken(cardBrand: CardBrand) {
        makePayment()
    }

    private fun makePayment() {

        try {
            checkServices.makePayment(object : MakePaymentCallback.Stub() {
                override fun onPaymentSuccess(paymentResponse: PaymentResponse?) {
                    Log.i("initKeysRequired", paymentResponse?.mustReloadKeySoon.toString() + " " + paymentResponse?.mustReloadKeyNow  )
                    if (paymentResponse?.mustReloadKeySoon == true || paymentResponse?.mustReloadKeyNow == true){
                        initKeysRequired = true
                    }

                    setSuccessResponse(paymentResponse)
                }

                override fun onSignatureRequired() {
                    runOnUiThread {
                        Log.i("Signature", "REQUIERD")
                    }
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    setErrorResponse(errorResponse)
                }
            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }
    private fun getTokenInPotency(token: String) {

        try {
            checkServices.getTokenInPotency(token, object : TokenStatusCallback.Stub() {
                override fun onSuccessful(status: String?, order: TokenStatusOrder?) {
                    if (order != null) {
                        val paymentResponse = PaymentResponse()

                        //#region [setPaymentResponse]
                        paymentResponse.BIN = order.BIN
                        paymentResponse.AID = order.AID
                        paymentResponse.BIN8 = order.BIN8
                        paymentResponse.ARQC = order.ARQC
                        paymentResponse.last4 = order.last4
                        paymentResponse.brand = order.brand
                        paymentResponse.folio = order.folio
                        paymentResponse.amount = order.amount
                        paymentResponse.emvType = order.emvType
                        paymentResponse.employee = order.employee
                        paymentResponse.pinEntry = order.pinEntry
                        paymentResponse.authCode = order.authCode
                        paymentResponse.tipAmount = order.tipAmount
                        paymentResponse.status = order.status ?: status
                        paymentResponse.description = order.description
                        paymentResponse.accountType = order.accountType
                        paymentResponse.responseCode = order.responseCode
                        paymentResponse.operationType = order.operationType
                        paymentResponse.merchantIdCode = order.merchantIdCode
                        paymentResponse.transactionTime = order.transactionTime
                        paymentResponse.requireSignature = order.requireSignature
                        //#endregion

                    }
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    if (errorResponse != null) {
                        Log.e("GetTokenInPotencyError" , errorResponse.message)
                    }
                }

            })
        } catch (ex: Exception) {
            Log.e("GetTokenInPotencyError Exception" , ex.message.toString())
        }
    }

    private fun logout(){

        try {
            checkServices.logout(object : LogoutCallback.Stub(){
                override fun onSuccessful() {
                    //Do Nothing...
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    //Do Nothing...
                }
            })
        }catch(e: Exception){
            Log.e("LogoutError Exception", e.message.toString())
        }
    }
    private fun setSuccessResponse(paymentResponse: PaymentResponse?) {
        runOnUiThread {
            if (paymentResponse != null) {
                this@MainActivity2.paymentResponse = paymentResponse
                finishButton.isVisible = true
                cancelButton.isVisible = false
                instructionsText.text = "Aprobada"


            }
        }
    }
}
