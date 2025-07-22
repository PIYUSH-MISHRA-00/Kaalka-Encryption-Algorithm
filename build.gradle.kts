

plugins {
    kotlin("jvm") version "1.5.30"
}

version = "4.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("commons-net:commons-net:3.8.0") // For NTP functionality
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}


