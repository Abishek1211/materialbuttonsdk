package com.sdk.buttons

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SegmentedButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        orientation = HORIZONTAL
    }

    fun addSegment(button: BaseMaterialButton) {
        val params = LayoutParams(0, LayoutParams.WRAP_CONTENT)
        params.weight = 1f
        addView(button, params)
    }
}