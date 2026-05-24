package com.sdk.buttons

import android.graphics.Color

/**
 * Default [ButtonStyleProvider] for [TextButton].
 * No background, no elevation, primary-colour label — lowest visual emphasis.
 *
 * Pass a custom [ThemeProvider] to restyle using your own colour palette:
 * ```
 * textButton.applyStyle(TextButtonStyleProvider(MyAppTheme))
 * ```
 */
class TextButtonStyleProvider(
    theme: ThemeProvider = DefaultThemeProvider
) : ButtonStyleProvider {
    override val backgroundColor = Color.TRANSPARENT
    override val textColor       = theme.primaryColor
    override val cornerRadiusPx  = 24
    override val strokeWidthPx   = 0
    override val strokeColor     = Color.TRANSPARENT
    override val elevationPx     = 0f
    override val iconPaddingPx   = 16
}

