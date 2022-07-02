package utill;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeIntervalsList implements Serializable {
    private final long serialVersionUID = 1L;
    private List<TimeInterval> timeIntervals;

    public TimeIntervalsList() {
        this.timeIntervals = new ArrayList<>();
    }

    public boolean add(TimeInterval newTimeInterval) {
        final int INDEX_FIRST_ELEMENT = 0;
        final int INDEX_SECOND_ELEMENT = 1;
        final int INDEX_THIRD_ELEMENT = 2;
        if (timeIntervals.isEmpty()) {
            timeIntervals.add(newTimeInterval);
            return true;
        }
        if (timeIntervals.size() == 1) {
            TimeInterval singleInterval = timeIntervals.get(INDEX_FIRST_ELEMENT);
            if (singleInterval.isInsideInterval(newTimeInterval)) {
                return false;
            } else {
                if (singleInterval.isBefore(newTimeInterval)) timeIntervals.add(newTimeInterval);
                if (singleInterval.isAfter(newTimeInterval)) timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
                return true;
            }
        }
        int middleOfList = timeIntervals.size() / 2;
        while (middleOfList > 0) {
            switch (timeIntervals.size()) {
                case (2):
                    if (timeIntervals.get(INDEX_FIRST_ELEMENT).isInsideInterval(newTimeInterval)
                            || timeIntervals.get(INDEX_SECOND_ELEMENT).isInsideInterval(newTimeInterval)) {
                        return false;
                    }
                    if (timeIntervals.get(INDEX_FIRST_ELEMENT).isBefore(newTimeInterval)) {
                        timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
                        return true;
                    }
                    if (timeIntervals.get(INDEX_SECOND_ELEMENT).isAfter(newTimeInterval))  {
                        timeIntervals.add(newTimeInterval);
                        return true;
                    }
                    timeIntervals.add(INDEX_SECOND_ELEMENT, newTimeInterval);
                    return  true;
                case (3):
                    if (timeIntervals.get(INDEX_FIRST_ELEMENT).isInsideInterval(newTimeInterval)
                            || timeIntervals.get(INDEX_SECOND_ELEMENT).isInsideInterval(newTimeInterval)
                            || timeIntervals.get(INDEX_THIRD_ELEMENT).isInsideInterval(newTimeInterval)) {
                        return false;
                    }
                    if (timeIntervals.get(INDEX_FIRST_ELEMENT).isBefore(newTimeInterval)) {
                        timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
                        return true;
                    }
                    if (timeIntervals.get(INDEX_THIRD_ELEMENT).isAfter(newTimeInterval))  {
                        timeIntervals.add(newTimeInterval);
                        return true;
                    }
                    if (timeIntervals.get(INDEX_FIRST_ELEMENT).isAfter(newTimeInterval)
                            && timeIntervals.get(INDEX_SECOND_ELEMENT).isAfter(newTimeInterval)) {
                        timeIntervals.add(INDEX_SECOND_ELEMENT, newTimeInterval);
                    }
                    timeIntervals.add(INDEX_THIRD_ELEMENT, newTimeInterval);
                    return  true;
                default: //TODO то-то туту не то
                    TimeInterval middleElement = timeIntervals.get(middleOfList);
                    TimeInterval previousElement = timeIntervals.get(middleOfList - 1);
                    if (middleElement.isBefore(newTimeInterval)) {
                        middleOfList += middleOfList / 2;
                        break;
                    }
                    if (previousElement.isBefore(middleElement)) {
                        middleOfList -= middleOfList / 2;
                        break;
                    }
                    if (previousElement.isInsideInterval(newTimeInterval)
                            || middleElement.isInsideInterval(newTimeInterval)) {
                        return false;
                    }
                    if (previousElement.isBefore(newTimeInterval) && middleElement.isAfter(newTimeInterval)) {
                        timeIntervals.add(middleOfList, newTimeInterval);
                    }
            }
        }
        return false;
    }

    public class TimeInterval implements Serializable, Comparable<TimeInterval> {
        private final long serialVersionUID = 1L;
        private LocalDateTime start;
        private LocalDateTime finish;

        public TimeInterval(LocalDateTime start, LocalDateTime finish) {
            this.start = start;
            this.finish = finish;
        }

        public TimeInterval(String start, String finish) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.start = LocalDateTime.parse(start, formatter);
            this.finish = LocalDateTime.parse(finish, formatter);
        }

        public boolean isInsideInterval(TimeInterval anotherTimeInterval) {
            if (this.finish.isBefore(anotherTimeInterval.start)) {
                return false;
            }
            if (this.start.isAfter(anotherTimeInterval.finish)) {
                return false;
            }
            return true;
        }

        public boolean isAfter(TimeInterval anotherTimeInterval) {
            return this.start.isAfter(anotherTimeInterval.finish);
        }

        public boolean isBefore(TimeInterval anotherTimeInterval) {
            return this.finish.isBefore(anotherTimeInterval.start);
        }

        @Override
        public int compareTo(TimeInterval anotherTimeInterval) {
            if (this.start.compareTo(anotherTimeInterval.start) != 0) {
                return this.start.compareTo(anotherTimeInterval.start);
            } else return this.finish.compareTo(anotherTimeInterval.finish);
        }

        public LocalDateTime getStart() {
            return start;
        }

        public void setStart(LocalDateTime start) {
            this.start = start;
        }

        public LocalDateTime getFinish() {
            return finish;
        }

        public void setFinish(LocalDateTime finish) {
            this.finish = finish;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            TimeInterval timeInterval = (TimeInterval) o;
            return this.start == timeInterval.start &&
                    this.finish == timeInterval.finish;
        }

        @Override
        public int hashCode() {
            return Objects.hash(17, start, finish);
        }
    }
}
