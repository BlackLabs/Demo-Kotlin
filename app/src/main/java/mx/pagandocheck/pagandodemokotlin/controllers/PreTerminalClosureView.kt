package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.PreTerminalClosureCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PreTerminalClosure
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class PreTerminalClosureView : AppCompatActivity() {
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_terminal_closure)
        val btnAction = findViewById<Button>(R.id.btnPreClosureAction)
        txtVResponse = findViewById(R.id.txtVPreTerminalClosureResponse)
        btnAction.setOnClickListener { v: View? -> preTerminalClousure() }
    }

    private fun preTerminalClousure() {
        val checkServices = CheckServices.getInstance(this)
        checkServices.preTerminalClosure(object : PreTerminalClosureCallback.Stub() {
            override fun onError(error: ErrorResponse) {
                txtVResponse!!.text = "Error code: " + error.code + " " + error.message
            }

            override fun onSuccessful(terminalClosure: PreTerminalClosure) {
                runOnUiThread {
                    txtVResponse!!.text = "Success\n" + Stringfy.getString(
                        terminalClosure
                    )
                }
            }
        })
    }
}