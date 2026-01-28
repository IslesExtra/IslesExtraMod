val version: String by project
val group: String by project
val name: String by project
val targetJavaVersion = 21

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.shadow)
    // id("net.skyblockisles.islesextra.annotation-processor")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.isxander.dev/releases") }
    maven { url = uri("https://maven.terraformersmc.com/") }
    maven { url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") }
    maven { url = uri("https://libraries.minecraft.net") }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(libs.fabric.mappings)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.modmenu)
    modImplementation(libs.yacl)

    modRuntimeOnly(libs.dev.auth)

    implementation(platform(libs.log4j))
    implementation(libs.discord.ipc)
    implementation(libs.jackson.databind)
    implementation(libs.reflections)
    implementation(libs.javassist)
    implementation(libs.commands)
    implementation(libs.junixsocket.core)
    implementation(libs.junixsocket.common)
    implementation(libs.junixsocket.native.common)

    include(libs.reflections)
    include(libs.javassist)
    include(libs.discord.ipc)
    include(libs.junixsocket.core)
    include(libs.junixsocket.common)
    include(libs.junixsocket.native.common)

    testImplementation(libs.mockito)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release = targetJavaVersion
        }
    }

    withType<ProcessResources> {
        inputs.property("version", version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${name}" }
        }
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {

    }
}
