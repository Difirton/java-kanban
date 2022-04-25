import services.TasksManagerService;

public class Main {

    public static void main(String[] args) {
        TasksManagerService tasksManagerService = new TasksManagerService();
        tasksManagerService.createNewEpic("Epic 1", "Desc 1");
        tasksManagerService.createNewSubtask("Subtas 1.1", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtas 1.2", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtas 1.3", "Desc sub 1", 1L);
        tasksManagerService.createNewEpic("Epic 2", "Desc 2");
        tasksManagerService.createNewSubtask("Subtas 2.1", "Desc sub 1", 2L);
        tasksManagerService.createNewSubtask("Subtas 2.2", "Desc sub 1", 2L);

        System.out.println(tasksManagerService.getAllEpics());
        System.out.println(tasksManagerService.getAllSubtasks());
        System.out.println();

        tasksManagerService.changeSubtaskStatusInProgress(4L);
        tasksManagerService.changeSubtaskStatusDone(5L);
        tasksManagerService.changeSubtaskStatusInProgress(1L);

        System.out.println(tasksManagerService.getAllEpics());
        System.out.println(tasksManagerService.getAllEpicsSubtasks(1L));
        System.out.println(tasksManagerService.getAllEpicsSubtasks(2L));
        System.out.println();

        tasksManagerService.changeSubtaskStatusDone(4L);
        System.out.println(tasksManagerService.getEpicById(2L));
        System.out.println();

        tasksManagerService.updateEpicDescription(2L, "NEW DESCRIPTION");
        tasksManagerService.updateEpicName(1L, "NEW NAME");
        tasksManagerService.updateSubtaskName(1L, "NEW NAME");
        tasksManagerService.updateSubtaskDescription(2L, "NEW DESCRIPTION");

        System.out.println(tasksManagerService.getEpicById(1L));
        System.out.println(tasksManagerService.getEpicById(2L));
        System.out.println(tasksManagerService.getSubtaskById(1L));
        System.out.println(tasksManagerService.getSubtaskById(2L));
        System.out.println();

        tasksManagerService.removeSubtasksByEpicId(2L);
        tasksManagerService.removeEpic(2L); //Удаление эпика влечет каскадное удаление подзадач

        System.out.println(tasksManagerService.getAllEpics());
        System.out.println(tasksManagerService.getAllSubtasks());
        System.out.println();

        tasksManagerService.removeAllSubtasks();
        System.out.println(tasksManagerService.getAllSubtasks());
        System.out.println(tasksManagerService.getAllEpics());
        tasksManagerService.removeAllEpics();
        System.out.println(tasksManagerService.getAllEpics());
    }
}
