package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.PromotionsCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.Promotion
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class GetPromotionsView : AppCompatActivity() {
    var txtResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_promotions_view)
        val btnAction = findViewById<Button>(R.id.btnGetPromotionsGet)
        txtResponse = findViewById(R.id.txtVGetPromotionsResponse)
        btnAction.setOnClickListener { v: View? -> promotions }
    }

    private val promotions: Unit
        private get() {
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.getPromotions(object : PromotionsCallback.Stub() {
                    override fun onError(error: ErrorResponse) {
                        runOnUiThread {
                            txtResponse!!.text = "Error code: " + error.code + " " + error.message
                        }
                    }

                    override fun onSuccessful(promotions: List<Promotion>) {
                        runOnUiThread { txtResponse!!.text = "${txtResponse!!.text} Promotions:\n $promotions" }
                    }

                })
            } catch (e: RemoteException) {
                Log.e("GetPromotionsView", "RemoteException in getPromotions", e)
                runOnUiThread { txtResponse!!.text = "Remote exception occurred" }
            }
        }
}
