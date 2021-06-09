package ru.mm.cw;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

import static org.apache.spark.sql.functions.struct;
import static org.apache.spark.sql.functions.to_json;

public class PrepareKafkaMessages {
    public static void main(String[] args) throws StreamingQueryException {


        SparkSession spark = SparkSession.builder()
            .appName("Prepare Kafka Messages")
            .config("spark.master", "local")
            .getOrCreate();

        int sensorsCount = 100;
        int chunkSize = 1000;
        int chunkCount = 1;

        for (int i = 0; i < chunkCount; i++) {
            Dataset<Row> eventList = DatasetProvider.createDataset(spark, sensorsCount, chunkSize);

            eventList
                .select(to_json(struct("*")).as("value"))
                .selectExpr("CAST(value AS STRING)")
                .write()
                .format("kafka")
                .option("kafka.bootstrap.servers", Utils.getKafkaHosts())
                .option("topic", Utils.getTopic())
                .save();

        }

        Dataset<Row> df = spark
            .read()
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "verytesttopic123")
            .load();
        Dataset<Row> dataset = df.selectExpr("CAST(value AS STRING)");
        dataset
            .show();

    }


}
