package ru.mm.cw;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGenerator {
    private static final Random random = new Random(42);

    public static void generateEvents(long eventsCount, int sensorCount, Consumer<Event> consumer) {
        List<String> sensorIds = generateSensorIds(sensorCount);
        for (long i = 0; i < eventsCount; i++) {
            Event event = generateEvent();
            consumer.accept(event);
        }
    }

    public static List<Event> generateChunk(int sensorsCount, int chunkSize) {
        List<Event> eventList = new ArrayList<>();
        DataGenerator.generateEvents(chunkSize, sensorsCount, eventList::add);
        return eventList;
    }

    private static List<String> generateSensorIds(int sensorCount) {
        return IntStream.range(0, sensorCount)
            .mapToObj(__ -> UUID.randomUUID().toString())
            .collect(Collectors.toList());
    }

    private static Event generateEvent() {
        Event event = new Event();
        event.setTimestamp(LocalDateTime.now());
        event.setSensorId(randomString(10));
        event.setEventDescription(randomString(100));
        return event;
    }

    private static String randomString(int length) {
        int leftLimit = 48;
        int rightLimit = 122;

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
