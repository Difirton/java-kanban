package entity;

import constant.TaskStatus;
import constant.TypeTask;
import utill.DateParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static constant.TypeTask.EPIC;

public class Epic extends Task {
    private List<Long> allIdSubtasksInEpic;

    private Epic() {
        super(0);
    }

    private Epic(EpicBuilder epicBuilder) {
        super(epicBuilder.id);
        super.setName(epicBuilder.name);
        super.setDescription(epicBuilder.description);
        super.setStatus(epicBuilder.status);
        super.setTimeExecution(epicBuilder.timeExecution);
        super.setStartDateTime(epicBuilder.startDateTime);
        this.allIdSubtasksInEpic = epicBuilder.allIdSubtasksInEpic;
    }

    public TypeTask getType() {
        return EPIC;
    }

    public void addSubtask(Long idSubtask) {
        this.allIdSubtasksInEpic.add(idSubtask);
    }

    public void removeSubtasks() {
        this.allIdSubtasksInEpic.clear();
    }

    public void removeSubtask(Long idSubtask) {
        this.allIdSubtasksInEpic.remove(idSubtask);
    }

    public List<Long> getAllIdSubtasksInEpic() {
        return allIdSubtasksInEpic;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Epic.class.getSimpleName() + "{", "}")
                .add("id=" + this.getId())
                .add("allIdSubtasksInEpic=" + allIdSubtasksInEpic)
                .add("name='" + this.getName() + "'")
                .add("description='" + this.getDescription() + "'")
                .add("status=" + this.getStatus())
                .add("start=" + this.getStartDateTime())
                .add("execution=" + this.getTimeExecution().toMinutes())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        Collections.sort(allIdSubtasksInEpic);
        Collections.sort( epic.allIdSubtasksInEpic);
        return Objects.equals(allIdSubtasksInEpic, epic.allIdSubtasksInEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), allIdSubtasksInEpic);
    }

    public static class EpicBuilder {
        private long id;
        private String name = "";
        private String description = "";
        private TaskStatus status = TaskStatus.NEW;
        private List<Long> allIdSubtasksInEpic = new ArrayList<>();
        private Duration timeExecution = Duration.ofMinutes(0);
        private LocalDateTime startDateTime = LocalDateTime.MAX;

        public EpicBuilder() { }

        public EpicBuilder ID(long id) {
            this.id = id;
            return this;
        }

        public EpicBuilder Name(String name) {
            this.name = name;
            return this;
        }

        public EpicBuilder Description(String description) {
            this.description = description;
            return this;
        }

        public EpicBuilder Status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public EpicBuilder AllIdSubtasksInEpic(List<Long> allIdSubtasksInEpic) {
            this.allIdSubtasksInEpic = allIdSubtasksInEpic;
            return this;
        }

        public EpicBuilder TimeExecution(Duration timeExecution) {
            this.timeExecution = timeExecution;
            return this;
        }

        public EpicBuilder StartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public EpicBuilder StartDateTime(String startDateTime) {
            LocalDateTime formatterStartDateTime = DateParser.stringToDate(startDateTime);
            this.startDateTime = formatterStartDateTime;
            return this;
        }

        public Epic build() {
            return new Epic(this);
        }
    }
}
