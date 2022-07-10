package utill.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import entity.Epic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsonEpicAdapter extends TypeAdapter<Epic> {

    @Override
    public Epic read(JsonReader reader) throws IOException {
        reader.beginObject();
        String fieldName = null;
        Epic.EpicBuilder epicBuilder = null;
        List<Long> allIdSubtasksInEpic = new ArrayList<>(); //TODO сделать парсинг листа
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if ("id".equals(fieldName)) {
                token = reader.peek();
                epicBuilder = new Epic.EpicBuilder(reader.nextLong());
            }
            if("allIdSubtasksInEpic".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.AllIdSubtasksInEpic(allIdSubtasksInEpic);
            }
            if ("name".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.Name(reader.nextString());
            }
            if ("description".equals(fieldName)) {
                token = reader.peek();
                epicBuilder.Description(reader.nextString());
            }
//            if ("status".equals(fieldName)) {
//                token = reader.peek();
//                epicBuilder.Status(TaskStatus.NEW); //TODO добавить парсинг enum
//            }
//            if ("start".equals(fieldName)) {
//                token = reader.peek();
//                subtaskBuilder.StartDateTime(reader.nextString());
//            }
//            if ("execution".equals(fieldName)) {
//                token = reader.peek();
//                subtaskBuilder.TimeExecutionInMinutes(reader.nextInt());
//            }
        }
        reader.endObject();
        return epicBuilder.build();
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