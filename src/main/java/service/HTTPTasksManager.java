package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.gson.GsonEpicAdapter;
import config.gson.GsonSubtaskAdapter;
import entity.Epic;
import entity.Subtask;
import error.ManagerSaveException;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class HTTPTasksManager extends FileBackedTasksManager{
    private final long serialVersionUID = 2L;
    private final KVTaskClient kvTaskClient;
    private final URI serverURI;
    private Gson gson;

    protected HTTPTasksManager() {
        serverURI  = readKVServerURL();
        this.kvTaskClient = new KVTaskClient(serverURI);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new GsonSubtaskAdapter())
                .registerTypeAdapter(Epic.class, new GsonEpicAdapter())
                .create();
    }

    private URI readKVServerURL() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            String address = properties.getProperty("KVServer.address");
            String port = properties.getProperty("KVServer.port");
            return URI.create(address + port);
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data on the address and port, the address is located KVServer. " +
                    "Check that there is a config.properties file at the root of the project with the keys " +
                    "KVServer.address and KVServer.port" + exception.getMessage());
        }
    }

    @Override
    protected void save() {
        System.out.println(gson.toJson(this)); //TODO убрать
        kvTaskClient.put(gson.toJson(this));
    }

    public void loadFromServer() {
        String json = kvTaskClient.load();
        System.out.println(json);
        gson.fromJson(json, HTTPTasksManager.class);
    }
}
