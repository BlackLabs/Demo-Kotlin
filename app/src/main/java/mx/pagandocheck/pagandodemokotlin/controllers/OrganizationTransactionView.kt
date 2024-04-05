package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.OrganizationTransactionCallBack
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentHistoryResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class OrganizationTransactionView : AppCompatActivity() {
    var edtPerPage: EditText? = null
    var edtCurrentPage: EditText? = null
    var edtSearch: EditText? = null
    var edtFilters: EditText? = null
    var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_transaction_view)
        initializeViews()
        val btnAction = findViewById<Button>(R.id.btnGetOrganizationTransactions)
        btnAction.setOnClickListener { v: View? -> transactions }
    }

    private fun initializeViews() {
        edtCurrentPage = findViewById(R.id.edtOrganizationTransactioncurrentPage)
        edtPerPage = findViewById(R.id.edtOrganizationTransactionPerPage)
        edtSearch = findViewById(R.id.edtOrganizationTransactionSearch)
        edtFilters = findViewById(R.id.edtOrganizationTransactionFilters)
        txtVResponse = findViewById(R.id.txtGetOrganizationTransactionsResponse)
    }

    private val transactions: Unit
        private get() {
            val currentPage = edtCurrentPage!!.getText().toString()
            val perPage = edtPerPage!!.getText().toString()
            val search = edtSearch!!.getText().toString()
            val filters = edtFilters!!.getText().toString()
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.organizationTransaction(
                    currentPage,
                    perPage,
                    search,
                    filters,
                    object : OrganizationTransactionCallBack.Stub() {
                        override fun onSuccessful(response: PaymentHistoryResponse) {
                            runOnUiThread {
                                txtVResponse!!.text = "Success\n" + Stringfy.getString(
                                    response
                                )
                            }
                        }

                        override fun onError(error: ErrorResponse) {
                            runOnUiThread {
                                txtVResponse!!.text =
                                    "Error Code: " + error.code + " " + error.message
                            }
                        }
                    })
            } catch (e: RemoteException) {
                Log.e("OrganizationTransaction", "RemoteException in getTransactions", e)
                runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
            }
        }
}
