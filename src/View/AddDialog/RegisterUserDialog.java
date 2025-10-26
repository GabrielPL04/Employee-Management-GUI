package View.AddDialog;

import Model.Employee;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public
    class RegisterUserDialog
    extends JDialog
    implements ActionListener {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JTextField loginField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton registerButton = new JButton("Zarejestruj");

    public RegisterUserDialog(JFrame registerUserFrame) {
        super(registerUserFrame, "Rejestracja użytkownika", true);
        setSize(370, 270);
        setLayout( new GridLayout(6, 2, 10, 10));
        setLocationRelativeTo(registerUserFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.add( new JLabel("Imię:")); this.add(firstNameField);
        this.add( new JLabel("Nazwisko:")); this.add(lastNameField);
        this.add( new JLabel("Data urodzenia (yyyy-mm-dd):")); this.add(birthDateField);
        this.add( new JLabel("Login:")); this.add(loginField);
        this.add( new JLabel("Hasło:")); this.add(passwordField);
        this.add( new JLabel()); this.add(registerButton);

        registerButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String login = loginField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String birthDateStr = birthDateField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty() || password.isEmpty() || birthDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowy format daty. Użyj yyyy-mm-dd.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginExists = Employee.getAllEmployees().values().stream()
                    .filter(emp -> emp instanceof User)
                    .map(emp -> (User) emp)
                    .anyMatch(user -> user.getLogin().equals(login));

            if (loginExists) {
                JOptionPane.showMessageDialog(this, "Podany login już istnieje. Wybierz inny.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new User(firstName, lastName, birthDate, login, password);
            JOptionPane.showMessageDialog(this, "Rejestracja zakończona sukcesem!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }
}
