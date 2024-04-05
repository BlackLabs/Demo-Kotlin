package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.PendingCheckoutCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PendingCheckOutResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class GetPendingCheckoutsView : AppCompatActivity() {
    private var edtPerPage: EditText? = null
    private var edtCurrentPage: EditText? = null
    private var edtSearch: EditText? = null
    private var edtFilters: EditText? = null
    private var edtOrganizationId: EditText? = null
    private var edtDate: EditText? = null
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_pending_checkouts_view)
        initializeViews()
        val btnAction = findViewById<Button>(R.id.btnGetPendingCheckoutsGet)
        btnAction.setOnClickListener { v: View? -> pendingCheckouts }
    }

    private fun initializeViews() {
        edtCurrentPage = findViewById(R.id.edtGetPendingCheckoutsPage)
        edtPerPage = findViewById(R.id.edtGetPendingCheckoutsPerPage)
        edtSearch = findViewById(R.id.edtGetPendingCheckoutsSearch)
        edtFilters = findViewById(R.id.edtGetPendingCheckoutsFilters)
        edtOrganizationId = findViewById(R.id.edtGetPendingCheckoutsOrganizationId)
        edtDate = findViewById(R.id.edtGetPendingCheckoutsDate)
        txtVResponse = findViewById(R.id.txtGetPendingCheckoutsResponse)
    }

    private val pendingCheckouts: Unit
        private get() {
            val currentPage = parseOrDefault(edtCurrentPage!!.getText().toString(), 1)
            val perPage = parseOrDefault(edtPerPage!!.getText().toString(), 10)
            val search = edtSearch!!.getText().toString()
            val filters = edtFilters!!.getText().toString()
            val organizationId = edtOrganizationId!!.getText().toString()
            val date = edtDate!!.getText().toString()
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.getPendingCheckouts(
                    currentPage,
                    perPage,
                    search,
                    filters,
                    organizationId,
                    date,
                    object : PendingCheckoutCallback.Stub() {
                        override fun onSuccessful(response: PendingCheckOutResponse) {
                            runOnUiThread {
                                txtVResponse!!.text = "Successfull" + Stringfy.getString(
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
                Log.e("GetPendingCheckouts", "RemoteException in getPendingCheckouts", e)
                runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
            }
        }

    private fun parseOrDefault(value: String, defaultValue: Int): Int {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            defaultValue
        }
    }
}
