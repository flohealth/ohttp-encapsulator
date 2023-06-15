@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

val javaVersion = JavaVersion.VERSION_1_8
val buildType = "release"
val libVersion = "0.0.4"
val jniVersionTag = "v0.0.4"

android {
    namespace = "health.flo.network.ohttp.encapsulator"

    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 29
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }

    testOptions {
        managedDevices {
            devices {
                val pixel2api30 = maybeCreate<ManagedVirtualDevice>(name = "pixel2api30").apply {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp-atd"
                }

                groups {
                    maybeCreate("ohttpTestDevices").apply {
                        targetDevices.add(devices[pixel2api30.name])
                    }
                }
            }
        }
    }

    publishing {
        singleVariant(buildType) {
            withSourcesJar()
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}

publishing {
    publications {
        create<MavenPublication>(buildType) {
            groupId = "health.flo.network"
            artifactId = "ohttp-encapsulator-android"
            version = "$libVersion${artifactVersionSuffix()}"

            afterEvaluate {
                from(components[buildType])
            }
        }
    }

    repositories {
        // ./gradlew publishReleasePublicationToMavenLocal to publish to ~/.m2

        maven {
            //  ./gradlew publishReleaseToMvnBuildDirRepository to publish to lib/build/repo
            name = "MvnBuildDir"
            url = uri("${project.buildDir}/repo")
        }
    }
}

// ./gradlew runAndroidIntegrationTests to run integration tests
tasks.create("runAndroidIntegrationTests") {
    dependsOn("ohttpTestDevicesGroupDebugAndroidTest")
}

tasks.create<Exec>("downloadJni") {
    commandLine("sh", "download-jni-libs.sh", jniVersionTag)
    workingDir = rootProject.projectDir

    doLast {
        val exitValue = executionResult.get().exitValue
        if (exitValue != 0) {
            throw RuntimeException("JNI libs download failed with exit code: $exitValue")
        }
    }
}

tasks.named("preBuild") {
    dependsOn("downloadJni")
}

tasks.create<Delete>("cleanJniLibs") {
    this.delete(rootProject.projectDir.resolve("./lib/src/main/jniLibs"))
}

tasks.named("clean") {
    dependsOn("cleanJniLibs")
}

fun artifactVersionSuffix(): String {
    return if (isReleaseBranch()) "" else "-SNAPSHOT"
}

fun isReleaseBranch(): Boolean {
    val branch = System.getenv("GITHUB_HEAD_REF")
    return branch != null && (branch == "main")
}
