dependencies {
    implementation(project(":test-data"))
    implementation("org.apache.spark:spark-core_2.12:2.4.7")
    implementation("org.apache.spark:spark-mllib_2.12:2.4.7")
    implementation("io.delta:delta-core_2.12:0.6.1")
    testImplementation("junit", "junit", "4.12")
}
