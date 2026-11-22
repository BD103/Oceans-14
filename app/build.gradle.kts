import org.gradle.internal.jvm.Jvm
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

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
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platformlauncher)
}

java {
    toolchain {
        // Require Java 17.
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar {
    archiveBaseName = "Oceans14"

    manifest {
        // Configure entrypoint when jar is run.
        attributes("Main-Class" to "edu.miamioh.cse201.oceans14.Main")
    }

    // Bundle all dependencies into the jar.
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.test {
    useJUnitPlatform()

    // Track the `BLESS` environmental variable. Used to invalidate the Gradle cache when `BLESS`
    // is changed.
    inputs.property("bless") {
        System.getenv("BLESS") ?: "0"
    }
}

tasks.register("package") {
    group = "Package"
    description = "Bundles the Jar into complete user-facing application."

    dependsOn("jpackage")

    if (DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
        dependsOn("copyLaunchWithTerminalMacOS", "resignPackageMacOS")
    }
}

abstract class JPackage : Exec() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty
}

tasks.register<JPackage>("jpackage") {
    group = "Package"
    description = "Runs jpackage on the Jar."

    dependsOn("jar", "cleanPackage")

    executable = Jvm.current().getExecutable("jpackage").absolutePath
    args = listOf(
        "--name", "Oceans14",
        // Find the Jar in `build/libs`.
        "--input", layout.buildDirectory.dir("libs").get().asFile.absolutePath,
        // Package the Jar built with the "jar" task.
        "--main-jar", tasks.jar.get().archiveFileName.get(),
        // By default, jpackage includes a bunch of modules in the package that we don't use. By
        // telling it we only use java.base, we shave >100 MB off of the package size.
        "--add-modules", "java.base",
        // Load resource overrides from `resources`.
        // <https://docs.oracle.com/en/java/javase/17/jpackage/override-jpackage-resources.html>
        "--resource-dir", layout.projectDirectory.dir("resources").asFile.absolutePath,
        // Write the generated package to `build/package`.
        "--dest", layout.buildDirectory.dir("package").get().asFile.absolutePath,
        // Create a folder with the executable and resources, but don't create an installer.
        "--type", "app-image",
        "--verbose",
    )

    if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) {
        // Open the terminal when the exe is run on Windows.
        args = args + listOf("--win-console")
    }

    outputDirectory.set(layout.buildDirectory.dir("package"))
}

tasks.register<Delete>("cleanPackage") {
    group = "Package"
    description = "Deletes the package directory."

    // jpackage errors if the destination directory isn't empty, so we delete it first.
    delete(layout.buildDirectory.dir("package"))
}

tasks.register<Copy>("copyLaunchWithTerminalMacOS") {
    group = "Package"
    description = "Copies launch_with_terminal.sh to the generated package on MacOS."

    dependsOn("jpackage")

    from(layout.projectDirectory.dir("resources").file("launch_with_terminal.sh"))
    into(layout.buildDirectory.dir("package/Oceans14.app/Contents/MacOS"))
}

abstract class Codesign : Exec() {
    @get:OutputDirectories
    abstract val outputDirectories: ConfigurableFileCollection
}

tasks.register<Codesign>("resignPackageMacOS") {
    group = "Package"
    description = "Forcibly signs the package after modification on MacOS."

    dependsOn("copyLaunchWithTerminalMacOS", "jpackage")

    executable = "codesign"
    args = listOf(
        // Ad-hoc signing, don't use an identity.
        "--sign", "-",
        // Replace existing signature, since we made modifications after it was made.
        "--force",
        // Very verbose output.
        "-vvvv",
        // Path to the app we're signing.
        layout.buildDirectory.dir("package/Oceans14.app").get().asFile.absolutePath,
    )

    outputDirectories.setFrom(
        layout.buildDirectory.dir("package/Oceans14.app/Contents/_CodeSignature"),
        layout.buildDirectory.dir("package/Oceans14.app/Contents/runtime/Contents/_CodeSignature")
    )
}
