package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public
    class Foreman
    extends User {

    private static final long serialVersionUID = 1L;

    private final List<Team> teams = new ArrayList<>();
    private final List<Assignment> assignmentHistory = new ArrayList<>();

    public Foreman(String firstName, String lastName, LocalDate birthDate, Department department, String login, String password) {
        super(firstName, lastName, birthDate, department, login, password);
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    public List<Assignment> getAssignmentHistory() {
        return this.assignmentHistory;
    }

    @Override
    public String toString() {
        return super.toString() + " | TeamsCount: " + this.teams.size();
    }
}
