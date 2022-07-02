package utill;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class TimeIntervalsList implements Serializable {
    private final long serialVersionUID = 1L;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            TimeInterval timeInterval = (TimeInterval) o;
            return this.isInsideInterval(timeInterval);
        }
    }
}
