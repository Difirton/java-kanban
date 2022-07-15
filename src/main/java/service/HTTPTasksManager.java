package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.gson.*;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.ManagerSaveException;
import utill.TimeIntervalsList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class HTTPTasksManager extends FileBackedTasksManager {
    private transient final KVTaskClient kvTaskClient;
    private URI serverURI;
    private static Gson gson;
    private static String key;

    protected HTTPTasksManager() {
        serverURI  = readKVServerURL();
        this.kvTaskClient = new KVTaskClient(serverURI);
        gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .registerTypeAdapter(File.class, new GsonFileAdapter())
                .registerTypeAdapter(HistoryManager.class, new GsonHistoryManagerAdapter())
                .registerTypeAdapter(Task.class, new GsonTaskAdapter())
                .registerTypeAdapter(TimeIntervalsList.class, new GsonTimeIntervalsListAdapter())
                .create();
    }

    private static URI readKVServerURL() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            String address = properties.getProperty("KVServer.address");
            String port = properties.getProperty("KVServer.port");
            key = properties.getProperty("KVServer.key");
            return URI.create(address + port);
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data on the address and port, the address is located KVServer. " +
                    "Check that there is a config.properties file at the root of the project with the keys " +
                    "KVServer.address and KVServer.port" + exception.getMessage());
        }
    }

    @Override
    protected void save() {
        kvTaskClient.put(key, gson.toJson(this));
    }

    public static TasksManager loadFromServer(String key) {
        URI serverURI = HTTPTasksManager.readKVServerURL();
        KVTaskClient kvTaskClient = new KVTaskClient(serverURI);
        String json = kvTaskClient.load(key);
        return gson.fromJson(json, HTTPTasksManager.class);
    }
}
