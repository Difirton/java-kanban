package main.java.service;

import main.java.entity.Epic;
import main.java.entity.Subtask;
import main.java.error.ManagerSaveException;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements Serializable {
    private final long serialVersionUID = 1L;
    private final File file;

    FileBackedTasksManager() {
        this.file = new File("src" + File.separator + "main" + File.separator+ "resources" + File.separator
                + "data" + File.separator + "data.bin");
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
    public void removeSubtasksById(Long subtaskId) {
        super.removeSubtasksById(subtaskId);
        this.save();
    }

    @Override
    public Epic getEpicById(Long epicId) {
        Epic tempEpic = super.getEpicById(epicId);
        this.save();
        return tempEpic;
    }

    @Override
    public Subtask getSubtaskByIdOrNull(Long subtaskId) {
        Subtask tempSubtask = super.getSubtaskByIdOrNull(subtaskId);
        this.save();
        return tempSubtask;
    }

    @Override
    public Epic getEpicBySubtaskIdOrNull(Long subtaskId) {
        Epic tempEpic = super.getEpicBySubtaskIdOrNull(subtaskId);
        this.save();
        return tempEpic;
    }

    @Override
    public void createNewEpic(String name, String description) {
        super.createNewEpic(name, description);
        this.save();
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
        super.createNewSubtask(name, description, epicId);
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
    public void updateEpicName(Long epicId, String newName) {
        super.updateEpicName(epicId, newName);
        this.save();
    }

    @Override
    public void updateEpicDescription(Long epicId, String newDescription) {
        super.updateEpicDescription(epicId, newDescription);
        this.save();
    }

    @Override
    public void updateSubtaskName(Long subtaskId, String newName) {
        super.updateSubtaskName(subtaskId, newName);
        this.save();
    }

    @Override
    public void updateSubtaskDescription(Long subtaskId, String newDescription) {
        super.updateSubtaskDescription(subtaskId, newDescription);
        this.save();
    }

    private void save() {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file))) {
            writer.writeObject(this);//Наверное здесь тогда не reader, а writer. А в loadFromFile как раз reader
        } catch (IOException exception) {
            throw new ManagerSaveException("There was a problem saving the file, probably the directory specified for" +
                    " saving the data does not exist. Check if the directory exists: \"resources\\data\"" +
                    exception.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File loadFile) {
        FileBackedTasksManager fileBackedTasksManager = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(loadFile))) {
            fileBackedTasksManager = (FileBackedTasksManager) reader.readObject();
        } catch (FileNotFoundException | ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
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
