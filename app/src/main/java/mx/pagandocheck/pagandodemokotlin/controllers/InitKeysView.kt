package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.InitKeysCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class InitKeysView : AppCompatActivity() {
    var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_keys_view)
        txtVResponse = findViewById(R.id.txtVInitKeysResponse)
        val btnAction = findViewById<Button>(R.id.btnInitKeysAction)
        btnAction.setOnClickListener { v: View? -> initKeys() }
    }

    private fun initKeys() {
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.initKeys(object : InitKeysCallback.Stub() {
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
            Log.e("InitKeysView", "RemoteException in initKeys", e)
            runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
        }
    }
}
