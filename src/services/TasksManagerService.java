package services;

import entitys.Epic;
import entitys.Subtask;

import java.util.*;
import java.util.stream.Collectors;

public class TasksManagerService {
    private HashMap<Long, Epic> allEpics;

    public TasksManagerService() {
        this.allEpics = new HashMap<>();
    }

    public void removeAllEpics() {
        this.allEpics.clear();
    }

    public void removeAllSubtasks() {
        for (long epic:allEpics.keySet()) {
            allEpics.get(epic).removeSubtasks();
        }
    }

    public void  removeSubtasksByEpicId(Long epicId) {
        allEpics.get(epicId).removeSubtasks();
    }

    public void  removeEpic(Long epicId) {
        allEpics.get(epicId).removeSubtasks();
        allEpics.remove(epicId);
    }

    public Epic getEpicById(Long epicId) {
        return allEpics.get(epicId);
    }

    public Subtask getSubtaskById(Long subtaskId) {
        return this.getAllSubtasks().stream()
                .filter(o -> subtaskId.equals(o.getId()))
                .findFirst().get();
    }

    public Epic getEpicBySubtaskId(Long subtaskId) {
        return this.getAllSubtasks().stream()
                .filter(o -> subtaskId.equals(o.getId()))
                .map(o -> o.getEpicsId())
                .map(o -> this.getEpicById(o))
                .findFirst().get();
    }

    public void createNewEpic(String name, String description) {
        allEpics.put(Epic.getNewId(), new Epic(name, description));
    }

    public void createNewSubtask(String name, String description, long epicsId) {
        allEpics.get(epicsId).addSubtasks(name, description);
    }

    public ArrayList<Epic> getAllEpics() {
        return allEpics.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        this.getAllEpics().stream()
                .map(o -> o.getAllSubtask())
                .forEach(o -> allSubtasks.addAll(o));
        return allSubtasks;
    }

    public ArrayList<Subtask> getAllEpicsSubtasks(Long epicId) {
        return allEpics.get(epicId).getAllSubtask();
    }

    public void changeSubtaskStatusDone(Long subtaskId) {
        getEpicBySubtaskId(subtaskId).changeStatusSubtask(subtaskId);
    }

    public void updateEpicName(Long epicId, String newName) {
        allEpics.get(epicId).setName(newName);
    }

    public void updateEpicDescription(Long epicId, String newDescription) {
        allEpics.get(epicId).setDescription(newDescription);
    }

    public void updateSubtaskName(Long subtaskId, String newName) {
        getSubtaskById(subtaskId).setName(newName);
    }

    public void updateSubtaskDescription(Long subtaskId, String newDescription) {
        getSubtaskById(subtaskId).setDescription(newDescription);
    }
}
