package ru.mm.cw;

import org.apache.spark.ml.util.LocalStopwatch;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Utils {
    public static String getBasePath() {
//        return "hdfs://namenode:9000/";
        return "hdfs://localhost:9000/";
    }

    public static String getCsvPath(int chunkSize) {
        return getCsvPath(chunkSize, 10);
    }

    public static String getCsvPath(int chunkSize, int chunkCount) {
        return getBasePath() + "events_" + chunkSize + "_" + chunkCount + ".csv";
    }

    public static List<Integer> getChunkSizes() {
        List<Integer> integers = new ArrayList<>();
//        integers.add(10000000);
//        integers.add(1000000);
        integers.add(100000);
        integers.add(10000);
        integers.add(1000);
        return integers;
    }

    public static String getKafkaHosts() {
        return "localhost:9092";
    }

    public static String getTopic() {
        return "verytesttopic123";
    }

    public static void run(Logger logger, SparkSession spark, String runId, BiConsumer<Dataset<Row>, String> mergeNewDataUsingRunId) {
        List<Integer> chunkSizes = getChunkSizes();

        List<Long> runTimes = new ArrayList<>();
        for (Integer chunkSize : chunkSizes) {
            String sourcePath = Utils.getCsvPath(chunkSize);
            LocalStopwatch localStopwatch = new LocalStopwatch("");
            localStopwatch.start();
            Dataset<Row> data = spark.read()
                .option("header", true)
                .csv(sourcePath);
            mergeNewDataUsingRunId.accept(data, runId);
            long runTime = localStopwatch.stop();
            runTimes.add(runTime);
        }

        for (int i = 0; i < chunkSizes.size(); i++) {
            Integer chunkSize = chunkSizes.get(i);
            Long runTime = runTimes.get(i);
            logger.info("etl finished for chunk size {}, time taken = {}", chunkSize, runTime);
        }
    }

    public static void runStreaming(Logger logger, SparkSession spark, Function<DataStreamWriter<Row>, StreamingQuery> writerStreamingQueryFunction, String checkpointPath) {

        DataStreamWriter<Row> writeStream = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", getKafkaHosts())
            .option("subscribe", getTopic())
            .option("startingOffsets", "latest")
            .load()
            .selectExpr("CAST(value AS STRING)")
            .writeStream();
        StreamingQuery query = writerStreamingQueryFunction.apply(writeStream);
        LocalStopwatch localStopwatch = new LocalStopwatch("");
        localStopwatch.start();
        query.processAllAvailable();
        long runTime = localStopwatch.stop();
        logger.info("etl finished , time taken = {}", runTime);
    }

    public int getChunkCount() {
        return 10;
    }

}
