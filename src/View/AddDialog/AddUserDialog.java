package View.AddDialog;

import Model.*;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public
    class AddUserDialog
    extends JDialog {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JTextField loginField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JComboBox<String> departmentCombo;
    private final JButton addButton = new JButton("Dodaj");

    public AddUserDialog(JFrame addUserFrame, CentralPanel cp) {
        super(addUserFrame, "Dodaj użytkownika", true);
        setSize(400, 300);
        setLayout( new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(addUserFrame);

        departmentCombo = new JComboBox<>(
                Department.getAllDepartments().stream()
                        .map(Department::getDepartmentName)
                        .toArray(String[]::new)
        );

        add( new JLabel("Imię:")); this.add(firstNameField);
        add( new JLabel("Nazwisko:")); this.add(lastNameField);
        add( new JLabel("Data urodzenia (yyyy-mm-dd):")); this.add(birthDateField);
        add( new JLabel("Login:")); this.add(loginField);
        add( new JLabel("Hasło:")); this.add(passwordField);
        add( new JLabel("Dział:")); this.add(departmentCombo);
        add( new JLabel()); this.add(addButton);

        addButton.addActionListener((ActionEvent e) -> {

            try {

                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                LocalDate birthDate = LocalDate.parse(birthDateField.getText().trim());
                String login = loginField.getText().trim();
                String password = passwordField.getText().trim();
                String deptName = (String) departmentCombo.getSelectedItem();

                Department department = Department.getAllDepartments().stream()
                        .filter(d -> d.getDepartmentName().equals(deptName))
                        .findFirst().orElseThrow();

                new User(firstName, lastName, birthDate, department, login, password);
                JOptionPane.showMessageDialog(this, "Użytkownik dodany.");
                dispose();
                cp.updateTable("Użytkownik", cp.getUserData());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
