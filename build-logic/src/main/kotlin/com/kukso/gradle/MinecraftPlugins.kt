package com.kukso.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

class MinecraftBasePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("java-library")

        group = "com.kukso.minecraft.$name"
        version = projectDir.resolve("version.txt").takeIf { it.exists() }?.readText()?.trim().orEmpty()
            .ifBlank { "0.0.0-SNAPSHOT" }

        extensions.configure<JavaPluginExtension> {
        }

        configurations.named("testCompileOnly") {
            extendsFrom(configurations.getByName("compileOnly"))
        }

        configurations.named("testRuntimeOnly") {
            extendsFrom(configurations.getByName("compileOnly"))
        }

        dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter:5.10.3")
        dependencies.add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher:1.10.3")

        tasks.withType<Jar>().configureEach {
            archiveBaseName.set("${kuksoProductName()}-Paper")
            archiveVersion.set(version.toString())
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.release.set(21)
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }

        tasks.withType<Javadoc>().configureEach {
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        }

        tasks.withType<Copy>().matching { it.name == "processResources" }.configureEach {
            filteringCharset = "UTF-8"
            inputs.property("version", version.toString())
            filesMatching(listOf("plugin.yml")) {
                expand(mapOf("version" to version.toString()))
            }
        }
    }

    private fun Project.kuksoProductName(): String {
        return when (name) {
            "lib" -> "KuksoLib"
            "dialogs" -> "KuksoDialogs"
            "dialogs-exp-config-addon" -> "KuksoDialogsExpConfigAddon"
            "worlds" -> "KuksoWorlds"
            "items" -> "KuksoItems"
            else -> "Kukso${name.replaceFirstChar { it.uppercase() }}"
        }
    }
}

class MinecraftLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply(MinecraftBasePlugin::class.java)
        pluginManager.apply("maven-publish")
        pluginManager.apply("signing")

        extensions.configure<JavaPluginExtension> {
            withSourcesJar()
            withJavadocJar()
        }

        extensions.configure<PublishingExtension> {
            publications {
                create("mavenJava", MavenPublication::class.java) {
                    from(components.getByName("java"))
                    artifactId = "kukso-minecraft-lib"
                    pom {
                        name.set("KuksoLib")
                        description.set("Core library for the Kukso Minecraft plugin ecosystem.")
                        url.set("https://github.com/KuksoHQ/kukso-minecraft-plugins")
                    }
                }
            }
            repositories {
                maven {
                    name = "KuksoStudios"
                    val releases = uri("https://nexus.kukso.com/repository/maven-releases/")
                    val snapshots = uri("https://nexus.kukso.com/repository/maven-snapshots/")
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshots else releases
                    credentials {
                        username = findProperty("kuksoUser") as String? ?: System.getenv("KUKSO_USER")
                        password = findProperty("kuksoToken") as String? ?: System.getenv("KUKSO_TOKEN")
                    }
                }
            }
        }
    }
}

class MinecraftPluginPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.apply(MinecraftBasePlugin::class.java)
            tasks.named<Jar>("jar") {
                duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
            }
        }
    }
}
