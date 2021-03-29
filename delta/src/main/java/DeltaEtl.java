import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class DeltaEtl {
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


        data.write()
            .mode(SaveMode.Overwrite)
            .option("overwriteSchema", "true")
            .format("delta")
            .save(PATH);

        //Read
        Dataset<Row> df = spark.read()
            .format("delta")
            .load(PATH);
        df.show();
    }
}
