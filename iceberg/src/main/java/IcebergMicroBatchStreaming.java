import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.Trigger;

import java.util.concurrent.TimeUnit;

public class IcebergMicroBatchStreaming {

    static String BASE_PATH = "hdfs://localhost:9000/";
    static String CATALOG_PATH = BASE_PATH + "catalog-path";
    static String CHECKPOINT_PATH = BASE_PATH + "iceberg-checkpoint";

    static String TABLE_NAME = "iceberg-events";


    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
            .appName("Iceberg ETL")
            .config("spark.master", "local")
            .config("spark.sql.catalog.default_catalog", "org.apache.iceberg.spark.SparkCatalog")
            .config("spark.sql.catalog.default_catalog.type", "hadoop")
            .config("spark.sql.catalog.default_catalog.warehouse", CATALOG_PATH)
            .getOrCreate();

        StreamingQuery streamingQuery = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "verytesttopic123")
            .option("startingOffsets", "latest")
            .load()
            .selectExpr("CAST(value AS STRING)")

            .writeStream()
            .format("iceberg")
            .outputMode("append")
            .trigger(Trigger.ProcessingTime(1, TimeUnit.MINUTES))
            .option("path", "default_catalog." + TABLE_NAME)
//            .option("fanout-enabled", "true")
            .option("checkpointLocation", CHECKPOINT_PATH)
            .start();

        streamingQuery.processAllAvailable();
    }
}
