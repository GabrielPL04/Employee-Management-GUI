package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public
    class Work
    extends Thread
    implements Serializable {

public
    enum workType {
        GENERAL, ASSEMBLY, DISASSEMBLY, REPLACEMENT
    }

    private static final long serialVersionUID = 1L;

    private static final Map<Integer, Work> workMap = new HashMap<>();
    private final List<Work> waitingFor;
    private final List<Work> startsAfterThis;

    private static int workCounter = 1;
    private final int id;
    private workType type;
    private int workTime;
    private boolean isCompleted;
    private String description;

    public Work(workType type, int workTime, String description, List<Work> waitingFor) {
        this.id = workCounter++;
        this.type = type;
        this.workTime = workTime;
        this.description = description;
        this.isCompleted = false;

        this.waitingFor = waitingFor != null ? waitingFor : new ArrayList<>();
        this.startsAfterThis = new ArrayList<>();
        workMap.put(this.id, this);
    }

    public int getWorkId() {
        return this.id;
    }

    public static synchronized Work getWorkById(int id) {
        return workMap.get(id);
    }

    public void addNextWork(Work work) {
        startsAfterThis.add(work);
    }

    public static void resetCounter() {
        workCounter = 1;
    }

    public static void setCounter(int value) {
        workCounter = value;
    }

    public Work.workType getType() {
        return type;
    }
    public void setType(Work.workType type) {
        this.type = type;
    }

    public int getWorkTime() {
        return workTime;
    }
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void run() {
        try {
            for(Work w : waitingFor) {
                w.join();
            }

            System.out.println(this);

            Thread.sleep(workTime * 1000L);
            this.isCompleted = true;

            System.out.print("[Work ID: (" + this.id + ") completed]\n");

            for(Work w : startsAfterThis) {
                w.start();
            }

        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Work ID: (" + this.id + ") was interrupted.");
        }
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public static Map<Integer, Work> getWorkMap() {
        return workMap;
    }

    @Override
    public String toString() {
        return "Work ID: (" + this.id + ")" + " | Type: " + this.type + " | Description: " + this.description +
                " | Work Time: " + this.workTime + "s " + " | Completed: " + this.isCompleted +
                " | Waiting For: " + this.waitingFor.size() + " | Starts After This: " + this.startsAfterThis.size();
    }
}
