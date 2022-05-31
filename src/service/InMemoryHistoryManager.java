package service;

import entity.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList historyQueueTasks = new CustomLinkedList();

    @Override
    public void add(Task task) {
        //TODO подумать о хранении id не подходит
        historyQueueTasks.add(task);
    }

    @Override
    public void remove(Long id) {
        historyQueueTasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyQueueTasks.toList();
    }

    private class CustomLinkedList {
        private Map<Long, Node> entryMap;
        private Long head;
        private Long tail;

        private CustomLinkedList() {
            this.entryMap = new HashMap<>();
        }

        private void add(Task task) {
            Long id = task.getId();
            if (this.head == null) {
                Node node = new Node(task);
                this.entryMap.put(id, node);
                this.head = id;
                this.tail = id;
            } else {
                if (entryMap.containsKey(id)){
                    Node node = entryMap.get(id);
                    if (node.getHead() == null) {
                        this.head = node.getTail();
                    }
                    rebindingLinksAfterTaskExtraction(id);
                    entryMap.get(tail).setTail(id);
                    node.setHead(this.tail);
                    node.setTail(null);
                    this.tail = id;
                } else {
                    Node newTail = new Node(task, this.tail);
                    entryMap.put(id, newTail);
                    this.entryMap.get(this.tail).setTail(id);
                    this.tail = id;
                }
            }
        }

        private void remove(Long id) {
            rebindingLinksAfterTaskExtraction(id);
            this.entryMap.remove(id);
        }

        private List<Task> toList() {
            List<Task> taskList = new ArrayList<>();
            Node node = this.entryMap.getOrDefault(this.head, null);
            while (node != null) {
                taskList.add(node.getTask());
                node = this.entryMap.get(node.getTail());
            }
            return taskList;
        }

        private void rebindingLinksAfterTaskExtraction(Long id) {
            Node removedNode= this.entryMap.get(id);
            Long tail = removedNode.getTail();
            if (tail != null) {
                Long head = removedNode.getHead();
                if (head != null) {
                    this.entryMap.get(tail).setHead(head);
                    this.entryMap.get(head).setTail(tail);
                } else {
                    this.entryMap.get(tail).setHead(null);
                }
            }
        }

        private class Node{
            private final Task task;
            private Long head;
            private Long tail;

            private Node(Task task) {
                this.task = task;
                this.head = null;
                this.tail = null;

            }

            private Node(Task task, Long head) {
                this.task = task;
                this.head = head;
                this.tail = null;
            }

            private Task getTask() {
                return task;
            }

            private Long getHead() {
                return head;
            }

            private void setHead(Long head) {
                this.head = head;
            }

            private Long getTail() {
                return tail;
            }

            private void setTail(Long tail) {
                this.tail = tail;
            }

            public final String toString() {
                return task.toString() + ", head: " + head + "; tail: " + tail;
            }
        }
    }
}
