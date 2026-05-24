package com.sdk.buttons

import android.content.Context
import android.util.AttributeSet

open class FilledButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseMaterialButton(context, attrs) {

    init {
        applyStyle(FilledButtonStyleProvider())
    }
}