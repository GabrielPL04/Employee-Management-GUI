package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public
    abstract class Employee
    implements Comparable<Employee>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<Integer, Employee> allEmployees = new HashMap<>();

    private static int employeeCounter = 1;
    private final int id;
    protected String firstName;
    protected String lastName;
    protected LocalDate birthDate;
    public Department department;
    protected boolean busy = false;

    public Employee(String firstName, String lastName, LocalDate birthDate, Department department) {
        synchronized(allEmployees) {
            this.id = employeeCounter++;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.department = department;
            allEmployees.put(this.id, this);
        }

        if (department != null) {
            department.addEmployee(this);
        }
    }

    public static void addEmployee(Employee employee) {
        allEmployees.put(employee.getId(), employee);
    }

    public int getId() {
        return this.id;
    }

    public boolean isBusy() {
        return this.busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public static Map<Integer, Employee> getAllEmployees() {
        return allEmployees;
    }

    public Department getDepartment() {
        return this.department;
    }

    public static void resetCounter() {
        employeeCounter = 1;
    }

    public static void setCounter(int value) {
        employeeCounter = value;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int compareTo(Employee other) {
        int lastNameCompare = this.lastName.compareTo(other.lastName);
        return lastNameCompare != 0 ? lastNameCompare : this.firstName.compareTo(other.firstName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(birthDate, employee.birthDate) && Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, department);
    }

    @Override
    public String toString() {
        return "Employee ID: (" + this.id + ") | FirstName: " + this.firstName + " | LastName: " + this.lastName;
    }
}
