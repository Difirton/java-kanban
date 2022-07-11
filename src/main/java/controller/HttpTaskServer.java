package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import config.gson.GsonEpicAdapter;
import config.gson.GsonSubtaskAdapter;
import constant.TypeTasksManager;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import service.Manager;
import service.TasksManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private TasksManager taskManager;
    private Gson gson;

    public HttpTaskServer() throws IOException {
        this.taskManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        taskManager.createNewEpic("Epic 1", "Desc 1");
        taskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
        taskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        taskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        taskManager.createNewEpic("Epic 2", "Desc 2");
        taskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        taskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);
        taskManager.getEpicById(1L);
        taskManager.getSubtaskById(2L);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/tasks/task", new SingleTasksHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    private class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            List<Task> priority = taskManager.getPrioritizedTasks();
            String response = gson.toJson(taskManager.getAllSubtasks());
            sendResponseOkAndTasks(response, httpExchange);
        }
    }

    private void sendResponseOkAndTasks(String response, HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO подумать что делать с исключением
        }
    }

    private void sendResponseOk(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO подумать что делать с исключением
        }
    }

    private class SingleTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            String[] queryParams = httpExchange.getRequestURI().getQuery().split("=");
            if (queryParams[0].equals("id") && !queryParams[1].isEmpty()) {
                Long idTaskToFind = Long.parseLong(queryParams[1]);
                String response = gson.toJson(taskManager.getTaskById(idTaskToFind));
                sendResponseOkAndTasks(response, httpExchange);
            }
        }
    }

    private class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            switch (httpExchange.getRequestMethod()) {
                case ("GET"):
                    String response = gson.toJson(taskManager.getAllEpics());
                    sendResponseOkAndTasks(response, httpExchange);
                    break;
                case ("POST"):
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic newEpic = gson.fromJson(body, Epic.class); //TODO поработать над добавлением в менеджер
                    sendResponseOk(httpExchange);
                    break;
                case ("DELETE"):
                    String[] queryParams = httpExchange.getRequestURI().getQuery().split("=");
                    if (queryParams[0].equals("id") && !queryParams[1].isEmpty()) {
                        Long idEpicToRemove = Long.parseLong(queryParams[1]);
                        taskManager.removeEpicById(idEpicToRemove);
                        sendResponseOk(httpExchange);
                    }
                    break;
            }

        }
    }

    private class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            switch (httpExchange.getRequestMethod()) {
                case ("GET"):
                    String response = gson.toJson(taskManager.getAllSubtasks());
                    sendResponseOkAndTasks(response, httpExchange);
                    break;
                case ("POST"):
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask newSubtask = gson.fromJson(body, Subtask.class);
                    sendResponseOk(httpExchange);
                    break;
                case ("DELETE"):
                    String[] queryParams = httpExchange.getRequestURI().getQuery().split("=");
                    if (queryParams[0].equals("id") && !queryParams[1].isEmpty()) {
                        Long idSubtaskToRemove = Long.parseLong(queryParams[1]);
                        taskManager.removeSubtaskById(idSubtaskToRemove);
                        sendResponseOk(httpExchange);
                    }
                    break;
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = gson.toJson(taskManager.getHistory());
            sendResponseOkAndTasks(response, httpExchange);
        }
    }
}
