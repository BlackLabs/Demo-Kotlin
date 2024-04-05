package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.GetClosureByIdCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.closure.ClosureDetail
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class TerminalClosureByIDView : AppCompatActivity() {
    var edtId: EditText? = null
    var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal_closure_by_idview)
        val btnAction = findViewById<Button>(R.id.btnClosureByIdAction)
        edtId = findViewById<EditText>(R.id.edtTClosureByIDid)
        txtVResponse = findViewById<TextView>(R.id.txtVClosureByIdResponse)
        btnAction.setOnClickListener { v: View? -> terminalClosureById() }
    }

    private fun terminalClosureById() {
        val id = edtId!!.getText().toString()
        val checkServices: CheckServices = CheckServices.getInstance(this)
        checkServices.getClosureById(id, object : GetClosureByIdCallback.Stub() {
            override fun onError(error: ErrorResponse) {
                txtVResponse!!.text = "Error code" + error.code + " " + error.message
            }

            override fun onSuccessful(closureDetail: ClosureDetail) {
                runOnUiThread {
                    txtVResponse!!.text = "Success\n" + Stringfy.getString(
                        closureDetail
                    )
                }
            }
        })
    }
}