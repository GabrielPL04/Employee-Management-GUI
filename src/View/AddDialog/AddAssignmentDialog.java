package View.AddDialog;

import Model.*;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class AddAssignmentDialog
    extends JDialog {

    private final JComboBox<Assignment.Type> typeCombo = new JComboBox<>(Assignment.Type.values());
    private JComboBox<String> teamCombo;
    private JButton addButton = new JButton("Dodaj");

    public AddAssignmentDialog(JFrame addAssignmentFrame, CentralPanel cp) {
        super(addAssignmentFrame, "Dodaj zlecenie", true);
        this.setSize(480, 230);
        this.setLayout( new GridLayout(3, 2, 10, 10));
        this.setLocationRelativeTo(addAssignmentFrame);

        teamCombo = new JComboBox<>(
                Employee.getAllEmployees().values().stream()
                        .filter(e -> e instanceof Foreman)
                        .flatMap(e -> ((Foreman) e).getTeams().stream())
                        .map(Object::toString)
                        .toArray(String[]::new)
        );

        this.add( new JLabel("Typ zlecenia:")); this.add(typeCombo);
        this.add( new JLabel("Brygada:")); this.add(teamCombo);
        this.add( new JLabel()); this.add(addButton);

        addButton.addActionListener(e -> {
            Assignment.Type type = (Assignment.Type) typeCombo.getSelectedItem();

            Team selectedTeam = Employee.getAllEmployees().values().stream()
                    .filter(emp -> emp instanceof Foreman)
                    .flatMap(emp -> ((Foreman) emp).getTeams().stream())
                    .filter(t -> t.toString().equals(teamCombo.getSelectedItem()))
                    .findFirst().orElse(null);

            if (selectedTeam != null && type != null) {
                boolean planned = (type == Assignment.Type.PLANNED);
                new Assignment(planned, selectedTeam);

                JOptionPane.showMessageDialog(this, "Zlecenie dodane.");
                dispose();
                cp.updateTable("Zlecenie", cp.getAssignmentData());

            } else {
                JOptionPane.showMessageDialog(this, "Nie wybrano danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}