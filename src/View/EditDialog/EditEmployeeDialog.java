package View.EditDialog;

import Model.Employee;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public
    class EditEmployeeDialog
    extends JDialog {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditEmployeeDialog(JFrame editEmployeeFrame, CentralPanel cp, Employee emp) {
        super(editEmployeeFrame, "Edytuj pracownika", true);
        setLayout( new GridLayout(4, 2, 10, 10));
        setSize(400, 200);
        setLocationRelativeTo(editEmployeeFrame);

        firstNameField.setText(emp.getFirstName());
        lastNameField.setText(emp.getLastName());
        birthDateField.setText(emp.getBirthDate().toString());

        this.add( new JLabel("Imię:")); this.add(firstNameField);
        this.add( new JLabel("Nazwisko:")); this.add(lastNameField);
        this.add( new JLabel("Data urodzenia (yyyy-mm-dd):")); this.add(birthDateField);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                emp.setFirstName(firstNameField.getText().trim());
                emp.setLastName(lastNameField.getText().trim());
                emp.setBirthDate(LocalDate.parse(birthDateField.getText().trim()));

                cp.updateTable("Pracownik", cp.getEmployeeData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd danych: " + ex.getMessage());
            }
        });
    }
}
