package write;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.types.Types;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mm.cw.Utils;

public class IcebergEtlBenchmarks {
    private static final Logger LOGGER = LoggerFactory.getLogger(IcebergEtlBenchmarks.class);
    static String RUN_ID = "1";
    static String BASE_PATH = Utils.getBasePath();
    static String TABLE_NAME = "iceberg-events";

    static String CATALOG_PATH = BASE_PATH + "catalog-path";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Iceberg ETL")
            .config("spark.master", "local")
            .config("spark.executor.memory", "8g")
            .config("spark.driver.memory", "4g")
            .config("spark.rdd.compress", "true")
            .config("spark.driver.maxResultSize", "2g")
            .config("spark.sql.catalog.default_catalog", "org.apache.iceberg.spark.SparkCatalog")
            .config("spark.sql.catalog.default_catalog.type", "hadoop")
            .config("spark.sql.catalog.default_catalog.warehouse", CATALOG_PATH)
            .getOrCreate();
        Configuration conf = new Configuration();
        HadoopCatalog catalog = new HadoopCatalog(conf, CATALOG_PATH);
        Table table = recreate(catalog);
        Utils.run(LOGGER, spark, RUN_ID, (rowDataset, runId) -> write(rowDataset, runId, spark));
    }

    private static void write(Dataset<Row> rowDataset, String runId, SparkSession spark) {


        String tempUpdate = "icebergupdatetemp";
        rowDataset.createOrReplaceTempView(tempUpdate);
        spark.sql("MERGE INTO " + "default_catalog." + TABLE_NAME + " old" +
            "        USING (SELECT * from icebergupdatetemp) new" +
            "        ON old.uuid = new.uuid" +
            "        WHEN MATCHED THEN UPDATE SET old.uuid = new.uuid, old.sensorId = new.sensorId,old.eventDescription = new.eventDescription, old.timestamp = new.timestamp" +
            "        WHEN NOT MATCHED THEN INSERT *");


    }


    private static Table recreate(Catalog catalog) {

        TableIdentifier name = TableIdentifier.of(TABLE_NAME);
        catalog.dropTable(name);

        Schema schema = new Schema(
            Types.NestedField.optional(1, "uuid", Types.StringType.get()),
            Types.NestedField.optional(2, "sensorId", Types.StringType.get()),
            Types.NestedField.optional(3, "eventDescription", Types.StringType.get()),
            Types.NestedField.optional(4, "timestamp", Types.StringType.get())
        );

//        PartitionSpec spec = PartitionSpec.builderFor(schema)
//            .hour("event_time")
//            .identity("uuid")
//            .build();
        return catalog.createTable(name, schema);


    }
}
