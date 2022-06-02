package utill;

import entity.Task;

import java.util.*;
/*Я с Вами согласен, просто в заднии было: "Отдельный класс для списка создавать не нужно — реализуйте его прямо в
классе InMemoryHistoryManager". Мне самому было непонятно зачем такая матрешка.*/
public class CustomLinkedList {
    private final Map<Long, Node> entryMap;
    private Long head;
    private Long tail;

    public CustomLinkedList() {
        this.entryMap = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    public void add(Task task) {
        Long id = task.getId();
        if (this.head == null) {
            Node node = new Node(task);
            this.entryMap.put(id, node);
            this.head = id;
            this.tail = id;
        } else {
            if (entryMap.containsKey(id)) {
                Node node = entryMap.get(id);
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

    public void remove(Long id) {
        try { //Да, может. У меня был NPE, если попытаться удалить несуществующую задачу. Ситуация спецефичная=)
            rebindingLinksAfterTaskExtraction(id);
            this.entryMap.remove(id);
        } catch (NullPointerException e) { }
    }

    public List<Task> toList() {
        List<Task> taskList = new ArrayList<>();
        Node node = this.entryMap.getOrDefault(this.head, null);
        while (node != null) {
            taskList.add(node.getTask());
            node = this.entryMap.get(node.getTail());
        }
        return taskList;
    }

    private void rebindingLinksAfterTaskExtraction(Long id) {
        Node removedNode = this.entryMap.get(id);
        Long tail = removedNode.getTail();
        Long head = removedNode.getHead();
        if (head == null) {
            this.head = removedNode.getTail();
        }
        if (tail != null) {
            if (head != null) {
                this.entryMap.get(tail).setHead(head);
                this.entryMap.get(head).setTail(tail);
            } else {
                this.entryMap.get(tail).setHead(null);
            }
        }
    }

    public void clear() {
        this.entryMap.clear();
        this.head = null;
        this.tail = null;
    }

    private class Node {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(node.task, this.task) &&
                    Objects.equals(node.head, this.head) &&
                    Objects.equals(node.tail, this.tail);
        }

        @Override
        public int hashCode() {
            int result = task.hashCode();
            result = 31 * result + (head != null ? head.hashCode() : 0) + (tail != null ? tail.hashCode() : 0);
            return result;
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
