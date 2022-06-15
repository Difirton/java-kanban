package service;

import entity.Epic;
import entity.Subtask;
import error.ManagerSaveException;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements Serializable {
    private final long serialVersionUID = 1L;
    private final File file;

    FileBackedTasksManager() {
        file = new File("resources" + File.separator + "data" + File.separator + "data.bin");
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

    /*Реализовал через сериализацию, так как ТЗ не обязывает использовать CSV. Я с ним уже несколько раз работал, было
     не интересно просто повторить. С бинарной сериализацией не сталкивался, но вычитал, что она быстрее работает. */
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTasksManager loadFromFile(File loadFile) {
        FileBackedTasksManager fileBackedTasksManager = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loadFile))) {
            fileBackedTasksManager = (FileBackedTasksManager) ois.readObject();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileBackedTasksManager{");
        sb.append("Epics=").append(this.getAllEpics());
        sb.append(", Subtasks=").append(this.getAllSubtasks());
        sb.append(", History=").append(this.getHistory());
        sb.append('}');
        return sb.toString();
    }
}
