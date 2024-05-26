plugins {
    id("java")
    id("fabric-loom") version "1.6-SNAPSHOT"
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

loom {
    accessWidenerPath = file("src/main/resources/toil-and-trouble.accesswidener")
}

repositories {
    mavenCentral()
    maven("https://cursemaven.com")
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.15.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.1+1.20.1")

    modImplementation("curse.maven:william-wythers-overhauled-overworld-921022:4793728")
    modImplementation("curse.maven:falling-tree-349559:5010620")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    javaToolchains {
        toolchain.languageVersion = JavaLanguageVersion.of(17)
    }
}