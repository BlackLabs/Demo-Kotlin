package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.GetOrganizationsCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.GetOrganizationsResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class GetOrganizationsView : AppCompatActivity() {
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_organizations_view)
        val btnAction = findViewById<Button>(R.id.btnOrganizationsGetOrganization)
        txtVResponse = findViewById(R.id.txtVGetOrganizationOrganizationResponse)
        btnAction.setOnClickListener { v: View? -> organizations }
    }

    private val organizations: Unit
        private get() {
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.getOrganizations(object : GetOrganizationsCallback.Stub() {
                    override fun onSuccessful(GetOrganizationsResponse: GetOrganizationsResponse) {
                        runOnUiThread {
                            txtVResponse!!.text = """
     Success 
     ${GetOrganizationsResponse.name}
     """.trimIndent()
                        }
                    }

                    override fun onError(error: ErrorResponse) {
                        runOnUiThread {
                            txtVResponse!!.text = "Error code: " + error.code + " " + error.message
                        }
                    }
                })
            } catch (e: RemoteException) {
                Log.e("GetOrganizationsView", "RemoteException in getOrganizations", e)
                runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
            }
        }
}
