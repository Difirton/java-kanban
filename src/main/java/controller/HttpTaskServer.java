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
    private final TasksManager taskManager;
    private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .registerTypeAdapter(InMemoryHistoryManager.class, new GsonHistoryManagerAdapter())
                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
                .create();

    public HttpTaskServer() throws IOException {
        this.taskManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        readKVServerURL();
        server = HttpServer.create(new InetSocketAddress(hostname, PORT), 0);
        server.createContext("/tasks", new TasksHandler());
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
        server.start();
        System.out.println("Открой в браузере http://" + hostname + ":" + PORT + "/");
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

    private void sendResponseCreated(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(201, 0);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO подумать что делать с исключением
        }
    }

    private class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            switch (httpExchange.getRequestMethod()) {
                case ("GET"):
                    handleEpicGetRequestMethod(httpExchange);
                    break;
                case ("POST"):
                    handleEpicPostRequestMethod(httpExchange);
                    break;
                case ("DELETE"):
                    handleEpicDeleteRequestMethod(httpExchange);
                    break;
                case ("PUT"):
                    handleEpicPutRequestMethod(httpExchange);
                    break;
                default:
                    throw new RuntimeException(); //TODO подумать что делать с исключением
            }
        }
    }

    private void handleEpicGetRequestMethod(HttpExchange httpExchange) {
        String response;
        if (isRequestContainsParameters(httpExchange)) {
            long idEpicToFind = defineIdRequest(httpExchange);
            response = gson.toJson(taskManager.getEpicById(idEpicToFind));
        } else {
            response = gson.toJson(taskManager.getAllEpics());
        }
        sendResponseOkAndTasks(response, httpExchange);
    }

    private void handleEpicPostRequestMethod(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic newEpic = gson.fromJson(body, Epic.class);
        taskManager.createNewEpic(newEpic.getName(), newEpic.getDescription());
        sendResponseCreated(httpExchange);
    }

    private void handleEpicDeleteRequestMethod(HttpExchange httpExchange) {
        if (isRequestContainsParameters(httpExchange)) {
            long idEpicToRemove = defineIdRequest(httpExchange);
            taskManager.removeEpicById(idEpicToRemove);
            sendResponseOk(httpExchange);
        } else {
            taskManager.removeAllEpics();
            sendResponseOk(httpExchange);
        }
    }

    private void handleEpicPutRequestMethod(HttpExchange httpExchange) throws IOException {
        if (isRequestContainsParameters(httpExchange)) {
            long idEpicToPut = defineIdRequest(httpExchange);
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic newEpic = gson.fromJson(body, Epic.class);
            taskManager.updateTaskName(idEpicToPut, newEpic.getName());
            taskManager.updateTaskDescription(idEpicToPut, newEpic.getDescription());
            sendResponseOk(httpExchange);
        } else {
            throw new RuntimeException(); //TODO подумать что делать с исключением
        }
    }

    private long defineIdRequest(HttpExchange httpExchange) {
        long idTaskToFind;
        String[] queryParams = httpExchange.getRequestURI().getQuery().split("=");
        if (queryParams[0].equals("id") && !queryParams[1].isEmpty()) {
            idTaskToFind = Long.parseLong(queryParams[1]);
        } else{
            throw new RuntimeException(); //TODO подумать что делать с исключением
        }
        return idTaskToFind;
    }

    private boolean isRequestContainsParameters(HttpExchange httpExchange) {
        String requestURI = httpExchange.getRequestURI().toString();
        return requestURI.contains("?");
    }

    private class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            switch (httpExchange.getRequestMethod()) {
                case ("GET"):
                    handleSubtaskGetRequestMethod(httpExchange);
                    break;
                case ("POST"):
                    handleSubtaskPostRequestMethod(httpExchange);
                    break;
                case ("DELETE"):
                    handleSubtaskDeleteRequestMethod(httpExchange);
                    break;
                case ("PUT"):
                    handleSubtaskPutRequestMethod(httpExchange);
                    break;
                default:
                    throw new RuntimeException(); //TODO подумать что делать с исключением
            }
        }
    }

    private void handleSubtaskGetRequestMethod(HttpExchange httpExchange) {
        String response;
        if (isRequestContainsParameters(httpExchange)) {
            long idSubtaskToFind = defineIdRequest(httpExchange);
            response = gson.toJson(taskManager.getSubtaskById(idSubtaskToFind));
        } else {
            response = gson.toJson(taskManager.getAllSubtasks());
        }
        sendResponseOkAndTasks(response, httpExchange);
    }

    private void handleSubtaskPostRequestMethod(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask newSubtask = gson.fromJson(body, Subtask.class);
        taskManager.createNewSubtask(newSubtask.getName(), newSubtask.getDescription(), newSubtask.getEpicsId());
        sendResponseCreated(httpExchange);
    }

    private void handleSubtaskDeleteRequestMethod(HttpExchange httpExchange) {
        if (isRequestContainsParameters(httpExchange)) {
            long idSubtaskToRemove = defineIdRequest(httpExchange);
            taskManager.removeSubtaskById(idSubtaskToRemove);
            sendResponseOk(httpExchange);
        } else {
            taskManager.removeAllSubtasks();
            sendResponseOk(httpExchange);
        }
    }

    private void handleSubtaskPutRequestMethod(HttpExchange httpExchange) throws IOException {
        if (isRequestContainsParameters(httpExchange)) {
            long idSubtaskToPut = defineIdRequest(httpExchange);
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask newSubtask = gson.fromJson(body, Subtask.class);
            taskManager.updateTaskName(idSubtaskToPut, newSubtask.getName());
            taskManager.updateTaskDescription(idSubtaskToPut, newSubtask.getDescription());
            sendResponseOk(httpExchange);
        } else {
            throw new RuntimeException(); //TODO подумать что делать с исключением
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            String response = gson.toJson(taskManager.getHistory());
            sendResponseOkAndTasks(response, httpExchange);
        }
    }

    public void stop() {
        server.stop(1);
        System.out.println("Cервер на порту " + PORT +" остановлен");
    }
}
