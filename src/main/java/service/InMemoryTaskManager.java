package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.EpicNotFoundException;
import error.TaskNotFoundException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TasksManager, Serializable {
    private final long serialVersionUID = 2L;
    private final Map<Long, Task> allTasks;
    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.allTasks = new HashMap<>();
        this.inMemoryHistoryManager = Manager.getDefaultHistory();
    }

    @Override
    public void createNewEpic(String name, String description) {
        this.allTasks.put(Epic.getNewId(), new Epic(name, description));
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
        try {
            this.allTasks.put(Subtask.getNewId(), new Subtask(name, description, epicId));
            checkEpicStatus(epicId);
        } catch (NullPointerException exception) {
            throw new EpicNotFoundException("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }

    private void checkEpicStatus(Long epicId) {
        Task taskToCheck = this.allTasks.get(epicId);
        Epic epicToCheck = (Epic) taskToCheck;
        if (isAllSubtaskEpicDone(epicToCheck)) {
            epicToCheck.setStatus(TaskStatus.DONE);
        } else if (isAllSubtaskEpicNew(epicToCheck)) {
            epicToCheck.setStatus(TaskStatus.NEW);
        } else {
            epicToCheck.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private boolean isAllSubtaskEpicDone(Epic epicToCheck) {
        return epicToCheck.getAllIdSubtasks().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.DONE);
    }

    private boolean isAllSubtaskEpicNew(Epic epicToCheck) {
        return epicToCheck.getAllIdSubtasks().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.NEW);
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task requestedTask;
        try {
            requestedTask = this.allTasks.get(taskId);
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Недопустимое действие. Задача с id=" + taskId + " не существует");
        }
        return requestedTask;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return this.allTasks.entrySet().stream()
                .filter(o -> o.getValue().getClass() == Epic.class)
                .map(Map.Entry::getValue)
                .map(o -> (Epic) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return this.allTasks.entrySet().stream()
                .filter(o -> o.getValue().getClass() == Subtask.class)
                .map(Map.Entry::getValue)
                .map(o -> (Subtask) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Subtask> getAllEpicsSubtasks(Long epicId) {
        Epic epicToExtract;
        try {
            Task taskToCheck = this.getTaskById(epicId);
            epicToExtract = (Epic) taskToCheck;
        } catch (NullPointerException exception) {
            throw new EpicNotFoundException("Недопустимое действие. Задача с id=" + epicId + " не существует");
        } catch (ClassCastException exception) {
            throw new EpicNotFoundException("Ошибка приведения типа. Задача с id=" + epicId + " не является эпиком");
        }
        return epicToExtract.getAllIdSubtasks().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void updateTaskName(Long taskId, String newName) {
        try {
            Task taskToChangeName = this.allTasks.get(taskId);
            taskToChangeName.setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Задачи с id=" + taskId + " не существует");
        }
    }

    @Override
    public void updateTaskDescription(Long taskId, String newDescription) {
        try {
            Task taskToChangeDescription = this.allTasks.get(taskId);
            taskToChangeDescription.setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Задачи с id=" + taskId + " не существует");
        }
    }

    @Override
    public void changeSubtaskStatusDone(Long subtaskId) {
        try {
            Task taskToCheck = this.getTaskById(subtaskId);
            Subtask subtaskToChangeStatus = (Subtask) taskToCheck;
            subtaskToChangeStatus.changeStatusDone();
            Long epicIdToCheckStatus = subtaskToChangeStatus.getEpicsId();
            checkEpicStatus(epicIdToCheckStatus);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        } catch (ClassCastException exception) {
            System.out.println("Ошибка приведения типа. Задача с id=" + subtaskId + " не является подзадачей");
        }
    }

    @Override
    public void changeSubtaskStatusInProgress(Long subtaskId) {
        try {
            Task taskToCheck = this.getTaskById(subtaskId);
            Subtask subtaskToChangeStatus = (Subtask) taskToCheck;
            subtaskToChangeStatus.changeStatusInProgress();
            Long epicIdToCheckStatus = subtaskToChangeStatus.getEpicsId();
            checkEpicStatus(epicIdToCheckStatus);
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        } catch (ClassCastException exception) {
            System.out.println("Ошибка приведения типа. Задача с id=" + subtaskId + " не является подзадачей");
        }
    }

    @Override
    public void removeSubtasksById(Long subtaskId) {
        try {
            Task taskToCheck = this.allTasks.get(subtaskId);
            Subtask subtaskToRemove = (Subtask) taskToCheck;
            Long epicIdToCheckStatus = subtaskToRemove.getEpicsId();
            inMemoryHistoryManager.remove(subtaskId);
            this.allTasks.remove(subtaskId);
            this.checkEpicStatus(epicIdToCheckStatus);
        } catch (NullPointerException exception) {
            System.out.println("Удаление невозможно. Подзадача с id=" + subtaskId + " не существует");
        }
    }

    @Override
    public void removeEpicById(Long epicId) {
        try {
            Task taskToCheck = this.allTasks.get(epicId);
            Epic epicToRemove = (Epic) taskToCheck;
            epicToRemove.getAllIdSubtasks().stream()
                    .forEach(inMemoryHistoryManager::remove);
            epicToRemove.getAllIdSubtasks().stream()
                    .forEach(this.allTasks::remove);
            inMemoryHistoryManager.remove(epicId);
            this.allTasks.remove(epicId);
        } catch (NullPointerException exception) {
            System.out.println("Удаление невозможно. Эпик с id=" + epicId + " не существует");
        }
    }





















    @Override
    public void removeAllEpics() {
        allTasks.entrySet().stream()
                .filter();
        inMemoryHistoryManager.clearHistory();
    }

    @Override
    public void removeAllSubtasks() {
        for (long taskId : allTasks.keySet()) {
            allTasks.entrySet().stream()
                    .filter(o -> o.getValue() instanceof Subtask)
                    .map(o -> o.getKey())
                    .forEach(inMemoryHistoryManager::remove);
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
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
