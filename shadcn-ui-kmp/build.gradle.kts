import com.vanniktech.maven.publish.SonatypeHost

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

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.shadcn.ui.kmp"
        compileSdk = 36
        minSdk = 26

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "shadcn-ui-kmpKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)

            implementation(libs.kotlin.datetime)

            implementation(libs.androidx.material.icons.extended)
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
    }
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = "io.github.derangga",
        artifactId = "shadcn-ui-kmp",
        version = "0.1.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Shadcn UI KMP")
        description.set("Shadcn-Compose is a UI kit for Android Jetpack Compose, inspired by the principles and components of shadcn/ui. This library provides a set of beautifully designed and customizable Compose components to help you build modern and consistent user interfaces with ease")
        inceptionYear.set("2025")
        url.set("https://github.com/derangga/shadcn-ui-kmp")

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
            url.set("https://github.com/derangga/shadcn-ui-kmp")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}