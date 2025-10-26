package Model;

public
    class IDManager {

    public static void updateAllCounters() {
        updateAssignmentCounter();
        updateDepartmentCounter();
        updateEmployeeCounter();
        updateTeamCounter();
        updateWorkCounter();
    }

    private static void updateAssignmentCounter() {
        int maxId = Assignment.getAssignmentsMap().values().stream()
                .mapToInt(Assignment::getAssignmentId)
                .max().orElse(0);
        Assignment.setCounter(maxId + 1);
    }

    private static void updateDepartmentCounter() {
        int maxId = Department.getAllDepartments().stream()
                .mapToInt(Department::getId)
                .max().orElse(0);
        Department.setCounter(maxId + 1);
    }

    private static void updateEmployeeCounter() {
        int maxId = Employee.getAllEmployees().values().stream()
                .mapToInt(Employee::getId)
                .max().orElse(0);
        Employee.setCounter(maxId + 1);
    }

    private static void updateTeamCounter() {
        int maxId = Team.getAllTeams().stream()
                .mapToInt(Team::getId)
                .max().orElse(0);
        Team.setCounter(maxId + 1);
    }

    private static void updateWorkCounter() {
        int maxId = Work.getWorkMap().values().stream()
                .mapToInt(Work::getWorkId)
                .max().orElse(0);
        Work.setCounter(maxId + 1);
    }
}
