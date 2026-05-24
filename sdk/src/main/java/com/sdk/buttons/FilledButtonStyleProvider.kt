package com.sdk.buttons

import android.graphics.Color

/**
 * Default [ButtonStyleProvider] for [FilledButton].
 * Solid primary-colour background with white text.
 *
 * Pass a custom [ThemeProvider] to restyle using your own colour palette:
 * ```
 * filledButton.applyStyle(FilledButtonStyleProvider(MyAppTheme))
 * ```
 */
class FilledButtonStyleProvider(
    theme: ThemeProvider = DefaultThemeProvider
) : ButtonStyleProvider {
    override val backgroundColor = theme.primaryColor
    override val textColor       = theme.onPrimaryColor
    override val cornerRadiusPx  = 24
    override val strokeWidthPx   = 0
    override val strokeColor     = Color.TRANSPARENT
    override val elevationPx     = 2f
    override val iconPaddingPx   = 16
}

