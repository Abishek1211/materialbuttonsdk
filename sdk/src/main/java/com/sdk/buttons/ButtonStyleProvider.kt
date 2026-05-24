package com.sdk.buttons

import android.graphics.Color

/**
 * Defines the complete visual appearance of a button.
 *
 * Implement this interface to create a fully custom button style, then
 * apply it at any time via [BaseMaterialButton.applyStyle] — even at
 * runtime to support dynamic theme switching.
 *
 * Usage:
 * ```
 * class BrandFilledStyle : ButtonStyleProvider {
 *     override val backgroundColor = Color.parseColor("#FF5722")
 *     override val textColor       = Color.WHITE
 *     override val cornerRadiusPx  = 8
 *     override val strokeWidthPx   = 0
 *     override val strokeColor     = Color.TRANSPARENT
 *     override val elevationPx     = 4f
 *     override val iconPaddingPx   = 12
 * }
 *
 * filledButton.applyStyle(BrandFilledStyle())
 * ```
 */
interface ButtonStyleProvider {
    /** Fill colour of the button background. Use [Color.TRANSPARENT] for no fill. */
    val backgroundColor: Int
    /** Colour of the button label text. */
    val textColor: Int
    /** Corner radius in pixels — 24px produces a pill shape. */
    val cornerRadiusPx: Int
    /** Stroke (border) width in pixels. Set to 0 for no stroke. */
    val strokeWidthPx: Int
    /** Stroke border colour. Ignored when [strokeWidthPx] is 0. */
    val strokeColor: Int
    /** Drop-shadow elevation in pixels. */
    val elevationPx: Float
    /** Spacing between the optional icon and the label text, in pixels. */
    val iconPaddingPx: Int
}

