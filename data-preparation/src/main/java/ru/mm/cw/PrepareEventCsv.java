package ru.mm.cw;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.List;

import static ru.mm.cw.Utils.getChunkSizes;
import static ru.mm.cw.Utils.getCsvPath;

public class PrepareEventCsv {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Prepare CSV")
            .config("spark.master", "local")
            .getOrCreate();

        int sensorsCount = 100;
        int chunkCount = 10;

        List<Integer> chunkSizes = getChunkSizes();
        for (Integer size : chunkSizes) {
            prepareCsv(spark, sensorsCount, size, chunkCount);
        }
    }


    private static void prepareCsv(SparkSession spark, int sensorsCount, int chunkSize, int chunkCount) {
        String csvPath = getCsvPath(chunkSize, chunkCount);
        {
            Dataset<Row> eventList = DatasetProvider.createDataset(spark, sensorsCount, chunkSize);
            write(spark, SaveMode.Overwrite, eventList, csvPath);
        }
        for (int i = 0; i < chunkCount - 1; i++) {
            Dataset<Row> eventList = DatasetProvider.createDataset(spark, sensorsCount, chunkSize);

            write(spark, SaveMode.Append, eventList, csvPath);
        }
    }

    private static void write(SparkSession spark, SaveMode mode, Dataset<Row> events, String path) {
        events.write()
            .mode(mode)
            .option("header", true)
            .csv(path);
    }
}
