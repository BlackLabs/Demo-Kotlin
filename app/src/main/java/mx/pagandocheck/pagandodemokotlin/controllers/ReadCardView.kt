package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.MakePaymentCallback
import mx.pagando.check.services.ReadCardCallback
import mx.pagando.check.services.SelectAppCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.NipPagandoView
import mx.pagandocheck.pagandodemokotlin.R

class ReadCardView : AppCompatActivity() {

    private lateinit var txtVResponse: TextView
    private lateinit var edtAmount: EditText
    private lateinit var nipView: NipPagandoView
    private lateinit var btnAction: Button
    private lateinit var btnMakePayment: Button

    private var makePaymentReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card_view)
        btnAction = findViewById(R.id.btnReadCardAction)
        txtVResponse = findViewById(R.id.txtVReadCardResponse)
        edtAmount = findViewById(R.id.edtTReadCardAmount)
        nipView = findViewById(R.id.nipPagandoView)
        btnMakePayment = findViewById(R.id.btnMakePayment)
        nipView.visibility = View.GONE
        nipView.setTitle("Ingresa tu NIP")
        nipView.setActivity(this)

        btnAction.setOnClickListener { readCard(nipView) }
        btnMakePayment.setOnClickListener {
            if (makePaymentReady) {
                makePayment()
            }
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
                    runOnUiThread { txtVResponse.text = "Successful.\n" + Stringfy.getString(paymentResponse) }
                }

                override fun onSignatureRequired() {
                    txtVResponse.text = "Signature required."
                }
            })
        } catch (e: RemoteException) {
            throw RuntimeException(e)
        }
    }

    private fun readCard(nipPagandoView: NipPagandoView) {
        val checkServices = CheckServices.getInstance(this)
        checkServices.readCard(nipPagandoView, edtAmount.text.toString(), "SALE", object : ReadCardCallback.Stub() {
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

            override fun onActionNip(message: String) {
                nipPagandoView.proccessMessage(message)
            }

            override fun onApplicationSelection(apps: Array<String>, selectAppCallback: SelectAppCallback) {
                runOnUiThread {
                    AlertDialog.Builder(this@ReadCardView)
                        .setTitle("Select Application")
                        .setCancelable(false)
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                        .setItems(apps) { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        })
    }
}