# Ocean's 14

A text-based adventure game where you rob a casino for untold riches and wealth.

## Development

### Software Requirements

In order to build this project, you need:

- Java 17
- A modern C compiler (GCC, Clang, or MSVC)

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
java -jar app/build/libs/app.jar

# Windows
gradlew.bat jar
java -jar app\build\libs\app.jar
```

Some IDEs such as IntelliJ IDEA integrate directly with Gradle, and let you run the project using their UI rather than through the terminal.

### Other Gradle Tasks

Gradle has several other capabilities:

```bash
# Run tests
./gradlew test

# Run tests, blessing snapshot tests
BLESS=1 ./gradlew test

# Build a .jar file for distribution
./gradlew jar

# Build API documentation, placed in app/build/docs/javadoc
./gradlew javadoc

# Delete temporary build artifacts, useful if Gradle is giving you issues
./gradlew clean

# View all tasks you can run
./gradlew tasks
```
