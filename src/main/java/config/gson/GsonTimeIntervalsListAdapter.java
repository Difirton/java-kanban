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
        writer.beginObject();
        writer.value(timeIntervalsList.getTimeIntervals().toString());
        writer.endObject();
    }

    @Override
    public TimeIntervalsList read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        TimeIntervalsList timeIntervalsList = new TimeIntervalsList();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                LocalDateTime start = LocalDateTime.MAX;
                LocalDateTime finish = LocalDateTime.MAX;
                if (token.equals(JsonToken.NAME)) {
                    fieldName = reader.nextName();
                }
                if ("start".equals(fieldName)) {
                    token = reader.peek();
                    start = LocalDateTime.parse(reader.nextString());
                }
                if ("finish".equals(fieldName)) {
                    token = reader.peek();
                    finish = LocalDateTime.parse(reader.nextString());
                }
                timeIntervalsList.add(start, finish);
            }
            reader.endObject();
        }
        reader.endArray();
        return timeIntervalsList;
    }
}
