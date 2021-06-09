dependencies {
    implementation(project(":test-data"))
    implementation("org.apache.spark:spark-core_2.11:2.4.5")
    implementation("org.apache.spark:spark-mllib_2.11:2.4.5")
    implementation("org.apache.iceberg:iceberg-spark-runtime:0.11.0")
}
