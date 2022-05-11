package utill;

import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.util.ArrayList;
import java.util.List;

public interface TasksManager {

    void createNewEpic(String name, String description);
    void createNewSubtask(String name, String description, long epicId);

    Epic getEpicById(Long epicId);
    Subtask getSubtaskByIdOrNull(Long subtaskId);
    Epic getEpicBySubtaskIdOrNull(Long subtaskId);
    ArrayList<Epic> getAllEpics();
    ArrayList<Subtask> getAllSubtasks();
    ArrayList<Subtask> getAllEpicsSubtasks(Long epicId);

    void changeSubtaskStatusDone(Long subtaskId);
    void changeSubtaskStatusInProgress(Long subtaskId);
    void updateEpicName(Long epicId, String newName);
    void updateEpicDescription(Long epicId, String newDescription);
    void updateSubtaskName(Long subtaskId, String newName);
    void updateSubtaskDescription(Long subtaskId, String newDescription);

    void removeAllEpics();
    void removeAllSubtasks() ;
    void removeEpicById(Long epicId);
    void removeSubtasksById(Long subtaskId);
    void removeSubtasksByEpicId(Long epicId);

    List<Task> getHistory();
}