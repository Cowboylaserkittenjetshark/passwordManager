/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("passwordmanager.java-library-conventions")
}

dependencies {
  implementation("com.password4j:password4j:1.6.3") {
      because("OS independent Argon2 KDF implementation")
    }
    implementation("com.password4j:password4j-jca:1.0.4") {
      because("password4j extension for the Java Cryptography Architecture")
    }
    implementation("org.slf4j:slf4j-nop:2.0.6") {
        because("No operation logging for p4j")
      }
    implementation("com.google.code.gson:gson:2.10") {
      because("Convert Objects to JSON")
    }
}
