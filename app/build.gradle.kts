/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.5/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.6.21"

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Apply GraalVM Native Image plugin
    id("org.graalvm.buildtools.native") version "0.9.13"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.0.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("AppKt")
    // Set name of 'installDist' artifacts
    applicationName = "nix-kt-template"
}

graalvmNative {
    binaries {
        named("main") {
            // Disable native toolchain checking
            buildArgs.add("-H:-CheckToolchain")
            // Set executable name
            imageName.set("nix-kt-template")
        }
    }
    // Just use GRAALVM_HOME for finding the GraalVM installation
    toolchainDetection.set(false)
}