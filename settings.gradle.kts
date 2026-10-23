// This file describes what projects are in this workspace.
// For more details, see <https://docs.gradle.org/9.8.1/userguide/multi_project_builds.html>.

plugins {
    // Automatically download the correct version of Java if you don't have it installed.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Oceans-14"
include("app")
