package com.sdk.buttons

import android.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Defines a brand colour palette that [ButtonStyleProvider] implementations
 * can reference. Swap in a custom [ThemeProvider] to restyle every button
 * in your app at once without touching individual button classes.
 *
 * Usage:
 * ```
 * object MyAppTheme : ThemeProvider {
 *     override val primaryColor     = Color.parseColor("#00897B")
 *     override val onPrimaryColor   = Color.WHITE
 *     override val secondaryColor   = Color.parseColor("#26A69A")
 *     override val onSecondaryColor = Color.WHITE
 *     override val surfaceColor     = Color.WHITE
 *     override val onSurfaceColor   = Color.parseColor("#1C1B1E")
 *     override val backgroundColor  = Color.parseColor("#E0F2F1")
 * }
 *
 * button.applyStyle(FilledButtonStyleProvider(MyAppTheme))
 * ```
 */
interface ThemeProvider {
    /** Brand primary colour — used as button fill / stroke / text. */
    val primaryColor: Int
    /** Colour of content (text/icons) on top of [primaryColor]. */
    val onPrimaryColor: Int
    /** Secondary brand colour. */
    val secondaryColor: Int
    /** Colour of content on top of [secondaryColor]. */
    val onSecondaryColor: Int
    /** Surface/card background colour. */
    val surfaceColor: Int
    /** Colour of content on top of [surfaceColor]. */
    val onSurfaceColor: Int
    /** App background colour. */
    val backgroundColor: Int
}

/**
 * Default Material 3 colour palette (purple seed `#6750A4`).
 * Used automatically when no custom [ThemeProvider] is supplied.
 */
object DefaultThemeProvider : ThemeProvider {
    override val primaryColor     = "#6750A4".toColorInt()
    override val onPrimaryColor   = Color.WHITE
    override val secondaryColor   = "#625B71".toColorInt()
    override val onSecondaryColor = Color.WHITE
    override val surfaceColor     = Color.WHITE
    override val onSurfaceColor   = "#1C1B1E".toColorInt()
    override val backgroundColor  = "#F6F0FF".toColorInt()
}

