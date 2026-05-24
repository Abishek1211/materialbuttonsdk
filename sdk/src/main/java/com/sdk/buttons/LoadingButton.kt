package com.sdk.buttons

import android.content.Context
import android.util.AttributeSet

/**
 * A [FilledButton] that shows a spinning progress indicator while an async
 * operation is in progress.
 *
 * Usage:
 * ```kotlin
 * button.setLoading(true)           // show spinner, disable button
 * button.setLoading(false)          // restore label, re-enable button
 * button.loadingText = "Saving…"    // optional text shown during loading
 * ```
 *
 * Via [ButtonFactory]:
 * ```kotlin
 * val btn = ButtonFactory.createLoading(context)
 *     .setText("Submit")
 *     .setLoadingText("Submitting…")
 *     .setOnClick { submit() }
 *     .build()
 * ```
 */
class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FilledButton(context, attrs) {

    /** Text shown while [isLoading] is `true`. Empty string shows only the spinner. */
    var loadingText: String = ""

    private var _isLoading = false

    private val progressDrawable by lazy {
        CircularButtonProgressDrawable(android.graphics.Color.WHITE).apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
    }

    private var originalText: CharSequence = ""

    /**
     * Toggle the loading state.
     *
     * - `true`  → replaces label with [loadingText], shows spinner icon, disables button
     * - `false` → restores original label, removes spinner, re-enables button
     */
    fun setLoading(loading: Boolean) {
        if (_isLoading == loading) return
        _isLoading = loading

        if (loading) {
            originalText = text
            text = loadingText
            icon = progressDrawable
            iconGravity = ICON_GRAVITY_TEXT_START
            progressDrawable.start()
            isEnabled = false
        } else {
            progressDrawable.stop()
            icon = null
            text = originalText
            isEnabled = true
        }
    }

    fun isLoading(): Boolean = _isLoading

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (_isLoading) progressDrawable.stop()
    }
}

