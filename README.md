# Ocean's 14

A text-based adventure game where you rob a casino for untold riches and wealth.

## Getting Started

Ocean's 14 is currently compiled for MacOS (aarch64), Windows (x86_64 and aarch64), and Linux (x86_64 and aarch64).  If you would like to play the latest published version of the game, visit [the releases page](https://github.com/BD103/Oceans-14/releases), find the "Assets" section for the latest release, and download the zip file for your operating system and CPU architecture.

- On MacOS you may not be able to open the application extracted from the zip file because "Apple could not verify 'Oceans14.app' is free of malware". To open it anyway open the Settings app, scroll down to "Privacy & Security", find the line titled "'Oceans14.app' was blocked to protect your Mac", and press "Open Anyway".
- On Linux you will have to execute the program through the terminal. Open the extracted `Oceans14/bin` folder in your terminal emulator of choice and run `./Oceans14` to start the game.

If you are using an unsupported platform, you may still be able to play the game by installing Java 17+, downloading `Oceans14.jar` from [the releases page](https://github.com/BD103/Oceans-14/releases), and running `java -jar Oceans14.jar` from the command line.

## Development

### Software Requirements

In order to build this project, you need:

- Java 17

### Architecture

- `app/src/main/java`: Java source code (`Main.java` is the entrypoint for the program)
- `app/src/main/resources`: supporting game files and configuration
- `app/src/test/java`: Java tests
- `app/src/test/resources`: supporting test files and configuration

### Running

This project uses the [Gradle build system](https://gradle.org/), which is responsible for downloading dependencies, compiling the project, and creating release artifacts. For more details, please see [Gradle's Getting Started page](https://docs.gradle.org/current/userguide/getting_started.html).

You can compile and run the project with the following command:

```bash
# Linux, MacOS, or Git Bash on Windows
./gradlew jar
java -jar app/build/libs/Oceans14.jar

# Windows
gradlew.bat jar
java -jar app\build\libs\Oceans14.jar
```

Some IDEs such as IntelliJ IDEA integrate directly with Gradle, and let you run the project using their UI rather than through the terminal.

### Other Gradle Tasks

Gradle has several other capabilities:

```bash
# Run tests
./gradlew test

# Run tests, blessing snapshot tests
BLESS=1 ./gradlew test

# Build application for distribution
./gradlew package

# Build API documentation, placed in app/build/docs/javadoc
./gradlew javadoc

# Delete temporary build artifacts, useful if Gradle is giving you issues
./gradlew clean

# View all tasks you can run
./gradlew tasks
```
