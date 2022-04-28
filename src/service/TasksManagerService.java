package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
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
        try {
            allEpics.get(epicId).removeSubtasks();
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id="+ epicId + " не существует");
        }
    }

    public void  removeEpic(Long epicId) {
        try {
            allEpics.get(epicId).removeSubtasks();
            allEpics.remove(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id="+ epicId + " не существует");
        }
    }

    public void  removeSubtasksById(Long subtaskId) {
        try {
            Long epicId = this.getEpicBySubtaskIdOrNull(subtaskId).getId();
            this.getEpicById(epicId).removeSubtask(subtaskId);
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
        }
    }

    public Epic getEpicById(Long epicId) {
        return allEpics.get(epicId);
    }

    public Subtask getSubtaskByIdOrNull(Long subtaskId) {
        try {
            return this.getAllSubtasks().stream()
                    .filter(o -> subtaskId.equals(o.getId()))
                    .findFirst().get();
        } catch (NoSuchElementException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
            return null;
        }
    }

    public Epic getEpicBySubtaskIdOrNull(Long subtaskId) {
        try {
            return this.getAllSubtasks().stream()
                    .filter(o -> subtaskId.equals(o.getId()))
                    .map(o -> o.getEpicsId())
                    .map(o -> this.getEpicById(o))
                    .findFirst().get();
        } catch (NoSuchElementException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
            return null;
        }
    }

    public void createNewEpic(String name, String description) {
        allEpics.put(Epic.getNewId(), new Epic(name, description));
    }

    public void createNewSubtask(String name, String description, long epicId) {
        try {
            allEpics.get(epicId).addSubtask(name, description);
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id="+ epicId + " не существует");
        }
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
        try {
            Long epicId = this.getEpicBySubtaskIdOrNull(subtaskId).getId();
            this.getSubtaskByIdOrNull(subtaskId).changeStatusDone();
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
        }
    }

    public void changeSubtaskStatusInProgress(Long subtaskId) {
        try {
            getEpicBySubtaskIdOrNull(subtaskId).changeStatusSubtaskInProgress(subtaskId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
        }
    }

    public void updateEpicName(Long epicId, String newName) {
        try {
            allEpics.get(epicId).setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id="+ epicId + " не существует");
        }
    }

    public void updateEpicDescription(Long epicId, String newDescription) {
        try {
            allEpics.get(epicId).setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id="+ epicId + " не существует");
        }
    }

    public void updateSubtaskName(Long subtaskId, String newName) {
        try {
            getSubtaskByIdOrNull(subtaskId).setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
        }
    }

    public void updateSubtaskDescription(Long subtaskId, String newDescription) {
        try{
            getSubtaskByIdOrNull(subtaskId).setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id="+ subtaskId + " не существует");
        }
    }

    public void checkEpicStatus(Long epicId) {
        if (this.getAllEpicsSubtasks(epicId).stream()
                .allMatch(o -> o.getStatus().equals(TaskStatus.DONE))) {
            this.getEpicById(epicId).setStatus(TaskStatus.DONE);
        } else if (this.getEpicById(epicId)
                .getAllSubtask().stream()
                .allMatch(o -> o.getStatus().equals(TaskStatus.NEW))) {
            this.getEpicById(epicId).setStatus(TaskStatus.NEW);
        } else {
            this.getEpicById(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
