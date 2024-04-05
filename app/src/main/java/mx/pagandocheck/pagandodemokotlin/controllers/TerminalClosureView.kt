package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.PaginateTerminalClosureCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.TerminalClosureList
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class TerminalClosureView : AppCompatActivity() {
    var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal_closure_view)
        val btnAction = findViewById<Button>(R.id.btnClosureAction)
        txtVResponse = findViewById<TextView>(R.id.txtVTerminalClosureResponse)
        btnAction.setOnClickListener { v: View? -> terminalClousure() }
    }

    private fun terminalClousure() {
        val checkServices: CheckServices = CheckServices.getInstance(this)
        checkServices.getTerminalClosurePage(
            10,
            1,
            object : PaginateTerminalClosureCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    txtVResponse!!.text = "Error code: " + error.code + " " + error.message
                }

                override fun onSuccessful(preTerminalCut: TerminalClosureList) {
                    runOnUiThread {
                        txtVResponse!!.text = """Success ${preTerminalCut.docs}""".trimIndent()
                    }
                }
            })
    }
}