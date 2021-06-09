apply(plugin = "com.github.johnrengelman.shadow")

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        isZip64 = true
        manifest {
            attributes(mapOf("Main-Class" to "write.HudiEtlBenchmarks"))
        }
    }
}


dependencies {
    implementation(project(":test-data"))
    implementation("org.apache.spark:spark-core_2.11:2.4.5")
    implementation("org.apache.spark:spark-mllib_2.11:2.4.5")
    implementation("org.apache.hudi:hudi-spark-bundle_2.11:0.7.0")
    implementation("org.apache.spark:spark-avro_2.11:2.4.5")
    implementation("org.apache.parquet:parquet-hive-storage-handler:1.11.1")
}
