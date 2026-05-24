package com.sdk.buttons

import androidx.core.graphics.toColorInt

/**
 * A [ThemeProvider] tuned for dark mode with Material 3 dark-scheme colours.
 *
 * Usage:
 * ```kotlin
 * button.applyStyle(FilledButtonStyleProvider(DarkThemeProvider))
 * ```
 */
object DarkThemeProvider : ThemeProvider {
    override val primaryColor     = "#D0BCFF".toColorInt()  // MD3 dark primary
    override val onPrimaryColor   = "#381E72".toColorInt()
    override val secondaryColor   = "#CCC2DC".toColorInt()
    override val onSecondaryColor = "#332D41".toColorInt()
    override val surfaceColor     = "#1C1B1F".toColorInt()
    override val onSurfaceColor   = "#E6E1E5".toColorInt()
    override val backgroundColor  = "#1C1B1F".toColorInt()
}

