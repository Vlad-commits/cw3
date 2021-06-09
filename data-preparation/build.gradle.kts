dependencies {
    implementation(project(":test-data"))
    implementation("org.apache.spark:spark-core_2.11:2.4.5")
    implementation("org.apache.spark:spark-sql_2.11:2.4.5")
    implementation("org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.5")
}
