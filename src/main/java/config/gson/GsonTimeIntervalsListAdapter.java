package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import utill.TimeIntervalsList;

import java.io.IOException;
import java.time.LocalDateTime;

public class GsonTimeIntervalsListAdapter extends TypeAdapter<TimeIntervalsList> {
    @Override
    public void write(JsonWriter writer, TimeIntervalsList timeIntervalsList) throws IOException {
        writer.beginArray();
        timeIntervalsList.getTimeIntervals().forEach(o -> {
            try {
                writer.beginObject();
                writer.name("start");
                writer.value(o.getStart().toString());
                writer.name("finish");
                writer.value(o.getFinish().toString());
                writer.endObject();
            } catch (IOException exception) {
                throw new RuntimeException("Error when translating to Json TimeIntervalsList. Wrong format.");
            }
        });
        writer.endArray();
    }

    @Override
    public TimeIntervalsList read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        TimeIntervalsList timeIntervalsList = new TimeIntervalsList();
        while (reader.hasNext()) {
            reader.beginObject();
            LocalDateTime start = LocalDateTime.MAX;
            LocalDateTime finish = LocalDateTime.MAX;
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NAME)) {
                    fieldName = reader.nextName();
                }
                if ("start".equals(fieldName)) {
                    reader.peek();
                    start = LocalDateTime.parse(reader.nextString());
                }
                if ("finish".equals(fieldName)) {
                    reader.peek();
                    finish = LocalDateTime.parse(reader.nextString());
                }
            }
            timeIntervalsList.add(start, finish);
            reader.endObject();
        }
        reader.endArray();
        return timeIntervalsList;
    }
}
