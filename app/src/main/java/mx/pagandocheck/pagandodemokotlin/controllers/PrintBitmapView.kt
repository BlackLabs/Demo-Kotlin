package mx.pagandocheck.pagandodemokotlin.controllers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Picture
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import mx.pagando.check.services.PrintCallback
import mx.pagando.check.services.models.ErrorResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R
import mx.pagandocheck.pagandodemokotlin.models.ElementToPrint

class PrintBitmapView : AppCompatActivity() {
    private var edtText: EditText? = null
    private var edtSize: EditText? = null
    private var txtVResponse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_bitmap_view)
        initializeViews()
        val btnAction = findViewById<Button>(R.id.btnTPrintBitmapAction)
        btnAction.setOnClickListener { v: View? -> prepareAndPrint() }
    }

    fun createPicture(): Picture {
        val picture = Picture()
        val canvas = picture.beginRecording(width, height)
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)
        if (drawable != null) {
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
        picture.endRecording()
        return picture
    }

    private val width: Int
        private get() = 200
    private val height: Int
        private get() = 200

    private fun initializeViews() {
        edtSize = findViewById(R.id.edtTTPrintBitmapSize)
        edtText = findViewById(R.id.edtTPrintBitmapText)
        txtVResponse = findViewById(R.id.txtVTPrintBitmapResponse)
    }

    private fun prepareAndPrint() {
        try {
            val floatSize = edtSize!!.getText().toString().toFloat()
            val strText = edtText!!.getText().toString()
            val element = ElementToPrint(
                floatSize,
                strText,
                createPicture()
            ) // Assuming ElementToPrint constructor accepts String, float
            val items: ArrayList<ElementToPrint> = ArrayList<ElementToPrint>()
            items.add(element)
            printTicket(items)
        } catch (e: NumberFormatException) {
            txtVResponse!!.text = "Wrong Size"
        }
    }

    private fun createBitmapFromPicture(picture: Picture): Bitmap {
        val bitmap =
            Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLACK)
        picture.draw(canvas)
        return bitmap
    }

    private fun printTicket(items: ArrayList<ElementToPrint>) {
        Thread {
            for (item in items) {
                if (item.getPicture() != null && item.getPicture()
                        .getHeight() > 0 && item.getPicture().getWidth() > 0
                ) {
                    val bitmap = createBitmapFromPicture(item.getPicture())
                    printBmp(bitmap)
                    try {
                        Thread.sleep(200) // Sleep for 200 ms
                    } catch (e: InterruptedException) {
                        Log.e("PrintBitmapView", "Thread interrupted", e)
                    }
                } else {
                    Log.e("PrintBitmapView", "Incorrect size")
                }
            }
        }.start()
    }

    private fun printBmp(bitmap: Bitmap) {
        val checkServices = CheckServices.getInstance(this)
        try {
            checkServices.printBitmap(bitmap, object : PrintCallback.Stub() {
                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        txtVResponse!!.text = "Error Code: " + error.code + " " + error.message
                    }
                }

                override fun onSuccessful() {
                    runOnUiThread { txtVResponse!!.text = "Printing..." }
                }
            })
        } catch (e: RemoteException) {
            Log.e("PrintBitmapView", "RemoteException in printBmp", e)
            runOnUiThread { txtVResponse!!.text = "Remote exception occurred" }
        } catch (e: NullPointerException) {
            Log.e("Exception", e.message!!)
        }
    }
}

