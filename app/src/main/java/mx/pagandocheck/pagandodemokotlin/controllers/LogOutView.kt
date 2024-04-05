package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.LogoutCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class LogOutView : AppCompatActivity() {
    private var txtLogoutResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out_view)
        val btnAction = findViewById<Button>(R.id.btnLogoutLogout)
        txtLogoutResponse = findViewById(R.id.txtVLogOutResponse)
        btnAction.setOnClickListener { v: View? -> logOut() }
    }

    private fun logOut() {
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.logout(object : LogoutCallback.Stub() {
                override fun onSuccessful() {
                    runOnUiThread { txtLogoutResponse!!.text = "Successful" }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        txtLogoutResponse!!.text = "Error Code: " + error.code + " " + error.message
                    }
                }
            })
        } catch (e: RemoteException) {
            Log.e("LogOutView", "RemoteException in logOut", e)
            runOnUiThread { txtLogoutResponse!!.text = "Remote exception occurred" }
        }
    }
}

