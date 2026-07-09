# KomoUI

Kotlin Multiplatform UI component library. Provides customizable Compose Multiplatform components for Android and iOS.

Published as `io.github.derangga:komoui` on Maven Central.

## Project Structure

```
komoui/                 # Library module (the published artifact)
  src/commonMain/       # Shared KMP code — all components live here
  src/androidMain/      # Android-specific code (Ktor Android client)
  src/iosMain/          # iOS-specific code (Ktor Darwin client)
composeApp/             # Demo/sample app showcasing components
  src/commonMain/       # Shared demo app code (pages, navigation, themes)
  src/androidMain/      # Android demo entry point
  src/iosMain/          # iOS demo entry point
iosApp/                 # iOS app wrapper (Xcode project)
```

### Library Package Layout (`com.komoui`)

- `components/` — One file per component (Button.kt, Card.kt, etc.)
- `components/sidebar/` — Sidebar component (multi-file)
- `components/sonner/` — Sonner toast system (multi-file)
- `components/charts/` — Chart components (Bar/Line/Area)
- `themes/` — KomoTheme, KomoStyles, light/dark colors, radius, typography
- `utils/` — Shared utilities
- `kmp/` — Platform expect/actual declarations

## Tech Stack

- **Kotlin** 2.2.20 with Compose Multiplatform 1.9.1
- **Material 3** as the base design system
- **Gradle** with Kotlin DSL and version catalogs (`gradle/libs.versions.toml`)
- **Targets**: Android (minSdk 24, compileSdk 36), iOS (arm64, x64, simulatorArm64)
- **Dependencies**: Coil 3 (images), Ktor 3 (networking), kotlinx-datetime

## Build & Test Commands

```bash
./gradlew :komoui:build                  # Build library
./gradlew :komoui:allTests               # Run all platform tests
./gradlew :composeApp:build              # Build demo app
./gradlew :komoui:publishToMavenLocal    # Publish locally
```

## Code Conventions

- Kotlin official code style (`kotlin.code.style=official`)
- One `@Composable` component per file, named after the component (PascalCase)
- Enum classes for variants/sizes in the same file (e.g., `ButtonVariant`, `ButtonSize`)
- `Modifier` as second parameter (after primary callback like `onClick`)
- Use `internal` for helper composables, `private` for file-scoped helpers
- Access theme via `MaterialTheme.styles` (colors), `MaterialTheme.radius`, `MaterialTheme.isDark`
- KDoc on all public composable functions

## Component Authoring Pattern

When creating a new component:

1. Create a new file in `komoui/src/commonMain/kotlin/com/komoui/components/`
2. Define variant/size enums if applicable
3. Use `MaterialTheme.styles` and `MaterialTheme.radius` for theming — never hardcode colors
4. Keep all code in `commonMain` unless platform-specific behavior is needed
5. Add a demo page in `composeApp/src/commonMain/kotlin/com/komoui/demo/pages/`

## CI/CD

- GitHub Actions workflow (`.github/workflows/maven-central.yml`)
- Publishes to Maven Central on version tag push (e.g., `0.3.0`)
- Runs on `macos-latest` with Java 17 (Zulu) and latest Xcode
