package write;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.config.HoodieCompactionConfig;
import org.apache.hudi.config.HoodieIndexConfig;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.hudi.execution.bulkinsert.BulkInsertSortMode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mm.cw.Utils;

import static org.apache.spark.sql.SaveMode.Append;
import static ru.mm.cw.Utils.getBasePath;

public class HudiEtlBenchmarks {
    private static final Logger LOGGER = LoggerFactory.getLogger(HudiEtlBenchmarks.class);

    private static final String RUN_ID = "1";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Hudi ETL")
//            .config("spark.master", "local")
//            .config("spark.executor.memory", "8g")
//            .config("spark.driver.memory", "4g")
            .config("spark.rdd.compress", "true")
            .config("spark.driver.maxResultSize", "2g")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.kryoserializer.buffer.max", "512m")
            .getOrCreate();
        Utils.run(LOGGER, spark, RUN_ID, HudiEtlBenchmarks::write);
    }

    private static void write(Dataset<Row> data, String runId) {
        String hudiTableName = "hudi-events_" + "_" + runId;
        String hudiPath = getBasePath() + "hudi";
        write(data, hudiTableName, hudiPath);
    }

    private static void write(Dataset<Row> data, String hudiTableName, String hudiPath) {

        data.write()
            .format("hudi")
//            .options(getQuickstartWriteConfigs())
            .option("hoodie.insert.shuffle.parallelism", "64")
            .option("hoodie.upsert.shuffle.parallelism", "64")
            .option("hoodie.index.type", "SIMPLE")
            .option("hoodie.memory.merge.max.size", "2004857600000")
//            .option("hoodie.clean.automatic", "false")
//            .option("hoodie.bulkinsert.sort.mode", BulkInsertSortMode.NONE.toString())
//            .option("hoodie.bloom.index.bucketized.checking","false")
//            .option("hoodie.bloom.index.prune.by.ranges","false")
//            .option("hoodie.bloom.index.keys.per.bucket","100000")

//            .option(DataSourceWriteOptions.OPERATION_OPT_KEY(),"insert_overwrite")
            .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "timestamp")
            .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
            .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "")
            .option(HoodieWriteConfig.TABLE_NAME, hudiTableName)
            .mode(Append)
            .save(hudiPath);
    }
}
