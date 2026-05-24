package com.sdk.buttons

import android.content.Context
import android.util.AttributeSet

class ToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FilledButton(context, attrs) {

    private var checkedState = false

    /**
     * Overriding [performClick] — rather than [setOnClickListener] — ensures
     * the state always flips and the [animationProvider] is always notified,
     * even when an external click listener is attached by the caller.
     */
    override fun performClick(): Boolean {
        checkedState = !checkedState
        animationProvider.onCheckedChanged(this, checkedState)
        return super.performClick()
    }

    fun isCheckedState(): Boolean = checkedState
}