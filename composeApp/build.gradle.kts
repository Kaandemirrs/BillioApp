import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    jvmToolchain(17)
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-opt-in=kotlinx.cinterop.ExperimentalForeignApi")
                }
            }
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)
            implementation(libs.napier)

            // AndroidX SavedState - EKLE ⬇️
            implementation("org.jetbrains.androidx.savedstate:savedstate:1.2.1")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.0")


            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(compose.materialIconsExtended)

            // Stately - EKLE (eğer yoksa)
            implementation("co.touchlab:stately-common:2.1.0")

            implementation(libs.firebase.common.gitlive)
            implementation(libs.firebase.auth.gitlive)
            implementation(libs.firebase.firestore.gitlive)

        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.insert-koin:koin-android:4.0.0")
            implementation("com.google.android.gms:play-services-auth:21.0.0")

            implementation("com.google.firebase:firebase-common-ktx:21.0.0")
            implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
            implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")
            implementation("androidx.compose:compose-bom:2024.01.00")        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

        }
    }
}

android {
    namespace = "com.billioapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.billioapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
