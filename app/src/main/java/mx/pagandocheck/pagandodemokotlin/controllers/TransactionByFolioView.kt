package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.TransactionByFolioCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.TransactionFolioResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class TransactionByFolioView : AppCompatActivity() {
    var edtTFolio: EditText? = null
    var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_by_folio_view)
        edtTFolio = findViewById<EditText>(R.id.edtTransactionByFolioFolio)
        val btnAction = findViewById<Button>(R.id.btnTransactionByFolioGet)
        txtVResponse = findViewById<TextView>(R.id.txtVTransactionByFolioResponse)
        edtTFolio!!.setText("PAG-000000011783")
        btnAction.setOnClickListener { v: View? -> transaction }
    }

    val transaction: Unit
        get() {
            val folio = edtTFolio!!.getText().toString()
            val checkServices: CheckServices = CheckServices.getInstance(this)
            try {
                checkServices.transactionByFolio(folio, object : TransactionByFolioCallback.Stub() {
                    override fun onSuccessful(paymentResponse: TransactionFolioResponse) {
                        runOnUiThread {
                            txtVResponse!!.text = "Success\n" + Stringfy.getString(
                                paymentResponse
                            )
                        }
                    }

                    override fun onError(error: ErrorResponse) {
                        txtVResponse!!.text = "Error Code: " + error.code + " " + error.message
                    }
                })
            } catch (e: RemoteException) {
                e.printStackTrace()
                txtVResponse!!.text = "Remote exception occurred"
            }
        }
}