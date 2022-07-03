package service;

import constant.TypeTasksManager;

public class InMemoryTasksManagerTest extends TasksManagerTest {

    @Override
    TasksManager createTaskManager() {
        return Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
    }
}
