package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import entity.Epic;
import entity.Subtask;
import service.HistoryManager;
import service.Manager;

import java.io.IOException;

public class GsonHistoryManagerAdapter extends TypeAdapter<HistoryManager> {
    GsonSubtaskAdapter gsonSubtaskAdapter = new GsonSubtaskAdapter();
    GsonEpicAdapter gsonEpicAdapter = new GsonEpicAdapter();

    @Override
    public void write(JsonWriter writer, HistoryManager historyManager) throws IOException {
        writer.beginArray();
        historyManager.getHistory().forEach(o -> {
            try {
                switch (o.getClass().getSimpleName()) {
                    case ("Epic"):
                        gsonEpicAdapter.write(writer, (Epic) o);
                    case ("Subtask"):
                        gsonSubtaskAdapter.write(writer, (Subtask) o);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.endArray();
    }

    @Override
    public HistoryManager read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        HistoryManager historyManager = Manager.getDefaultHistory();
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
