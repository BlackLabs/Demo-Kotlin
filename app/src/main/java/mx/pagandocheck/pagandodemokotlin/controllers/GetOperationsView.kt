package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.GetOperationsCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class GetOperationsView : AppCompatActivity() {
    private var txtResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_operations_view)
        txtResponse = findViewById(R.id.txtVGetOperationsResponse)
        val btnAction = findViewById<Button>(R.id.btnGetOperationsAction)
        btnAction.setOnClickListener { v: View? -> operations }
    }

    private val operations: Unit
        private get() {
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.getOperations(object : GetOperationsCallback.Stub() {
                    override fun onSuccessful(terminalOperations: List<String>) {
                        runOnUiThread { txtResponse!!.text = "Success\n$terminalOperations" }
                    }

                    override fun onError(error: ErrorResponse) {
                        runOnUiThread {
                            txtResponse!!.text = "Error code: " + error.code + " " + error.message
                        }
                    }
                })
            } catch (e: RemoteException) {
                Log.e("GetOperationsView", "RemoteException in getOperations", e)
                runOnUiThread { txtResponse!!.text = "Remote exception occurred" }
            }
        }
}
