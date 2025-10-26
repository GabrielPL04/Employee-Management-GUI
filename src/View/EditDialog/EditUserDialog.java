package View.EditDialog;

import Model.User;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public
    class EditUserDialog
    extends JDialog {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JTextField loginField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditUserDialog(JFrame editUserFrame, CentralPanel cp, User user) {
        super(editUserFrame, "Edytuj użytkownika", true);
        setLayout( new GridLayout(6, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(editUserFrame);

        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        birthDateField.setText(user.getBirthDate().toString());
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword());

        this.add( new JLabel("Imię:")); this.add(firstNameField);
        this.add( new JLabel("Nazwisko:")); this.add(lastNameField);
        this.add( new JLabel("Data urodzenia (yyyy-mm-dd):")); this.add(birthDateField);
        this.add( new JLabel("Login:")); this.add(loginField);
        this.add( new JLabel("Hasło:")); this.add(passwordField);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                user.setFirstName(firstNameField.getText().trim());
                user.setLastName(lastNameField.getText().trim());
                user.setBirthDate(LocalDate.parse(birthDateField.getText().trim()));
                user.setLogin(loginField.getText().trim());
                user.setPassword(passwordField.getText().trim());

                cp.updateTable("Użytkownik", cp.getUserData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
