package config.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import constant.TypeTasksManager;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import service.*;
import utill.TimeIntervalsList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class GsonHttpManagerAdapter extends TypeAdapter<HTTPTasksManager> {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task .class, new GsonTaskAdapter())
            .registerTypeAdapter(Subtask .class, new GsonSubtaskAdapter())
            .registerTypeAdapter(Epic .class, new GsonEpicAdapter())
            .registerTypeAdapter(HistoryManager .class, new GsonHistoryManagerAdapter())
            .registerTypeAdapter(TimeIntervalsList .class, new GsonTimeIntervalsListAdapter())
            .create();

    @Override
    public void write(JsonWriter writer, HTTPTasksManager httpTasksManager) throws IOException {
        writer.beginObject();
        try {
            Field amountTaskIdField = httpTasksManager.getClass().getDeclaredField("amountTaskId");
            amountTaskIdField.setAccessible(true);
            long amountTaskId =  amountTaskIdField.getLong(httpTasksManager);
            writer.name("amount_task_id");
            writer.value(gson.toJson(amountTaskId));

            Field allTasksField = httpTasksManager.getClass().getDeclaredField("allTasks");
            allTasksField.setAccessible(true);
            Map<Long, Task> allTasks =  (Map<Long, Task>) allTasksField.get(httpTasksManager);
            writer.name("all_tasks");
            writer.value(gson.toJson(allTasks));

            Field sortedSubtasksField = httpTasksManager.getClass().getDeclaredField("sortedSubtasks");
            sortedSubtasksField.setAccessible(true);
            Set<Subtask> sortedSubtasks =  (Set<Subtask>) sortedSubtasksField.get(httpTasksManager);
            writer.name("sorted_subtasks");
            writer.value(gson.toJson(sortedSubtasks));

            Field inMemoryHistoryManagerField = httpTasksManager.getClass().getDeclaredField("inMemoryHistoryManager");
            inMemoryHistoryManagerField.setAccessible(true);
            HistoryManager inMemoryHistoryManager =  (HistoryManager) inMemoryHistoryManagerField.get(httpTasksManager);
            writer.name("in_memory_history_manager");
            writer.value(gson.toJson(inMemoryHistoryManager));

            Field occupiedSlotsField = httpTasksManager.getClass().getDeclaredField("occupiedSlots");
            occupiedSlotsField.setAccessible(true);
            TimeIntervalsList occupiedSlots =  (TimeIntervalsList) occupiedSlotsField.get(httpTasksManager);
            writer.name("occupied_slots");
            writer.value(gson.toJson(occupiedSlots));
            writer.endObject();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HTTPTasksManager read(JsonReader reader) throws IOException {
        String fieldName = null;
        HTTPTasksManager httpTasksManager = (HTTPTasksManager) Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
        reader.beginObject();
        try {
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NAME)) {
                    fieldName = reader.nextName();
                }
                if ("file".equals(fieldName)) {
                    token = reader.peek();
                    reader.skipValue();
                }
                if ("amountTaskId".equals(fieldName)) {
                    token = reader.peek();
                    Field amountTaskIdField = InMemoryTaskManager.class.getDeclaredField("amountTaskId");
                    amountTaskIdField.setAccessible(true);
                    amountTaskIdField.set(httpTasksManager, reader.nextLong());
                    System.out.println("111");
                }
                if ("allTasks".equals(fieldName)) {
                    token = reader.peek();
                    Field allTasksField = InMemoryTaskManager.class.getDeclaredField("allTasks");
                    allTasksField.setAccessible(true);
                    allTasksField.set(httpTasksManager, (Map<Long, Task>) gson.fromJson(reader.nextString(), Map.class));
                }

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        reader.endObject();
        return httpTasksManager;
    }
}
