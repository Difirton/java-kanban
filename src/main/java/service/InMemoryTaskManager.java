package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.TaskNotFoundException;
import utill.TimeIntervalsList;
import utill.TimeIntervalsList.TimeInterval;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TasksManager, Serializable {
    private final long serialVersionUID = 2L;
    private long amountTaskId;
    private final Map<Long, Task> allTasks;
    private final Set<Subtask> sortedSubtasks;
    private final HistoryManager inMemoryHistoryManager;
    private final TimeIntervalsList occupiedSlots;

    protected InMemoryTaskManager() {
        amountTaskId = 1L;
        this.allTasks = new HashMap<>();
        this.sortedSubtasks = new TreeSet<>();
        this.inMemoryHistoryManager = Manager.getDefaultHistory();
        this.occupiedSlots = new TimeIntervalsList();
    }

    @Override
    public void createNewEpic(String name,
                              String description) {
        long idNewEpic = amountTaskId++;
        Epic newEpic = new Epic.EpicBuilder(idNewEpic)
                .Name(name)
                .Description(description)
                .build();
        this.allTasks.put(idNewEpic, newEpic);
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder(idNewSubtask, epicId)
                .Name(name)
                .Description(description)
                .build();
        this.subtaskFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder(idNewSubtask, epicId)
                .Name(name)
                .Description(description)
                .StartDateTime(startDateTime)
                .build();
        this.subtaskFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime,
                                 int timeExecutionInMinutes) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder(idNewSubtask, epicId)
                .Name(name)
                .Description(description)
                .StartDateTime(startDateTime)
                .TimeExecutionInMinutes(timeExecutionInMinutes)
                .build();
        this.subtaskCreatorFacade(newSubtask, idNewSubtask, epicId);
    }

    private void subtaskCreatorFacade(Subtask newSubtask, long idNewSubtask, long epicId){
        if (this.addTaskIfFreeTimeInterval(newSubtask)) {
            this.allTasks.put(idNewSubtask, newSubtask);
            this.sortedSubtasks.add(newSubtask);
            this.getEpicAfterValid(epicId).addSubtask(idNewSubtask);
            this.checkEpicStatusAndTimeExecution(epicId);
        } else {
            amountTaskId--;
            System.out.println("Добавление не выполнено. Временной интервал занят.");
        }
    }

    private boolean addTaskIfFreeTimeInterval(Subtask newSubtask) {
        LocalDateTime startDateTime = newSubtask.getStartDateTime();
        LocalDateTime finishDateTime = newSubtask.getEndDateTime();
        TimeInterval timeInterval = occupiedSlots.createTimeInterval(startDateTime, finishDateTime);
        return occupiedSlots.add(timeInterval);
    }

    private void checkEpicStatusAndTimeExecution(Long epicId) {
        Epic epicToCheck = getEpicAfterValid(epicId);
        this.checkEpicStatus(epicToCheck);
        this.checkEpicTimeExecution(epicToCheck);
    }

    private void checkEpicStatus(Epic epicToCheck) {
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

    private void checkEpicTimeExecution(Epic epicToCheck) {
        final int INDEX_SINGLE_ELEMENT = 0;
        List<Subtask> sortedEpicsSubtasks = epicToCheck.getAllIdSubtasks().stream()
                .map(this::getSubtaskAfterValid)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        if (sortedEpicsSubtasks.size() == 1) {
            epicToCheck.setStartDateTime(sortedEpicsSubtasks.get(INDEX_SINGLE_ELEMENT).getStartDateTime());
            epicToCheck.setTimeExecution(sortedEpicsSubtasks.get(INDEX_SINGLE_ELEMENT).getTimeExecution());
        } else if (sortedEpicsSubtasks.size() > 1) {
            setEpicTimeCharacteristics(epicToCheck, sortedEpicsSubtasks);
        }
    }

    private void setEpicTimeCharacteristics(Epic epicToSet, List<Subtask> sortedEpicsSubtasks) {
        final int INDEX_FIRST_ELEMENT = 0;
        LocalDateTime minStartDate = sortedEpicsSubtasks.get(INDEX_FIRST_ELEMENT).getStartDateTime();
        LocalDateTime maxEndDate = sortedEpicsSubtasks.stream()
                .map(Subtask::getEndDateTime)
                .max(LocalDateTime::compareTo)
                .get();
        epicToSet.setStartDateTime(minStartDate);
        epicToSet.setTimeExecution(Duration.between(minStartDate, maxEndDate));
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

    protected Task getTaskById(Long taskId) {
        Task requestedTask;
        try {
            requestedTask = this.allTasks.get(taskId);
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Недопустимое действие. Задача с id=" + taskId + " не существует");
        }
        return requestedTask;
    }

    @Override
    public Epic getEpicById(Long epicId) {
        this.inMemoryHistoryManager.add(this.getTaskById(epicId));
        return this.getEpicAfterValid(epicId);
    }

    @Override
    public Subtask getSubtaskById(Long subtaskId) {
        this.inMemoryHistoryManager.add(this.getTaskById(subtaskId));
        return this.getSubtaskAfterValid(subtaskId);
    }


    @Override
    public Epic getEpicBySubtaskId(Long subtaskId) {
        Subtask subtaskToFindEpic = getSubtaskAfterValid(subtaskId);
        long epicId = subtaskToFindEpic.getEpicsId();
        this.inMemoryHistoryManager.add(this.getTaskById(epicId));
        return getEpicAfterValid(epicId);
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
    public List<Task> getPrioritizedTasks() {
        System.out.println(this.sortedSubtasks);
        return this.sortedSubtasks.stream().collect(Collectors.toList());
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
        this.checkEpicStatus(this.getEpicAfterValid(epicIdToCheckStatus));
    }

    @Override
    public void changeSubtaskStatusInProgress(Long subtaskId) {
        Subtask subtaskToChangeStatus = getSubtaskAfterValid(subtaskId);
        subtaskToChangeStatus.changeStatusInProgress();
        Long epicIdToCheckStatus = subtaskToChangeStatus.getEpicsId();
        this.checkEpicStatus(this.getEpicAfterValid(epicIdToCheckStatus));
    }

    @Override
    public void removeSubtasksById(Long subtaskId) {
        Subtask subtaskToRemove = getSubtaskAfterValid(subtaskId);
        Long epicId = subtaskToRemove.getEpicsId();
        getEpicAfterValid(epicId).removeSubtask(subtaskId);
        this.allTasks.remove(subtaskId);
        this.checkEpicStatus(this.getEpicAfterValid(epicId));
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
                .forEach(this.allTasks::remove);
        this.allTasks.keySet().stream()
                .map(this::getEpicAfterValid)
                .forEach(Epic::removeSubtasks);
        idSubtasksToRemove.stream()
                .forEach(inMemoryHistoryManager::remove);
    }

    @Override
    public void removeSubtasksByEpicId(Long epicId) {
        Epic epicToRemoveSubtasks = getEpicAfterValid(epicId);
        epicToRemoveSubtasks.getAllIdSubtasks().stream()
                .forEach(this.allTasks::remove);
        epicToRemoveSubtasks.getAllIdSubtasks().stream()
                .forEach(inMemoryHistoryManager::remove);
        epicToRemoveSubtasks.removeSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
