package scratch;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;

public class DeltaStreaming {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String PATH = BASE_PATH + "delta-events";

    static String CHECKPOINT_PATH = BASE_PATH + "delta-checkpoint";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Delta Lake ETL")
            .config("spark.master", "local")
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
            .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
            .getOrCreate();

        StreamingQuery query = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "verytesttopic123")
            .option("startingOffsets", "latest")
            .load()
            .selectExpr("CAST(value AS STRING)")
            .writeStream()
            .format("delta")
            .outputMode("append")
            .option("checkpointLocation", CHECKPOINT_PATH)
            .start(PATH);

        query.processAllAvailable();

    }
}
