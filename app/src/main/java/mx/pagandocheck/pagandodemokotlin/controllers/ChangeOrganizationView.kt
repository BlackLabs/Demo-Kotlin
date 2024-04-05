package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.ChangeOrganizationCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R


class ChangeOrganizationView : AppCompatActivity() {
    private var edtId: EditText? = null
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_organization_view)
        val btnAction = findViewById<Button>(R.id.btnChangeOrganizationChangeOrganization)
        edtId = findViewById<EditText>(R.id.edtChangeOrganizationId)
        txtVResponse = findViewById<TextView>(R.id.txtVChangeOrganizationResponse)
        btnAction.setOnClickListener { v: View? -> changeOrganization() }
    }

    private fun changeOrganization() {
        val profile = edtId!!.getText().toString()
        val checkServices: CheckServices = CheckServices.getInstance(this)
        try {
            checkServices.changeOrganization(profile, object : ChangeOrganizationCallback.Stub() {
                override fun onSuccessful() {
                    runOnUiThread { txtVResponse!!.text = "Successful" }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        txtVResponse!!.text = "Error code: " + error.code + " " + error.message
                    }
                }
            })
        } catch (e: RemoteException) {
            e.printStackTrace()
            txtVResponse!!.text = "Remote exception occurred"
        }
    }
}
