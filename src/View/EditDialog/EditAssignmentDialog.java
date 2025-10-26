package View.EditDialog;

import Model.Assignment;
import Model.Employee;
import Model.Foreman;
import Model.Team;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class EditAssignmentDialog
    extends JDialog {

    private final JComboBox<Assignment.Type> typeCombo = new JComboBox<>(Assignment.Type.values());
    private final JComboBox<String> teamCombo;
    private final JButton saveButton = new JButton("Zapisz");

    public EditAssignmentDialog(JFrame editAssignmentFrame, CentralPanel cp, Assignment assignment) {
        super(editAssignmentFrame, "Edytuj zlecenie", true);
        setLayout( new GridLayout(3, 2, 10, 10));
        setSize(480, 230);
        setLocationRelativeTo(editAssignmentFrame);

        teamCombo = new JComboBox<>(
                Employee.getAllEmployees().values().stream()
                        .filter(e -> e instanceof Foreman)
                        .flatMap(e -> ((Foreman) e).getTeams().stream())
                        .map(Team::toString)
                        .toArray(String[]::new)
        );

        typeCombo.setSelectedItem(assignment.getType());
        if (assignment.getTeam() != null) {
            teamCombo.setSelectedItem(assignment.getTeam().toString());
        }

        this.add( new JLabel("Typ zlecenia:")); this.add(typeCombo);
        this.add( new JLabel("Brygada:")); this.add(teamCombo);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                Assignment.Type type = (Assignment.Type) typeCombo.getSelectedItem();
                assignment.setPlanned(type == Assignment.Type.PLANNED);

                Team selectedTeam = Employee.getAllEmployees().values().stream()
                        .filter(emp -> emp instanceof Foreman)
                        .flatMap(emp -> ((Foreman) emp).getTeams().stream())
                        .filter(t -> t.toString().equals(teamCombo.getSelectedItem()))
                        .findFirst().orElse(null);

                if (selectedTeam != null) {
                    assignment.setTeam(selectedTeam);
                }

                cp.updateTable("Zlecenie", cp.getAssignmentData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
