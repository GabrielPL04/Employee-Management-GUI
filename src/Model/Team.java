package Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public
    class Team
    implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Set<Team> allTeams = new HashSet<>();
    private final Set<Employee> employees;

    private static int teamCounter = 1;
    private final int id;
    private String name;
    private final Foreman foreman;

    public Team(String name, Foreman foreman) {
        this.id = teamCounter++;
        this.name = name;
        this.foreman = foreman;
        this.employees = new HashSet<>();
        this.employees.add(foreman);
        allTeams.add(this);
    }

    public void addEmployee(Employee employee) {
        if(employee instanceof User && !(employee instanceof Foreman)) {
            System.err.println("We cannot add the User to the Team!");
            return;
        }
        employees.add(employee);
    }

    public void addEmployee(Set<Employee> employees) {
        this.employees.addAll(employees);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public static Set<Team> getAllTeams() {
        return allTeams;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public static void resetCounter() {
        teamCounter = 1;
    }

    public static void setCounter(int value) {
        teamCounter = value;
    }

    @Override
    public String toString() {
        return "Team ID: (" + this.id + ") | Team Name: " + this.name +
                " | Foreman: " + this.foreman + " | EmployeesCount: " + employees.size();
    }
}
