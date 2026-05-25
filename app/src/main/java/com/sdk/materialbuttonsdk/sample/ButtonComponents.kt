package com.sdk.materialbuttonsdk.sample

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sdk.buttons.*
import com.sdk.materialbuttonsdk.sample.ui.theme.MaterialButtonSdkTheme
import androidx.core.graphics.toColorInt

// ─── Filled Button ───────────────────────────────────────────────────────────

@Composable
fun FilledButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            FilledButton(ctx).apply {
                this.text = text
                isEnabled = enabled
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.text = text
            view.isEnabled = enabled
        },
        modifier = modifier
    )
}

// ─── Outlined Button ─────────────────────────────────────────────────────────

@Composable
fun OutlinedButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            OutlinedButton(ctx).apply {
                this.text = text
                isEnabled = enabled
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.text = text
            view.isEnabled = enabled
        },
        modifier = modifier
    )
}

// ─── Text Button ─────────────────────────────────────────────────────────────

@Composable
fun TextButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            TextButton(ctx).apply {
                this.text = text
                isEnabled = enabled
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.text = text
            view.isEnabled = enabled
        },
        modifier = modifier
    )
}

// ─── Toggle Button ────────────────────────────────────────────────────────────

@Composable
fun ToggleButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (isChecked: Boolean) -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            ToggleButton(ctx).apply {
                this.text = text
                setOnClickListener { onClick(isCheckedState()) }
            }
        },
        update = { view -> view.text = text },
        modifier = modifier
    )
}

// ─── Segmented Button Group ───────────────────────────────────────────────────

@Composable
fun SegmentedButtonGroupComponent(
    segments: List<String>,
    modifier: Modifier = Modifier,
    onSegmentClick: (index: Int, label: String) -> Unit = { _, _ -> }
) {
    AndroidView(
        factory = { ctx ->
            SegmentedButtonGroup(ctx).also { group ->
                segments.forEachIndexed { index, label ->
                    val btn = FilledButton(ctx).apply {
                        text = label
                        setOnClickListener { onSegmentClick(index, label) }
                    }
                    group.addSegment(btn)
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

// ─── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

// ─── Full Button Showcase ─────────────────────────────────────────────────────

@Composable
fun ButtonShowcase(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Material Button SDK",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Button component showcase",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Filled Button ──
        SectionHeader("Filled Button")
        FilledButtonComponent(
            text = "Filled Button",
            modifier = Modifier.fillMaxWidth()
        )

        // ── Outlined Button ──
        SectionHeader("Outlined Button")
        OutlinedButtonComponent(
            text = "Outlined Button",
            modifier = Modifier.fillMaxWidth()
        )

        // ── Text Button ──
        SectionHeader("Text Button")
        TextButtonComponent(
            text = "Text Button",
            modifier = Modifier.fillMaxWidth()
        )

        // ── Toggle Buttons ──
        SectionHeader("Toggle Buttons")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToggleButtonComponent(
                text = "Option A",
                modifier = Modifier.weight(1f)
            )
            ToggleButtonComponent(
                text = "Option B",
                modifier = Modifier.weight(1f)
            )
        }

        // ── Segmented Button Group ──
        SectionHeader("Segmented Button Group")
        SegmentedButtonGroupComponent(
            segments = listOf("Day", "Week", "Month"),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Disabled States ──
        SectionHeader("Disabled States")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledButtonComponent(
                text = "Filled",
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            OutlinedButtonComponent(
                text = "Outlined",
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            TextButtonComponent(
                text = "Text",
                enabled = false,
                modifier = Modifier.weight(1f)
            )
        }

        // ── Loading Button ──
        SectionHeader("Loading Button")
        var loading by remember { mutableStateOf(false) }
        LoadingButtonComponent(
            text         = if (loading) "" else "Submit",
            isLoading    = loading,
            loadingText  = "Saving…",
            modifier     = Modifier.fillMaxWidth(),
            onClick      = { loading = !loading }
        )

        // ── Gradient Button ──
        SectionHeader("Gradient Button")
        GradientButtonComponent(
            text        = "Get Started",
            startColor  = "#6750A4".toColorInt(),
            endColor    = "#9C27B0".toColorInt(),
            modifier    = Modifier.fillMaxWidth()
        )
        GradientButtonComponent(
            text        = "Sunrise",
            startColor  = "#FF6B35".toColorInt(),
            endColor    = "#F7C59F".toColorInt(),
            orientation = GradientDrawable.Orientation.LEFT_RIGHT,
            modifier    = Modifier.fillMaxWidth()
        )

        // ── Dark Theme ──
        SectionHeader("Dark Theme (DarkThemeProvider)")
        DarkThemeButtonRowComponent()

        // ── Provider Use-Cases ──
        SectionHeader("Provider API Use-Cases")
        Text(
            text = "ButtonStyleProvider · ThemeProvider · ButtonAnimationProvider",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(2.dp))
        CustomStyleButtonComponent(
            text     = "Brand Style (BrandFilledStyle)",
            style    = BrandFilledStyle(),
            modifier = Modifier.fillMaxWidth()
        )
        ThemedButtonRowComponent(theme = TealBrandTheme)
        AnimationProviderButtonRowComponent()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Loading Button ───────────────────────────────────────────────────────────

@Composable
fun LoadingButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    loadingText: String = "",
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            LoadingButton(ctx).apply {
                this.text = text
                this.loadingText = loadingText
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            if (!view.isLoading()) view.text = text
            view.loadingText = loadingText
            view.setLoading(isLoading)
        },
        modifier = modifier
    )
}

// ─── Gradient Button ──────────────────────────────────────────────────────────

@Composable
fun GradientButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    startColor: Int = "#6750A4".toColorInt(),
    endColor: Int   = "#9C27B0".toColorInt(),
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            GradientButton(ctx).apply {
                this.text = text
                setGradient(startColor, endColor, orientation)
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.text = text
            view.setGradient(startColor, endColor, orientation)
        },
        modifier = modifier
    )
}

// ─── Icon Button ──────────────────────────────────────────────────────────────

@Composable
fun IconButtonComponent(
    iconRes: Int,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            IconButton(ctx).apply {
                setButtonIcon(iconRes)
                this.contentDescription = contentDescription
                isEnabled = enabled
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.isEnabled = enabled
        },
        modifier = modifier
    )
}

// ═══════════════════════════════════════════════════════════════════════════
//  PROVIDER USE-CASE COMPONENTS
//  These show clients exactly how to consume ButtonStyleProvider,
//  ThemeProvider, and ButtonAnimationProvider from the SDK.
// ═══════════════════════════════════════════════════════════════════════════

// ─── Use-Case 1 : ButtonStyleProvider — Custom Brand Style ────────────────
//
// A client defines their own ButtonStyleProvider to paint a button in
// their brand colours without touching the SDK internals.

/** Deep-orange brand style — implements ButtonStyleProvider directly. */
private class BrandFilledStyle : ButtonStyleProvider {
    override val backgroundColor = "#E65100".toColorInt()   // deep-orange
    override val textColor       = Color.WHITE
    override val cornerRadiusPx  = 8                        // squared corners
    override val strokeWidthPx   = 0
    override val strokeColor     = Color.TRANSPARENT
    override val elevationPx     = 4f
    override val iconPaddingPx   = 12
}

/** Teal outlined style — no fill, coloured stroke and text. */
private class BrandOutlinedStyle : ButtonStyleProvider {
    override val backgroundColor = Color.TRANSPARENT
    override val textColor       = "#00796B".toColorInt()   // teal
    override val cornerRadiusPx  = 4
    override val strokeWidthPx   = 3
    override val strokeColor     = "#00796B".toColorInt()
    override val elevationPx     = 0f
    override val iconPaddingPx   = 12
}

/**
 * HOW A CLIENT USES [ButtonStyleProvider]:
 * Create a [FilledButton] and call [BaseMaterialButton.applyStyle] with your
 * custom [ButtonStyleProvider] implementation.
 */
@Composable
fun CustomStyleButtonComponent(
    text: String,
    style: ButtonStyleProvider,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    AndroidView(
        factory = { ctx ->
            FilledButton(ctx).apply {
                this.text = text
                applyStyle(style)                  // ← the SDK hook
                setOnClickListener { onClick() }
            }
        },
        update = { view ->
            view.text = text
            view.applyStyle(style)
        },
        modifier = modifier
    )
}

// ─── Use-Case 2 : ThemeProvider — Swap Entire Colour Palette ─────────────
//
// A client defines a ThemeProvider for their brand, then passes it to every
// style provider so all buttons share one source of truth.

/** Teal brand theme — swap this singleton to restyle every button at once. */
private object TealBrandTheme : ThemeProvider {
    override val primaryColor     = "#00796B".toColorInt()
    override val onPrimaryColor   = Color.WHITE
    override val secondaryColor   = "#26A69A".toColorInt()
    override val onSecondaryColor = Color.WHITE
    override val surfaceColor     = Color.WHITE
    override val onSurfaceColor   = "#1C1B1E".toColorInt()
    override val backgroundColor  = "#E0F2F1".toColorInt()
}

/**
 * HOW A CLIENT USES [ThemeProvider]:
 * Pass a [ThemeProvider] into any built-in [ButtonStyleProvider] subclass.
 * Every colour in the style will automatically use the theme's palette.
 */
@Composable
fun ThemedButtonRowComponent(
    theme: ThemeProvider,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            android.widget.LinearLayout(ctx).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                val dp8 = (8 * ctx.resources.displayMetrics.density).toInt()

                // Filled — uses theme.primaryColor as background
                val filled = FilledButton(ctx).apply {
                    text = "Filled"
                    applyStyle(FilledButtonStyleProvider(theme))   // ← theme injected
                    val lp = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    lp.marginEnd = dp8
                    layoutParams = lp
                }

                // Outlined — uses theme.primaryColor as stroke/text
                val outlined = OutlinedButton(ctx).apply {
                    text = "Outlined"
                    applyStyle(OutlinedButtonStyleProvider(theme)) // ← theme injected
                    layoutParams = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }

                addView(filled)
                addView(outlined)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

// ─── Use-Case 3 : DarkThemeProvider — Drop-in Dark Mode ──────────────────
//
// The SDK ships DarkThemeProvider.  Clients pass it to FilledButtonStyleProvider
// (or any style provider) to instantly get dark-scheme colours.

/**
 * HOW A CLIENT USES [DarkThemeProvider]:
 * Same API as any other ThemeProvider — just pass DarkThemeProvider instead.
 */
@Composable
fun DarkThemeButtonRowComponent(modifier: Modifier = Modifier) {
    // Wrap in a dark background so the buttons are legible in the preview.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ComposeColor(0xFF1C1B1F))   // MD3 dark surface
            .padding(12.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                android.widget.LinearLayout(ctx).apply {
                    orientation = android.widget.LinearLayout.HORIZONTAL
                    val dp8 = (8 * ctx.resources.displayMetrics.density).toInt()

                    val filled = FilledButton(ctx).apply {
                        text = "Dark Filled"
                        // ← DarkThemeProvider from the SDK
                        applyStyle(FilledButtonStyleProvider(DarkThemeProvider))
                        val lp = android.widget.LinearLayout.LayoutParams(0,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        lp.marginEnd = dp8
                        layoutParams = lp
                    }

                    val outlined = OutlinedButton(ctx).apply {
                        text = "Dark Outline"
                        applyStyle(OutlinedButtonStyleProvider(DarkThemeProvider))
                        layoutParams = android.widget.LinearLayout.LayoutParams(0,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    }

                    addView(filled)
                    addView(outlined)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─── Use-Case 4 : ButtonAnimationProvider — Swap Press Animation ──────────
//
// The SDK provides DefaultButtonAnimationProvider (scale + alpha).
// Clients can replace it with a custom implementation on any button.

/** Bounce animation — scales past 1 on release for a springy feel. */
private object BounceAnimationProvider : ButtonAnimationProvider {
    override fun onPress(view: View) {
        view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(80).start()
    }
    override fun onRelease(view: View) {
        // Overshoot to 1.08, then settle to 1.0
        view.animate().scaleX(1.08f).scaleY(1.08f).setDuration(100)
            .withEndAction {
                view.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
    }
    override fun onCheckedChanged(view: View, isChecked: Boolean) = Unit
}

/** No animation at all — a11y-friendly or for reduced-motion preference. */
private object NoAnimationProvider : ButtonAnimationProvider {
    override fun onPress(view: View)   = Unit
    override fun onRelease(view: View) = Unit
    override fun onCheckedChanged(view: View, isChecked: Boolean) = Unit
}

/**
 * HOW A CLIENT USES [ButtonAnimationProvider]:
 * Assign a custom provider to [BaseMaterialButton.animationProvider] at
 * any point — factory lambda, runtime, or after a settings change.
 */
@Composable
fun AnimationProviderButtonRowComponent(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            android.widget.LinearLayout(ctx).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                val dp8 = (8 * ctx.resources.displayMetrics.density).toInt()

                val bounce = FilledButton(ctx).apply {
                    text = "Bounce"
                    animationProvider = BounceAnimationProvider   // ← custom
                    val lp = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    lp.marginEnd = dp8
                    layoutParams = lp
                }

                val noAnim = OutlinedButton(ctx).apply {
                    text = "No Anim"
                    animationProvider = NoAnimationProvider       // ← no-op
                    val lp = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    lp.marginEnd = dp8
                    layoutParams = lp
                }

                val default = TextButton(ctx).apply {
                    text = "Default"
                    // animationProvider already = DefaultButtonAnimationProvider
                    layoutParams = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }

                addView(bounce)
                addView(noAnim)
                addView(default)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

// ─── Provider Showcase — all use-cases in one scrollable screen ───────────

@Composable
fun ProviderShowcase(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Provider API Use-Cases",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "How clients consume SDK interfaces",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Use-case 1: ButtonStyleProvider ──
        SectionHeader("1 · Custom ButtonStyleProvider")
        Text(
            text = "Implement ButtonStyleProvider → call applyStyle()",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        CustomStyleButtonComponent(
            text     = "Brand Filled (deep-orange, r=8)",
            style    = BrandFilledStyle(),
            modifier = Modifier.fillMaxWidth()
        )
        CustomStyleButtonComponent(
            text     = "Brand Outlined (teal, stroke=3)",
            style    = BrandOutlinedStyle(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ── Use-case 2: ThemeProvider — custom brand ──
        SectionHeader("2 · Custom ThemeProvider (Teal Brand)")
        Text(
            text = "Pass ThemeProvider into FilledButtonStyleProvider / OutlinedButtonStyleProvider",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        ThemedButtonRowComponent(theme = TealBrandTheme)

        Spacer(modifier = Modifier.height(4.dp))

        // ── Use-case 3: DarkThemeProvider ──
        SectionHeader("3 · DarkThemeProvider (SDK built-in)")
        Text(
            text = "applyStyle(FilledButtonStyleProvider(DarkThemeProvider))",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        DarkThemeButtonRowComponent()

        Spacer(modifier = Modifier.height(4.dp))

        // ── Use-case 4: ButtonAnimationProvider ──
        SectionHeader("4 · Custom ButtonAnimationProvider")
        Text(
            text = "button.animationProvider = BounceAnimationProvider / NoAnimationProvider",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        AnimationProviderButtonRowComponent()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Button Showcase")
@Composable
fun ButtonShowcasePreview() {
    MaterialButtonSdkTheme {
        ButtonShowcase()
    }
}

@Preview(showBackground = true, name = "Filled Button")
@Composable
fun FilledButtonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledButtonComponent(text = "Filled Button", modifier = Modifier.fillMaxWidth())
            FilledButtonComponent(text = "Disabled", enabled = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, name = "Outlined Button")
@Composable
fun OutlinedButtonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButtonComponent(text = "Outlined Button", modifier = Modifier.fillMaxWidth())
            OutlinedButtonComponent(text = "Disabled", enabled = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, name = "Text Button")
@Composable
fun TextButtonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButtonComponent(text = "Text Button", modifier = Modifier.fillMaxWidth())
            TextButtonComponent(text = "Disabled", enabled = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, name = "Toggle Buttons")
@Composable
fun ToggleButtonPreview() {
    MaterialButtonSdkTheme {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToggleButtonComponent(text = "Option A", modifier = Modifier.weight(1f))
            ToggleButtonComponent(text = "Option B", modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, name = "Segmented Group")
@Composable
fun SegmentedGroupPreview() {
    MaterialButtonSdkTheme {
        SegmentedButtonGroupComponent(
            segments = listOf("Day", "Week", "Month"),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Loading Button")
@Composable
fun LoadingButtonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LoadingButtonComponent(
                text        = "Submit",
                isLoading   = false,
                modifier    = Modifier.fillMaxWidth()
            )
            LoadingButtonComponent(
                text        = "",
                isLoading   = true,
                loadingText = "Saving…",
                modifier    = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "Gradient Button")
@Composable
fun GradientButtonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            GradientButtonComponent(
                text       = "Material You",
                startColor = "#6750A4".toColorInt(),
                endColor   = "#9C27B0".toColorInt(),
                modifier   = Modifier.fillMaxWidth()
            )
            GradientButtonComponent(
                text        = "Sunrise",
                startColor  = "#FF6B35".toColorInt(),
                endColor    = "#F7C59F".toColorInt(),
                modifier    = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun DarkThemePreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("Dark Theme Buttons")
            FilledButtonComponent(text = "Dark Filled",   modifier = Modifier.fillMaxWidth())
            OutlinedButtonComponent(text = "Dark Outlined", modifier = Modifier.fillMaxWidth())
            TextButtonComponent(text = "Dark Text",       modifier = Modifier.fillMaxWidth())
        }
    }
}

// ─── Provider Use-Case Previews ───────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Provider Showcase")
@Composable
fun ProviderShowcasePreview() {
    MaterialButtonSdkTheme {
        ProviderShowcase()
    }
}

@Preview(showBackground = true, name = "UC1 · ButtonStyleProvider")
@Composable
fun CustomStylePreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Implement ButtonStyleProvider → applyStyle()",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            // Deep-orange brand filled
            CustomStyleButtonComponent(
                text     = "Brand Filled  (BrandFilledStyle)",
                style    = BrandFilledStyle(),
                modifier = Modifier.fillMaxWidth()
            )
            // Teal outlined
            CustomStyleButtonComponent(
                text     = "Brand Outlined  (BrandOutlinedStyle)",
                style    = BrandOutlinedStyle(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "UC2 · ThemeProvider — Teal Brand")
@Composable
fun TealThemePreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "FilledButtonStyleProvider(TealBrandTheme)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            ThemedButtonRowComponent(theme = TealBrandTheme)
        }
    }
}

@Preview(showBackground = true, name = "UC2b · ThemeProvider — Default vs Dark")
@Composable
fun ThemeComparisonPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "DefaultThemeProvider  (Material 3 purple seed)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            ThemedButtonRowComponent(theme = DefaultThemeProvider)

            Text(
                "DarkThemeProvider  (MD3 dark scheme)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            DarkThemeButtonRowComponent()
        }
    }
}

@Preview(showBackground = true, name = "UC3 · DarkThemeProvider")
@Composable
fun DarkThemeProviderPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "applyStyle(FilledButtonStyleProvider(DarkThemeProvider))",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            DarkThemeButtonRowComponent()
        }
    }
}

@Preview(showBackground = true, name = "UC4 · ButtonAnimationProvider")
@Composable
fun AnimationProviderPreview() {
    MaterialButtonSdkTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "button.animationProvider = BounceAnimationProvider / NoAnimationProvider / Default",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            AnimationProviderButtonRowComponent()
        }
    }
}

