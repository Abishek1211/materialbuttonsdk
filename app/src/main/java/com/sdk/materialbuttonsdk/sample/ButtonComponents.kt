package com.sdk.materialbuttonsdk.sample

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        SectionHeader("Dark Theme")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledButtonComponent(
                text     = "Dark Fill",
                modifier = Modifier.weight(1f),
                onClick  = {}
            )
            OutlinedButtonComponent(
                text     = "Dark Outline",
                modifier = Modifier.weight(1f),
                onClick  = {}
            )
        }

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

