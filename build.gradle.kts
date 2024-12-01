plugins {
    kotlin("jvm") version "1.7.22"
}


dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-main-kts:1.7.22")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.7.22")
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
        gradleVersion = "8.8"
    }
}
