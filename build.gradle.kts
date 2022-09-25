import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.diffplug.spotless") version "6.11.0"
}

group = "com.gitee.ixtf"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://plugins.gradle.org/m2")
    maven("https://jitpack.io")
    maven("https://maven.geo-solutions.it")
}

dependencies {
    implementation(enforcedPlatform("com.gitee.ixtf:bom:${properties["bomVersion"]}"))
    api("cn.hutool:hutool-core")
    api("cn.hutool:hutool-system")
    api("cn.hutool:hutool-crypto")

    testImplementation(kotlin("test"))
}

spotless {
    java {
        googleJavaFormat("1.15.0")
        formatAnnotations()
        target("**/java/**")
        targetExclude("**/generated/**")
    }
    kotlin {
        ktlint("0.47.1")
        target("**/kotlin/**")
        targetExclude("**/generated/**")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}