import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	//id 'com.github.johnrengelman.shadow' version '6.1.0'
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.serialization") version "1.5.21"
	id("fabric-loom") version "0.8-SNAPSHOT"
}

java {
	sourceCompatibility = JavaVersion.VERSION_16
	targetCompatibility = JavaVersion.VERSION_16
}

val modId: String by project
val modVersion: String by project
val group: String by project
val minecraftVersion: String by project
val fabricVersion: String by project
val kotlinVersion: String by project
val loaderVersion: String by project
val yarnMappings: String by project

project.group = group
version = modVersion



//compileKotlin.kotlinOptions.jvmTarget = "1.8"

repositories {
	mavenLocal()
	mavenCentral()
	maven(url = "https://kotlin.bintray.com/kotlinx")
	maven(url = "https://maven.shedaniel.me/")
	maven(url = "https://maven.terraformersmc.com/") {
		name = "Mod Menu"
	}
}

minecraft { }

dependencies {
	//to change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings("net.fabricmc:yarn:${yarnMappings}:v2")
	modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

	// Kambrik API
	modImplementation("io.ejekta:kambrik:2.1.+")

	modApi("me.shedaniel.cloth:cloth-config-fabric:5.0.38") {
		exclude(group = "net.fabricmc.fabric-api")
	}

	implementation("com.google.code.findbugs:jsr305:3.0.2")

	modApi("com.terraformersmc:modmenu:2.0.6") {
		exclude(module = "fabric-api")
		exclude(module = "config-2")
	}

	modImplementation(group = "net.fabricmc", name = "fabric-language-kotlin", version = "1.6.3+kotlin.1.5.21")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
}


tasks.getByName<ProcessResources>("processResources") {
	filesMatching("fabric.mod.json") {
		expand(
			mutableMapOf<String, String>(
				"modid" to modId,
				"version" to modVersion,
				"kotlinVersion" to kotlinVersion,
				"fabricApiVersion" to fabricVersion
			)
		)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "16"
	}
}
