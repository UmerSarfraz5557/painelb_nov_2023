package br.com.painelb.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialAutoCompleteTextView;

public class TextInputMaterialAutoCompleteTextView extends MaterialAutoCompleteTextView {
    private Boolean isPopup = false;

    public TextInputMaterialAutoCompleteTextView(@NonNull Context context) {
        super(context);
    }

    public TextInputMaterialAutoCompleteTextView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TextInputMaterialAutoCompleteTextView(@NonNull Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null)
                mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            setKeyListener(null);
            if (!isPopup) {
                showDropDown();
            } else {
                dismissDropDown();
            }
        } else {
            isPopup = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isPopup) {
                dismissDropDown();
            } else {
                requestFocus();
                showDropDown();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void showDropDown() {
        super.showDropDown();
    }

    @Override
    public void dismissDropDown() {
        super.dismissDropDown();
    }
}
