package mx.pagandocheck.pagandodemokotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.RemoteException
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import mx.pagando.check.services.ActionNipCallback

class NipPagandoView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var tvTitle: TextView
    private lateinit var btn0: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    lateinit var pinView: TextView
    lateinit var keypad: LinearLayout
    var callback: ActionNipCallback? = null
    var pin: String = ""

    private lateinit var btnCancel: LinearLayout
    private lateinit var btnConfirm: LinearLayout
    private lateinit var btnClean: LinearLayout
    private lateinit var activity: Activity
    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_nip_pagando, this, true)
        tvTitle = view.findViewById(R.id.tvTitle)

        pinView = view.findViewById(R.id.pinView)
        keypad = view.findViewById(R.id.keypad)

        btn0 = view.findViewById(R.id.btn0)
        btn1 = view.findViewById(R.id.btn1)
        btn2 = view.findViewById(R.id.btn2)
        btn3 = view.findViewById(R.id.btn3)
        btn4 = view.findViewById(R.id.btn4)
        btn5 = view.findViewById(R.id.btn5)
        btn6 = view.findViewById(R.id.btn6)
        btn7 = view.findViewById(R.id.btn7)
        btn8 = view.findViewById(R.id.btn8)
        btn9 = view.findViewById(R.id.btn9)

        val buttons = arrayListOf(
            btn0,
            btn1,
            btn2,
            btn3,
            btn4,
            btn5,
            btn6,
            btn7,
            btn8,
            btn9
        )

        btnCancel = view.findViewById(R.id.btnCancel)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        btnClean = view.findViewById(R.id.btnClean)

        for (button in buttons) {
            button.setOnClickListener {
                if(this.callback != null) {
                    pinView.text = pinView.text.toString() + "*"
                    pin += button.text
                }
            }
        }

        btnCancel.setOnClickListener {
            if(this.callback != null) {
                pinView.text = ""
                callback!!.onCancel()
                keypad.visibility = View.INVISIBLE
            }
        }

        btnConfirm.setOnClickListener {
            if(this.callback != null) {
                callback!!.onSetPin(pin)
                pinView.text = ""
                pin = ""
                keypad.visibility = View.INVISIBLE
            }
        }

        btnClean.setOnClickListener {
            if(this.callback != null) {
                pin = ""
                pinView.text = ""
            }
        }
    }

    fun getPinLayout(): IntArray? {
        val pinPadLayoutEntity = getPinpadLayoutEntity()
        try {
            val var2 = IntArray(60)
            val var3: Byte = 0
            var var8 = var3 + 1
            var2[var3.toInt()] = pinPadLayoutEntity.key1!!.left
            var2[var8++] = pinPadLayoutEntity.key1!!.top
            var2[var8++] = pinPadLayoutEntity.key1!!.right
            var2[var8++] = pinPadLayoutEntity.key1!!.bottom
            var2[var8++] = pinPadLayoutEntity.key2!!.left
            var2[var8++] = pinPadLayoutEntity.key2!!.top
            var2[var8++] = pinPadLayoutEntity.key2!!.right
            var2[var8++] = pinPadLayoutEntity.key2!!.bottom
            var2[var8++] = pinPadLayoutEntity.key3!!.left
            var2[var8++] = pinPadLayoutEntity.key3!!.top
            var2[var8++] = pinPadLayoutEntity.key3!!.right
            var2[var8++] = pinPadLayoutEntity.key3!!.bottom
            var2[var8++] = pinPadLayoutEntity.keyCancel!!.left
            var2[var8++] = pinPadLayoutEntity.keyCancel!!.top
            var2[var8++] = pinPadLayoutEntity.keyCancel!!.right
            var2[var8++] = pinPadLayoutEntity.keyCancel!!.bottom
            var2[var8++] = pinPadLayoutEntity.key4!!.left
            var2[var8++] = pinPadLayoutEntity.key4!!.top
            var2[var8++] = pinPadLayoutEntity.key4!!.right
            var2[var8++] = pinPadLayoutEntity.key4!!.bottom
            var2[var8++] = pinPadLayoutEntity.key5!!.left
            var2[var8++] = pinPadLayoutEntity.key5!!.top
            var2[var8++] = pinPadLayoutEntity.key5!!.right
            var2[var8++] = pinPadLayoutEntity.key5!!.bottom
            var2[var8++] = pinPadLayoutEntity.key6!!.left
            var2[var8++] = pinPadLayoutEntity.key6!!.top
            var2[var8++] = pinPadLayoutEntity.key6!!.right
            var2[var8++] = pinPadLayoutEntity.key6!!.bottom
            var2[var8++] = pinPadLayoutEntity.keyClear!!.left
            var2[var8++] = pinPadLayoutEntity.keyClear!!.top
            var2[var8++] = pinPadLayoutEntity.keyClear!!.right
            var2[var8++] = pinPadLayoutEntity.keyClear!!.bottom
            var2[var8++] = pinPadLayoutEntity.key7!!.left
            var2[var8++] = pinPadLayoutEntity.key7!!.top
            var2[var8++] = pinPadLayoutEntity.key7!!.right
            var2[var8++] = pinPadLayoutEntity.key7!!.bottom
            var2[var8++] = pinPadLayoutEntity.key8!!.left
            var2[var8++] = pinPadLayoutEntity.key8!!.top
            var2[var8++] = pinPadLayoutEntity.key8!!.right
            var2[var8++] = pinPadLayoutEntity.key8!!.bottom
            var2[var8++] = pinPadLayoutEntity.key9!!.left
            var2[var8++] = pinPadLayoutEntity.key9!!.top
            var2[var8++] = pinPadLayoutEntity.key9!!.right
            var2[var8++] = pinPadLayoutEntity.key9!!.bottom
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = pinPadLayoutEntity.key10!!.left
            var2[var8++] = pinPadLayoutEntity.key10!!.top
            var2[var8++] = pinPadLayoutEntity.key10!!.right
            var2[var8++] = pinPadLayoutEntity.key10!!.bottom
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = -1
            var2[var8++] = pinPadLayoutEntity.keyConfirm!!.left
            var2[var8++] = pinPadLayoutEntity.keyConfirm!!.top
            var2[var8++] = pinPadLayoutEntity.keyConfirm!!.right
            var2[var8++] = pinPadLayoutEntity.keyConfirm!!.bottom
            var result = " "
            for (i in var2.indices) {
                result += " " + var2[i]
            }


            return var2
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getPinpadLayoutEntity(): PinPadLayoutEntity {
        keypad.visibility = View.VISIBLE

        val digits = arrayListOf(
            btn0,
            btn1,
            btn2,
            btn3,
            btn4,
            btn5,
            btn6,
            btn7,
            btn8,
            btn9
        )

        val pinPadLayout = PinPadLayoutEntity()
        digits.shuffle()

        (context as Activity).runOnUiThread {
            for ((i, digit) in digits.withIndex()) {
                digit.text = i.toString()
            }
        }

        val location = IntArray(2)
        digits[1].getLocationOnScreen(location)
        var r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[1].width + r.left
        r.bottom = digits[1].height + r.top
        pinPadLayout.key1 = r
        digits[2].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[2].width + r.left
        r.bottom = digits[2].height + r.top
        pinPadLayout.key2 = r
        digits[3].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[3].width + r.left
        r.bottom = digits[3].height + r.top
        pinPadLayout.key3 = r
        digits[4].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[4].width + r.left
        r.bottom = digits[4].height + r.top
        pinPadLayout.key4 = r
        digits[5].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[5].width + r.left
        r.bottom = digits[5].height + r.top
        pinPadLayout.key5 = r
        digits[6].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[6].width + r.left
        r.bottom = digits[6].height + r.top
        pinPadLayout.key6 = r
        digits[7].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[7].width + r.left
        r.bottom = digits[7].height + r.top
        pinPadLayout.key7 = r
        digits[8].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[8].width + r.left
        r.bottom = digits[8].height + r.top
        pinPadLayout.key8 = r
        digits[9].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[9].width + r.left
        r.bottom = digits[9].height + r.top
        pinPadLayout.key9 = r
        digits[0].getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = digits[0].width + r.left
        r.bottom = digits[0].height + r.top
        pinPadLayout.key10 = r
        btnCancel.getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = btnCancel.width + r.left
        r.bottom = btnCancel.height + r.top
        pinPadLayout.keyCancel = r
        btnClean.getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = btnClean.width + r.left
        r.bottom = btnClean.height + r.top
        pinPadLayout.keyClear = r
        btnConfirm.getLocationOnScreen(location)
        r = Rect()
        r.left = location[0]
        r.top = location[1]
        r.right = btnConfirm.width + r.left
        r.bottom = btnConfirm.height + r.top
        pinPadLayout.keyConfirm = r
        keypad.visibility = View.INVISIBLE
        return pinPadLayout
    }

    fun setTitle(text: String) {
        tvTitle.text = text
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    /**
     * Cambia la visibilidad del teclado en pantalla.
     *
     * @param flag `true` para mostrar el teclado, `false` para ocultarlo.
     */
    fun setVisibility(flag: Boolean) {
        if (flag) {
            keypad.visibility = View.VISIBLE
        } else {
            keypad.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun proccessMessage(message: String?, callback: ActionNipCallback) {
        activity.runOnUiThread {
            when (message) {

                "MESSAGE_SHOW_KEYPAD" -> keypad.visibility = View.VISIBLE
                "MESSAGE_SET_CALLBACK" ->  {
                    this.callback = callback
                    keypad.visibility = View.VISIBLE
                }

                "MESSAGE_CLOSE_KEYPAD" -> {
                    pinView.text = ""
                    keypad.visibility = View.INVISIBLE
                }

                "MESSAGE_PIN_INPUT_CLEAR" -> pinView.text = ""
                "MESSAGE_PIN_INPUT_DIGIT" -> pinView.text = pinView.text.toString() + "*"
            }
        }

    }

    init {
        init(context)
    }
}

class PinPadLayoutEntity {
    var key1: Rect? = null
    var key2: Rect? = null
    var key3: Rect? = null
    var key4: Rect? = null
    var key5: Rect? = null
    var key6: Rect? = null
    var key7: Rect? = null
    var key8: Rect? = null
    var key9: Rect? = null
    var key10: Rect? = null
    var keyCancel: Rect? = null
    var keyConfirm: Rect? = null
    var keyClear: Rect? = null
}