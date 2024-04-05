package mx.pagandocheck.pagandodemokotlin.controllers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.RemoteException
import android.util.Base64
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import mx.pagando.check.services.SignatureCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class SetSignatureView : AppCompatActivity() {
    private lateinit var signatureCanvas: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var txtVSetSignatureResponse: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_signature_view)

        signatureCanvas = findViewById(R.id.signatureCanvas)
        val btnClearSignature = findViewById<Button>(R.id.btnClearSignature)
        val btnSetSignature = findViewById<Button>(R.id.btnSentSignatureAction)
        txtVSetSignatureResponse = findViewById(R.id.txtVSetSignatureResponse)

        initializeDrawing()

        signatureCanvas.setOnTouchListener { _, event ->
            val x = event.x
            val y = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(x, y)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(x, y)
                    canvas.drawPath(path, paint)
                    signatureCanvas.invalidate()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // No reseteamos el path aquí
                    true
                }
                else -> false
            }
        }

        btnClearSignature.setOnClickListener { clearCanvas() }
        btnSetSignature.setOnClickListener {
            try {
                setSignature { txtVSetSignatureResponse.text = "Success" }
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun initializeDrawing() {
        paint = Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(this@SetSignatureView, android.R.color.black)
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 4f
        }
        path = Path()
        bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        signatureCanvas.setImageBitmap(bitmap)
    }

    private fun clearCanvas() {
        canvas.drawColor(ContextCompat.getColor(this, android.R.color.white))
        path.reset() // Resetear el path aquí en lugar de en ACTION_UP
        signatureCanvas.invalidate()
    }

    private fun convertToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @Throws(RemoteException::class)
    private fun setSignature(callback: Runnable) {
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.setSignature(convertToBase64(bitmap), object : SignatureCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    txtVSetSignatureResponse.text = "Error code :${error.code} ${error.message}"
                }

                override fun onSuccessful(paymentResponse: PaymentResponse) {
                    callback.run()
                }
            })
        } catch (e: NullPointerException) {
            txtVSetSignatureResponse.text = "Success"
        }
    }
}
