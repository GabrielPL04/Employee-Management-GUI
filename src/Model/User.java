package Model;

import java.time.LocalDate;

public
    class User
    extends Employee {

    private static final long serialVersionUID = 1L;

    private String login;
    private String password;
    private String initials;

    public User(String firstName, String lastName, LocalDate birthDate, Department department, String login, String password) {
        super(firstName, lastName, birthDate, department);
        this.login = login;
        this.password = password;
        updateInitials();
    }

    public User(String firstName, String lastName, LocalDate birthDate, String login, String password) {
        super(firstName, lastName, birthDate, null);
        this.login = login;
        this.password = password;
        updateInitials();
    }

    public String getInitials() {
        return this.initials;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
        updateInitials();
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
        updateInitials();
    }

    private void updateInitials() {
        if (getFirstName() != null && !getFirstName().isEmpty() &&
                getLastName() != null && !getLastName().isEmpty()) {
            this.initials = (getFirstName().charAt(0) + "." + getLastName().charAt(0)).toUpperCase();
        } else {
            this.initials = "";
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | Login: " + this.login + " | Initials: " + this.initials;
    }
}