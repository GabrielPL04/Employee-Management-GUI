package View.AddDialog;

import Model.*;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public
    class AddEmployeeDialog
    extends JDialog {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JComboBox<String> departmentCombo;
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"User", "Specialist", "Foreman"});
    private final JTextField loginField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JTextField specializationField = new JTextField();

    private final JButton addButton = new JButton("Dodaj");
    private final CentralPanel centralPanel;

    public AddEmployeeDialog(JFrame addEmployeeFrame, CentralPanel cp) {
        super(addEmployeeFrame, "Dodaj pracownika", true);
        this.centralPanel = cp;

        List<String> departmentNames = Department.getAllDepartments().stream()
                .map(Department::getDepartmentName)
                .collect(Collectors.toList());

        departmentCombo = new JComboBox<>(departmentNames.toArray(new String[0]));

        this.setSize(500, 400);
        this.setLocationRelativeTo(addEmployeeFrame);
        this.setLayout( new GridLayout(9, 2, 10, 10));

        this.addRow("Imię:", this.firstNameField);
        this.addRow("Nazwisko:", this.lastNameField);
        this.addRow("Data urodzenia (yyyy-mm-dd):", this.birthDateField);
        this.addRow("Dział:", this.departmentCombo);
        this.addRow("Typ pracownika:", this.typeCombo);
        this.addRow("Login:", this.loginField);
        this.addRow("Hasło:", this.passwordField);
        this.addRow("Specjalizacja (dla Specialist):", this.specializationField);

        this.add( new JLabel());
        this.add(addButton);

        addButton.addActionListener(this::handleAdd);
    }

    private void addRow(String labelText, JComponent field) {
        this.add( new JLabel(labelText));
        this.add(field);
    }

    private List<String> getAllDepartmentNames() {
        return Department.getAllDepartments().stream()
                .map(Department::getDepartmentName)
                .toList();
    }

    private void handleAdd(ActionEvent e) {
        try {

            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String birthDateStr = birthDateField.getText().trim();
            String selectedDeptName = (String) departmentCombo.getSelectedItem();

            if (firstName.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty()
                    || selectedDeptName == null || selectedDeptName.isEmpty()) {
                throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
            }

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateStr);
            } catch (Exception parseEx) {
                throw new IllegalArgumentException("Data urodzenia musi być w formacie yyyy-mm-dd.");
            }

            Department department = Department.getAllDepartments().stream()
                    .filter(d -> d.getDepartmentName().equals(selectedDeptName))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wybranego działu."));

            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            String specialization = specializationField.getText().trim();

            String type = (String) typeCombo.getSelectedItem();
            switch (type) {
                case "User" -> {
                    if (login.isEmpty() || password.isEmpty())
                        throw new IllegalArgumentException("Login i hasło są wymagane dla typu User.");
                    new User(firstName, lastName, birthDate, department, login, password);
                }
                case "Specialist" -> {
                    if (specialization.isEmpty())
                        throw new IllegalArgumentException("Specjalizacja jest wymagana dla typu Specialist.");
                    new Specialist(firstName, lastName, birthDate, department, specialization);
                }
                case "Foreman" -> {
                    if (login.isEmpty() || password.isEmpty())
                        throw new IllegalArgumentException("Login i hasło są wymagane dla typu Foreman.");
                    new Foreman(firstName, lastName, birthDate, department, login, password);
                }
            }

            centralPanel.updateTable("Pracownik", centralPanel.getEmployeeData());
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd danych", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd systemowy: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
