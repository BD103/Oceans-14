import org.gradle.internal.jvm.Jvm

// This file describes how to build the Java project.
// For more details, see <https://docs.gradle.org/9.8.1/userguide/building_java_projects.html>.

plugins {
    // Add support for building Java applications.
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Cross-platform terminal control library.
    implementation(libs.jline)

    // Use JUnit test framework.
    testImplementation(libs.junit)
}

java {
    toolchain {
        // Require Java 17.
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar {
    manifest {
        // Configure entrypoint when jar is run.
        attributes("Main-Class" to "edu.miamioh.cse201.oceans14.Main")
    }

    // Bundle all dependencies into the jar.
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.register<Exec>("run") {
    group = "Application"
    description = "Runs this project as a JVM application."
    dependsOn(tasks.jar)
    standardInput = System.`in`

    commandLine = listOf(
        Jvm.current().javaExecutable.absolutePath,
        "-jar",
        layout.buildDirectory.file("libs/app.jar").get().asFile.absolutePath,
    )

}
