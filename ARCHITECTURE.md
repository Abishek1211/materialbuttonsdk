# Architecture — Material Button SDK
## Overview
This project is a **two-module Android project** that delivers a reusable, production-grade
Material Design 3 button component library (`:sdk`) alongside a Jetpack Compose sample app
(`:app`) that previews and demonstrates every button type in Android Studio's Preview panel.
The SDK is designed to be:
- **Drop-in** — works in any Android app, no Compose required
- **Extensible** — three provider interfaces let consumers swap themes, styles and animations
- **Accessible** — WCAG 2.1 minimum touch targets, screen-reader labels, keyboard navigation
- **XML-friendly** — full custom attribute support via `attrs.xml`
- **Builder-friendly** — fluent `ButtonFactory` API for programmatic creation
---
## Module Structure
```
materialbuttonsdk/                          ← root project
├── build.gradle                            ← project-level plugin declarations
├── settings.gradle                         ← registers :sdk and :app modules
├── ARCHITECTURE.md                         ← this file
├── README.md
│
├── sdk/                                    ← 📦 Android Library (.aar)
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/values/attrs.xml            ← custom XML attributes
│       └── java/com/sdk/buttons/
│           │
│           ├── ── Interfaces ──────────────────────────────
│           ├── ThemeProvider.kt            ← brand colour palette interface
│           ├── ButtonStyleProvider.kt      ← visual style interface
│           ├── ButtonAnimationProvider.kt  ← motion behaviour interface
│           │
│           ├── ── Default Implementations ─────────────────
│           ├── DefaultThemeProvider        ← Material 3 purple (inside ThemeProvider.kt)
│           ├── DarkThemeProvider.kt        ← Material 3 dark-scheme colours
│           ├── DynamicThemeProvider.kt     ← reads live from Activity theme (Material You)
│           ├── FilledButtonStyleProvider.kt
│           ├── OutlinedButtonStyleProvider.kt
│           ├── TextButtonStyleProvider.kt
│           ├── DefaultButtonAnimationProvider (inside ButtonAnimationProvider.kt)
│           │
│           ├── ── Button Classes ──────────────────────────
│           ├── BaseMaterialButton.kt       ← shared foundation + all provider hooks
│           ├── FilledButton.kt
│           ├── OutlinedButton.kt
│           ├── TextButton.kt
│           ├── ToggleButton.kt
│           ├── IconButton.kt               ← icon-only square button
│           ├── LoadingButton.kt            ← async spinner button
│           ├── GradientButton.kt           ← two-colour gradient fill
│           ├── SegmentedButtonGroup.kt     ← horizontal equal-weight container
│           │
│           ├── ── Builder API ─────────────────────────────
│           └── ButtonFactory.kt            ← fluent builder for all button types
│               (also contains SdkButtonBuilder<T>, FilledButtonBuilder,
│                OutlinedButtonBuilder, TextButtonBuilder, ToggleButtonBuilder,
│                IconButtonBuilder, LoadingButtonBuilder, GradientButtonBuilder)
│
└── app/                                    ← 📱 Sample / Preview App (.apk)
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/values/
        │   ├── strings.xml
        │   └── themes.xml                  ← MaterialComponents theme (required for SDK Views)
        └── java/com/sdk/materialbuttonsdk/sample/
            ├── MainActivity.kt
            ├── ButtonComponents.kt         ← @Composable wrappers + all @Preview functions
            └── ui/theme/
                └── MaterialButtonSdkTheme.kt
```
---
## Module 1 — `:sdk` (Android Library)
**Gradle plugin:** `com.android.library`
**Namespace:** `com.sdk.buttons`
**Min SDK:** 24 · **Compile SDK:** 35
**Dependencies:** `com.google.android.material:material:1.12.0`, `androidx.annotation:annotation:1.8.0`
Produces a standalone `.aar` with zero Compose dependency — usable in any Android app.
---
### Provider Interfaces
The SDK is built around three extensibility interfaces, mirroring the pattern used by
Google Material Components.
| Interface | Purpose | Built-in implementations |
|---|---|---|
| `ThemeProvider` | Brand colour palette | `DefaultThemeProvider`, `DarkThemeProvider`, `DynamicThemeProvider` |
| `ButtonStyleProvider` | Full visual style per button type | `FilledButtonStyleProvider`, `OutlinedButtonStyleProvider`, `TextButtonStyleProvider` |
| `ButtonAnimationProvider` | Press / release / toggle motion | `DefaultButtonAnimationProvider` |
**How they connect:**
```
ThemeProvider ──────────────────────────────────────────────────────────────┐
    (DefaultThemeProvider / DarkThemeProvider / DynamicThemeProvider)       │
                                                                             ▼
                                                         ButtonStyleProvider(theme)
                                                             └── applyStyle(style)
                                                                     │
                                                                     ▼
                                                         BaseMaterialButton (any subclass)
ButtonAnimationProvider ─────────────────────────────────────────────────────┐
    (DefaultButtonAnimationProvider / custom)                                 │
                                                                              ▼
                                                         button.animationProvider = provider
```
---
### Custom XML Attributes (`res/values/attrs.xml`)
Every `BaseMaterialButton` subclass reads these attributes from XML.
Values are applied via `post{}` **after** the full constructor chain, so they
always override the subclass's default style.
| Attribute | Format | Overrides |
|---|---|---|
| `app:sdkButtonColor` | color | background fill |
| `app:sdkTextColor` | color | label text colour |
| `app:sdkCornerRadius` | dimension | corner radius (px) |
| `app:sdkIconDrawable` | reference | icon drawable |
| `app:sdkStrokeWidth` | dimension | border width (px) |
| `app:sdkStrokeColor` | color | border colour |
```xml
<com.sdk.buttons.FilledButton
    android:layout_width="wrap_content"
    android:layout_height="48dp"
    android:text="Login"
    app:sdkButtonColor="#FF5722"
    app:sdkCornerRadius="8dp"
    app:sdkIconDrawable="@drawable/ic_login"/>
```
---
### Class Hierarchy
```
android.widget.LinearLayout
└── SegmentedButtonGroup           (horizontal equal-weight container)
com.google.android.material.button.MaterialButton
└── BaseMaterialButton             (open — shared foundation, all provider hooks)
    ├── FilledButton               (open — solid primary fill)
    │   ├── ToggleButton           (stateful checked/unchecked toggle)
    │   ├── IconButton             (icon-only square, no label)
    │   └── LoadingButton          (async spinner replaces label while loading)
    ├── OutlinedButton             (transparent bg, primary-colour stroke)
    ├── TextButton                 (no bg, no elevation)
    └── GradientButton             (two-colour gradient + RippleDrawable)
```
---
### Class Responsibilities
#### `BaseMaterialButton`
- **Extends:** `MaterialButton` (Google Material Components)
- **Purpose:** Shared foundation and integration point for every provider hook.
- **Base defaults (`init`):** `iconPadding=16`, `insetTop/Bottom=0`, `isAllCaps=false`, `isFocusable=true`
- **XML attribute pipeline:** reads `SdkButton` styled-attributes, stores them, then
  schedules `post { applyXmlOverrides() }` so they execute after all subclass `init` blocks.
- **Public API:**
  - `var animationProvider: ButtonAnimationProvider` — swappable at any time
  - `fun applyStyle(ButtonStyleProvider)` — applies full visual style; safe to call at runtime
  - `fun setAccessibilityLabel(String)` — sets `contentDescription`
  - `fun enforceMinTouchTarget()` — pads button to WCAG 2.1 minimum 48×48 dp
- **Visibility:** `open`
#### `FilledButton`
| | |
|---|---|
| Extends | `BaseMaterialButton` |
| Default style | `FilledButtonStyleProvider(DefaultThemeProvider)` — `#6750A4` fill, white text |
| Visibility | `open` (subclassed by `ToggleButton`, `IconButton`, `LoadingButton`) |
| Runtime restyle | `button.applyStyle(FilledButtonStyleProvider(DynamicThemeProvider(ctx)))` |
#### `OutlinedButton`
| | |
|---|---|
| Extends | `BaseMaterialButton` |
| Default style | `OutlinedButtonStyleProvider` — transparent bg, `strokeWidth=3`, `#6750A4` stroke+text |
| Runtime restyle | `button.applyStyle(OutlinedButtonStyleProvider(DarkThemeProvider))` |
#### `TextButton`
| | |
|---|---|
| Extends | `BaseMaterialButton` |
| Default style | `TextButtonStyleProvider` — transparent bg, `elevation=0f`, `#6750A4` text |
| Runtime restyle | `button.applyStyle(TextButtonStyleProvider(MyTheme))` |
#### `ToggleButton`
| | |
|---|---|
| Extends | `FilledButton` |
| State | `private var checkedState: Boolean` (starts `false`) |
| Behaviour | Overrides `performClick()` — state always flips and `animationProvider.onCheckedChanged` always fires, even when an external `setOnClickListener` is attached |
| Public API | `isCheckedState(): Boolean` |
#### `IconButton`
| | |
|---|---|
| Extends | `FilledButton` |
| Purpose | Square icon-only button with no label |
| Init | Sets `text=""`, `iconPadding=0`, `ICON_GRAVITY_TEXT_START`, adds symmetric padding |
| Public API | `setButtonIcon(@DrawableRes iconRes: Int)` |
#### `LoadingButton`
| | |
|---|---|
| Extends | `FilledButton` |
| Purpose | Shows a spinning `CircularButtonProgressDrawable` while an async op runs |
| Public API | `setLoading(Boolean)`, `isLoading(): Boolean`, `var loadingText: String` |
| Behaviour | `setLoading(true)` saves label text, shows spinner icon, disables button. `setLoading(false)` restores all. Stops animation in `onDetachedFromWindow`. |
#### `GradientButton`
| | |
|---|---|
| Extends | `BaseMaterialButton` |
| Purpose | Two-colour gradient fill with `RippleDrawable` tap feedback |
| Init | Calls `applyStyle(FilledButtonStyleProvider())` for text/padding, then `post { applyGradientBackground() }` |
| Public API | `setGradient(startColor, endColor, orientation)` — updates gradient at any time |
#### `SegmentedButtonGroup`
| | |
|---|---|
| Extends | `LinearLayout` |
| Purpose | Horizontal container; each child `BaseMaterialButton` gets `weight=1f` |
| Public API | `addSegment(button: BaseMaterialButton)` |
#### `CircularButtonProgressDrawable`
- Lightweight `Drawable` + `Animatable` used exclusively by `LoadingButton`.
- Draws a spinning sweep arc via `ValueAnimator` + `Canvas.drawArc`.
- `setColor(Int)` and `setStrokeWidth(Float)` for customisation.
---
### `ThemeProvider` Implementations
| Class | Use case |
|---|---|
| `DefaultThemeProvider` | Default Material 3 light palette — `#6750A4` primary |
| `DarkThemeProvider` | Hardcoded Material 3 dark palette — `#D0BCFF` primary |
| `DynamicThemeProvider(context)` | Reads live from Activity/Application theme attributes. Automatically respects **Material You** dynamic colours (Android 12+) and **dark mode** without any extra code. Requires the host app to call `DynamicColors.applyToActivitiesIfAvailable(this)` in `Application.onCreate`. |
---
### `ButtonFactory` — Builder API
Fluent programmatic creation for all seven button types.
```kotlin
// Minimal usage
val btn = ButtonFactory.createFilled(context)
    .setText("Login")
    .build()
// Full usage
val btn = ButtonFactory.createLoading(context)
    .setText("Submit")
    .setLoadingText("Saving…")
    .setStyle(FilledButtonStyleProvider(DynamicThemeProvider(context)))
    .setAnimation(DefaultButtonAnimationProvider)
    .setOnClick { submitForm() }
    .setAccessibilityLabel("Submit form")
    .build()
```
| Factory method | Returns |
|---|---|
| `ButtonFactory.createFilled(context)` | `FilledButtonBuilder` |
| `ButtonFactory.createOutlined(context)` | `OutlinedButtonBuilder` |
| `ButtonFactory.createText(context)` | `TextButtonBuilder` |
| `ButtonFactory.createToggle(context)` | `ToggleButtonBuilder` |
| `ButtonFactory.createIcon(context)` | `IconButtonBuilder` |
| `ButtonFactory.createLoading(context)` | `LoadingButtonBuilder` |
| `ButtonFactory.createGradient(context)` | `GradientButtonBuilder` |
All builders extend `SdkButtonBuilder<T>` which provides the common fluent methods:
`setText`, `setIcon`, `setEnabled`, `setStyle`, `setAnimation`, `setOnClick`, `setAccessibilityLabel`.
---
## Module 2 — `:app` (Sample / Preview App)
**Gradle plugin:** `com.android.application`
**Namespace:** `com.sdk.materialbuttonsdk.sample`
**Min SDK:** 24 · **Target SDK:** 35
**Key dependencies:**
- `project(':sdk')` — the button library
- `androidx.compose:compose-bom:2024.11.00`
- `androidx.activity:activity-compose:1.9.0`
- `androidx.compose.material3:material3`
- `org.jetbrains.kotlin.plugin.compose:2.0.21`
---
### File Responsibilities
#### `MainActivity.kt`
- Extends `ComponentActivity`
- Calls `enableEdgeToEdge()` for full-screen display
- Hosts: `MaterialButtonSdkTheme > Scaffold > ButtonShowcase`
#### `ButtonComponents.kt`
The entire preview layer. Each SDK button type has a `@Composable` wrapper that
uses `AndroidView` to embed the View-based button inside Compose.
**Composable wrappers:**
| Composable | Wraps SDK class | Key parameters |
|---|---|---|
| `FilledButtonComponent` | `FilledButton` | `text`, `enabled`, `onClick` |
| `OutlinedButtonComponent` | `OutlinedButton` | `text`, `enabled`, `onClick` |
| `TextButtonComponent` | `TextButton` | `text`, `enabled`, `onClick` |
| `ToggleButtonComponent` | `ToggleButton` | `text`, `onClick(isChecked)` |
| `SegmentedButtonGroupComponent` | `SegmentedButtonGroup` | `segments: List<String>`, `onSegmentClick(index, label)` |
| `LoadingButtonComponent` | `LoadingButton` | `text`, `isLoading`, `loadingText`, `onClick` |
| `GradientButtonComponent` | `GradientButton` | `text`, `startColor`, `endColor`, `orientation`, `onClick` |
| `IconButtonComponent` | `IconButton` | `iconRes`, `contentDescription`, `enabled`, `onClick` |
| `SectionHeader` | — | `title: String` — pure Compose section title |
| `ButtonShowcase` | All of the above | Full scrollable showcase screen |
**`AndroidView` pattern used in every wrapper:**
- `factory` lambda — creates the View once, sets initial state
- `update` lambda — called on every Compose recomposition to keep View in sync
**`@Preview` functions:**
| Preview name | Shows |
|---|---|
| `ButtonShowcasePreview` | Full device — all sections (showSystemUi = true) |
| `FilledButtonPreview` | Enabled + disabled filled button |
| `OutlinedButtonPreview` | Enabled + disabled outlined button |
| `TextButtonPreview` | Enabled + disabled text button |
| `ToggleButtonPreview` | Option A + Option B toggle pair |
| `SegmentedGroupPreview` | Day / Week / Month group |
| `LoadingButtonPreview` | Idle state + loading state |
| `GradientButtonPreview` | Purple→violet + orange→peach gradients |
| `DarkThemePreview` | Filled, Outlined, Text with dark section header |
#### `ui/theme/MaterialButtonSdkTheme.kt`
Compose `MaterialTheme` wrapper with the app's light colour scheme.
| Token | Value |
|---|---|
| `primary` | `#6750A4` |
| `onPrimary` | `#FFFFFF` |
| `primaryContainer` | `#EADDFF` |
| `secondary` | `#625B71` |
| `background` | `#F6F0FF` |
| `surface` | `#FFFBFE` |
#### `res/values/themes.xml`
Defines `Theme.MaterialButtonSdk` (parent: `Theme.MaterialComponents.DayNight.NoActionBar`).
**Required:** `MaterialButton` resolves its style attributes from the Activity theme at runtime
when embedded via `AndroidView`. Without a `MaterialComponents` parent theme, the buttons
will not render correctly.
---
## Data / Event Flow
```
User tap
   │
   ├─▶ BaseMaterialButton.onTouchListener
   │       └─▶ animationProvider.onPress(view)       ← scale down
   │
   ▼
SDK View.performClick()
   │
   ├─▶ ToggleButton.performClick()  (if ToggleButton)
   │       ├─▶ checkedState = !checkedState
   │       └─▶ animationProvider.onCheckedChanged()  ← alpha fade
   │
   ▼
setOnClickListener { … }  (wired by Composable wrapper factory lambda)
   │
   ▼
Composable onClick: () -> Unit parameter
   │
   ▼
Caller (ButtonShowcase / host screen) handles the event
   │
   ▼
Compose state change → recomposition
   │
   ▼
AndroidView update { view -> … } lambda
   │
   ▼
SDK View properties updated (text, isEnabled, isLoading, gradient…)
   │
   ▼
animationProvider.onRelease(view)   ← scale back up (from touch listener)
```
---
## Build Configuration
| Setting | Value |
|---|---|
| AGP version | `9.2.1` |
| Kotlin version | built-in via AGP |
| Compose compiler plugin | `org.jetbrains.kotlin.plugin.compose:2.0.21` (K2 bundled) |
| JVM target | JDK 17 |
| Gradle version | `9.4.1` |
| Min SDK | 24 (Android 7.0) |
| Compile / Target SDK | 35 (Android 15) |
### Build commands
```bash
# Build the SDK library (.aar)
./gradlew :sdk:assembleRelease
# Build the sample / preview app (.apk)
./gradlew :app:assembleDebug
# Install and launch on a connected device / emulator
./gradlew :app:installDebug
# Build everything
./gradlew assembleDebug
```
---
## SDK Feature Matrix
| Feature | Status | Class / File |
|---|---|---|
| Filled Button | ✅ | `FilledButton` |
| Outlined Button | ✅ | `OutlinedButton` |
| Text Button | ✅ | `TextButton` |
| Toggle Button | ✅ | `ToggleButton` |
| Icon Button | ✅ | `IconButton` |
| Loading Button | ✅ | `LoadingButton` + `CircularButtonProgressDrawable` |
| Gradient Button | ✅ | `GradientButton` |
| Segmented Group | ✅ | `SegmentedButtonGroup` |
| Custom XML Attrs | ✅ | `attrs.xml` + `BaseMaterialButton` |
| Builder / Factory API | ✅ | `ButtonFactory` + `SdkButtonBuilder<T>` |
| Press / release animation | ✅ | `DefaultButtonAnimationProvider` |
| Toggle animation | ✅ | `DefaultButtonAnimationProvider.onCheckedChanged` |
| Default (light) theme | ✅ | `DefaultThemeProvider` |
| Dark mode theme | ✅ | `DarkThemeProvider` |
| Material You / Dynamic colors | ✅ | `DynamicThemeProvider` |
| Accessibility labels | ✅ | `setAccessibilityLabel()` |
| Minimum touch target (WCAG) | ✅ | `enforceMinTouchTarget()` |
| Keyboard / D-pad navigation | ✅ | `isFocusable = true` |
| Compose preview | ✅ | `ButtonComponents.kt` — 9 `@Preview` functions |
| Runtime theme switching | ✅ | `applyStyle()` callable at any time |
---
## Design Decisions
| Decision | Rationale |
|---|---|
| SDK has no Compose dependency | Keeps the `.aar` usable in any Android app regardless of UI toolkit. Compose is only in `:app`. |
| `BaseMaterialButton` is `open` | Lets consumers subclass and extend without duplicating base boilerplate. |
| `FilledButton` is `open` | `ToggleButton`, `IconButton`, and `LoadingButton` all share the solid-fill appearance. |
| `ToggleButton` overrides `performClick()` | Prevents external `setOnClickListener` calls from breaking state management — internal state always updates first, then the external listener fires via `super.performClick()`. |
| XML overrides via `post{}` | Ensures `sdkButtonColor` etc. win over subclass `applyStyle()` defaults without needing a three-phase constructor or `onFinishInflate`. |
| `DynamicThemeProvider` reads theme attributes | The host app controls its theme (including Material You dynamic colors and dark mode). Reading from `context.obtainStyledAttributes` means the SDK adapts automatically with no SDK changes needed. |
| `GradientButton` wraps gradient in `RippleDrawable` | Preserves the tactile press ripple feedback even though the background is a custom `GradientDrawable` rather than a `MaterialShapeDrawable`. |
| `CircularButtonProgressDrawable` is custom | Avoids adding `swiperefreshlayout` or other heavy dependencies just for a progress spinner. The custom drawable is ~60 lines and zero extra dependencies. |
| `AndroidView` bridge in `:app` | Lets all View-based SDK buttons be used and previewed inside Compose without rewriting the SDK. The `factory`/`update` separation correctly handles initial creation vs. recomposition updates. |
| `ButtonFactory` + `SdkButtonBuilder<T>` | The abstract base builder eliminates ~80% of builder code duplication while keeping each concrete builder type-safe and independently extensible. |
| Separate `:sdk` and `:app` modules | The SDK can be snapshotted, versioned, and published as a standalone `.aar`. The app is a consumer, not part of the distributed artifact. |
