import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

kotlin {
    jvm()

    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "ChatML"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain)
            api(projects.data)
        }
    }
}

android {
    namespace = "pl.matiz22.chatml"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set("ChatML")
                description.set("ChatML Kotlin Multiplatform Library")
            }
        }
    }

    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/matiz22/ChatML")
            credentials {
                username = System.getenv("GitHubPackagesUsername")
                password = System.getenv("GitHubPackagesPassword")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}
