package com.sdk.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import androidx.core.graphics.toColorInt

/**
 * A button with a two-colour gradient background and a ripple overlay.
 *
 * All other [BaseMaterialButton] features (animations, accessibility, XML attrs)
 * still apply. The gradient overrides the fill colour set by [FilledButtonStyleProvider].
 *
 * Usage in code:
 * ```kotlin
 * val btn = ButtonFactory.createGradient(context)
 *     .setText("Get Started")
 *     .setColors(Color.parseColor("#6750A4"), Color.parseColor("#9C27B0"))
 *     .setOrientation(GradientDrawable.Orientation.TL_BR)
 *     .build()
 * ```
 *
 * Usage in XML (default purple → deep-purple gradient):
 * ```xml
 * <com.sdk.buttons.GradientButton
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:text="Get Started"/>
 * ```
 */
class GradientButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseMaterialButton(context, attrs) {

    private var startColor  = "#6750A4".toColorInt()
    private var endColor    = "#9C27B0".toColorInt()
    private var orientation = GradientDrawable.Orientation.LEFT_RIGHT

    init {
        // Apply text colour, padding and animation defaults from FilledButtonStyleProvider.
        // The background will be overridden by applyGradientBackground() below.
        applyStyle(FilledButtonStyleProvider())
        // Run after the full constructor chain so cornerRadius is finalised.
        post { applyGradientBackground() }
    }

    /**
     * Set the gradient start/end colours and direction.
     * Can be called at any time to update the gradient.
     */
    fun setGradient(
        startColor:  Int,
        endColor:    Int,
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    ) {
        this.startColor  = startColor
        this.endColor    = endColor
        this.orientation = orientation
        applyGradientBackground()
    }

    private fun applyGradientBackground() {
        val gradient = GradientDrawable(orientation, intArrayOf(startColor, endColor)).apply {
            cornerRadius = this@GradientButton.cornerRadius.toFloat()
        }
        // Wrap with a RippleDrawable so press feedback still works.
        val ripple = RippleDrawable(
            ColorStateList.valueOf(Color.argb(80, 255, 255, 255)),
            gradient,
            null
        )
        background = ripple
    }
}

