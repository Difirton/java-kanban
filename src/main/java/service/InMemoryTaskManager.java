package service;

import constant.TaskStatus;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.TaskNotFoundException;
import utill.TimeIntervalsList;

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
        Epic newEpic = new Epic.EpicBuilder()
                .ID(idNewEpic)
                .Name(name)
                .Description(description)
                .build();
        this.allTasks.put(idNewEpic, newEpic);
    }

    @Override
    public void createNewSubtask(String name, String description, long epicId) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder()
                .ID(idNewSubtask)
                .EpicsID(epicId)
                .Name(name)
                .Description(description)
                .build();
        this.subtaskCreatorFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 LocalDateTime startDateTime) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder()
                .ID(idNewSubtask)
                .EpicsID(epicId)
                .Name(name)
                .Description(description)
                .StartDateTime(startDateTime)
                .build();
        this.subtaskCreatorFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder()
                .ID(idNewSubtask)
                .EpicsID(epicId)
                .Name(name)
                .Description(description)
                .StartDateTime(startDateTime)
                .build();
        this.subtaskCreatorFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 LocalDateTime startDateTime,
                                 Duration timeExecutionInMinutes) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder()
                .ID(idNewSubtask)
                .EpicsID(epicId)
                .Name(name)
                .Description(description)
                .StartDateTime(startDateTime)
                .TimeExecution(timeExecutionInMinutes)
                .build();
        this.subtaskCreatorFacade(newSubtask, idNewSubtask, epicId);
    }

    @Override
    public void createNewSubtask(String name,
                                 String description,
                                 long epicId,
                                 String startDateTime,
                                 int timeExecutionInMinutes) {
        long idNewSubtask = amountTaskId++;
        Subtask newSubtask = new Subtask.SubtaskBuilder()
                .ID(idNewSubtask)
                .EpicsID(epicId)
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
            System.out.println("Adding failed. Timeslot is busy.");
        }
    }

    private boolean addTaskIfFreeTimeInterval(Subtask newSubtask) {
        LocalDateTime startDateTime = newSubtask.getStartDateTime();
        LocalDateTime finishDateTime = newSubtask.getEndDateTime();
        return occupiedSlots.add(startDateTime, finishDateTime);
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
        if (epicToCheck.getAllIdSubtasksInEpic().isEmpty()) {
            return false;
        }
        return epicToCheck.getAllIdSubtasksInEpic().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.DONE);
    }

    private boolean isAllSubtaskEpicNew(Epic epicToCheck) {
        if (epicToCheck.getAllIdSubtasksInEpic().isEmpty()) {
            return true;
        }
        return epicToCheck.getAllIdSubtasksInEpic().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .map(Subtask::getStatus)
                .allMatch(o -> o == TaskStatus.NEW);
    }

    private void checkEpicTimeExecution(Epic epicToCheck) {
        final int INDEX_SINGLE_ELEMENT = 0;
        List<Subtask> sortedEpicsSubtasks = epicToCheck.getAllIdSubtasksInEpic().stream()
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
        Optional<LocalDateTime> optMaxEndDate = sortedEpicsSubtasks.stream()
                .map(Subtask::getEndDateTime)
                .max(LocalDateTime::compareTo);
        if (optMaxEndDate.isPresent()) {
            LocalDateTime maxEndDate = optMaxEndDate.get();
            epicToSet.setStartDateTime(minStartDate);
            epicToSet.setTimeExecution(Duration.between(minStartDate, maxEndDate));
        }
    }

    private Epic getEpicAfterValid(Long epicId) {
        try {
            Task taskToCheck = this.getTaskByIdWithoutHistory(epicId);
            return (Epic) taskToCheck;
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Invalid action. Epic with id=" + epicId + " does not exist");
        } catch (ClassCastException exception) {
            throw new TaskNotFoundException("Type cast error. Epic with id=" + epicId + " is not an epic");
        }
    }

    private Subtask getSubtaskAfterValid(Long subtaskId) {
        try {
            Task taskToCheck = this.getTaskByIdWithoutHistory(subtaskId);
            return (Subtask) taskToCheck;
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Invalid action. Subtask with id=" + subtaskId + " does not exist");
        } catch (ClassCastException exception) {
            throw new TaskNotFoundException("Type cast error. Subtask with id=" + subtaskId + " is not an epic");
        }
    }

    public Task getTaskByIdWithoutHistory(Long taskId) {
        Task requestedTask;
        try {
            requestedTask = this.allTasks.get(taskId);
        } catch (NullPointerException exception) {
            throw new TaskNotFoundException("Invalid action. Task with id=" + taskId + " does not exist");
        }
        return requestedTask;
    }

    @Override
    public Epic getEpicById(Long epicId) {
        this.inMemoryHistoryManager.add(this.getTaskByIdWithoutHistory(epicId));
        return this.getEpicAfterValid(epicId);
    }

    @Override
    public Subtask getSubtaskById(Long subtaskId) {
        this.inMemoryHistoryManager.add(this.getTaskByIdWithoutHistory(subtaskId));
        return this.getSubtaskAfterValid(subtaskId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        this.inMemoryHistoryManager.add(this.getTaskByIdWithoutHistory(taskId));
        return this.getTaskByIdWithoutHistory(taskId);
    }

    @Override
    public Epic getEpicBySubtaskId(Long subtaskId) {
        Subtask subtaskToFindEpic = getSubtaskAfterValid(subtaskId);
        long epicId = subtaskToFindEpic.getEpicsId();
        this.inMemoryHistoryManager.add(this.getTaskByIdWithoutHistory(epicId));
        return getEpicAfterValid(epicId);
    }


    @Override
    public List<Epic> getAllEpics() {
        return this.allTasks.values().stream()
                .filter(task -> task instanceof Epic)
                .map(o -> (Epic) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return this.allTasks.values().stream()
                .filter(task -> task instanceof Subtask)
                .map(o -> (Subtask) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Subtask> getAllEpicsSubtasks(Long epicId) {
        Epic epicToExtract = getEpicAfterValid(epicId);
        return epicToExtract.getAllIdSubtasksInEpic().stream()
                .map(this.allTasks::get)
                .map(o -> (Subtask) o)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(this.sortedSubtasks);
    }

    @Override
    public void updateTaskName(Long taskId, String newName) {
        try {
            Task taskToChangeName = this.allTasks.get(taskId);
            taskToChangeName.setName(newName);
        } catch (NullPointerException exception) {
            System.out.println("Invalid action. Task with id=" + taskId + " does not exist");
        }
    }

    @Override
    public void updateTaskDescription(Long taskId, String newDescription) {
        try {
            Task taskToChangeDescription = this.allTasks.get(taskId);
            taskToChangeDescription.setDescription(newDescription);
        } catch (NullPointerException exception) {
            System.out.println("Invalid action. Task with id=" + taskId + " does not exist");
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
    public void removeSubtaskById(Long subtaskId) {
        Subtask subtaskToRemove = getSubtaskAfterValid(subtaskId);
        Long epicId = subtaskToRemove.getEpicsId();
        getEpicAfterValid(epicId).removeSubtask(subtaskId);
        this.sortedSubtasks.remove(subtaskToRemove);
        this.occupiedSlots.remove(subtaskToRemove.getStartDateTime(), subtaskToRemove.getEndDateTime());
        this.allTasks.remove(subtaskId);
        this.checkEpicStatus(this.getEpicAfterValid(epicId));
        inMemoryHistoryManager.remove(subtaskId);
    }

    @Override
    public void removeEpicById(Long epicId) {
        Epic epicToRemove = getEpicAfterValid(epicId);
        epicToRemove.getAllIdSubtasksInEpic()
                .forEach(inMemoryHistoryManager::remove);
        epicToRemove.getAllIdSubtasksInEpic()
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
        idSubtasksToRemove
                .forEach(this.allTasks::remove);
        this.allTasks.keySet().stream()
                .map(this::getEpicAfterValid)
                .forEach(Epic::removeSubtasks);
        idSubtasksToRemove
                .forEach(inMemoryHistoryManager::remove);
    }

    @Override
    public void removeSubtasksByEpicId(Long epicId) {
        Epic epicToRemoveSubtasks = getEpicAfterValid(epicId);
        epicToRemoveSubtasks.getAllIdSubtasksInEpic()
                .forEach(this.allTasks::remove);
        epicToRemoveSubtasks.getAllIdSubtasksInEpic()
                .forEach(inMemoryHistoryManager::remove);
        epicToRemoveSubtasks.removeSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
