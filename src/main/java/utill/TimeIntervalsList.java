package utill;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class TimeIntervalsList implements Serializable {
    private final long serialVersionUID = 1L;
    private final int INDEX_FIRST_ELEMENT = 0;
    private final int INDEX_SECOND_ELEMENT = 1;
    private final int INDEX_THIRD_ELEMENT = 2;
    private final List<TimeInterval> timeIntervals;
    private final TreeSet<TimeInterval> timeIntervalsSet;

    public TimeIntervalsList() {
        this.timeIntervals = new ArrayList<>();
        this.timeIntervalsSet = new TreeSet<>();
    }

    public TimeInterval createTimeInterval(LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        return new TimeInterval(startDateTime, finishDateTime);
    }

    public boolean add(TimeInterval newTimeInterval) {
        if (!timeIntervals.contains(newTimeInterval)) {
            timeIntervals.add(newTimeInterval);
            return true;
        } else {
            return false;
        }
//        switch (timeIntervals.size()) {
//            case (0):
//                timeIntervals.add(newTimeInterval);
//                return true;
//            case (1):
//                return this.insertForOneElement(newTimeInterval);
//            case (2):
//                return this.insertForTwoElement(newTimeInterval);
//            case (3):
//                return this.insertForThreeElement(newTimeInterval);
//            default:
//                return this.insertMoreThanThreeElement(newTimeInterval);
//        }
    }

    private boolean insertForOneElement(TimeInterval newTimeInterval) {
        TimeInterval singleInterval = timeIntervals.get(INDEX_FIRST_ELEMENT);
        if (singleInterval.isInsideInterval(newTimeInterval)) {
            return false;
        } else {
            if (singleInterval.isBefore(newTimeInterval)) timeIntervals.add(newTimeInterval);
            if (singleInterval.isAfter(newTimeInterval)) timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
            return true;
        }
    }

    private boolean insertForTwoElement(TimeInterval newTimeInterval) {
        if (timeIntervals.get(INDEX_FIRST_ELEMENT).isInsideInterval(newTimeInterval)
                || timeIntervals.get(INDEX_SECOND_ELEMENT).isInsideInterval(newTimeInterval)) {
            return false;
        }
        if (timeIntervals.get(INDEX_FIRST_ELEMENT).isAfter(newTimeInterval)) {
            timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
            return true;
        }
        if (timeIntervals.get(INDEX_SECOND_ELEMENT).isBefore(newTimeInterval))  {
            timeIntervals.add(newTimeInterval);
            return true;
        }
        timeIntervals.add(INDEX_SECOND_ELEMENT, newTimeInterval);
        return  true;
    }

    private boolean insertForThreeElement(TimeInterval newTimeInterval) {
        if (timeIntervals.get(INDEX_FIRST_ELEMENT).isInsideInterval(newTimeInterval)
                || timeIntervals.get(INDEX_SECOND_ELEMENT).isInsideInterval(newTimeInterval)
                || timeIntervals.get(INDEX_THIRD_ELEMENT).isInsideInterval(newTimeInterval)) {
            return false;
        }
        if (timeIntervals.get(INDEX_FIRST_ELEMENT).isAfter(newTimeInterval)) {
            timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
            return true;
        }
        if (timeIntervals.get(INDEX_THIRD_ELEMENT).isBefore(newTimeInterval))  {
            timeIntervals.add(newTimeInterval);
            return true;
        }
        if (timeIntervals.get(INDEX_FIRST_ELEMENT).isAfter(newTimeInterval)
                && timeIntervals.get(INDEX_SECOND_ELEMENT).isAfter(newTimeInterval)) {
            timeIntervals.add(INDEX_SECOND_ELEMENT, newTimeInterval);
        }
        timeIntervals.add(INDEX_THIRD_ELEMENT, newTimeInterval);
        return  true;
    }

    private boolean insertMoreThanThreeElement(TimeInterval newTimeInterval) {
        CollectionMovement collectionMovement = null;
        int middleOfList = timeIntervals.size() / 2;
        int numberIteration = 1;
        TimeInterval middleElement;
        while (true) {
            middleElement =  timeIntervals.get(middleOfList);
            if (middleElement.isAfter(newTimeInterval)) {
                if (collectionMovement == CollectionMovement.DOWN_MOVEMENT) {
                    timeIntervals.add(middleOfList, newTimeInterval);
                    return true;
                } else {
                    middleOfList -= middleOfList / 2;
                    if (middleOfList == INDEX_SECOND_ELEMENT && middleElement.isAfter(newTimeInterval)) {
                        timeIntervals.add(INDEX_FIRST_ELEMENT, newTimeInterval);
                        return true;
                    }
                    if (middleOfList == INDEX_SECOND_ELEMENT && middleElement.isBefore(newTimeInterval)) {
                        timeIntervals.add(INDEX_SECOND_ELEMENT, newTimeInterval);
                        return true;
                    }
                    collectionMovement = CollectionMovement.UP_MOVEMENT;
                    continue;
                }
            }
            if (middleElement.isBefore(newTimeInterval)) {
                if (collectionMovement == CollectionMovement.UP_MOVEMENT) {
                    timeIntervals.add(middleOfList, newTimeInterval);
                    return true;
                } else {
                    middleOfList += middleOfList / 2; //TODO переделать, неверны коэффициент
                    if (middleOfList == (timeIntervals.size() - 1) && middleElement.isAfter(newTimeInterval)) {
                        timeIntervals.add(timeIntervals.size() - 1, newTimeInterval);
                        return true;
                    }
                    if (middleOfList == (timeIntervals.size() - 1) && middleElement.isBefore(newTimeInterval)) {
                        timeIntervals.add(newTimeInterval);
                        return true;
                    }
                    collectionMovement = CollectionMovement.DOWN_MOVEMENT;
                    continue;
                }
            }
        }
    }

    enum CollectionMovement{
        UP_MOVEMENT,
        DOWN_MOVEMENT
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

        @Override
        public int compareTo(TimeInterval anotherTimeInterval) {
            if (this.isInsideInterval(anotherTimeInterval)) {
                return 0;
            }
            return this.start.compareTo(anotherTimeInterval.start);
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
            return this.isInsideInterval(timeInterval);
        }

        @Override
        public int hashCode() {
            return Objects.hash(17, start, finish);
        }
    }
}
