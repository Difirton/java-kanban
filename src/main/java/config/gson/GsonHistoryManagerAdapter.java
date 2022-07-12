package config.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.Epic;
import entity.Subtask;
import service.InMemoryHistoryManager;
import utill.CustomLinkedList;

import java.io.IOException;

public class GsonHistoryManagerAdapter extends TypeAdapter<InMemoryHistoryManager> {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
            .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
            .create();;

    @Override
    public InMemoryHistoryManager read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        CustomLinkedList customLinkedList = new CustomLinkedList();
        System.out.println(reader.peek());
        while (reader.hasNext()) {
            switch (reader.peek()) {
                case BEGIN_OBJECT:
                    reader.beginObject();
                    System.out.println(reader.peek());
                    fieldName = reader.nextName();
                    System.out.println(fieldName);
                    System.out.println(reader.nextString());

            }
//        }

        reader.endArray();
        return new InMemoryHistoryManager();
    }

    @Override
    public void write(JsonWriter writer, InMemoryHistoryManager historyManager) throws IOException {
        writer.beginObject();
        writer.name("CustomLinkedList");
        writer.value(historyManager.getHistory().toString());
        writer.endObject();
    }
}
