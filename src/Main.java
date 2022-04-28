import service.TasksManagerService;

public class Main {

    public static void main(String[] args) {
        /*Добавил тестов в папке test с инспользование junit-4.13.2, это не входило в ТЗ, но захотелось попробовать.
        Удалять не стал, так как в задании сказано, что мы будем доделывать программу, могут еще пригодиться.
        Ниже только обязатеьные тесты по ТЗ.*/

        TasksManagerService tasksManagerService = new TasksManagerService();
        tasksManagerService.createNewEpic("Epic 1", "Desc 1");
        tasksManagerService.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        tasksManagerService.createNewEpic("Epic 2", "Desc 2");
        tasksManagerService.createNewSubtask("Subtask 2.1", "Desc sub 2", 2L);
        tasksManagerService.createNewSubtask("Subtask 2.2", "Desc sub 2", 2L);

        System.out.println(tasksManagerService.getEpicBySubtaskIdOrNull(4L));
        tasksManagerService.changeSubtaskStatusInProgress(5L);
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(5L));
        tasksManagerService.changeSubtaskStatusDone(5L);
        System.out.println(tasksManagerService.getAllSubtasks());
        tasksManagerService.changeSubtaskStatusDone(4L);
        System.out.println();
        System.out.println(tasksManagerService.getEpicBySubtaskIdOrNull(4L));
        tasksManagerService.createNewSubtask("Subtask 2.3", "Desc sub 2", 2L);
        System.out.println(tasksManagerService.getEpicBySubtaskIdOrNull(4L));
        tasksManagerService.changeSubtaskStatusDone(6L);
        System.out.println(tasksManagerService.getEpicBySubtaskIdOrNull(4L));

        System.out.println(tasksManagerService.getEpicById(1L).hashCode());
        System.out.println(tasksManagerService.getEpicById(2L).hashCode());
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(1L).hashCode());
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(2L).hashCode());
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(3L).hashCode());
        System.out.println(tasksManagerService.getEpicById(1L).equals(tasksManagerService.getEpicById(1L)));
        System.out.println(tasksManagerService.getEpicById(1L).equals(tasksManagerService.getEpicById(2L)));
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(1L)
                .equals(tasksManagerService.getSubtaskByIdOrNull(1L)));
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(2L)
                .equals(tasksManagerService.getSubtaskByIdOrNull(3L)));
        System.out.println();

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
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(1L));
        System.out.println(tasksManagerService.getSubtaskByIdOrNull(2L));
        System.out.println();

        tasksManagerService.removeSubtasksByEpicId(2L);
        tasksManagerService.removeEpic(2L); //Удаление эпика влечет каскадное удаление подзадач
        tasksManagerService.removeEpic(2L);

        System.out.println(tasksManagerService.getAllEpics());
        System.out.println(tasksManagerService.getAllSubtasks());
        System.out.println();

        tasksManagerService.removeAllSubtasks();
        System.out.println(tasksManagerService.getAllSubtasks());
        System.out.println(tasksManagerService.getAllEpics());
        tasksManagerService.removeAllEpics();
        System.out.println(tasksManagerService.getAllEpics());
        System.out.println();

        tasksManagerService.removeEpic(1L);
        tasksManagerService.changeSubtaskStatusInProgress(1L);
        tasksManagerService.updateEpicName(1L, "Abra-kadabra");
        tasksManagerService.updateEpicDescription(1L, "Again Abra-kadabra");
        tasksManagerService.updateSubtaskName(1L, "Not Abra-kadabra");
        tasksManagerService.updateEpicDescription(1L, "Again not Abra-kadabra");
        tasksManagerService.getEpicById(1L);
        tasksManagerService.removeSubtasksByEpicId(1L);
        tasksManagerService.getSubtaskByIdOrNull(1L);
        tasksManagerService.getEpicBySubtaskIdOrNull(1L);
    }
}
