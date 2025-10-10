package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.ActionNipCallback
import mx.pagando.check.services.GetCardBrandCallback
import mx.pagando.check.services.MakePaymentCallback
import mx.pagando.check.services.ReadCardCallback
import mx.pagando.check.services.SelectAppCallback
import mx.pagando.check.services.models.CardBrand
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.NipPagandoView
import mx.pagandocheck.pagandodemokotlin.R

class ReadCardView : AppCompatActivity() {

    private lateinit var txtVResponse: TextView
    private lateinit var edtAmount: EditText
    private lateinit var edtWaiterNumber: EditText
    private lateinit var edtTipAmount: EditText
    private lateinit var lnrTip: LinearLayout
    private lateinit var nipView: NipPagandoView
    private lateinit var btnAction: Button
    private lateinit var btnMakePayment: Button
    private lateinit var btnCardBrand: Button
    private lateinit var spinTransaction: Spinner
    private var makePaymentReady = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card_view)
        btnAction = findViewById(R.id.btnReadCardAction)
        txtVResponse = findViewById(R.id.txtVReadCardResponse)
        edtAmount = findViewById(R.id.edtTReadCardAmount)
        edtTipAmount = findViewById(R.id.edtTipAmount)
        edtWaiterNumber = findViewById(R.id.edtWaiterNumber)
        lnrTip = findViewById(R.id.lnrTip)
        nipView = findViewById(R.id.nipPagandoView)
        btnMakePayment = findViewById(R.id.btnMakePayment)
        spinTransaction = findViewById(R.id.spinnerTransactionType)
        btnCardBrand = findViewById(R.id.btnReadCardBrand)
        nipView.setVisibility(false)
        nipView.setTitle("Ingresa tu NIP")
        nipView.setActivity(this)

        btnCardBrand.setOnClickListener { cardBrand() }
        btnAction.setOnClickListener { readCard(nipView) }
        btnMakePayment.setOnClickListener {
            if (makePaymentReady) {
                makePayment()
            }
        }

        spinTransaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                lnrTip.visibility = if (selectedItem == "SALE_TIP") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                lnrTip.visibility = View.GONE
            }
        }
    }

    private fun cardBrand() {
        val checkServices = CheckServices.getInstance(this)
        checkServices.getCardBrand(object : GetCardBrandCallback.Stub() {
            override fun onSuccessful(cardBrand: CardBrand?) {
                runOnUiThread {
                    txtVResponse.text =
                        "Success\n Card Brand: " + cardBrand?.let { Stringfy.getString(it) }
                }
                saveTokenInSharedPreferences(cardBrand?.idempotencyToken)
            }

            override fun onError(error: ErrorResponse) {
                runOnUiThread { txtVResponse.text = error.code + " " + error.message }
            }
        })
    }

    private fun saveTokenInSharedPreferences(token: String?) {
        if (token != null) {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("tokenInPotency", token)
            editor.apply()
        }
    }

    private fun makePayment() {
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.makePayment(object : MakePaymentCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    txtVResponse.text = error.code + " " + error.message
                }

                override fun onPaymentSuccess(paymentResponse: PaymentResponse) {
                    runOnUiThread {
                        txtVResponse.text = "Successful.\n" + Stringfy.getString(paymentResponse)
                    }
                }

                override fun onSignatureRequired() {
                    txtVResponse.text = "Signature required."
                }
            })
        } catch (e: RemoteException) {
            throw RuntimeException(e)
        }
    }

    fun setTip() {
        val checkServices = CheckServices.getInstance(this)
        checkServices.setTipAndWaiter(edtTipAmount.text.toString(), edtWaiterNumber.text.toString())
    }

    private fun readCard(nipPagandoView: NipPagandoView) {
        if (spinTransaction.selectedItem.toString() == "SALE_TIP")
            setTip()

        val checkServices = CheckServices.getInstance(this)
        checkServices.readCard(
            nipPagandoView,
            edtAmount.text.toString(),
            spinTransaction.selectedItem.toString(),
            object : ReadCardCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    txtVResponse.text = error.code + " " + error.message
                }

                override fun onSuccessful(typeCard: Int) {
                    txtVResponse.text = "Puede retirar su tarjeta."
                    btnAction.isActivated = true
                    makePaymentReady = true
                }

                override fun onMessage(message: String) {
                    txtVResponse.text = message
                    btnAction.isActivated = false
                }

                override fun onActionNip(message: String, callback: ActionNipCallback) {
                    nipPagandoView.proccessMessage(message, callback)
                }

                override fun onApplicationSelection(
                    apps: Array<String>,
                    selectAppCallback: SelectAppCallback
                ) {
                    runOnUiThread {
                        AlertDialog.Builder(this@ReadCardView)
                            .setTitle("Select Application")
                            .setCancelable(false)
                            .setNegativeButton("Cancel") { dialog, wich ->
                                dialog.dismiss()
                                selectAppCallback.cancel()
                                checkServices.cancelCardRead()
                            }
                            .setItems(apps) { dialog, wich ->
                                dialog.dismiss()
                                selectAppCallback.selectApp(wich)
                            }
                            .show()
                    }
                }
            })
    }
}