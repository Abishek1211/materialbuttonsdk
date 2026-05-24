package com.sdk.buttons

import android.content.Context

/**
 * A [ThemeProvider] that reads colours live from the Activity/Application theme.
 *
 * This automatically supports:
 *  - **Material You** dynamic colours (Android 12+) when the host app calls
 *    `DynamicColors.applyToActivitiesIfAvailable(this)` in Application.onCreate
 *  - **Dark mode** — theme attributes resolve to dark-scheme values automatically
 *  - Any custom `Theme.MaterialComponents` or `Theme.Material3` theme
 *
 * Usage:
 * ```kotlin
 * // In your Activity / Fragment:
 * val theme = DynamicThemeProvider(requireContext())
 * filledButton.applyStyle(FilledButtonStyleProvider(theme))
 * outlinedButton.applyStyle(OutlinedButtonStyleProvider(theme))
 * ```
 */
class DynamicThemeProvider(context: Context) : ThemeProvider {

    override val primaryColor:     Int
    override val onPrimaryColor:   Int
    override val secondaryColor:   Int
    override val onSecondaryColor: Int
    override val surfaceColor:     Int
    override val onSurfaceColor:   Int
    override val backgroundColor:  Int

    init {
        val attrs = intArrayOf(
            com.google.android.material.R.attr.colorPrimary,
            com.google.android.material.R.attr.colorOnPrimary,
            com.google.android.material.R.attr.colorSecondary,
            com.google.android.material.R.attr.colorOnSecondary,
            com.google.android.material.R.attr.colorSurface,
            com.google.android.material.R.attr.colorOnSurface,
            android.R.attr.colorBackground
        )
        val ta = context.obtainStyledAttributes(attrs)
        primaryColor     = ta.getColor(0, DefaultThemeProvider.primaryColor)
        onPrimaryColor   = ta.getColor(1, DefaultThemeProvider.onPrimaryColor)
        secondaryColor   = ta.getColor(2, DefaultThemeProvider.secondaryColor)
        onSecondaryColor = ta.getColor(3, DefaultThemeProvider.onSecondaryColor)
        surfaceColor     = ta.getColor(4, DefaultThemeProvider.surfaceColor)
        onSurfaceColor   = ta.getColor(5, DefaultThemeProvider.onSurfaceColor)
        backgroundColor  = ta.getColor(6, DefaultThemeProvider.backgroundColor)
        ta.recycle()
    }
}

