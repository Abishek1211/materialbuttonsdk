package com.sdk.buttons

import android.graphics.Color

/**
 * Default [ButtonStyleProvider] for [OutlinedButton].
 * Transparent background with a primary-colour stroke and label.
 *
 * Pass a custom [ThemeProvider] to restyle using your own colour palette:
 * ```
 * outlinedButton.applyStyle(OutlinedButtonStyleProvider(MyAppTheme))
 * ```
 */
class OutlinedButtonStyleProvider(
    theme: ThemeProvider = DefaultThemeProvider
) : ButtonStyleProvider {
    override val backgroundColor = Color.TRANSPARENT
    override val textColor       = theme.primaryColor
    override val cornerRadiusPx  = 24
    override val strokeWidthPx   = 3
    override val strokeColor     = theme.primaryColor
    override val elevationPx     = 0f
    override val iconPaddingPx   = 16
}

