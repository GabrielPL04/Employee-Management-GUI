package Model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public
    class Department
    implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Set<Department> allDepartments = new HashSet<>();
    private static Set<String> departmentNames = new HashSet<>();
    private Set<Employee> employees;

    private static int departmentCounter = 1;
    private final int id;
    private String name;

    private Department(String name) throws NotUniqueNameException {
        if(departmentNames.contains(name)) {
            throw new NotUniqueNameException("Department name: (" + name + ") already exists!");
        }
        this.id = departmentCounter++;
        this.name = name;
        this.employees = new HashSet<>();

        departmentNames.add(name);
        allDepartments.add(this);
    }

    public static synchronized Department createDepartment(String name) throws NotUniqueNameException {
        return new Department(name);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public Set<Employee> getEmployees() {
        return Collections.unmodifiableSet(this.employees);
    }

    public String getDepartmentName() {
        return name;
    }

    public void setDepartmentName(String newName) throws NotUniqueNameException {
        if (newName == null || newName.trim().isEmpty()) return;

        newName = newName.trim();
        if (departmentNames.contains(newName) && !newName.equals(this.name)) {
            throw new NotUniqueNameException("Nazwa działu '" + newName + "' już istnieje.");
        }

        departmentNames.remove(this.name);
        departmentNames.add(newName);
        this.name = newName;
    }

    public int getId() {
        return this.id;
    }

    public void delete() {
        departmentNames.remove(this.name);
        allDepartments.remove(this);
    }

    public static void resetCounter() {
        departmentCounter = 1;
    }

    public static void setCounter(int value) {
        departmentCounter = value;
    }

    public static Set<Department> getAllDepartments() {
        return allDepartments;
    }

    @Override
    public String toString() {
        return "Department ID: (" + this.id + ") | Name: " + this.name + " | Employees: " + getEmployees().size();
    }
}
