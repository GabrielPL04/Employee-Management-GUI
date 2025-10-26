package View.EditDialog;

import Model.Team;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class EditTeamDialog
    extends JDialog {

    private final JTextField nameField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditTeamDialog(JFrame editTeamFrame, CentralPanel cp, Team team) {
        super(editTeamFrame, "Edytuj brygadę", true);
        setLayout( new GridLayout(2, 2, 10, 10));
        setSize(400, 150);
        setLocationRelativeTo(editTeamFrame);

        nameField.setText(team.getName());

        this.add( new JLabel("Nazwa brygady:")); this.add(nameField);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                team.setName(nameField.getText().trim());
                cp.updateTable("Brygada", cp.getTeamData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
