plugins {
    kotlin("jvm") version "2.1.0"
    application
}

application {
    mainClass.set("DayRunAll") // Use the name of your file with "Kt" suffix for default package.
}

dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-main-kts:2.1.0")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:2.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.11.1"
    }
}
