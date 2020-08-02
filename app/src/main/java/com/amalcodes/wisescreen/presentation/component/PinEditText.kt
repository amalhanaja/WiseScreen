package com.amalcodes.wisescreen.presentation.component

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText
import com.mukesh.OtpView


class PinEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : OtpView(context, attrs, defStyleAttr) {
    init {
        isFocusableInTouchMode = true
    }
}