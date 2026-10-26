package mx.pagandocheck.pagandodemokotlin.controllers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import mx.pagando.check.services.PrintCallback
import mx.pagando.check.services.models.PaymentResponse
import mx.pagandocheck.pagandodemokotlin.CheckServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Test sale ticket with the data required by CID (no logo, no QR).
 * Based on checkproclient PrintElements.
 */
object Ticket {

    fun build(payment: PaymentResponse, amount: String): String {
        val subtotal = amount.replace(",", "").toDoubleOrNull() ?: 0.0
        val tip = payment.tipAmount?.replace(",", "")?.toDoubleOrNull() ?: 0.0
        // shortcut: getSerial needs a privileged permission this demo lacks, prints empty then
        val serial = runCatching { Build.getSerial() }.getOrDefault("")
        val date = SimpleDateFormat("dd/MM/yyyy    HH:mm:ss", Locale.getDefault()).format(Date())

        return buildString {
            appendLine(payment.operationType ?: "")
            appendLine("FOLIO: ${payment.folio ?: ""}")
            appendLine("Terminal: $serial")
            appendLine()
            if (tip > 0) {
                appendLine("Subtotal   ${"%.2f".format(subtotal)}")
                appendLine("Propina    ${"%.2f".format(tip)}")
            }
            appendLine("TOTAL M.N. ${"%.2f".format(subtotal + tip)}")
            appendLine("**** **** ${payment.last4 ?: ""}")
            appendLine("${payment.accountType ?: ""}    ${payment.brand ?: ""}")
            if (payment.isDeferredPayment) {
                appendLine(
                    if (!payment.hasInterest) "${payment.promotionType}: ${payment.numberOfPayments} MESES SIN INTERESES"
                    else "${payment.promotionType}: ${payment.numberOfPayments} PAGOS FIJOS"
                )
            }
            appendLine()
            when {
                payment.requireSignature -> appendLine("\n\n________________________\nFirma del cliente")
                payment.pinEntry -> appendLine("AUTORIZADO POR FIRMA ELECTRÓNICA")
                else -> appendLine("AUTORIZADO SIN FIRMA")
            }
            if (payment.emvType == "CONTACTLESS") appendLine("CONTACTLESS - NFC")
            appendLine()
            appendLine("AUT: ${payment.authCode ?: ""}")
            appendLine("ARQC: ${payment.ARQC ?: ""}")
            appendLine("AID: ${payment.AID ?: ""}")
            appendLine("AFILIACIÓN: ${payment.merchantIdCode ?: ""}")
            appendLine(date)
            appendLine()
            appendLine("¡GRACIAS POR TU COMPRA!")
            append("\n\n\n\n")
        }
    }

    // shortcut: fixed 384px width (58mm paper), long lines get clipped instead of wrapped
    fun toBitmap(text: String, width: Int = 384): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 18f // ~35 monospace chars per line, fits "AUTORIZADO POR FIRMA ELECTRÓNICA"
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }
        val lines = text.lines()
        val lineHeight = paint.fontSpacing
        val bitmap = Bitmap.createBitmap(width, (lines.size * lineHeight).toInt() + 1, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        lines.forEachIndexed { i, line -> canvas.drawText(line, 0f, (i + 1) * lineHeight - paint.descent(), paint) }
        return bitmap
    }

    fun print(checkServices: CheckServices, payment: PaymentResponse, amount: String, callback: PrintCallback.Stub) {
        checkServices.printBitmap(toBitmap(build(payment, amount)), callback)
    }
}
