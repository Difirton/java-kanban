package services;

import entitys.Epic;

import java.util.HashMap;

public class TasksManagerService {
    HashMap<Long, Epic> allEpics;

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

    public Epic returnEpicById(Long epicId) {
        return allEpics.get(epicId);
    }

    public void createNewEpic(String name, String description) {
        allEpics.put(Epic.getNewId(), new Epic(name, description));
    }
}
