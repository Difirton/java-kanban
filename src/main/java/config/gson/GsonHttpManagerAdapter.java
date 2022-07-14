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
import java.util.*;

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
            Field amountTaskIdField = InMemoryTaskManager.class.getDeclaredField("amountTaskId");
            amountTaskIdField.setAccessible(true);
            long amountTaskId =  amountTaskIdField.getLong(httpTasksManager);
            writer.name("amount_task_id");
            writer.value(gson.toJson(amountTaskId));

            Field allTasksField = InMemoryTaskManager.class.getDeclaredField("allTasks");
            allTasksField.setAccessible(true);
            Map<Long, Task> allTasks =  (Map<Long, Task>) allTasksField.get(httpTasksManager);
            writer.name("all_tasks");
            writer.jsonValue(gson.toJson(allTasks));

            Field sortedSubtasksField = InMemoryTaskManager.class.getDeclaredField("sortedSubtasks");
            sortedSubtasksField.setAccessible(true);
            Set<Subtask> sortedSubtasks =  (Set<Subtask>) sortedSubtasksField.get(httpTasksManager);
            writer.name("sorted_subtasks");
            writer.jsonValue(gson.toJson(sortedSubtasks));

            Field inMemoryHistoryManagerField = InMemoryTaskManager.class.getDeclaredField("inMemoryHistoryManager");
            inMemoryHistoryManagerField.setAccessible(true);
            HistoryManager inMemoryHistoryManager =  (HistoryManager) inMemoryHistoryManagerField.get(httpTasksManager);
            writer.name("in_memory_history_manager");
            writer.jsonValue(gson.toJson(inMemoryHistoryManager));

            Field occupiedSlotsField = InMemoryTaskManager.class.getDeclaredField("occupiedSlots");
            occupiedSlotsField.setAccessible(true);
            TimeIntervalsList occupiedSlots =  (TimeIntervalsList) occupiedSlotsField.get(httpTasksManager);
            writer.name("occupied_slots");
            writer.jsonValue(gson.toJson(occupiedSlots));
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

                if ("amount_task_id".equals(fieldName)) {
                    token = reader.peek();
                    Field amountTaskIdField = InMemoryTaskManager.class.getDeclaredField("amountTaskId");
                    amountTaskIdField.setAccessible(true);
                    amountTaskIdField.set(httpTasksManager, reader.nextLong());
                }

                if ("all_tasks".equals(fieldName)) {
                    token = reader.peek();
                    Map<Long, Task> allTasks = new HashMap<>();
                    reader.beginObject();
                    while (reader.hasNext()) {
                        Long key = Long.parseLong(reader.nextName());
                        reader.beginObject();
                        token = reader.peek();
                        if (token.equals(JsonToken.NAME)) {
                            fieldName = reader.nextName();
                        }
                        if ("task_type".equals(fieldName)) {
                            switch (reader.nextString()) {
                                case ("Epic"):
                                    allTasks.put(key, GsonEpicAdapter.constructEpic(reader));
                                    break;
                                case ("Subtask"):
                                    allTasks.put(key, GsonSubtaskAdapter.constructSubtask(reader));
                            }
                            reader.endObject();
                        }
                        token = reader.peek();
                    }
                    reader.endObject();
                    Field allTasksField = InMemoryTaskManager.class.getDeclaredField("allTasks");
                    allTasksField.setAccessible(true);
                    allTasksField.set(httpTasksManager, allTasks);
                }

                if ("sorted_subtasks".equals(fieldName)) {
                    token = reader.peek();
                    Set<Subtask> sortedSubtasks = new TreeSet<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        token = reader.peek();
                        if (token.equals(JsonToken.NAME)) {
                            fieldName = reader.nextName();
                        }
                        if ("task_type".equals(fieldName)) {
                            reader.skipValue();
                            Subtask subtask = GsonSubtaskAdapter.constructSubtask(reader);
                            sortedSubtasks.add(subtask);
                            reader.endObject();
                        }
                        token = reader.peek();
                    }
                    reader.endArray();
                    Field amountTaskIdField = InMemoryTaskManager.class.getDeclaredField("sortedSubtasks");
                    amountTaskIdField.setAccessible(true);
                    amountTaskIdField.set(httpTasksManager, sortedSubtasks);
                }


                if ("in_memory_history_manager".equals(fieldName)) {

                }


            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return httpTasksManager;
    }
}
