package com.sdk.buttons
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.button.MaterialButton
open class BaseMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {
    /**
     * Controls press / release and state-change animations.
     * Replace at any time with a custom [ButtonAnimationProvider]:
     * ```kotlin
     * button.animationProvider = NoAnimation
     * ```
     */
    var animationProvider: ButtonAnimationProvider = DefaultButtonAnimationProvider
    // XML attribute overrides — stored during init, applied after subclass inits via post{}
    private var xmlCornerRadiusPx:  Int? = null
    private var xmlBackgroundColor: Int? = null
    private var xmlTextColor:       Int? = null
    private var xmlStrokeWidthPx:   Int? = null
    private var xmlStrokeColor:     Int? = null
    init {
        iconPadding = 16
        insetTop    = 0
        insetBottom = 0
        isAllCaps   = false
        isFocusable = true   // support keyboard / D-pad navigation
        // Delegate touch feedback to the animation provider.
        // Return false so click listeners still fire normally.
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN                   -> animationProvider.onPress(v)
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL                 -> animationProvider.onRelease(v)
            }
            false
        }
        // Read custom XML attributes and schedule application AFTER the full
        // constructor chain via post{}, so XML values override subclass styles.
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SdkButton)
            try {
                val cr  = a.getDimensionPixelSize(R.styleable.SdkButton_sdkCornerRadius, -1)
                val bc  = a.getColor(R.styleable.SdkButton_sdkButtonColor,  Int.MIN_VALUE)
                val tc  = a.getColor(R.styleable.SdkButton_sdkTextColor,    Int.MIN_VALUE)
                val sw  = a.getDimensionPixelSize(R.styleable.SdkButton_sdkStrokeWidth, -1)
                val sc  = a.getColor(R.styleable.SdkButton_sdkStrokeColor,  Int.MIN_VALUE)
                val ico = a.getResourceId(R.styleable.SdkButton_sdkIconDrawable, 0)
                if (cr  != -1)            xmlCornerRadiusPx  = cr
                if (bc  != Int.MIN_VALUE) xmlBackgroundColor = bc
                if (tc  != Int.MIN_VALUE) xmlTextColor       = tc
                if (sw  != -1)            xmlStrokeWidthPx   = sw
                if (sc  != Int.MIN_VALUE) xmlStrokeColor     = sc
                if (ico != 0)             setIconResource(ico)
            } finally {
                a.recycle()
            }
            post { applyXmlOverrides() }
        }
    }
    private fun applyXmlOverrides() {
        xmlCornerRadiusPx?.let  { cornerRadius = it }
        xmlBackgroundColor?.let { setBackgroundColor(it) }
        xmlTextColor?.let       { setTextColor(it) }
        xmlStrokeWidthPx?.let   { strokeWidth = it }
        xmlStrokeColor?.let     { strokeColor = ColorStateList.valueOf(it) }
    }
    /**
     * Apply a [ButtonStyleProvider] to update this button's visual appearance.
     * Safe to call at any time to support runtime theme switching.
     *
     * ```kotlin
     * button.applyStyle(FilledButtonStyleProvider(DynamicThemeProvider(context)))
     * ```
     */
    fun applyStyle(style: ButtonStyleProvider) {
        setBackgroundColor(style.backgroundColor)
        setTextColor(style.textColor)
        cornerRadius = style.cornerRadiusPx
        iconPadding  = style.iconPaddingPx
        elevation    = style.elevationPx
        strokeWidth  = style.strokeWidthPx
        strokeColor  = ColorStateList.valueOf(
            if (style.strokeWidthPx > 0) style.strokeColor
            else android.graphics.Color.TRANSPARENT
        )
    }
    /**
     * Provide a content description for screen readers.
     * If omitted, Android uses the button label text automatically.
     */
    fun setAccessibilityLabel(label: String) {
        contentDescription = label
    }
    /**
     * Enforce the WCAG 2.1 minimum 48x48 dp touch target.
     * Call this when the button is displayed smaller than 48dp.
     */
    fun enforceMinTouchTarget() {
        val min = (48 * resources.displayMetrics.density).toInt()
        if (minimumWidth  < min) minimumWidth  = min
        if (minimumHeight < min) minimumHeight = min
    }
}
