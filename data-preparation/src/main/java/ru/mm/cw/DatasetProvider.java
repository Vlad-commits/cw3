package ru.mm.cw;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.stream.Collectors;

public class DatasetProvider {

    public static Dataset<Row> createDataset(SparkSession spark, int sensorsCount, int chunkSize) {
        List<Event> eventList = DataGenerator.generateChunk(sensorsCount, chunkSize);

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
        return spark.createDataFrame(rowList, structType);
    }
}
