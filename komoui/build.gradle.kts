import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.dokka)
}

configurations.matching {
    it.name == "desktopMainCompileClasspath" || it.name == "desktopMainRuntimeClasspath"
}.configureEach {
    attributes {
        attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
    }
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.komoui"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    wasmJs {
        browser()
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "KomoUI"

    iosX64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "com.komoui")
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "com.komoui")
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "com.komoui")
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            // Add KMP dependencies here
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)

            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)

            implementation(libs.kotlin.datetime)

            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            // Add Android-specific dependencies here. Note that this source set depends on
            // commonMain by default and will correctly pull the Android artifacts of any KMP
            // dependencies declared in commonMain.
            implementation(libs.ktor.client.android)
        }

        getByName("desktopMain") {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.cio)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain.dependencies {
            // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
            // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
            // part of KMP’s default source set hierarchy. Note that this source set depends
            // on common by default and will correctly pull the iOS artifacts of any
            // KMP dependencies declared in commonMain.
            implementation(libs.ktor.client.darwin)
            implementation(libs.kotlin.datetime)
        }

        getByName("wasmJsMain") {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = "io.github.derangga",
        artifactId = "komoui",
        version = "0.4.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("KomoUI")
        description.set("Compose Multiplatform UI components for Android, iOS, desktop, and web, inspired by the design language of shadcn/ui.")
        inceptionYear.set("2025")
        url.set("https://github.com/derangga/komoui")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developer information
        developers {
            developer {
                id.set("derangga")
                name.set("Dimas Rangga")
                email.set("aldebaransdev@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/derangga/komoui")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}
