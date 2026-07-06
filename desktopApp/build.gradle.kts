import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        getByName("desktopMain") {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation("org.jetbrains.skiko:skiko:0.8.18")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.kraftnote.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KraftNote"
            packageVersion = "1.0.0"
            description = "Page Editor on Kotlin Multiplatform"
            vendor = "KraftNote"

            macOS {
                bundleID = "com.kraftnote.app"
            }
            windows {
                menuGroup = "KraftNote"
                upgradeUuid = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
            }
        }
    }
}
