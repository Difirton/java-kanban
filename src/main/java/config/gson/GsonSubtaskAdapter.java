package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import constant.TaskStatus;
import entity.Subtask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class GsonSubtaskAdapter extends TypeAdapter<Subtask> {

    @Override
    public void write(JsonWriter writer, Subtask subtask) throws IOException {
        writer.beginObject();
        writer.name("task_type");
        writer.value(subtask.getType().name());
        writer.name("id");
        writer.value(subtask.getId());
        writer.name("epicsId");
        writer.value(subtask.getEpicsId());
        writer.name("name");
        writer.value(subtask.getName());
        writer.name("description");
        writer.value(subtask.getDescription());
        writer.name("status");
        writer.value(subtask.getStatus().toString());
        writer.name("start");
        writer.value(subtask.getStartDateTime().toString());
        writer.name("execution");
        writer.value(subtask.getTimeExecution().toString());
        writer.endObject();
    }

    @Override
    public Subtask read(JsonReader reader) throws IOException {
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
        Subtask newSubtask = constructSubtask(reader, fieldName);
        reader.endObject();
        return newSubtask;
    }

    protected static Subtask constructSubtask(JsonReader reader, String fieldNameIfAlreadyDefine) throws IOException {
        Subtask.SubtaskBuilder subtaskBuilder = new Subtask.SubtaskBuilder();
        String fieldName = fieldNameIfAlreadyDefine;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if ("id".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.ID(reader.nextLong());
            }
            if("epicsId".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.EpicsID(reader.nextLong());
            }
            if ("name".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.Name(reader.nextString());
            }
            if ("description".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.Description(reader.nextString());
            }
            if ("status".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.Status(TaskStatus.valueOf(reader.nextString()));
            }
            if ("start".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.StartDateTime(LocalDateTime.parse(reader.nextString()));
            }
            if ("execution".equals(fieldName)) {
                reader.peek();
                subtaskBuilder.TimeExecution(Duration.parse(reader.nextString()));
            }
        }
        return subtaskBuilder.build();
    }
}
