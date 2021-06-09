apply(plugin = "com.github.johnrengelman.shadow")

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        isZip64 = true
        manifest {
            attributes(mapOf("Main-Class" to "write.DeltaEtlBenchmarking"))
        }
    }
}
dependencies {
    implementation(project(":test-data"))
    implementation("org.apache.spark:spark-core_2.11:2.4.5")
    implementation("org.apache.spark:spark-mllib_2.11:2.4.5")
    implementation("io.delta:delta-core_2.11:0.6.1")
    testImplementation("junit", "junit", "4.12")
}
