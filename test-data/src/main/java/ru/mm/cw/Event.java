package ru.mm.cw;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Event {
    private UUID uuid = UUID.randomUUID();
    private String sensorId;
    private String eventDescription;
    private Long timestamp;
    public String getUuid() {
        return uuid.toString();
    }
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
