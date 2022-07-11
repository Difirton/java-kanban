package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import constant.TaskStatus;
import entity.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GsonEpicAdapter extends TypeAdapter<Epic> {

    @Override
    public Epic read(JsonReader reader) throws IOException {
        reader.beginObject();
        String fieldName = null;
        Epic.EpicBuilder epicBuilder = new Epic.EpicBuilder();
        List<Long> allIdSubtasksInEpic = new ArrayList<>(); //TODO сделать парсинг листа
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if ("id".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.ID(reader.nextLong());
            }
            if("allIdSubtasksInEpic".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.AllIdSubtasksInEpic(parsingIdSubtasks(reader.nextString()));
            }
            if ("name".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.Name(reader.nextString());
            }
            if ("description".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.Description(reader.nextString());
            }
            if ("status".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.Status(TaskStatus.valueOf(reader.nextString()));
            }
            if ("start".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.StartDateTime(LocalDateTime.parse(reader.nextString()));
            }
            if ("execution".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.TimeExecution(Duration.parse(reader.nextString()));
            }
        }
        reader.endObject();
        return epicBuilder.build();
    }

    private List<Long> parsingIdSubtasks(String arrayIdSubtasks) {
        return Arrays.stream(arrayIdSubtasks.substring(1, arrayIdSubtasks.length()-1).split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Override
    public void write(JsonWriter writer, Epic epic) throws IOException {
        writer.beginObject();
        writer.name("id");
        writer.value(epic.getId());
        writer.name("allIdSubtasksInEpic");
        writer.value(epic.getAllIdSubtasksInEpic().toString());
        writer.name("name");
        writer.value(epic.getName());
        writer.name("description");
        writer.value(epic.getDescription());
        writer.name("status");
        writer.value(epic.getStatus().toString());
        writer.name("start");
        writer.value(epic.getStartDateTime().toString());
        writer.name("execution");
        writer.value(epic.getTimeExecution().toString());
        writer.endObject();
    }
}