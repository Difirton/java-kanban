# java-kanban

## About 
This repository contains homework for sprint number 6 yandex practicum. This is the backend of the task tracker program. It contains the main classes epic and subtask. All main functions are performed by In Memory Tasks Manager. Saving the history of viewing tasks is engaged in In Memory History Manager. To save the tasks tracker state file, it is provided File Backed Tasks Manager. The Manager class provides both implementations

## Main function In Memory Tasks Manager and In Memory History Manager

1. createNewEpic - creates a new epic by it's name and description;
2. createNewSubtask - creates a new subtask by it's name, description and epics id, start time and duration time; 
3. removeAllEpics - removes all tasks from the epic category;
4. removeAllSubtasks removes all tasks from the subtask category;
5. removeSubtasksByEpicId - deletes all epic subtasks by epic id;
6. removeEpic - deletes an epic task by it's id;
7. removeSubtasksById - deletes an subtask by it's id;
8. getEpicById - returns an epic by its id;
9. getSubtaskById - returns an subtask by it's id;
10. getEpicBySubtaskId - returns an epic by the id of its subtask;
11.  getAllEpics - return all epics ArrayList;
12.  getAllSubtasks - return all subtasks ArrayList;
13.  getAllEpicsSubtasks - return ArrayList of subtasks of one epic by the specified id; 
14.  changeSubtaskStatusDone - change status of subtask to "Done" the specified subtask id;
15.  changeSubtaskStatusInProgress - change status of subtask to "In Progress" the specified subtask id;
16.  updateEpicName - change name of epic by the specified id;
17.  updateEpicDescription - change description of epic by the specified id;
18.  updateSubtaskName - change name of subtask by the specified id;
19.  updateSubtaskDescription - change description of subtask by the specified id.
