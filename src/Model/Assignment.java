package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public
    class Assignment
    implements Runnable, Serializable {

public
    enum Type {
        PLANNED, UNPLANNED
    }

    private static final long serialVersionUID = 1L;

    private final static Map<Integer, Assignment> assignmentsMap = new HashMap<>();
    private final Queue<String> tasks = new LinkedList<>();
    private final List<Work> works = new ArrayList<>();

    private static int assignmentCounter = 1;
    private final int id;
    private Type type;
    private Team team;
    private final LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Assignment(boolean planned) {
        this.type = planned ? Type.PLANNED : Type.UNPLANNED;
        this.id = assignmentCounter++;
        this.creationDate = LocalDateTime.now();
        assignmentsMap.put(this.id, this);
    }

    public Assignment(boolean planned, Team team) {
        this(planned);
        this.team = team;
    }

    public Assignment(boolean planned, Queue<String> tasks) {
        this(planned);
        this.tasks.addAll(tasks);
    }

    public Assignment(boolean planned, Queue<String> tasks, Team team) {
        this(planned, team);
        this.tasks.addAll(tasks);
    }

    public void addWork(Work work) {
        if (work != null && !works.contains(work)) {
            works.add(work);
        }
    }

    public List<Work> getWorks() {
        return new ArrayList<>(works);
    }

    public int getAssignmentId() {
        return this.id;
    }

    public static synchronized Assignment getAssignmentById(int id) {
        return assignmentsMap.get(id);
    }

    public static void resetCounter() {
        assignmentCounter = 1;
    }

    public static void setCounter(int value) {
        assignmentCounter = value;
    }

    public Assignment.Type getType() {
        return type;
    }

    public void setPlanned(boolean planned) {
        this.type = planned ? Type.PLANNED : Type.UNPLANNED;
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    public String getStatus() {
        if(endDate != null) return "Completed";
        if(startDate != null) return "Started";
        return "Created";
    }

    public static Map<Integer, Assignment> getAssignmentsMap() {
        return assignmentsMap;
    }

    @Override
    public void run() {
        if(team == null || tasks.isEmpty()) {
            System.out.println("Cannot start assignment - missing team or tasks.");
            return;
        }

        for(Employee e : team.getEmployees()) {
            if(e.isBusy()) {
                System.err.println("Assignment cannot be completed - the Employee is busy: " + e);
                return;
            }
        }

        team.getEmployees().forEach((e) -> e.setBusy(true));
        this.startDate = LocalDateTime.now();

        System.out.println("Starting Assignment...");
        while(!tasks.isEmpty()) {
            String task = tasks.poll();
            System.out.println("Executing task..." + task);
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Assignment ID: (" + this.id + ") was interrupted.");
            }
        }
        this.endDate = LocalDateTime.now();
        team.getEmployees().forEach((e) -> e.setBusy(false));
        System.out.println("The Assignment has been completed");
    }

    @Override
    public String toString() {
        return "Assignment ID: (" + this.id + ") | Status: " + getStatus();
    }
}
