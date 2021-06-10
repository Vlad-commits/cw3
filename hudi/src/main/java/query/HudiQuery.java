package query;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class HudiQuery {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String HUDI_PATH = BASE_PATH + "hudi";


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Hudi Query")
            .config("spark.master", "local")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.kryoserializer.buffer.max", "256")
            .getOrCreate();


        Dataset<Row> snapshotDf = spark.read().
            format("hudi").
            load(HUDI_PATH + "/*/");
        snapshotDf.createOrReplaceTempView("temp_events");

        spark.sql("select count(1) from  temp_events ").show();
        spark.sql("select sensorId , count(1) from  temp_events group by sensorId").show();
        spark.sql("select * from (select temp_events.* , row_number() over (partition by sensorId order by timestamp desc) as rnk from temp_events) where rnk=1").show();


    }
}
