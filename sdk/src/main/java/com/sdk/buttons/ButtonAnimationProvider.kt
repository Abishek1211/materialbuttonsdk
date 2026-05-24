package com.sdk.buttons

import android.view.View

/**
 * Controls the motion/animation behaviour of a button.
 *
 * Implement this interface to replace, extend, or disable the default
 * press-scale and toggle-alpha animations. Assign a custom instance to
 * [BaseMaterialButton.animationProvider] at any time.
 *
 * Usage:
 * ```
 * object NoAnimation : ButtonAnimationProvider {
 *     override fun onPress(view: View)   = Unit
 *     override fun onRelease(view: View) = Unit
 *     override fun onCheckedChanged(view: View, isChecked: Boolean) = Unit
 * }
 *
 * toggleButton.animationProvider = NoAnimation
 * ```
 */
interface ButtonAnimationProvider {
    /** Called when the user presses the button down. */
    fun onPress(view: View)

    /** Called when the user lifts their finger or the press is cancelled. */
    fun onRelease(view: View)

    /**
     * Called by [ToggleButton] whenever its checked state flips.
     * @param isChecked `true` = button is now selected.
     */
    fun onCheckedChanged(view: View, isChecked: Boolean)
}

/**
 * Default implementation — subtle scale-down on press/release and a
 * smooth alpha fade for [ToggleButton] state changes.
 */
object DefaultButtonAnimationProvider : ButtonAnimationProvider {

    override fun onPress(view: View) {
        view.animate()
            .scaleX(0.96f)
            .scaleY(0.96f)
            .setDuration(80)
            .start()
    }

    override fun onRelease(view: View) {
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(100)
            .start()
    }

    override fun onCheckedChanged(view: View, isChecked: Boolean) {
        view.animate()
            .alpha(if (isChecked) 1f else 0.6f)
            .setDuration(150)
            .start()
    }
}

