import constant.TypeTasksManager;
import entity.Task;
import service.FileBackedTasksManager;
import service.Manager;
import service.TasksManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String... args) {
        /*Спасибо за совет прочитать книгу Чистый код, очень понравилась, за первые 2 дня пол книжки прочитал. Сейчас
        купил чистую архитектуру, но пока она до меня доехала, взялся читать другую.
        По поводу задания: Фактически entity все переписаны на 50%, InMemoryTaskManager целиком. В FileBackedTasksManager
        теперь путь для сохранения задается в properties, тесты запускаются от одной кнопки. Добавил еще свое исключение,
        т.к. показалось намного проще получать исключения и исправлять логику программы в проблемном месте.
         */

        TasksManager tasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L, "2005-12-10 12:20", 40 );
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L, "2005-10-10 12:20", 60 );
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L, "2005-08-16 12:20", 650);
        tasksManager.createNewSubtask("Subtask 6", "Subtask 6", 2L, "2005-08-16 12:30", 660);
        tasksManager.createNewEpic("Epic 7", "Description Epic 7");
        tasksManager.createNewSubtask("Subtask 8", "Subtask 8", 7L, "2010-12-10 19:20", 900);
        tasksManager.createNewSubtask("Subtask 9", "Subtask 9", 2L, "2008-12-10 22:20", 6000);
        tasksManager.getSubtaskById(3L);
        tasksManager.getEpicById(2L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(2L);
        tasksManager.getSubtaskById(5L);


        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("src\\main\\resources\\data" + File.separator + "dataFileBackedTasksManager.bin"));

        List<Task> tasks = fileBackedTasksManager.getPrioritizedTasks();
        tasks.sort(Task::compareTo);
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
