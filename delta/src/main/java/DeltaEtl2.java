import io.delta.tables.DeltaTable;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import java.util.HashMap;

public class DeltaEtl2 {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String PATH = BASE_PATH + "delta-events";

    static String CSV_PATH = BASE_PATH + "events.csv";


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Delta Lake ETL")
            .config("spark.master", "local")
            .getOrCreate();

        Dataset<Row> data = spark.read()
            .option("header", true)
            .csv(CSV_PATH);
        data.show();


        // Init table
        DeltaTable deltaTable = DeltaTable.forPath(PATH);

        // Upsert (merge) new data

        deltaTable.as("oldData")
            .merge(
                data.as("newData"),
                "oldData.uuid = newData.uuid")
            .whenMatched()
            .update(
                new HashMap<>() {{
                    put("uuid", functions.col("newData.uuid"));
                    put("sensorId", functions.col("newData.sensorId"));
                    put("eventDescription", functions.col("newData.eventDescription"));
                    put("timestamp", functions.col("newData.timestamp"));
                }})
            .whenNotMatched()
            .insert(
                new HashMap<>() {{
                    put("uuid", functions.col("newData.uuid"));
                    put("sensorId", functions.col("newData.sensorId"));
                    put("eventDescription", functions.col("newData.eventDescription"));
                    put("timestamp", functions.col("newData.timestamp"));
                }})
            .execute();

        deltaTable.toDF().show();


        //Read
        Dataset<Row> df = spark.read()
            .format("delta")
            .load(PATH);
        df.show();
    }
}
