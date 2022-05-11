# java-kanban

## About 
This repository contains homework for sprint number 4 yandex practicum. This is the backend of the task tracker program. It contains the main classes epic and subtask. All main functions are performed by InMemoryTaskManager.

## Main function InMemoryTaskManager

1. removeAllEpics - removes all tasks from the epic category;
2. removeAllSubtasks removes all tasks from the subtask category;
3. removeSubtasksByEpicId - deletes all epic subtasks by epic id;
4. removeEpic - deletes an epic task by it's id;
5. removeSubtasksById - deletes an subtask by it's id;
6. getEpicById - returns an epic by its id;
7. getSubtaskById - returns an subtask by it's id;
8. getEpicBySubtaskId - returns an epic by the id of its subtask;
9. createNewEpic - creates a new epic by it's name and description;
10. createNewSubtask - creates a new subtask by it's name, description and epics id;
11. getAllEpics - return all epics ArrayList;
12. getAllSubtasks - return all subtasks ArrayList;
13. getAllEpicsSubtasks - return ArrayList of subtasks of one epic by the specified id; 
14. changeSubtaskStatusDone - change status of subtask to "Done" the specified subtask id;
15. changeSubtaskStatusInProgress - change status of subtask to "In Progress" the specified subtask id;
16. updateEpicName - change name of epic by the specified id;
17. updateEpicDescription - change description of epic by the specified id;
18. updateSubtaskName - change name of subtask by the specified id;
19. updateSubtaskDescription - change description of subtask by the specified id;
20. getHistory - returns the method call history: getEpicById, getSubtaskById, getEpicBySubtaskId.
