package scratch;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mm.cw.Utils;

import java.util.HashMap;

import static ru.mm.cw.Utils.getBasePath;
import static ru.mm.cw.Utils.getChunkSizes;

public class DeltaStreaming2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeltaStreaming2.class);
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String PATH = BASE_PATH + "delta-events-stream";
    static String CHECKPOINT_PATH = BASE_PATH + "delta-checkpoint";
    static String deltaTableName = getBasePath() + "events-streaming";


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Delta Lake ETL")
            .config("spark.master", "local")
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
            .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
            .getOrCreate();

        DataStreamWriter<Row> writeStream = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", Utils.getKafkaHosts())
            .option("subscribe", Utils.getTopic())
            .option("startingOffsets", "latest")
            .load()
            .selectExpr("CAST(value AS STRING)")
            .writeStream();
        DeltaTable deltaTable = DeltaTable.forPath(deltaTableName);

        StreamingQuery query = writeStream
            .option("checkpointLocation", CHECKPOINT_PATH)
            .foreachBatch(((v1, v2) -> {
                v1.persist();
                deltaTable.as("oldData")
                    .merge(
                        v1.as("newData"),
                        "oldData.uuid = newData.uuid")
                    .whenMatched()
                    .update(
                        new HashMap<String, Column>() {{
                            put("uuid", functions.col("newData.uuid"));
                            put("sensorId", functions.col("newData.sensorId"));
                            put("eventDescription", functions.col("newData.eventDescription"));
                            put("timestamp", functions.col("newData.timestamp"));
                        }})
                    .whenNotMatched()
                    .insert(
                        new HashMap<String, Column>() {{
                            put("uuid", functions.col("newData.uuid"));
                            put("sensorId", functions.col("newData.sensorId"));
                            put("eventDescription", functions.col("newData.eventDescription"));
                            put("timestamp", functions.col("newData.timestamp"));
                        }})
                    .execute();
                v1.unpersist();
            }))
            .start();
        query.processAllAvailable();
    }

    private static void recreateTableIfNeeded(SparkSession spark) {

        try {
            DeltaTable deltaTable = DeltaTable.forPath(deltaTableName);
        } catch (Exception e) {
            Dataset<Row> data = spark.read()
                .option("header", true)
                .csv(Utils.getCsvPath(getChunkSizes().get(0)));

            data.write().format("delta").save(deltaTableName);
        }
    }

}
