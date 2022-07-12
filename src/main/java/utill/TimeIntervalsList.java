package utill;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class TimeIntervalsList implements Serializable {
    private final TreeSet<TimeInterval> timeIntervals;

    public TimeIntervalsList() {
        this.timeIntervals = new TreeSet<>();
    }

    public boolean add(LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        TimeInterval newTimeInterval = createTimeInterval(startDateTime, finishDateTime);
        if (!timeIntervals.contains(newTimeInterval)) {
            timeIntervals.add(newTimeInterval);
            return true;
        } else {
            return false;
        }
    }

    private TimeInterval createTimeInterval(LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        return new TimeInterval(startDateTime, finishDateTime);
    }

    public void remove(LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        TimeInterval removedInterval = new TimeInterval(startDateTime, finishDateTime);
        timeIntervals.remove(removedInterval);
    }

    /**
     * The TimeInterval class is not suitable for use in hash tables and other structures that use a hash code.
     * This class is intended for storage only in structures: red-black tree.
     * For this class, it is considered that if the time intervals overlap each other, then they are equal.
     *
     * An important comparison condition: if, when comparing, the starting values of two time limits are equal
     * to LocalDateTime.MAX, then these two limits are not equal to each other. The first specified interval under such
     * conditions will always be greater than the second interval.
     */

    private class TimeInterval implements Serializable, Comparable<TimeInterval> {
        private final long serialVersionUID = 1L;
        private LocalDateTime start;
        private LocalDateTime finish;

        private TimeInterval(LocalDateTime start, LocalDateTime finish) {
            this.start = start;
            this.finish = finish;
        }

        @Override
        public int compareTo(TimeInterval anotherTimeInterval) {
            if (this.isInsideInterval(anotherTimeInterval)) {
                return 0;
            }
            if (start.equals(LocalDateTime.MAX) && anotherTimeInterval.start.equals(LocalDateTime.MAX)) {
                return -1;
            }
            return this.start.compareTo(anotherTimeInterval.start);
        }

        private boolean isInsideInterval(TimeInterval anotherTimeInterval) {
            if (start.equals(LocalDateTime.MAX) && anotherTimeInterval.start.equals(LocalDateTime.MAX)) {
                return false;
            }
            if (this.finish.isBefore(anotherTimeInterval.start)) {
                return false;
            }
            if (this.start.isAfter(anotherTimeInterval.finish)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            TimeInterval timeInterval = (TimeInterval) o;
            return this.isInsideInterval(timeInterval);
        }
    }
}
