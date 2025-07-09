package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.PrintCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class PrintStrView : AppCompatActivity() {
    var edtTxtToPrint: EditText? = null
    var txtResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_str_view)
        edtTxtToPrint = findViewById(R.id.edtTextToPrint)
        val btnAction = findViewById<Button>(R.id.btnPrintStrTicket)
        txtResponse = findViewById(R.id.txtVPrintStrResponse)
        btnAction.setOnClickListener { _ -> printStr() }
    }

    private fun printStr() {
        val textToPrint = edtTxtToPrint!!.getText().toString()
        val checkServices: CheckServices = CheckServices.getInstance(this)
        try {
            checkServices.printStr(textToPrint, 15, object : PrintCallback.Stub() {
                override fun onSuccessful() {
                    runOnUiThread { txtResponse!!.text = "Printing..." }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread { txtResponse!!.text = "Printing..." }
                }
            })
        } catch (e: RemoteException) {
            Log.e("PrintStrView", "RemoteException in printStr", e)
            runOnUiThread { txtResponse!!.text = "Remote exception occurred" }
        }
    }
}
