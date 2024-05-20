package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.LoginCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class LoginApiView : AppCompatActivity() {
    private var edtKey: EditText? = null
    private var edtCookie: EditText? = null
    private var txtVResponse: TextView? = null
    private var switchSandbox: Switch? = null
    private val key = "" // insert your provided key
    private val cookie = "" // insert your provided cookie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_api_view)
        edtKey = findViewById(R.id.edtTLoginApiKey)
        edtCookie = findViewById(R.id.edtTLoginApiCookie)
        txtVResponse = findViewById(R.id.txtVLoginApiResponse)
        switchSandbox = findViewById(R.id.switchSandbox)
        val btnAction = findViewById<Button>(R.id.btnLoginApiLogin)
        edtKey!!.setText(key)
        edtCookie!!.setText(cookie)
        btnAction.setOnClickListener { v: View? -> login() }
    }

    private fun login() {
        val checkServices = CheckServices.getInstance(this)
        val email = edtKey!!.getText().toString()
        val password = edtCookie!!.getText().toString()
        val sandbox = switchSandbox!!.isChecked
        try {
            checkServices.loginApi(email, password, sandbox, object : LoginCallback.Stub() {
                override fun onSuccessful() {
                    Log.d("LoginApiView", "Successful")
                    runOnUiThread { txtVResponse!!.text = "Successful" }
                }

                override fun onError(error: ErrorResponse) {
                    Log.d(
                        "LoginApiView",
                        "Error: " + if (error.code != null) error.code else "Unknown Error"
                    )
                    runOnUiThread {
                        txtVResponse!!.text = "Error Code: " + error.code + " " + error.message
                    }
                }
            })
        } catch (e: RemoteException) {
            Log.e("LoginApiView", "RemoteException in login", e)
            runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
        }
    }
}

