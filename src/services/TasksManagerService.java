package services;

import entitys.Epic;
import entitys.Subtask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        allEpics.remove(epicId);
    }

    public Epic getEpicById(Long epicId) {
        return allEpics.get(epicId);
    }

    public Epic getEpicBySubtaskIdOrNull(Long subtaskId) {
        for (long epic:allEpics.keySet()) {
            if (allEpics.get(epic).getAllSubtask().contains(subtaskId)) {
                return allEpics.get(epic);
            }
        }
        return null;
    }

    public Subtask getSubtaskByIdOrNull(Long subtaskId) {
        for (long epic:allEpics.keySet()) {
            if (allEpics.get(epic).getAllSubtask().contains(subtaskId)) {
                return allEpics.get(epic).getSubtaskById(subtaskId);
            }
        }
        return null;
    }

    public void createNewEpic(String name, String description) {
        allEpics.put(Epic.getNewId(), new Epic(name, description));
    }

    public void createNewSubtask(String name, String description, long epicsId) {
        allEpics.get(epicsId).addSubtasks(name, description);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList(List.of(allEpics.values()));
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        allEpics.values().stream()
                .map(o -> o.getAllSubtask())
                .forEach(o ->allSubtasks.addAll(o));
        return allSubtasks;
    }

    public ArrayList<Subtask> getAllEpicsSubtasks(Long epicId) {
        return allEpics.get(epicId).getAllSubtask();
    }

    public void changeSubtaskStatusDone(Long subtaskId) {
        getEpicBySubtaskIdOrNull(subtaskId).changeStatusSubtask(subtaskId);
    }

    public void updateEpicName(Long epicId, String newName) {
        allEpics.get(epicId).setName(newName);
    }

    public void updateEpicDescription(Long epicId, String newDescription) {
        allEpics.get(epicId).setDescription(newDescription);
    }

    public void updateSubtaskName(Long subtaskId, String newName) {
        getEpicBySubtaskIdOrNull(subtaskId).setName(newName);
    }

    public void updateSubtaskDescription(Long subtaskId, String newDescription) {
        getEpicBySubtaskIdOrNull(subtaskId).setName(newDescription);
    }
}
