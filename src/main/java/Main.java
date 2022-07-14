import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.gson.*;
import constant.TypeTasksManager;
import controller.HttpTaskServer;
import controller.KVServer;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import service.HTTPTasksManager;
import service.HistoryManager;
import service.Manager;
import service.TasksManager;
import utill.TimeIntervalsList;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException { //TODO добавить обработчик
        new KVServer().start();
        new HttpTaskServer().start();
        TasksManager taskManager = Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
        taskManager.createNewEpic("Epic 1", "Desc 1");
        taskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
        taskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        taskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        taskManager.createNewEpic("Epic 2", "Desc 2");
        taskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        taskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new GsonTaskAdapter())
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .registerTypeAdapter(HistoryManager.class, new GsonHistoryManagerAdapter())
                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
                .registerTypeAdapter(TasksManager.class, new GsonHttpManagerAdapter())
                .create();

        String managerStr = taskManager.getKvTaskClient().load();
        System.out.println(managerStr);
        TasksManager newManager = gson.fromJson(managerStr, TasksManager.class);
        System.out.println(newManager.getTaskById(1L));
        System.out.println("111");





    }
}
