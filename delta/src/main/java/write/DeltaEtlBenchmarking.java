package write;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mm.cw.Utils;

import java.util.HashMap;

import static ru.mm.cw.Utils.getBasePath;
import static ru.mm.cw.Utils.getChunkSizes;

public class DeltaEtlBenchmarking {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeltaEtlBenchmarking.class);

    static String RUN_ID = "1";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Delta Lake ETL")
            .config("spark.master", "local")
            .config("spark.executor.memory", "10g")
            .config("spark.driver.memory", "4g")
            .config("spark.rdd.compress", "true")
//            .config("spark.driver.maxResultSize", "2g")
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
            .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
            .getOrCreate();

        recreateTableIfNeeded(spark);
        Utils.run(LOGGER, spark, RUN_ID, DeltaEtlBenchmarking::write);

    }

    private static void recreateTableIfNeeded(SparkSession spark) {
        String deltaTableName = getBasePath() + "events" + RUN_ID;

        try {
            DeltaTable deltaTable = DeltaTable.forPath(deltaTableName);
        } catch (Exception e) {
            Dataset<Row> data = spark.read()
                .option("header", true)
                .csv(Utils.getCsvPath(getChunkSizes().get(0)));

            data.write().format("delta").save(deltaTableName);
        }
    }

    private static void write(Dataset<Row> data, String runId) {
        String deltaTableName = getBasePath() + "events" + runId;

        write(deltaTableName, data);
    }

    private static void write(String deltaTableName, Dataset<Row> data) {
        DeltaTable deltaTable = DeltaTable.forPath(deltaTableName);
        write(data, deltaTable);
    }

    private static void write(Dataset<Row> data, DeltaTable deltaTable) {
        deltaTable.as("oldData")
            .merge(
                data.as("newData"),
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
    }
}
