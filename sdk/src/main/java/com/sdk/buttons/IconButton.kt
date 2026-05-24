package com.sdk.buttons

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes

/**
 * A square icon-only button with no label text.
 *
 * Inherits the filled style and all provider hooks from [FilledButton].
 * Swap [animationProvider] or call [applyStyle] to customise.
 *
 * Usage in code:
 * ```kotlin
 * val btn = ButtonFactory.createIcon(context)
 *     .setIcon(R.drawable.ic_add)
 *     .setOnClick { showAddDialog() }
 *     .setAccessibilityLabel("Add item")
 *     .build()
 * ```
 *
 * Usage in XML:
 * ```xml
 * <com.sdk.buttons.IconButton
 *     android:layout_width="48dp"
 *     android:layout_height="48dp"
 *     app:sdkIconDrawable="@drawable/ic_add"
 *     android:contentDescription="Add item"/>
 * ```
 */
class IconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FilledButton(context, attrs) {

    init {
        text        = ""
        iconPadding = 0
        iconGravity = ICON_GRAVITY_TEXT_START
        // Remove horizontal insets so the button can be truly square
        val p = (12 * resources.displayMetrics.density).toInt()
        setPaddingRelative(p, p, p, p)
    }

    /** Set the icon displayed in the centre of this button. */
    fun setButtonIcon(@DrawableRes iconRes: Int) {
        setIconResource(iconRes)
    }
}


