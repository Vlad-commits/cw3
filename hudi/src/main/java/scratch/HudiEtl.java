package scratch;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.hudi.execution.bulkinsert.BulkInsertSortMode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.SaveMode.Append;

public class HudiEtl {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String HUDI_PATH = BASE_PATH + "hudi";
    static String TABLE_NAME = "hudi-events__1";

    static String CSV_PATH = BASE_PATH + "events_10000_10.csv";

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

        Dataset<Row> data = spark.read()
            .option("header", true)
            .csv(CSV_PATH);

        //Write
        data.write()
            .format("hudi")
            .option("hoodie.insert.shuffle.parallelism", "1")
            .option("hoodie.upsert.shuffle.parallelism", "1")
            .option("hoodie.clean.automatic", "false")
            .option("hoodie.bulkinsert.sort.mode", BulkInsertSortMode.NONE.toString())
            .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "timestamp")
            .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
            .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "")
            .option(HoodieWriteConfig.TABLE_NAME, TABLE_NAME)
            .mode(Append)
            .save(HUDI_PATH);

        //Read
        Dataset<Row> snapshotDf = spark.read().
            format("hudi").
            load(HUDI_PATH + "/*/");
        snapshotDf.show();

    }
}
