package mx.pagandocheck.pagandodemokotlin.controllers

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.ActionNipCallback
import mx.pagando.check.services.GetCardBrandCallback
import mx.pagando.check.services.GetOperationsCallback
import mx.pagando.check.services.InitKeysCallback
import mx.pagando.check.services.LoginCallback
import mx.pagando.check.services.MakePaymentCallback
import mx.pagando.check.services.ReadCardCallback
import mx.pagando.check.services.SelectAppCallback
import mx.pagando.check.services.models.CardBrand
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.NipPagandoView
import mx.pagandocheck.pagandodemokotlin.R

/**
 * Runs the complete sale flow in cascade:
 * Login -> Get Operations -> Init Keys -> Read Card -> Get Card Brand -> Make Payment.
 * Each step only runs if the previous one succeeded.
 */
class CascadeOperationView : AppCompatActivity() {

    private lateinit var edtKey: EditText
    private lateinit var edtCookie: EditText
    private lateinit var edtAmount: EditText
    private lateinit var edtOperationType: EditText
    private lateinit var btnStart: Button
    private lateinit var txtVResponse: TextView
    private lateinit var nipView: NipPagandoView
    private val logBuilder = SpannableStringBuilder()

    private enum class LogType(val color: Int) {
        START(Color.parseColor("#0D47A1")), // strong blue: a new step is starting
        SUCCESS(Color.parseColor("#1B5E20")), // strong green: step succeeded
        ERROR(Color.parseColor("#B71C1C")) // strong red: step failed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cascade_operation_view)

        edtKey = findViewById(R.id.edtCascadeKey)
        edtCookie = findViewById(R.id.edtCascadeCookie)
        edtAmount = findViewById(R.id.edtCascadeAmount)
        edtOperationType = findViewById(R.id.edtCascadeOperationType)
        btnStart = findViewById(R.id.btnCascadeStart)
        txtVResponse = findViewById(R.id.txtVCascadeResponse)
        nipView = findViewById(R.id.nipCascadeView)
        nipView.setVisibility(false)
        nipView.setTitle("Enter your PIN")
        nipView.setActivity(this)

        btnStart.isEnabled = false
        val credentialsWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btnStart.isEnabled =
                    edtKey.text.toString().isNotBlank() && edtCookie.text.toString().isNotBlank()
            }
        }
        edtKey.addTextChangedListener(credentialsWatcher)
        edtCookie.addTextChangedListener(credentialsWatcher)

        btnStart.setOnClickListener { startCascade() }
    }

    private fun log(message: String, type: LogType) {
        runOnUiThread {
            if (logBuilder.isNotEmpty()) logBuilder.append("\n")
            val lineStart = logBuilder.length
            logBuilder.append(message)
            logBuilder.setSpan(
                ForegroundColorSpan(type.color),
                lineStart,
                logBuilder.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            txtVResponse.text = logBuilder
        }
    }

    private fun finishCascade() {
        runOnUiThread { btnStart.isEnabled = true }
    }

    private fun startCascade() {
        btnStart.isEnabled = false
        logBuilder.clear()
        txtVResponse.text = ""

        val key = edtKey.text.toString().trim()
        val cookie = edtCookie.text.toString().trim()
        val amount = edtAmount.text.toString().trim()
        val operationType = edtOperationType.text.toString().trim()

        val checkServices = CheckServices.getInstance(this)

        log("1) Login: starting...", LogType.START)
        checkServices.loginApi(key, cookie, false, object : LoginCallback.Stub() {
            override fun onSuccessful() {
                log("1) Login: OK", LogType.SUCCESS)
                doGetOperations(checkServices, amount, operationType)
            }

            override fun onError(error: ErrorResponse) {
                log("1) Login: ERROR ${error.code} ${error.message}", LogType.ERROR)
                finishCascade()
            }
        })
    }

    private fun doGetOperations(checkServices: CheckServices, amount: String, operationType: String) {
        log("2) Get Operations: starting...", LogType.START)
        checkServices.getOperations(object : GetOperationsCallback.Stub() {
            override fun onSuccessful(terminalOperations: List<String>) {
                log("2) Get Operations: OK $terminalOperations", LogType.SUCCESS)
                doInitKeys(checkServices, amount, operationType)
            }

            override fun onError(error: ErrorResponse) {
                log("2) Get Operations: ERROR ${error.code} ${error.message}", LogType.ERROR)
                finishCascade()
            }
        })
    }

    private fun doInitKeys(checkServices: CheckServices, amount: String, operationType: String) {
        log("3) Init Keys: starting...", LogType.START)
        checkServices.initKeys(object : InitKeysCallback.Stub() {
            override fun onSuccessful() {
                log("3) Init Keys: OK", LogType.SUCCESS)
                doReadCard(checkServices, amount, operationType)
            }

            override fun onError(error: ErrorResponse) {
                log("3) Init Keys: ERROR ${error.code} ${error.message}", LogType.ERROR)
                finishCascade()
            }
        })
    }

    private fun doReadCard(checkServices: CheckServices, amount: String, operationType: String) {
        log("4) Read Card: waiting for card ($amount, $operationType)...", LogType.START)
        checkServices.readCard(
            nipView,
            amount,
            operationType,
            object : ReadCardCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    log("4) Read Card: ERROR ${error.code} ${error.message}", LogType.ERROR)
                    finishCascade()
                }

                override fun onSuccessful(typeCard: Int) {
                    log("4) Read Card: OK, you may remove your card", LogType.SUCCESS)
                    doGetCardBrand(checkServices)
                }

                override fun onMessage(message: String) {
                    log("4) Read Card: $message", LogType.START)
                }

                override fun onActionNip(message: String, callback: ActionNipCallback) {
                    nipView.proccessMessage(message, callback)
                }

                override fun onApplicationSelection(
                    apps: Array<String>,
                    selectAppCallback: SelectAppCallback
                ) {
                    runOnUiThread {
                        AlertDialog.Builder(this@CascadeOperationView)
                            .setTitle("Select Application")
                            .setCancelable(false)
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                                selectAppCallback.cancel()
                                checkServices.cancelCardRead()
                                log("4) Read Card: cancelled by user", LogType.ERROR)
                                finishCascade()
                            }
                            .setItems(apps) { dialog, which ->
                                dialog.dismiss()
                                selectAppCallback.selectApp(which)
                            }
                            .show()
                    }
                }
            })
    }

    private fun doGetCardBrand(checkServices: CheckServices) {
        log("5) Card Brand: starting...", LogType.START)
        checkServices.getCardBrand(object : GetCardBrandCallback.Stub() {
            override fun onSuccessful(cardBrand: CardBrand?) {
                log("5) Card Brand: OK ${cardBrand?.let { Stringfy.getString(it) }}", LogType.SUCCESS)
                doMakePayment(checkServices)
            }

            override fun onError(error: ErrorResponse) {
                log("5) Card Brand: ERROR ${error.code} ${error.message}", LogType.ERROR)
                finishCascade()
            }
        })
    }

    private fun doMakePayment(checkServices: CheckServices) {
        log("6) Make Payment: starting...", LogType.START)
        checkServices.makePayment(object : MakePaymentCallback.Stub() {
            override fun onError(error: ErrorResponse) {
                log("6) Make Payment: ERROR ${error.code} ${error.message}", LogType.ERROR)
                finishCascade()
            }

            override fun onPaymentSuccess(paymentResponse: PaymentResponse) {
                log("6) Make Payment: OK\n${Stringfy.getString(paymentResponse)}", LogType.SUCCESS)
                finishCascade()
            }

            override fun onSignatureRequired() {
                log("6) Make Payment: signature required", LogType.ERROR)
                finishCascade()
            }
        })
    }
}
