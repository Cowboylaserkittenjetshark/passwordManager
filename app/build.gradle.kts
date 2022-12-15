/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("passwordmanager.java-application-conventions")
}

dependencies {
  implementation(project(":database")) {
      because("Password storage backend")
    }
  implementation(project(":utils")) {
      because("Generate and check passwords")
    }
}

application {
    // Define the main class for the application.
    mainClass.set("passwordmanager.app.App")
}
