package View.AddDialog;

import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
    class ChangePasswordDialog
    extends JDialog {

    private final JPasswordField oldPasswordField = new JPasswordField();
    private final JPasswordField newPasswordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JButton changePasswordButton = new JButton("Zmień hasło");
    private final JButton cancelButton = new JButton("Anuluj");

    private final User loggedUser;

    public ChangePasswordDialog(JFrame changePasswordFrame, User loggedUser) {
        super(changePasswordFrame, "Zmień hasło", true);
        this.loggedUser = loggedUser;

        setSize(350, 250);
        setLayout( new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(changePasswordFrame);

        this.add( new JLabel("Stare hasło:")); this.add(oldPasswordField);
        this.add( new JLabel("Nowe hasło:")); this.add(newPasswordField);
        this.add( new JLabel("Potwierdź nowe hasło:")); this.add(confirmPasswordField);

        this.add(changePasswordButton);
        this.add(cancelButton);

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword = new String(oldPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!oldPassword.equals(loggedUser.getPassword())) {
                    JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Stare hasło jest niepoprawne!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Nowe hasło i potwierdzenie nie pasują!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Hasło musi mieć co najmniej 6 znaków!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                loggedUser.setPassword(newPassword);

                JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Hasło zostało zmienione pomyślnie.");
                dispose();
            }
        });

        cancelButton.addActionListener((evt) -> {

            dispose();

        });
    }
}
