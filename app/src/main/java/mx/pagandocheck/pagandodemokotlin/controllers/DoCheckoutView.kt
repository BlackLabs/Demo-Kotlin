package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.DoCheckoutCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R


class DoCheckoutView : AppCompatActivity() {
    private var edtReference: EditText? = null
    private var edtAmount: EditText? = null
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_checkout_view)
        edtAmount = findViewById(R.id.edtTDocheckAmount)
        edtReference = findViewById(R.id.edtTDoCheckReference)
        val btnAction = findViewById<Button>(R.id.btnDoCheckoutAction)
        txtVResponse = findViewById(R.id.txtVDocheckResponse)
        btnAction.setOnClickListener { v: View? -> doCheckout() }
    }

    private fun doCheckout() {
        val reference = edtReference!!.getText().toString()
        val amount = edtAmount!!.getText().toString()
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.doCheckout(reference, amount, object : DoCheckoutCallback.Stub() {
                override fun onSuccessful(paymentResponse: PaymentResponse) {
                    runOnUiThread {
                        txtVResponse!!.text = "Success\n" + Stringfy.getString(
                            paymentResponse.toString()
                        )
                    }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        txtVResponse!!.text = "Error Code: " + error.code + " " + error.message
                    }
                }
            })
        } catch (e: RemoteException) {
            Log.e("DoCheckoutView", "RemoteException in doCheckout", e)
            runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
        }
    }
}

