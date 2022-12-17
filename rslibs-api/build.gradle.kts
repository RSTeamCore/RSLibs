import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.8.0-Beta"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    //CodeMc
    maven ("https://repo.codemc.org/repository/maven-public/")
    //WorldGuard
    maven ("https://maven.enginehub.org/repo/")
    //PlaceHolderAPI
    maven ("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    //PaperMC
    maven ("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    //Annotations
    compileOnly("org.jetbrains:annotations:23.0.0")

    implementation("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    compileOnly("net.kyori:adventure-api:4.12.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    //PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.2")
    //WorldGuard 7+
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
    //Paper 1.19
    compileOnly(dependencyNotation = "io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    // Configure reobfJar to run when invoking the build task
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    jar {
        archiveFileName.set("${rootProject.name}-bukkit-no-shade-${rootProject.version}.${archiveExtension.getOrElse("jar")}")
    }
    shadowJar {
        archiveFileName.set("${rootProject.name}-bukkit-${rootProject.version}.${archiveExtension.getOrElse("jarinjar")}")
    }
    build {
        dependsOn(shadowJar)
    }
}

tasks {
    shadowJar {}
    artifacts {
        archives(shadowJar)
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}