package entitys;

public class Subtask extends Task {
    private static long amountId = 1;
    private long epicsId;

    public Subtask(String name, String description, long epicsId) {
        super(name, description);
        this.id = amountId++;
        this.epicsId = epicsId;
    }

    public static long getNewId() {
        return ++amountId;
    }

    public long getEpicsId() {
        return epicsId;
    }
}
