package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.TaskNotFoundException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TasksManager, Serializable {
    private final long serialVersionUID = 2L;
    private long amountTaskId;
    private final Map<Long, Task> allTasks;
    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        amountTaskId = 1L;
        this.allTasks = new HashMap<>();
        this.inMemoryHistoryManager = Manager.getDefaultHistory();
    }

    @Override
    public void createNewEpic(String name, String description) {
        long idNewEpic = amountTaskId++;
        this.allTasks.put(idNewEpic, new Epic(idNewEpic, name, description));
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
//        try {
            long idNewSubtask = amountTaskId++;
            this.allTasks.put(idNewSubtask, new Subtask(idNewSubtask, name, description, epicId));
            getEpicAfterValid(epicId).addSubtask(idNewSubtask);
            checkEpicStatus(epicId);
//        } catch (NullPointerException exception) {
//            throw new TaskNotFoundException("Недопустимое действие. Епик с id=" + epicId + " не существует");
//        }
    }

    private void checkEpicStatus(Long epicId) {
        Epic epicToCheck = getEpicAfterValid(epicId);
        if (isAllSubtaskEpicDone(epicToCheck)) {
            epicToCheck.setStatus(TaskStatus.DONE);
        } else if (isAllSubtaskEpicNew(epicToCheck)) {
            epicToCheck.setStatus(TaskStatus.NEW);
        } else {
            epicToCheck.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private boolean isAllSubtaskEpicDone(Epic epicToCheck) {
        if (epicToCheck.getAllIdSubtasks().isEmpty()) {
            return false;
        }
        return epicToCheck.getAllIdSubtasks().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.DONE);
    }

    private boolean isAllSubtaskEpicNew(Epic epicToCheck) {
        if (epicToCheck.getAllIdSubtasks().isEmpty()) {
            return true;
        }
        return epicToCheck.getAllIdSubtasks().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.NEW);
    }

    private Epic getEpicAfterValid(Long epicId) {
        try {
            Task taskToCheck = this.getTaskById(epicId);
            Epic epicToCast = (Epic) taskToCheck;
            return epicToCast;
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Недопустимое действие. Эпик с id=" + epicId + " не существует");
        } catch (ClassCastException exception) {
            throw new TaskNotFoundException("Ошибка приведения типа. Эпик с id=" + epicId + " не является эпиком");
        }
    }

    private Subtask getSubtaskAfterValid(Long subtaskId) {
        try {
            Task taskToCheck = this.getTaskById(subtaskId);
            Subtask subtaskToCast = (Subtask) taskToCheck;
            return subtaskToCast;
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Недопустимое действие. Подзадача с id=" + subtaskId + " не существует");
        } catch (ClassCastException exception) {
            throw new TaskNotFoundException("Ошибка приведения типа. Подзадача с id=" + subtaskId + " не является эпиком");
        }
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
    public List<Epic> getAllEpics() {
        return this.allTasks.entrySet().stream()
                .filter(o -> o.getValue() instanceof Epic)
                .map(Map.Entry::getValue)
                .map(o -> (Epic) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return this.allTasks.entrySet().stream()
                .filter(o -> o.getValue() instanceof Subtask)
                .map(Map.Entry::getValue)
                .map(o -> (Subtask) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Subtask> getAllEpicsSubtasks(Long epicId) {
        Epic epicToExtract = getEpicAfterValid(epicId);
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
        Subtask subtaskToChangeStatus = getSubtaskAfterValid(subtaskId);
        subtaskToChangeStatus.changeStatusDone();
        Long epicIdToCheckStatus = subtaskToChangeStatus.getEpicsId();
        checkEpicStatus(epicIdToCheckStatus);
    }

    @Override
    public void changeSubtaskStatusInProgress(Long subtaskId) {
        Subtask subtaskToChangeStatus = getSubtaskAfterValid(subtaskId);
        subtaskToChangeStatus.changeStatusInProgress();
        Long epicIdToCheckStatus = subtaskToChangeStatus.getEpicsId();
        checkEpicStatus(epicIdToCheckStatus);
    }

    @Override
    public void removeSubtasksById(Long subtaskId) {
        Subtask subtaskToRemove = getSubtaskAfterValid(subtaskId);
        Long epicId = subtaskToRemove.getEpicsId();
        getEpicAfterValid(epicId).removeSubtask(subtaskId);
        this.allTasks.remove(subtaskId);
        this.checkEpicStatus(epicId);
        inMemoryHistoryManager.remove(subtaskId);
    }

    @Override
    public void removeEpicById(Long epicId) {
        Epic epicToRemove = getEpicAfterValid(epicId);
        epicToRemove.getAllIdSubtasks().stream()
                .forEach(inMemoryHistoryManager::remove);
        epicToRemove.getAllIdSubtasks().stream()
                .forEach(this.allTasks::remove);
            inMemoryHistoryManager.remove(epicId);
            this.allTasks.remove(epicId);
    }

    @Override
    public void removeAllEpics() {
        allTasks.clear();
        inMemoryHistoryManager.clearHistory();
    }

    @Override
    public void removeAllSubtasks() {
        List<Long> idSubtasksToRemove = allTasks.entrySet().stream()
                .filter(o -> o.getValue() instanceof Subtask)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
        idSubtasksToRemove.stream()
                .forEach(this::removeSubtasksById);
    }

    @Override
    public void removeSubtasksByEpicId(Long epicId) {
        try { //TODO переименовать epic и разобраться с историей
            Task taskToCheck = this.allTasks.get(epicId);
            Epic epicToRemove = (Epic) taskToCheck;
            epicToRemove.getAllIdSubtasks().stream()
                    .forEach(this.allTasks::remove);
            epicToRemove.removeSubtasks();
        } catch (NullPointerException exception) {
            System.out.println("Недопустимое действие. Епик с id=" + epicId + " не существует");
        }
    }


    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
