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

class GetPayLaterPromotionsView : AppCompatActivity() {
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_pay_later_promotions_view)
        val btnAction = findViewById<Button>(R.id.btnGetPayLaterPromotionsGet)
        txtVResponse = findViewById(R.id.txtVGetPayLaterPromotionsResponse)
        btnAction.setOnClickListener { v: View? -> paylaterPromotions }
    }

    private val paylaterPromotions: Unit
        private get() {
            val checkServices = CheckServices.getInstance(this)
            try {
                checkServices.getPayLaterPromotions(object : PromotionsCallback.Stub() {
                    override fun onError(error: ErrorResponse) {
                        runOnUiThread {
                            txtVResponse!!.text = "Error code: " + error.code + " " + error.message
                        }
                    }

                    override fun onSuccessful(promotions: List<Promotion>) {
                        runOnUiThread { txtVResponse!!.text = "Promotions:\n $promotions" }
                    }

                })
            } catch (e: RemoteException) {
                Log.e("GetPayLaterPromotions", "RemoteException in getPaylaterPromotions", e)
                runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
            }
        }
}
