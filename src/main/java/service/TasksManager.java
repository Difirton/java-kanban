package service;

import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.util.List;

public interface TasksManager {
    void createNewEpic(String name, String description);

    void createNewSubtask(String name, String description, long epicId);

    Epic getEpicById(Long taskId);

    Subtask getSubtaskById(Long taskId);

    Epic getEpicBySubtaskId(Long subtaskId);

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getAllEpicsSubtasks(Long epicId);

    void changeSubtaskStatusDone(Long subtaskId);

    void changeSubtaskStatusInProgress(Long subtaskId);

    void updateTaskName(Long taskId, String newName);

    void updateTaskDescription(Long taskId, String newDescription);

    void removeAllEpics();

    void removeAllSubtasks();

    void removeEpicById(Long epicId);

    void removeSubtasksById(Long subtaskId);

    void removeSubtasksByEpicId(Long epicId);

    List<Task> getHistory();
}
