package View;

import Model.User;
import View.AddDialog.RegisterUserDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
    class LoginFrame
    extends JFrame
    implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton signInButton, resetButton, signUpButton;

    public LoginFrame() {
        super("Logowanie");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout( new BorderLayout());

        JLabel titleLabel = new JLabel("Zaloguj się do systemu", SwingConstants.CENTER);
        titleLabel.setFont( new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout( new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel loginRow = new JPanel( new BorderLayout(10, 0));
        JLabel loginLabel = new JLabel("Login:");
        usernameField = new JTextField();
        loginRow.add(loginLabel, BorderLayout.WEST);
        loginRow.add(usernameField, BorderLayout.CENTER);

        JPanel passRow = new JPanel( new BorderLayout(10, 0));
        JLabel passLabel = new JLabel("Hasło:");
        passwordField = new JPasswordField();
        passRow.add(passLabel, BorderLayout.WEST);
        passRow.add(passwordField, BorderLayout.CENTER);

        JPanel showPassRow = new JPanel( new FlowLayout(FlowLayout.LEFT, 0, 0));
        showPassRow.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0));
        showPassword = new JCheckBox("Pokaż hasło");
        showPassword.addActionListener(this);
        showPassRow.add(showPassword);

        centerPanel.add(loginRow);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(passRow);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(showPassRow);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        JPanel row1 = new JPanel( new FlowLayout(FlowLayout.CENTER, 20, 0));
        signInButton = new JButton("Zaloguj");
        resetButton = new JButton("Wyczyść");
        signInButton.addActionListener(this);
        resetButton.addActionListener(this);
        row1.add(signInButton);
        row1.add(resetButton);

        JPanel row2 = new JPanel( new FlowLayout(FlowLayout.CENTER));
        signUpButton = new JButton("Rejestracja");
        signUpButton.addActionListener(this);
        row2.add(signUpButton);

        buttonPanel.add(row1);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(row2);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == showPassword) {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*');
        }

        if (source == signInButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            User user = findUser(username, password);
            if (user != null) {
                dispose();
                new MyFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Nieprawidłowy login lub hasło!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (source == resetButton) {
            usernameField.setText("");
            passwordField.setText("");
        }

        if (source == signUpButton) {
            new RegisterUserDialog(this).setVisible(true);
        }
    }

    private User findUser(String login, String password) {
        return Model.Employee.getAllEmployees().values().stream()
                .filter(e -> e instanceof User u && u.getLogin().equals(login) && u.getPassword().equals(password))
                .map(e -> (User) e)
                .findFirst()
                .orElse(null);
    }
}
