package View.EditDialog;

import Model.Foreman;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public
    class EditForemanDialog
    extends JDialog {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JTextField loginField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditForemanDialog(JFrame editForemanFrame, CentralPanel cp, Foreman foreman) {
        super(editForemanFrame, "Edytuj brygadzistę", true);
        setLayout( new GridLayout(6, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(editForemanFrame);

        firstNameField.setText(foreman.getFirstName());
        lastNameField.setText(foreman.getLastName());
        birthDateField.setText(foreman.getBirthDate().toString());
        loginField.setText(foreman.getLogin());
        passwordField.setText(foreman.getPassword());

        this.add( new JLabel("Imię:")); this.add(firstNameField);
        this.add( new JLabel("Nazwisko:")); this.add(lastNameField);
        this.add( new JLabel("Data urodzenia (yyyy-mm-dd):")); this.add(birthDateField);
        this.add( new JLabel("Login:")); this.add(loginField);
        this.add( new JLabel("Hasło:")); this.add(passwordField);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                foreman.setFirstName(firstNameField.getText().trim());
                foreman.setLastName(lastNameField.getText().trim());
                foreman.setBirthDate(LocalDate.parse(birthDateField.getText().trim()));
                foreman.setLogin(loginField.getText().trim());
                foreman.setPassword(passwordField.getText().trim());

                cp.updateTable("Brygadzista", cp.getForemanData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
