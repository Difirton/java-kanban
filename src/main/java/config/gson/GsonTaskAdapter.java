package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
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
        System.out.println(task.getClass().getSimpleName());
        try {
            switch (task.getClass().getSimpleName()) {
                case ("Epic"):
                    gsonEpicAdapter.write(writer, (Epic) task);
                case ("Subtask"):
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
            token = reader.peek();
            switch (reader.nextString()) {
                case ("Epic"):
                    taskToParse = GsonEpicAdapter.constructEpic(reader);
                    break;
                case ("Subtask"):
                    taskToParse = GsonSubtaskAdapter.constructSubtask(reader);
            }
        }
        reader.endObject();
        return taskToParse;
    }
}
