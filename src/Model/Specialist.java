package Model;

import java.time.LocalDate;

public
    class Specialist
    extends Employee {

    private static final long serialVersionUID = 1L;

    private String specialization;

    public Specialist(String firstName, String lastName, LocalDate birthDate, Department department, String specialization) {
        super(firstName, lastName, birthDate, department);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return super.toString() + " | Specialization: " + this.specialization;
    }
}
