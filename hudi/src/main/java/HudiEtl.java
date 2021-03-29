import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

import static org.apache.hudi.QuickstartUtils.getQuickstartWriteConfigs;
import static org.apache.spark.sql.SaveMode.Overwrite;
//8m
public class HudiEtl {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String HUDI_PATH = BASE_PATH + "hudi";
    static String TABLE_NAME = "hudi-events";

    static String CSV_PATH = BASE_PATH + "events.csv";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Hudi ETL")
            .config("spark.master", "local")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.kryoserializer.buffer.max", "256")
            .getOrCreate();


        Dataset<Row> data = spark.read()
            .option("header", true)
            .csv(CSV_PATH);
        data.show();


        //Write
        data.write()
            .format("hudi")
            .options(getQuickstartWriteConfigs())
            .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "timestamp")
            .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
//            .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "sensorId")
            .option(HoodieWriteConfig.TABLE_NAME, TABLE_NAME)
            .mode(Overwrite)
            .save(HUDI_PATH);

        //Read
        Dataset<Row> snapshotDf = spark.read().
            format("hudi").
            load(HUDI_PATH + "/*/");
        snapshotDf.show();

    }
}
