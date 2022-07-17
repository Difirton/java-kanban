package service;

import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.ManagerSaveException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

public class FileBackedTasksManager extends InMemoryTaskManager implements Serializable {
    private final File file;

    protected FileBackedTasksManager() {
        String pathToSaveAndLoadData = readPathToSave();
        this.file = new File(pathToSaveAndLoadData + File.separator + "dataFileBackedTasksManager.bin");
    }

    private String readPathToSave() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            return properties.getProperty("directory.toSaveFileBackedTasksManager");
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data for the path where the object will be serialized. " +
                    "Check that there is a config.properties file at the root of the project with the key " +
                    "directory.toSaveFileBackedTasksManager " + exception.getMessage());
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        this.save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        this.save();
    }

    @Override
    public void removeSubtasksByEpicId(Long epicId) {
        super.removeSubtasksByEpicId(epicId);
        this.save();
    }

    @Override
    public void removeEpicById(Long epicId) {
        super.removeEpicById(epicId);
        this.save();
    }

    @Override
    public void removeSubtaskById(Long subtaskId) {
        super.removeSubtaskById(subtaskId);
        this.save();
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task tempTask = super.getTaskById(taskId);
        this.save();
        return tempTask;
    }

    @Override
    public Epic getEpicById(Long taskId) {
        Epic tempEpic = super.getEpicById(taskId);
        this.save();
        return tempEpic;
    }

    @Override
    public Subtask getSubtaskById(Long taskId) {
        Subtask tempSubtask = super.getSubtaskById(taskId);
        this.save();
        return tempSubtask;
    }

    @Override
    public void createNewEpic(String name, String description) {
        super.createNewEpic(name, description);
        this.save();
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId) {
        super.createNewSubtask(name, description, epicId);
        this.save();
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 LocalDateTime startDateTime) {
        super.createNewSubtask(name, description, epicId, startDateTime);
        this.save();
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime) {
        super.createNewSubtask(name, description, epicId, startDateTime);
        this.save();
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 LocalDateTime startDateTime,
                                 Duration timeExecutionInMinutes) {
        super.createNewSubtask(name, description, epicId, startDateTime, timeExecutionInMinutes);
        this.save();
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime,
                                 int timeExecutionInMinutes) {
        super.createNewSubtask(name, description, epicId, startDateTime, timeExecutionInMinutes);
        this.save();
    }

    @Override
    public void changeSubtaskStatusDone(Long subtaskId) {
        super.changeSubtaskStatusDone(subtaskId);
        this.save();
    }

    @Override
    public void changeSubtaskStatusInProgress(Long subtaskId) {
        super.changeSubtaskStatusInProgress(subtaskId);
        this.save();
    }

    @Override
    public void updateTaskName(Long taskId, String newName) {
        super.updateTaskName(taskId, newName);
        this.save();
    }

    @Override
    public void updateTaskDescription(Long taskId, String newDescription) {
        super.updateTaskDescription(taskId, newDescription);
        this.save();
    }

    protected void save() {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file))) {
            writer.writeObject(this);
        } catch (IOException exception) {
            throw new ManagerSaveException("There was a problem saving the file, probably the directory specified for" +
                    " saving the data does not exist. Check if the directory exists: \"resources\\data\" " +
                    exception.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File loadFile) {
        FileBackedTasksManager fileBackedTasksManager;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(loadFile))) {
            fileBackedTasksManager = (FileBackedTasksManager) reader.readObject();
        } catch (ClassNotFoundException | IOException exception) {
            throw new RuntimeException("The link to the file is incorrect");
        }
        return fileBackedTasksManager;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileBackedTasksManager{")
        .append("Epics=").append(this.getAllEpics())
        .append(", Subtasks=").append(this.getAllSubtasks())
        .append(", History=").append(this.getHistory())
        .append('}');
        return sb.toString();
    }
}
