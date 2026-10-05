package mx.pagandocheck.pagandodemokotlin.controllers

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.pagando.check.services.ReadNonEmvCardCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R
import org.json.JSONObject

/**
 * Prueba de lectura de tarjetas de membresía por banda magnética y NFC, no requiere login.
 */
class ReadNonEmvCardView : AppCompatActivity() {
    private lateinit var txtVResponse: TextView

    private val readCallback = object : ReadNonEmvCardCallback.Stub() {
        override fun onSuccessful(cardData: String?) {
            runOnUiThread { txtVResponse.text = prettyJson(cardData) }
        }

        override fun onError(error: ErrorResponse) {
            runOnUiThread {
                txtVResponse.text = "Error Code: " + error.code + " " + error.message
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_non_emv_card_view)
        txtVResponse = findViewById(R.id.txtVReadNonEmvCardResponse)
        findViewById<Button>(R.id.btnReadMagneticCard).setOnClickListener { readMagneticCard() }
        findViewById<Button>(R.id.btnReadNfcCard).setOnClickListener { readNfcCard() }
        findViewById<Button>(R.id.btnCancelNonEmvCardRead).setOnClickListener { cancelRead() }
    }

    override fun onDestroy() {
        // Libera el lector si se sale de la pantalla con una lectura en curso
        cancelRead()
        super.onDestroy()
    }

    private fun readMagneticCard() {
        txtVResponse.text = "Swipe the card..."
        try {
            CheckServices.getInstance(this).readMagneticCard(READ_TIMEOUT_MS, readCallback)
        } catch (e: RemoteException) {
            Log.e("ReadNonEmvCardView", "RemoteException in readMagneticCard", e)
            txtVResponse.text = "Remote exception occurred"
        }
    }

    private fun readNfcCard() {
        txtVResponse.text = "Tap the card..."
        try {
            CheckServices.getInstance(this).readNfcCard(READ_TIMEOUT_MS, readCallback)
        } catch (e: RemoteException) {
            Log.e("ReadNonEmvCardView", "RemoteException in readNfcCard", e)
            txtVResponse.text = "Remote exception occurred"
        }
    }

    private fun cancelRead() {
        try {
            CheckServices.getInstance(this).cancelCardRead()
        } catch (e: RemoteException) {
            Log.e("ReadNonEmvCardView", "RemoteException in cancelCardRead", e)
        }
    }

    private fun prettyJson(cardData: String?): String = try {
        JSONObject(cardData ?: "").toString(2)
    } catch (e: Exception) {
        cardData ?: ""
    }

    companion object {
        private const val READ_TIMEOUT_MS = 30000
    }
}
