package br.com.painelb.widgets

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TimePicker
import br.com.painelb.util.getTimeFormat
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class TimePickerTextInputEditText : TextInputEditText, TimePickerDialog.OnTimeSetListener,
    DialogInterface.OnDismissListener {
    private var isPopup = false
    lateinit var mTimePickerDialog: TimePickerDialog

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val mCalendar = Calendar.getInstance()
        mTimePickerDialog = TimePickerDialog(
            context,
            this,
            mCalendar[Calendar.HOUR_OF_DAY],
            mCalendar[Calendar.MINUTE],
            true
        )
        mTimePickerDialog.setOnDismissListener(this)
    }

    override fun onFocusChanged(
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            val mInputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            keyListener = null
            if (!isPopup) {
                showDropDown()
            } else {
                dismissDropDown()
            }
        } else {
            isPopup = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (isPopup) {
                    dismissDropDown()
                } else {
                    requestFocus()
                    showDropDown()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun showDropDown() {
        mTimePickerDialog.show()
        isPopup = true
    }

    private fun dismissDropDown() {
        mTimePickerDialog.dismiss()
        isPopup = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        isPopup = false
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val dateFormat = context.applicationContext.getTimeFormat(hourOfDay,minute)
        setText(dateFormat)
    }
}