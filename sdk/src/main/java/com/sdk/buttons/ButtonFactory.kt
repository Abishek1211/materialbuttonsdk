package com.sdk.buttons

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.DrawableRes

/**
 * Factory with a fluent builder API for creating SDK buttons programmatically.
 *
 * All builders follow the same pattern:
 * ```kotlin
 * val button = ButtonFactory.createFilled(context)
 *     .setText("Login")
 *     .setIcon(R.drawable.ic_login)
 *     .setStyle(FilledButtonStyleProvider(DynamicThemeProvider(context)))
 *     .setOnClick { startLogin() }
 *     .setAccessibilityLabel("Login button")
 *     .build()
 * ```
 */
object ButtonFactory {
    fun createFilled(context: Context)   = FilledButtonBuilder(context)
    fun createOutlined(context: Context) = OutlinedButtonBuilder(context)
    fun createText(context: Context)     = TextButtonBuilder(context)
    fun createToggle(context: Context)   = ToggleButtonBuilder(context)
    fun createIcon(context: Context)     = IconButtonBuilder(context)
    fun createLoading(context: Context)  = LoadingButtonBuilder(context)
    fun createGradient(context: Context) = GradientButtonBuilder(context)
}

// ─── Base Builder ─────────────────────────────────────────────────────────────

/**
 * Shared fluent builder base for all SDK button types.
 * The `build()` function creates and returns the configured button.
 *
 * @param T the concrete button type produced by this builder
 */
abstract class SdkButtonBuilder<T : BaseMaterialButton>(
    protected val context: Context
) {
    protected var labelText:     String                   = ""
    protected var iconRes:       Int                      = 0
    protected var enabled:       Boolean                  = true
    protected var styleOverride: ButtonStyleProvider?     = null
    protected var animProvider:  ButtonAnimationProvider  = DefaultButtonAnimationProvider
    protected var clickListener: View.OnClickListener?    = null
    protected var accessLabel:   String?                  = null

    /** Set the button label. */
    fun setText(text: String)                               = apply { labelText = text }

    /** Set a drawable icon shown alongside the label. */
    fun setIcon(@DrawableRes iconRes: Int)                  = apply { this.iconRes = iconRes }

    /** Enable or disable the button. Default: `true`. */
    fun setEnabled(enabled: Boolean)                        = apply { this.enabled = enabled }

    /**
     * Override the default [ButtonStyleProvider].
     * Pass a custom implementation or a built-in provider with a custom [ThemeProvider]:
     * ```kotlin
     * .setStyle(FilledButtonStyleProvider(DynamicThemeProvider(context)))
     * ```
     */
    fun setStyle(provider: ButtonStyleProvider)             = apply { styleOverride = provider }

    /** Override the default [ButtonAnimationProvider]. */
    fun setAnimation(provider: ButtonAnimationProvider)     = apply { animProvider = provider }

    /** Set a click listener using a lambda. */
    fun setOnClick(action: () -> Unit)                      = apply { clickListener = View.OnClickListener { action() } }

    /** Set a click listener using [View.OnClickListener]. */
    fun setOnClick(listener: View.OnClickListener)          = apply { clickListener = listener }

    /**
     * Provide a content description for screen readers.
     * If omitted, the button text is used automatically by Android.
     */
    fun setAccessibilityLabel(label: String)                = apply { accessLabel = label }

    /** Apply all common properties to the constructed button. */
    protected fun applyCommon(button: T) {
        button.text = labelText
        if (iconRes != 0) button.setIconResource(iconRes)
        button.isEnabled        = enabled
        button.animationProvider = animProvider
        styleOverride?.let { button.applyStyle(it) }
        clickListener?.let  { button.setOnClickListener(it) }
        accessLabel?.let    { button.contentDescription = it }
    }

    /** Build and return the configured button instance. */
    abstract fun build(): T
}

// ─── Concrete Builders ────────────────────────────────────────────────────────

class FilledButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<FilledButton>(context) {
    override fun build() = FilledButton(context).also { applyCommon(it) }
}

class OutlinedButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<OutlinedButton>(context) {
    override fun build() = OutlinedButton(context).also { applyCommon(it) }
}

class TextButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<TextButton>(context) {
    override fun build() = TextButton(context).also { applyCommon(it) }
}

class ToggleButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<ToggleButton>(context) {
    override fun build() = ToggleButton(context).also { applyCommon(it) }
}

class IconButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<IconButton>(context) {
    override fun build() = IconButton(context).also { btn ->
        applyCommon(btn)
        if (iconRes != 0) btn.setButtonIcon(iconRes)
    }
}

// ─── Loading Button Builder ───────────────────────────────────────────────────

class LoadingButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<LoadingButton>(context) {

    private var loadingText: String = ""

    /** Text shown while the button is in loading state. Default: empty (spinner only). */
    fun setLoadingText(text: String) = apply { loadingText = text }

    override fun build() = LoadingButton(context).also { btn ->
        applyCommon(btn)
        btn.loadingText = loadingText
    }
}

// ─── Gradient Button Builder ──────────────────────────────────────────────────

class GradientButtonBuilder internal constructor(context: Context)
    : SdkButtonBuilder<GradientButton>(context) {

    private var startColor  = android.graphics.Color.parseColor("#6750A4")
    private var endColor    = android.graphics.Color.parseColor("#9C27B0")
    private var orientation = GradientDrawable.Orientation.LEFT_RIGHT

    /** Set the start and end gradient colours. */
    fun setColors(start: Int, end: Int)                         = apply { startColor = start; endColor = end }

    /** Set the gradient direction. Default: left → right. */
    fun setOrientation(o: GradientDrawable.Orientation)         = apply { orientation = o }

    override fun build() = GradientButton(context).also { btn ->
        applyCommon(btn)
        btn.setGradient(startColor, endColor, orientation)
    }
}

