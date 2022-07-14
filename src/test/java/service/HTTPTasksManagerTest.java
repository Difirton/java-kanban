package service;

import constant.TypeTasksManager;
import controller.KVServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class HTTPTasksManagerTest extends TasksManagerTest{

    @Override
    TasksManager createTaskManager() {
        return Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
    }

    @BeforeAll
    public static void startServer() throws IOException {
        new KVServer().start();
    }

    @AfterAll
    public static void stopServer() throws IOException {
        new KVServer().stop();
    }
}
