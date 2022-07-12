import controller.HttpTaskServer;
import controller.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException { //TODO добавить обработчик
        new KVServer().start();
        new HttpTaskServer().start();
//        TasksManager taskManager = Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
//        taskManager.createNewEpic("Epic 1", "Desc 1");
//        taskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
//        taskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
//        taskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
//        taskManager.createNewEpic("Epic 2", "Desc 2");
//        taskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
//        taskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);

    }
}
