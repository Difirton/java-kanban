package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TasksManager {
    private final Map<Long, Epic> allEpics;
    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.allEpics = new HashMap<>();
        this.inMemoryHistoryManager = Manager.getDefaultHistory();
    }

    @Override
    public void removeAllEpics() {
        allEpics.clear();
        inMemoryHistoryManager.clearHistory();
    }

    @Override
    public void removeAllSubtasks() {
        for (long epicId : allEpics.keySet()) {
            allEpics.get(epicId).getAllSubtask().stream()
                    .forEach(o -> inMemoryHistoryManager.remove(o.getId()));
            allEpics.get(epicId).removeSubtasks();
        }
    }

    @Override
    public void removeSubtasksByEpicId(Long epicId) {
        try {
            allEpics.get(epicId).getAllSubtask().stream()
                    .forEach(o -> inMemoryHistoryManager.remove(o.getId()));
            allEpics.get(epicId).removeSubtasks();
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    @Override
    public void removeEpicById(Long epicId) {
        try {
            allEpics.get(epicId).getAllSubtask().stream()
                    .forEach(o -> inMemoryHistoryManager.remove(o.getId()));
            inMemoryHistoryManager.remove(epicId);
            allEpics.get(epicId).removeSubtasks();
            allEpics.remove(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    @Override
    public void removeSubtasksById(Long subtaskId) {
        try {
            Long epicId = this.getEpicBySubtaskIdOrNull(subtaskId).getId();
            inMemoryHistoryManager.remove(subtaskId);
            this.getEpicById(epicId).removeSubtask(subtaskId);
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    @Override
    public Epic getEpicById(Long epicId) {
        try {
            inMemoryHistoryManager.add(allEpics.get(epicId));
            return allEpics.get(epicId);
        } catch (NoSuchElementException | NullPointerException exception) {
            System.out.println("Недопустимое действие. Эпика с id=" + epicId + " не существует");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskByIdOrNull(Long subtaskId) {
        try {
            Subtask foundSubtask = this.getAllSubtasks().stream()
                    .filter(o -> Objects.equals(subtaskId, o.getId()))
                    .findFirst()
                    .get();
            inMemoryHistoryManager.add(foundSubtask);
            return foundSubtask;
        } catch (NoSuchElementException | NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
            return null;
        }
    }

    @Override
    public Epic getEpicBySubtaskIdOrNull(Long subtaskId) {
        try {
            return this.getAllSubtasks().stream()
                    .filter(o -> subtaskId.equals(o.getId()))
                    .map(o -> o.getEpicsId())
                    .map(o -> this.getEpicById(o))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException | NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
            return null;
        }
    }

    @Override
    public void createNewEpic(String name, String description) {
        allEpics.put(Epic.getNewId(), new Epic(name, description));
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
        try {
            allEpics.get(epicId).addSubtask(name, description);
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return allEpics.values().stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        this.getAllEpics().stream()
                .map(o -> o.getAllSubtask())
                .forEach(o -> allSubtasks.addAll(o));
        return allSubtasks;
    }

    @Override
    public ArrayList<Subtask> getAllEpicsSubtasks(Long epicId) {
        return allEpics.get(epicId).getAllSubtask();
    }

    public void changeSubtaskStatusDone(Long subtaskId) {
        try {
            Long epicId = this.getEpicBySubtaskIdOrNull(subtaskId).getId();
            this.getSubtaskByIdOrNull(subtaskId).changeStatusDone();
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    @Override
    public void changeSubtaskStatusInProgress(Long subtaskId) {
        try {
            getEpicBySubtaskIdOrNull(subtaskId).changeStatusSubtaskInProgress(subtaskId);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    @Override
    public void updateEpicName(Long epicId, String newName) {
        try {
            allEpics.get(epicId).setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    @Override
    public void updateEpicDescription(Long epicId, String newDescription) {
        try {
            allEpics.get(epicId).setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    @Override
    public void updateSubtaskName(Long subtaskId, String newName) {
        try {
            getSubtaskByIdOrNull(subtaskId).setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    @Override
    public void updateSubtaskDescription(Long subtaskId, String newDescription) {
        try {
            getSubtaskByIdOrNull(subtaskId).setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    private void checkEpicStatus(Long epicId) {
        if (this.getAllEpicsSubtasks(epicId).stream()
                .allMatch(o -> o.getStatus() == TaskStatus.DONE)) {
            this.getEpicById(epicId).setStatus(TaskStatus.DONE);
        } else if (this.getEpicById(epicId)
                .getAllSubtask().stream()
                .allMatch(o -> o.getStatus() == TaskStatus.NEW)) {
            this.getEpicById(epicId).setStatus(TaskStatus.NEW);
        } else {
            this.getEpicById(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
