package service;

import constant.TypeTasksManager;

public class HTTPTasksManagerTest extends TasksManagerTest{


    @Override
    TasksManager createTaskManager() {
        return Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
    }
}
