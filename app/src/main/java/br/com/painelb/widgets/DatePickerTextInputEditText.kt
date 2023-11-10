package br.com.painelb.widgets

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import br.com.painelb.util.getDateFormat
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class DatePickerTextInputEditText : TextInputEditText, OnDateSetListener,
    DialogInterface.OnDismissListener {
    private var isPopup = false
    lateinit var mDatePickerDialog: DatePickerDialog

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
        mDatePickerDialog = DatePickerDialog(
            context, this,
            mCalendar[Calendar.YEAR],
            mCalendar[Calendar.MONTH],
            mCalendar[Calendar.DAY_OF_MONTH]
        )
        mDatePickerDialog.setOnDismissListener(this)
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
        mDatePickerDialog.show()
        isPopup = true
    }

    private fun dismissDropDown() {
        mDatePickerDialog.dismiss()
        isPopup = false
    }

    override fun onDateSet(
        view: DatePicker,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val mCalendar = Calendar.getInstance()
        mCalendar.set(year, month, dayOfMonth)
        val realValue = mCalendar.timeInMillis
        val dateFormat = context.applicationContext.getDateFormat(realValue)
        tag = realValue
        setText(dateFormat)
    }

    override fun onDismiss(dialog: DialogInterface) {
        isPopup = false
    }
}