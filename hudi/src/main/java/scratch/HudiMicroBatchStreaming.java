package scratch;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.hudi.execution.bulkinsert.BulkInsertSortMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;

import static org.apache.spark.sql.SaveMode.Append;

public class HudiMicroBatchStreaming {

    static String BASE_PATH = "hdfs://localhost:9000/";
    static String CHECKPOINT_PATH = BASE_PATH + "hudi-checkpoint";

    static String HUDI_PATH = BASE_PATH + "hudi";
    static String TABLE_NAME = "hudi-events__1";


    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
            .appName("Hudi ETL")
            .config("spark.master", "local")
            .config("spark.executor.memory", "8g")
            .config("spark.driver.memory", "4g")
            .config("spark.rdd.compress", "true")
            .config("spark.driver.maxResultSize", "2g")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.kryoserializer.buffer.max", "512m")
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
            .foreachBatch((v1, v2) -> {
                v1.persist();
                v1.write()
                    .format("hudi")
                    .option("hoodie.bulkinsert.sort.mode", BulkInsertSortMode.NONE.toString())
                    .option("hoodie.insert.shuffle.parallelism", "1")
                    .option("hoodie.upsert.shuffle.parallelism", "1")
                    .option("hoodie.clean.automatic", "false")
                    .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "timestamp")
                    .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
                    .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "")
                    .option(HoodieWriteConfig.TABLE_NAME, TABLE_NAME)
                    .mode(Append)
                    .save(HUDI_PATH);
                v1.unpersist();
            })
            .option("checkpointLocation", CHECKPOINT_PATH)
            .start();

        query.processAllAvailable();

    }
}
