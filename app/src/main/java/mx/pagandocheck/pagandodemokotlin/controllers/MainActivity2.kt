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
private val loginRequiredCodes = arrayListOf("0001", "0002", "0003", "1002", "400", "4001") // codigos Enerser

private var loginRequired : Boolean = true // verifica si se necesita realizar login
private var initKeysRequired : Boolean = true // verifica si se necesita realizar inicializacion de llaves

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
    // Datos propios
    private lateinit var stationUrl: String
    private lateinit var stationToken: String
    // IdInPotency
    private lateinit var idempotencyToken: String

    private lateinit var transactionDescription: String

    private var paymentResponse: PaymentResponse? = null
    private var errorResponse: ErrorResponse? = null

    private val CHECK_IN = "CHECK_IN"
    private val SELL = "SELL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // Enlazamos los elementos de la vista con sus correspondientes IDs
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
        loginKey = extras?.getString("loginKey") ?: "D0zJIQ9Zxbz4vu8BNh6R"
        loginCookie = extras?.getString("loginCookie") ?: "68f8bfab0c5e1a91b57358370512e08bd4f711b9"
        orderId = extras?.getString("orderId") ?: ""
        amount = extras?.getString("amount") ?: "1"
        operation = extras?.getString("operation") ?: SELL

        stationUrl = extras?.getString("stationUrl") ?: ""
        stationToken = extras?.getString("stationToken") ?: ""

        idempotencyToken = extras?.getString("idempotencyToken") ?: ""
        // Ejemplo de cómo podrías usar los elementos enlazados
        instructionsText.text = "Sigue las instrucciones para completar la operación"
        amountText.text = amount.toString()
        acceptButton.setOnClickListener {
            Log.i("CLICK ACCEPT", "CLCIECK")

            instructionsText.text = "ESTABLECIENDO CONEXION"
            //7__
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
            Log.i("Cancelado", "Cancelado por el usuario")
//            val intent = packageManager.getLaunchIntentForPackage("com.enerfueltech.mpos")
//
//            val failedPaymentResponse = FailedPaymentResponse()
//
//            if (errorResponse != null) {
//                failedPaymentResponse.responseCode = errorResponse?.code
//                failedPaymentResponse.description = errorResponse?.message
//            } else {
//                failedPaymentResponse.responseCode = "911"
//                failedPaymentResponse.description = "CANCELADO POR EL USUARIO"
//            }
//
//            transactionDescription += "exitApp (${getTransactionTime()})."
//            failedPaymentResponse.transactionDescription = transactionDescription
//
//            checkServices.cancelCardRead()
//
//            val result = Gson().toJson(failedPaymentResponse).toString()
//            intent?.putExtra("Result", result)
//
//            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
        finishButton.setOnClickListener {
            Log.i("FinishBUtton", "Button terminar")
//            val result = Gson().toJson(paymentResponse).toString()

            checkServices.cancelCardRead()

//            val intent = packageManager.getLaunchIntentForPackage("com.enerfueltech.mpos")
//            intent?.putExtra("Result", result)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

//    private var btnLoginApi: Button? = null
//    private var btnLogOut: Button? = null
//    private var btnGetOrganizations: Button? = null
//    private var btnChangeOrganization: Button? = null
//    private var btnOrganizationTransaction: Button? = null
//    private var btnGetOperations: Button? = null
//    private var btnSetSignature: Button? = null
//    private var btnPrintBitmap: Button? = null
//    private var btnPrintStr: Button? = null
//    private var btnGetPayLaterPromotions: Button? = null
//    private var btnGetPromotions: Button? = null
//    private var btnTransactionByFolio: Button? = null
//    private var btnGetPendingCheckouts: Button? = null
//    private var btnDoCheckout: Button? = null
//    private var btnReadCard: Button? = null
//    private var btnInitKeys: Button? = null
//    private var btnTerminalClosure: Button? = null
//    private var btnPreTerminalClosure: Button? = null
//    private var btnGenerateTerminalClosure: Button? = null
//    private var btnTerminalClosureByID: Button? = null
//    private var btnGetTokenStatus: Button? = null
//    private var btnGetTransactionById: Button? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
//        initializeViews()
//        addFunctions()
//        init()
//    }
//
//    private fun init() {
//        val checkServices = CheckServices.getInstance(this)
//    }
//
//    private fun initializeViews() {
//        btnLoginApi = findViewById(R.id.btnLoginApi)
//        btnLogOut = findViewById(R.id.btnLogOut)
//        btnGetOrganizations = findViewById(R.id.btnGetOrganizations)
//        btnChangeOrganization = findViewById(R.id.btnChangeOrganization)
//        btnOrganizationTransaction = findViewById(R.id.btnOrganizationTransaction)
//        btnGetOperations = findViewById(R.id.btnGetOperations)
//        btnSetSignature = findViewById(R.id.btnSetSignature)
//        btnPrintBitmap = findViewById(R.id.btnPrintBitmap)
//        btnPrintStr = findViewById(R.id.btnPrintStr)
//        btnGetPayLaterPromotions = findViewById(R.id.btnGetPayLaterPromotions)
//        btnGetPromotions = findViewById(R.id.btnGetPromotions)
//        btnTransactionByFolio = findViewById(R.id.btnTransactionByFolio)
//        btnGetPendingCheckouts = findViewById(R.id.btnGetPendingCheckouts)
//        btnDoCheckout = findViewById(R.id.btnDoCheckout)
//        btnReadCard = findViewById(R.id.btnReadCard)
//        btnTerminalClosure = findViewById(R.id.btnTerminalClosure)
//        btnPreTerminalClosure = findViewById(R.id.btnPreTerminalClosure)
//        btnGenerateTerminalClosure = findViewById(R.id.btnGenerateTerminalClosure)
//        btnTerminalClosureByID = findViewById(R.id.btnTerminalClosureById)
//        btnInitKeys = findViewById(R.id.btnInitKeys)
//        btnGetTokenStatus = findViewById(R.id.btnGetTokenStatus)
//        btnGetTransactionById = findViewById(R.id.btnTransactionById)
//    }
//
//    private fun addFunctions() {
//
//        btnLoginApi!!.setOnClickListener {
//            navigateTo(
//                LoginApiView::class.java
//            )
//        }
//        btnLogOut!!.setOnClickListener {
//            navigateTo(
//                LogOutView::class.java
//            )
//        }
//        btnGetOrganizations!!.setOnClickListener {
//            navigateTo(
//                GetOrganizationsView::class.java
//            )
//        }
//        btnChangeOrganization!!.setOnClickListener {
//            navigateTo(
//                ChangeOrganizationView::class.java
//            )
//        }
//        btnOrganizationTransaction!!.setOnClickListener {
//            navigateTo(
//                OrganizationTransactionView::class.java
//            )
//        }
//        btnGetOperations!!.setOnClickListener {
//            navigateTo(
//                GetOperationsView::class.java
//            )
//        }
//        btnSetSignature!!.setOnClickListener {
//            navigateTo(
//                SetSignatureView::class.java
//            )
//        }
//        btnPrintBitmap!!.setOnClickListener {
//            navigateTo(
//                PrintBitmapView::class.java
//            )
//        }
//        btnPrintStr!!.setOnClickListener {
//            navigateTo(
//                PrintStrView::class.java
//            )
//        }
//        btnGetPayLaterPromotions!!.setOnClickListener {
//            navigateTo(
//                GetPayLaterPromotionsView::class.java
//            )
//        }
//        btnGetPromotions!!.setOnClickListener {
//            navigateTo(
//                GetPromotionsView::class.java
//            )
//        }
//        btnTransactionByFolio!!.setOnClickListener {
//            navigateTo(
//                TransactionByFolioView::class.java
//            )
//        }
//        btnGetPendingCheckouts!!.setOnClickListener {
//            navigateTo(
//                GetPendingCheckoutsView::class.java
//            )
//        }
//        btnDoCheckout!!.setOnClickListener {
//            navigateTo(
//                DoCheckoutView::class.java
//            )
//        }
//        btnReadCard!!.setOnClickListener {
//            navigateTo(
//                ReadCardView::class.java
//            )
//        }
//        btnTerminalClosure!!.setOnClickListener {
//            navigateTo(
//                TerminalClosureView::class.java
//            )
//        }
//        btnPreTerminalClosure!!.setOnClickListener {
//            navigateTo(
//                PreTerminalClosureView::class.java
//            )
//        }
//        btnGenerateTerminalClosure!!.setOnClickListener {
//            navigateTo(
//                GenerateTerminalClosureView::class.java
//            )
//        }
//        btnTerminalClosureByID!!.setOnClickListener {
//            navigateTo(
//                TerminalClosureByIDView::class.java
//            )
//        }
//        btnInitKeys!!.setOnClickListener {
//            navigateTo(
//                InitKeysView::class.java
//            )
//        }
//        btnGetTokenStatus!!.setOnClickListener {
//            navigateTo(
//                TokenStatus::class.java
//            )
//        }
//
//        btnGetTransactionById!!.setOnClickListener {
//            navigateTo(
//                DetailTransactionById::class.java
//            )
//        }
//    }
//
//    private fun navigateTo(classToNavigate: Class<*>) {
//        val intent = Intent(this, classToNavigate)
//        startActivity(intent)
//    }
private fun setErrorResponse(errorResponse: ErrorResponse?) {
    checkServices = CheckServices.getInstance(this)
    runOnUiThread {
        instructionsText.text = "ERROR ${errorResponse?.code}:\n${errorResponse?.message?.uppercase()}"
        cancelButton.text = "CERRAR"
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
            exceptionResponse.code = "-2"
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
        transactionDescription += "getOperations (${getTransactionTime()}). "

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
        transactionDescription += "initKeys (${getTransactionTime()}). "

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
        transactionDescription += "readCard (${getTransactionTime()}). "

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
        transactionDescription += "getCardBrand (${getTransactionTime()}). "

        try {
            checkServices.getCardBrand(object : GetCardBrandCallback.Stub() {
                override fun onSuccessful(cardBrand: CardBrand?) {
                    if (cardBrand != null) {
                        idempotencyToken = cardBrand.idempotencyToken
                        saveIdempotencyToken(cardBrand)
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
        transactionDescription += "saveIdempotencyToken (${getTransactionTime()}). "
        makePayment()
//        try {
//            val request = StationRequest(orderId, idempotencyToken, cardBrand.brand, cardBrand.BIN, cardBrand.BIN8, cardBrand.accountingNature)
//            val jsonBody = Gson().toJson(request).toString()
//
//            HttpClient.makePostRequest(stationUrl, stationToken, jsonBody, object : okhttp3.Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        response.body?.let {
//                            val responseData = it.string()
//                            val apiResponse = Gson().fromJson(responseData, StationResponse::class.java)
//
//                            if (apiResponse.code == 0) {
//                                makePayment()
//                            } else {
//                                setHttpResponse(apiResponse.message)
//                            }
//                        }
//                    } else {
//                        setHttpResponse(response.message)
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    setHttpResponse(e.message ?: "okhttp3 onFailure")
//                }
//            })
//        } catch (ex: Exception) {
//            setExceptionResponse(ex)
//        }
    }

    private fun makePayment() {
        transactionDescription += "makePayment (${getTransactionTime()}). "

        try {
//            runOnUiThread {
//                instructionsText.text = "APROBADA\nPUEDE RETIRAR LA TARJETA"
////                        signatureView.setActivity(this@MainActivity)
////                        signatureView.setVisibility(true)
////                        signatureView.displayCanvas(::setSignature)
//                    }
                                setSuccessResponse(PaymentResponse())


//            checkServices.makePayment(object : MakePaymentCallback.Stub() {
//                override fun onPaymentSuccess(paymentResponse: PaymentResponse?) {
//                    Log.i("initKeysRequired", paymentResponse?.mustReloadKeySoon.toString() + " " + paymentResponse?.mustReloadKeyNow  )
//                    if (paymentResponse?.mustReloadKeySoon == true || paymentResponse?.mustReloadKeyNow == true){
//                        initKeysRequired = true
//                    }
//
//                    setSuccessResponse(paymentResponse)
//                }
//
//                override fun onSignatureRequired() {
//                    runOnUiThread {
//                        Log.i("Signature", "REQUIERD")
////                        signatureView.setActivity(this@MainActivity)
////                        signatureView.setVisibility(true)
////                        signatureView.displayCanvas(::setSignature)
//                    }
//                }
//
//                override fun onError(errorResponse: ErrorResponse?) {
//                    setErrorResponse(errorResponse)
//                }
//            })
        } catch (ex: Exception) {
            setExceptionResponse(ex)
        }
    }

    private fun setSignature(signature: String) {
        transactionDescription += "setSignature (${getTransactionTime()}). "

        try {
//            runOnUiThread {
//                signatureView.setVisibility(false)
//            }

            checkServices.setSignature(signature, object : SignatureCallback.Stub() {
                override fun onSuccessful(paymentResponse: PaymentResponse?) {
                    setSuccessResponse(paymentResponse)
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
        transactionDescription += "getTokenInPotency (${getTransactionTime()}). "

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

//                        setTokenInPotencySuccessResponse(paymentResponse)
                    } else {
//                        setTokenInPotencyErrorResponse("Null TokenStatusOrder", "-2")
                    }
                }

                override fun onError(errorResponse: ErrorResponse?) {
//                    setTokenInPotencyErrorResponse(errorResponse?.message, errorResponse?.code)
                }

            })
        } catch (ex: Exception) {
//            setTokenInPotencyErrorResponse(ex.message, "-2")
        }
    }

    private fun logout(){
        transactionDescription += "logout (${getTransactionTime()}). "

        try {
            checkServices.logout(object : LogoutCallback.Stub(){
                override fun onSuccessful() {
                    //Do Nothing...
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    //Do Nothing...
                }
            })
        }catch(_: Exception){
            //Do Nothing...
        }
    }
    //#endregion
    //#region [Methods]
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window, window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun getTransactionTime(): String {
        return "${Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')}:${
            Calendar.getInstance().get(
                Calendar.MINUTE).toString().padStart(2, '0')}:${Calendar.getInstance().get(Calendar.SECOND).toString().padStart(2, '0')}"
    }
    private fun setSuccessResponse(paymentResponse: PaymentResponse?) {
        runOnUiThread {
            if (paymentResponse != null) {
                this@MainActivity2.paymentResponse = paymentResponse
                finishButton.isVisible = true
                cancelButton.isVisible = false
                instructionsText.text = "APROBADA\nPUEDE RETIRAR LA TARJETA"


            }
        }
    }
}
