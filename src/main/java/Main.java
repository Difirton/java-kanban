import constant.TypeTasksManager;
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
        TasksManager tasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L);
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L);
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L);
        tasksManager.createNewSubtask("Subtask 6", "Subtask 6", 2L);
        tasksManager.createNewEpic("Epic 7", "Description Epic 7");
        tasksManager.createNewSubtask("Subtask 8", "Subtask 8", 7L);
        tasksManager.createNewSubtask("Subtask 9", "Subtask 9", 2L);
        tasksManager.getSubtaskById(3L);
        tasksManager.getEpicById(2L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(2L);
        tasksManager.getSubtaskById(5L);
        tasksManager.getSubtaskById(6L);
        System.out.println(tasksManager.getHistory());
        System.out.println(tasksManager.getAllSubtasks());
        System.out.println(tasksManager.getAllEpics());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("src\\main\\resources\\data" + File.separator + "dataFileBackedTasksManager.bin"));
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println(fileBackedTasksManager.getAllSubtasks());
        System.out.println(fileBackedTasksManager.getAllEpics());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        localDateTimeList.add(LocalDateTime.parse("2005-12-10 12:20", formatter));
        localDateTimeList.add(LocalDateTime.parse("2001-12-10 12:20", formatter));
        localDateTimeList.add(LocalDateTime.parse("2002-12-10 12:20", formatter));
        localDateTimeList.add(LocalDateTime.parse("2003-12-10 12:20", formatter));
        System.out.println(localDateTimeList.stream().min(LocalDateTime::compareTo).get());
        System.out.println(localDateTimeList);


    }
}
