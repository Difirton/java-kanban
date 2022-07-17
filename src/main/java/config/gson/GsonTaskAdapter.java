package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import constant.TypeTask;
import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.io.IOException;

public class GsonTaskAdapter extends TypeAdapter<Task> {
    GsonSubtaskAdapter gsonSubtaskAdapter = new GsonSubtaskAdapter();
    GsonEpicAdapter gsonEpicAdapter = new GsonEpicAdapter();

    @Override
    public void write(JsonWriter writer, Task task) throws IOException {
        writer.beginObject();
        try {
            switch (task.getType()) {
                case EPIC:
                    gsonEpicAdapter.write(writer, (Epic) task);
                    break;
                case SUBTASK:
                    gsonSubtaskAdapter.write(writer, (Subtask) task);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.endObject();
    }

    @Override
    public Task read(JsonReader reader) throws IOException {
        reader.beginObject();
        String fieldName = null;
        Task taskToParse = null;
        JsonToken token = reader.peek();
        if (token.equals(JsonToken.NAME)) {
            fieldName = reader.nextName();
        }
        if ("task_type".equals(fieldName)) {
            reader.peek();
            String typeTask = reader.nextString();
            switch (TypeTask.valueOf(typeTask)) {
                case EPIC:
                    taskToParse = GsonEpicAdapter.constructEpic(reader, fieldName);
                    break;
                case SUBTASK:
                    taskToParse = GsonSubtaskAdapter.constructSubtask(reader, fieldName);
                    break;
                default:
                    throw new RuntimeException("No such type task " + typeTask);
            }
        }
        reader.endObject();
        return taskToParse;
    }
}
