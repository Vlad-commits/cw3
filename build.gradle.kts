
plugins {
    id("com.github.johnrengelman.shadow") version "5.0.0" apply false
}
subprojects {
    apply(plugin = "java")
    tasks {
        named<JavaCompile>("compileJava") {
            targetCompatibility = "1.8"
            sourceCompatibility = "1.8"
        }
    }

    group = "org.mm"
    version = "1.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}
