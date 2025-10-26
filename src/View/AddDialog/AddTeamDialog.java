package View.AddDialog;

import Model.*;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class AddTeamDialog
    extends JDialog {

    private JTextField nameField = new JTextField();
    private JComboBox<String> foremanCombo;
    private JButton addButton = new JButton("Dodaj");

    public AddTeamDialog(JFrame addTeamFrame, CentralPanel cp) {
        super(addTeamFrame, "Dodaj brygadę", true);
        setSize(480, 230);
        setLayout( new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(addTeamFrame);

        foremanCombo = new JComboBox<>(
                Employee.getAllEmployees().values().stream()
                        .filter(e -> e instanceof Foreman)
                        .map(Object::toString)
                        .toArray(String[]::new)
        );

        this.add( new JLabel("Nazwa brygady:")); this.add(nameField);
        this.add( new JLabel("Brygadzista:")); this.add(foremanCombo);
        this.add( new JLabel()); this.add(addButton);

        addButton.addActionListener(e -> {

            try {

                String name = nameField.getText().trim();
                if (name.isEmpty()) throw new Exception("Nazwa brygady nie może być pusta.");

                Foreman foreman = (Foreman) Employee.getAllEmployees().values().stream()
                        .filter(emp -> emp instanceof Foreman)
                        .filter(emp -> emp.toString().equals(foremanCombo.getSelectedItem()))
                        .findFirst()
                        .orElseThrow();

                Team team = new Team(name, foreman);
                foreman.getTeams().add(team);

                JOptionPane.showMessageDialog(this, "Brygada dodana.");
                dispose();
                cp.updateTable("Brygada", cp.getTeamData());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
