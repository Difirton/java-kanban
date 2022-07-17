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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GsonEpicAdapter extends TypeAdapter<Epic> {

    @Override
    public void write(JsonWriter writer, Epic epic) throws IOException {
        writer.beginObject();
        writer.name("task_type");
        writer.value(epic.getType().getNameTypeTask());
        writer.name("id");
        writer.value(epic.getId());
        writer.name("all_id_subtasks_in_epic");
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

    @Override
    public Epic read(JsonReader reader) throws IOException {
        reader.beginObject();
        String fieldName = null;
        JsonToken token = reader.peek();
        if (token.equals(JsonToken.NAME)) {
            fieldName = reader.nextName();
        }
        if ("task_type".equals(fieldName)) {
            reader.peek();
            reader.skipValue();
        }
        Epic newEpic = constructEpic(reader, fieldName);
        reader.endObject();
        return newEpic;
    }

    protected static Epic constructEpic(JsonReader reader, String fieldNameIfAlreadyDefine) throws IOException {
        Epic.EpicBuilder epicBuilder = new Epic.EpicBuilder();
        String fieldName = fieldNameIfAlreadyDefine;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if ("id".equals(fieldName)) {
                reader.peek();
                epicBuilder.ID(reader.nextLong());
            }
            if ("all_id_subtasks_in_epic".equals(fieldName)) {
                reader.peek();
                epicBuilder.AllIdSubtasksInEpic(parsingIdSubtasks(reader.nextString()));
            }
            if ("name".equals(fieldName)) {
                reader.peek();
                epicBuilder.Name(reader.nextString());
            }
            if ("description".equals(fieldName)) {
                reader.peek();
                epicBuilder.Description(reader.nextString());
            }
            if ("status".equals(fieldName)) {
                reader.peek();
                epicBuilder.Status(TaskStatus.valueOf(reader.nextString()));
            }
            if ("start".equals(fieldName)) {
                reader.peek();
                epicBuilder.StartDateTime(LocalDateTime.parse(reader.nextString()));
            }
            if ("execution".equals(fieldName)) {
                reader.peek();
                epicBuilder.TimeExecution(Duration.parse(reader.nextString()));
            }
        }
        return epicBuilder.build();
    }

    protected static List<Long> parsingIdSubtasks(String arrayIdSubtasks) {
        return Arrays.stream(arrayIdSubtasks.substring(1, arrayIdSubtasks.length()-1).split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}