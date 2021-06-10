package query;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DeltaQuery {
    static String BASE_PATH = "hdfs://localhost:9000/";
    //    static String PATH = BASE_PATH + "delta-events";
    static String PATH = BASE_PATH + "events1";


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Delta Lake Query")
            .config("spark.master", "local")
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
            .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
            .getOrCreate();

        Dataset<Row> df = spark.read()
            .format("delta")
            .load(PATH);
        df.createOrReplaceTempView("temp_events");
        spark.sql("select count(1) from  temp_events ").show();
        spark.sql("select sensorId , count(1) from  temp_events group by sensorId").show();
        spark.sql("select * from (select temp_events.* , row_number() over (partition by sensorId order by timestamp desc) as rnk from temp_events) where rnk=1").show();

    }
}
