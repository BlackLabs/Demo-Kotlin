package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.pagando.check.services.TokenStatusCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.TokenStatusOrder
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class TokenStatus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_status)
        val button : Button = findViewById(R.id.btnTokenStatusGet)
        val buttonGetLast : Button = findViewById(R.id.btnGetLastToken)
        val edtText : EditText = findViewById(R.id.edtTokenStatus)
        val txtVResponse : TextView = findViewById(R.id.txtVTokenStatusResponse)

        buttonGetLast.setOnClickListener {
            edtText.setText(getTokenFromSharedPreferences())
        }
        button.setOnClickListener {
            val token = edtText.text.toString()
            val checkServices = CheckServices.getInstance(this)
            checkServices.getTokenInPotency(token, object : TokenStatusCallback.Stub() {
                override fun onSuccessful(status: String?, order: TokenStatusOrder?) {
                    runOnUiThread {
                        txtVResponse.text = "Status: $status\nOrder: ${order?.let { it1 ->
                            Stringfy.getString(
                                it1
                            )
                        }}"
                    }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        txtVResponse.text = "Error code: ${error.code} ${error.message}"
                    }
                }
            })
        }
    }
    private fun getTokenFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("tokenInPotency", null)
    }
}