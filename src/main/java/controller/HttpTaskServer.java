package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import config.gson.GsonEpicAdapter;
import config.gson.GsonHistoryManagerAdapter;
import config.gson.GsonSubtaskAdapter;
import config.gson.GsonTimeIntervalsListAdapter;
import constant.TypeTasksManager;
import entity.Epic;
import entity.Subtask;
import error.ManagerSaveException;
import service.InMemoryHistoryManager;
import service.Manager;
import service.TasksManager;
import utill.TimeIntervalsList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class HttpTaskServer {
    private static String hostname;
    private static int PORT;
    private final HttpServer server;
    private TasksManager taskManager;
    private Gson gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .registerTypeAdapter(InMemoryHistoryManager.class, new GsonHistoryManagerAdapter())
                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
                .create();

    public HttpTaskServer() throws IOException {
        this.taskManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        readKVServerURL();
        taskManager.createNewEpic("Epic 1", "Desc 1");
        taskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
        taskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        taskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        taskManager.createNewEpic("Epic 2", "Desc 2");
        taskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        taskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);
        server = HttpServer.create(new InetSocketAddress(hostname, PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/tasks/task", new SingleTasksHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
    }

    private static void readKVServerURL() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            hostname = properties.getProperty("HttpTaskServer.hostname");
            String port = properties.getProperty("HttpTaskServer.port");
            PORT = Integer.parseInt(port);
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data on the address and port, the address is located " +
                    "HttpTaskServer. Check that there is a config.properties file at the root of the project with " +
                    "the keys HttpTaskServer.address and HttpTaskServer.port" + exception.getMessage());
        }
    }

    public HttpTaskServer start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("Открой в браузере " + + PORT + "/");
        server.start();
        return this;
    }

    private class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendResponseOkAndTasks(response, httpExchange);
        }
    }

    private void sendResponseOkAndTasks(String response, HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200, response.length());
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
                    Epic newEpic = gson.fromJson(body, Epic.class);
                    taskManager.createNewEpic(newEpic.getName(), newEpic.getDescription());
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
                    taskManager.createNewSubtask(newSubtask.getName(),
                                                 newSubtask.getDescription(),
                                                 newSubtask.getEpicsId(),
                                                 newSubtask.getStartDateTime(),
                                                 newSubtask.getTimeExecution());
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
        public void handle(HttpExchange httpExchange) {
            String response = gson.toJson(taskManager.getHistory());
            System.out.println(response);
            sendResponseOkAndTasks(response, httpExchange);
        }
    }

    public void stop() {
        server.stop(1);
        System.out.println("Cервер на порту " + PORT +" остановлен");
    }
}
