plugins {
  id("idea")
  id("java-library")
  id("maven-publish")
  kotlin("jvm") version "1.8.10"
  id("com.diffplug.spotless")
}

group = "com.gitee.ixtf"

version = "1.0.0"

java {
  toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
  withJavadocJar()
  withSourcesJar()
}

tasks.wrapper {
  gradleVersion = "8.0"
  distributionType = Wrapper.DistributionType.ALL
}

repositories {
  mavenLocal()
  mavenCentral()
  maven("https://plugins.gradle.org/m2")
  maven("https://jitpack.io")
  maven("https://maven.geo-solutions.it")
}

dependencies {
  api(platform("com.gitee.ixtf:bom:${properties["bomVersion"]}"))
  api("cn.hutool:hutool-core")
  api("cn.hutool:hutool-system")
  api("cn.hutool:hutool-crypto")
  api("cn.hutool:hutool-jwt")
  api("org.bouncycastle:bcprov-jdk15on")
  api("org.hibernate.validator:hibernate-validator")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
  api("com.fasterxml.jackson.module:jackson-module-parameter-names")
  api("com.fasterxml.jackson.module:jackson-module-kotlin")
  api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  api("com.fasterxml.jackson.dataformat:jackson-dataformat-toml")
  api("ch.qos.logback:logback-classic")
  api("io.github.classgraph:classgraph")
  api("com.github.ben-manes.caffeine:caffeine")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
  testImplementation(kotlin("test"))
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      //      val archives by configurations
      //      setArtifacts(archives.artifacts)
      versionMapping {
        usage("java-api") { fromResolutionOf("runtimeClasspath") }
        usage("java-runtime") { fromResolutionResult() }
      }
    }
  }
}

spotless {
  java {
    target("**/java/**")
    targetExclude("**/generated/**")
    googleJavaFormat()
    formatAnnotations()
  }
  kotlin {
    target("**/kotlin/**")
    targetExclude("**/generated/**")
    ktfmt()
  }
  kotlinGradle {
    target("*.gradle.kts", "additionalScripts/*.gradle.kts")
    ktfmt()
  }
  format("styling") {
    target("**/graphql/**")
    prettier()
  }
}
