package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import service.InMemoryHistoryManager;
import service.Manager;

import java.io.IOException;
import java.util.List;

public class GsonHistoryManagerAdapter extends TypeAdapter<InMemoryHistoryManager> {
    GsonSubtaskAdapter gsonSubtaskAdapter = new GsonSubtaskAdapter();
    GsonEpicAdapter gsonEpicAdapter = new GsonEpicAdapter();

    @Override
    public void write(JsonWriter writer, InMemoryHistoryManager historyManager) throws IOException {
        writer.beginArray();
        List<Task> history = historyManager.getHistory();
        for (Task task : history) {
            if (task.getClass().getSimpleName().equals("Epic")) {
                gsonEpicAdapter.write(writer, (Epic) task);
            }
            if (task.getClass().getSimpleName().equals("Subtask")) {
                gsonSubtaskAdapter.write(writer, (Subtask) task);
            }
        }
        writer.endArray();
    }

    @Override
    public InMemoryHistoryManager read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Manager.getDefaultHistory();
        while (reader.hasNext()) {
            reader.beginObject();
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if ("task_type".equals(fieldName)) {
                token = reader.peek();
                switch (reader.nextString()) {
                    case ("Epic"):
                        historyManager.add(GsonEpicAdapter.constructEpic(reader));
                        break;
                    case ("Subtask"):
                        historyManager.add(GsonSubtaskAdapter.constructSubtask(reader));
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return historyManager;
    }
}
