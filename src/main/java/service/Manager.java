package service;

import constant.TypeTasksManager;

public class Manager {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TasksManager getTaskManager(TypeTasksManager typeTasksManager) {
        TasksManager tasksManager = null;
        switch (typeTasksManager) {
            case IN_MEMORY_TASKS_MANAGER:
                tasksManager = new InMemoryTaskManager();
                break;
            case FILE_BACKED_TASKS_MANAGER:
                tasksManager = new FileBackedTasksManager();
                break;
            case HTTP_TASKS_MANAGER:
                tasksManager = new HTTPTasksManager();
                break;
            default:
                throw new RuntimeException("No such TaskManager " + typeTasksManager);
        }
        return tasksManager;
    }

//    public static HTTPTasksManager getDefault(String key) {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
//                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
//                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
//                .registerTypeAdapter(Task.class, new GsonTaskAdapter())
//                .registerTypeAdapter(HistoryManager.class, new GsonHistoryManagerAdapter())
//                .registerTypeAdapter(File.class, new GsonFileAdapter())
//                .create();
//
//    }
}
