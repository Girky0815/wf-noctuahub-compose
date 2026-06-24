import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

val appVersion = project.findProperty("appVersion")?.toString() ?: "1.0.0"
val appVersionCode = project.findProperty("appVersionCode")?.toString()?.toIntOrNull() ?: 1

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.materialKolor)
            implementation(compose.materialIconsExtended)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.yamlkt)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.jna)
            implementation(libs.jna.platform)
            implementation(libs.tukaani.xz)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "jp.girky.wf_noctuahub"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
            val keystorePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            val keyAliasStr = System.getenv("ANDROID_KEY_ALIAS")
            val keyPasswordStr = System.getenv("ANDROID_KEY_PASSWORD")

            if (!keystorePath.isNullOrEmpty() && file(keystorePath).exists()) {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                keyAlias = keyAliasStr
                keyPassword = keyPasswordStr
            } else {
                val debugConfig = signingConfigs.getByName("debug")
                storeFile = debugConfig.storeFile
                storePassword = debugConfig.storePassword
                keyAlias = debugConfig.keyAlias
                keyPassword = debugConfig.keyPassword
            }
        }
    }

    defaultConfig {
        applicationId = "jp.girky.wf_noctuahub"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersion
        manifestPlaceholders["appLabel"] = "Noctua Hub"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appLabel"] = "Noctua Hub"
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders["appLabel"] = "Noctua Hub(デバッグ)"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    implementation(libs.tukaani.xz)
}

compose.desktop {
    application {
        mainClass = "jp.girky.wf_noctuahub.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "jp.girky.wf_noctuahub"
            packageVersion = appVersion
        }
    }
}

val generateLibraryVersions by tasks.registering {
    val outputFile = file("src/commonMain/kotlin/jp/girky/wf_noctuahub/platform/LibraryVersions.kt")
    inputs.file("../gradle/libs.versions.toml")
    outputs.file(outputFile)

    val agpVersion = libs.versions.agp.get()
    val composeVersion = libs.versions.composeMultiplatform.get()
    val kotlinVersion = libs.versions.kotlin.get()
    val ktorVersion = libs.versions.ktor.get()
    val serializationVersion = libs.versions.kotlinx.serialization.get()
    val datetimeVersion = libs.versions.kotlinx.datetime.get()
    val materialKolorVersion = libs.versions.materialKolor.get()
    val jnaVersion = libs.versions.jna.get()
    val tukaaniXzVersion = libs.versions.tukaani.xz.get()
    val settingsVersion = libs.versions.multiplatformSettings.get()
    val yamlktVersion = libs.versions.yamlkt.get()

    doLast {
        outputFile.parentFile.mkdirs()
        outputFile.writeText("""
            package jp.girky.wf_noctuahub.platform

            object LibraryVersions {
                const val AGP = "$agpVersion"
                const val COMPOSE = "$composeVersion"
                const val KOTLIN = "$kotlinVersion"
                const val KTOR = "$ktorVersion"
                const val SERIALIZATION = "$serializationVersion"
                const val DATETIME = "$datetimeVersion"
                const val MATERIAL_KOLOR = "$materialKolorVersion"
                const val JNA = "$jnaVersion"
                const val TUKAANI_XZ = "$tukaaniXzVersion"
                const val SETTINGS = "$settingsVersion"
                const val YAMLKT = "$yamlktVersion"
            }
        """.trimIndent())
    }
}

tasks.matching { 
    it.name.startsWith("compileKotlin") || 
    it.name.startsWith("compileDebugKotlin") || 
    it.name.startsWith("compileReleaseKotlin") 
}.configureEach {
    dependsOn(generateLibraryVersions)
}
