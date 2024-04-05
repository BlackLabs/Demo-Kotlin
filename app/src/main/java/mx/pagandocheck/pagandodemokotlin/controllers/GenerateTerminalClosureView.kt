package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.GenerateClosureCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.closure.ClosureDetail
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class GenerateTerminalClosureView : AppCompatActivity() {
    private var edtPassword: EditText? = null
    private var txtVReponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_terminal_closure_view)
        val btnAction = findViewById<Button>(R.id.btnGenerateClosureAction)
        edtPassword = findViewById(R.id.edtTGenerateClosurePassword)
        txtVReponse = findViewById(R.id.txtVGenerateClosureResponse)
        btnAction.setOnClickListener { v: View? -> generateClosure() }
    }

    private fun generateClosure() {
        val password = edtPassword!!.getText().toString()
        val checkServices = CheckServices.getInstance(this)
        checkServices.generateClosure(password, object : GenerateClosureCallback.Stub() {
            @Throws(RemoteException::class)
            override fun onError(error: ErrorResponse) {
                txtVReponse!!.text = "Error Code:" + error.code + " " + error.message
            }

            @Throws(RemoteException::class)
            override fun onSuccessful(closureDetail: ClosureDetail) {
                runOnUiThread {
                    txtVReponse!!.text = "Success \n" + Stringfy.getString(
                        closureDetail.toString()
                    )
                }
            }
        })
    }
}