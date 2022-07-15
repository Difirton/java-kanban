package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.gson.*;
import constant.TypeTasksManager;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Manager;
import service.TasksManager;
import utill.TimeIntervalsList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TasksManager fileBackedTasksManager;
    private HttpClient httpTaskServerClient;
    private HttpResponse.BodyHandler<String> handler;
    private String pathToTestSaveAndLoadData;
    private URI serverURI;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        httpTaskServer = new HttpTaskServer().start();
        httpTaskServerClient = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();
        readProperties();
        setUpTaskManagerInHttpTaskServer();
        gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .registerTypeAdapter(File.class, new GsonFileAdapter())
                .registerTypeAdapter(HistoryManager.class, new GsonHistoryManagerAdapter())
                .registerTypeAdapter(Task.class, new GsonTaskAdapter())
                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
                .create();
    }

    private void readProperties() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            String address = properties.getProperty("HttpTaskServer.address");
            String port = properties.getProperty("HttpTaskServer.port");
            serverURI = URI.create(address + port);
            pathToTestSaveAndLoadData = properties.getProperty("test.directory.HttpTaskServer");
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data on the address and port, the address is located " +
                    "HttpTaskServer. Check that there is a config.properties file at the root of the project with the " +
                    "keys HttpTaskServer.address and HttpTaskServer.port" + exception.getMessage());
        }
    }

    private void setUpTaskManagerInHttpTaskServer() throws NoSuchFieldException, IllegalAccessException {
        fileBackedTasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        File testFile = new File(pathToTestSaveAndLoadData + File.separator + "dataHttpTaskServer.bin");
        Field fileField = fileBackedTasksManager.getClass().getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(fileBackedTasksManager, testFile);
        fileBackedTasksManager.createNewEpic("Epic 1", "Desc 1");
        fileBackedTasksManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
        fileBackedTasksManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        fileBackedTasksManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        fileBackedTasksManager.createNewEpic("Epic 2", "Desc 2");
        fileBackedTasksManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        fileBackedTasksManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);
        Field taskManagerField = httpTaskServer.getClass().getDeclaredField("taskManager");
        taskManagerField.setAccessible(true);
        taskManagerField.set(httpTaskServer, fileBackedTasksManager);
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("Test GET /tasks, expected ok")
    public void testGetPrioritizedTasks() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURI + "/tasks"))
                .timeout(Duration.ofSeconds(20L))
                .GET()
                .build();
        HttpResponse<String> response = httpTaskServerClient.send(getRequest, handler);
        String expected = gson.toJson(fileBackedTasksManager.getPrioritizedTasks());
        String actual = response.body();
        assertTrue(actual.equals(expected));
    }

    @Test
    @DisplayName("Test find all epics: GET /tasks/epic, expected ok")
    public void testGetAllEpics() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURI + "/tasks/epic"))
                .timeout(Duration.ofSeconds(20L))
                .GET()
                .build();
        HttpResponse<String> response = httpTaskServerClient.send(getRequest, handler);
        String expected = gson.toJson(fileBackedTasksManager.getAllEpics());
        String actual = response.body();
        assertTrue(actual.equals(expected));
    }

    @Test
    @DisplayName("Test epic by id: GET /tasks/epic?id=5, expected ok")
    public void testGetEpicById() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURI + "/tasks/epic?id=5"))
                .timeout(Duration.ofSeconds(20L))
                .GET()
                .build();
        HttpResponse<String> response = httpTaskServerClient.send(getRequest, handler);
        String expected = gson.toJson(fileBackedTasksManager.getTaskById(5L));
        String actual = response.body();
        assertTrue(actual.equals(expected));
    }

    @Test
    @DisplayName("Test create new epic (short json): POST /tasks/epic, expected ok")
    public void testPostNewEpicShortJson() throws URISyntaxException, IOException, InterruptedException {
        String epicJson = "{\"name\":\"Test\",\"description\":\"Test\"}";
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURI + "/tasks/epic"))
                .timeout(Duration.ofSeconds(5L))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = httpTaskServerClient.send(getRequest, handler);
        int expectedStatusCode = 200;
        int actualStatusCode = response.statusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
        String expectedEpicName = "Test";
        String actualEpicName = fileBackedTasksManager.getEpicById(8L).getName();
        String expectedEpicDescription = "Test";
        String actualEpicDescription = fileBackedTasksManager.getEpicById(8L).getDescription();
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedEpicName, actualEpicName);
        assertEquals(expectedEpicDescription, actualEpicDescription);
    }

    @Test
    @DisplayName("Test create new epic (full json): POST /tasks/epic, expected ok")
    public void testPostNewEpicFullJson() throws URISyntaxException, IOException, InterruptedException {
        String epicJson = "{\n" +
                "\t\t\"task_type\": \"Epic\",\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"all_id_subtasks_in_epic\": \"[2, 3, 4]\",\n" +
                "\t\t\"name\": \"Test\",\n" +
                "\t\t\"description\": \"Test\",\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"start\": \"2020-01-01T00:00\",\n" +
                "\t\t\"execution\": \"PT2H40M\"\n" +
                "\t}";
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURI + "/tasks/epic"))
                .timeout(Duration.ofSeconds(5L))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = httpTaskServerClient.send(getRequest, handler);
        int expectedStatusCode = 200;
        int actualStatusCode = response.statusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
        String expectedEpicName = "Test";
        String actualEpicName = fileBackedTasksManager.getEpicById(8L).getName();
        String expectedEpicDescription = "Test";
        String actualEpicDescription = fileBackedTasksManager.getEpicById(8L).getDescription();
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedEpicName, actualEpicName);
        assertEquals(expectedEpicDescription, actualEpicDescription);
    }
}
