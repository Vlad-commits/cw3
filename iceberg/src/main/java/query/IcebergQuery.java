package query;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class IcebergQuery {

    static String BASE_PATH = "hdfs://localhost:9000/";
    static String TABLE_NAME = "iceberg-events";

    static String CATALOG_PATH = BASE_PATH + "catalog-path";


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Iceberg ETL")
            .config("spark.master", "local")
            .config("spark.sql.catalog.default_catalog", "org.apache.iceberg.spark.SparkCatalog")
            .config("spark.sql.catalog.default_catalog.type", "hadoop")
            .config("spark.sql.catalog.default_catalog.warehouse", CATALOG_PATH)
            .getOrCreate();

        Dataset<Row> df = spark.read()
            .format("iceberg")
            .load("default_catalog." + TABLE_NAME);
        df.createOrReplaceTempView("temp_events");

        spark.sql("select count(1) from  temp_events ").show();
        spark.sql("select sensorId , count(1) from  temp_events group by sensorId").show();
        spark.sql("select * from (select temp_events.* , row_number() over (partition by sensorId order by timestamp desc) as rnk from temp_events) where rnk=1").show();

    }
}
