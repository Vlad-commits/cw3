package ru.mm.cw;

import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrepareEventCsv {
    static String BASE_PATH = "hdfs://localhost:9000/";
    static String CSV_PATH = BASE_PATH + "events.csv";

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Prepare CSV")
            .config("spark.master", "local")
            .getOrCreate();

        int sensorsCount = 100;
        int chunkSize = 1000000;
        int chunkCount = 10;

        {
            List<Event> eventList = generateChunk(sensorsCount, chunkSize);
            write(spark, SaveMode.Overwrite, eventList);
        }
        for (int i = 0; i < chunkCount - 1; i++) {
            List<Event> eventList = generateChunk(sensorsCount, chunkSize);

            write(spark, SaveMode.Append, eventList);
        }

        //Read
        Dataset<Row> df = spark.read()
            .option("header", true)
            .csv(CSV_PATH);
        df.show();

    }

    private static void write(SparkSession spark, SaveMode mode, List<Event> eventList) {
        StructField[] fields = {
            new StructField("uuid", DataTypes.StringType.defaultConcreteType(), false, Metadata.empty()),
            new StructField("sensorId", DataTypes.StringType.defaultConcreteType(), false, Metadata.empty()),
            new StructField("eventDescription", DataTypes.StringType.defaultConcreteType(), false, Metadata.empty()),
            new StructField("timestamp", DataTypes.LongType.defaultConcreteType(), false, Metadata.empty())
        };
        List<Row> rowList = eventList.stream()
            .map(event -> RowFactory.create(event.getUuid(), event.getSensorId(), event.getEventDescription(), event.getTimestamp()))
            .collect(Collectors.toList());
        StructType structType = new StructType(fields);
        Dataset<Row> events = spark.createDataFrame(rowList, structType);
        events.write()
            .mode(mode)
            .option("header", true)
            .csv(CSV_PATH);
    }


    private static List<Event> generateChunk(int sensorsCount, int chunkSize) {
        List<Event> eventList = new ArrayList<>();
        DataGenerator.generateEvents(chunkSize, sensorsCount, eventList::add);
        return eventList;
    }
}
