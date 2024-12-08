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

tasks.register("printFullRunCommand") {
    doLast {
        val mainClass = "DayRunAll" // Replace with your main class
        val classDirs = files("build/classes/kotlin/main")
        val runtimeClasspath = configurations.runtimeClasspath.get().files.joinToString(separator = ";") { it.absolutePath }
        println("Run the application with the following command:")
        println("java -cp \"$classDirs;$runtimeClasspath\" $mainClass")
    }
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("adventofcode")
//    archiveVersion.set("1.0")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    exclude("META-INF/services/javax.script.ScriptEngineFactory") // Exclude specific file
    manifest {
        attributes["Main-Class"] = "DayRunAll"
    }
}

