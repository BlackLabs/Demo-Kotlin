package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.TransactionDetailCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.TransactionDetail
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class DetailTransactionById : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction_by_id)
        val txtResponse = findViewById<TextView>(R.id.txtVTransactionByIdResponse)
        val edtTransactionId = findViewById<TextView>(R.id.edtTransactionById)
        val btnAction = findViewById<TextView>(R.id.btnTransactionByIdGet)

        btnAction.setOnClickListener {

            val transactionId = edtTransactionId?.text?.toString()
            val checkServices = CheckServices.getInstance(this)
            checkServices.getTransactionDetail(transactionId.toString(), object : TransactionDetailCallback.Stub() {
                override fun onSuccessful(transactionDetail: TransactionDetail?) {
                    runOnUiThread { txtResponse.text = transactionDetail?.let { it1 -> Stringfy.getString(it1) }}
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    runOnUiThread {txtResponse.text = errorResponse.toString()}
                }
            })
        }

    }
}