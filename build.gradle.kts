plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "cc.venja"
version = "0.1.0"

repositories {
    mavenCentral()
}

mirai {
    jvmTarget = JavaVersion.VERSION_1_8
}
