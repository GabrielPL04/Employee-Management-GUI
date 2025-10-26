package View.AddDialog;

import Model.Assignment;
import Model.Work;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class AddWorkDialog
    extends JDialog {

    private JComboBox<Work.workType> typeCombo = new JComboBox<>(Work.workType.values());
    private JTextField timeField = new JTextField();
    private JTextField descriptionField = new JTextField();
    private JComboBox<Assignment> assignmentCombo;
    private JButton addButton = new JButton("Dodaj");

    public AddWorkDialog(JFrame addWorkFrame, CentralPanel cp) {
        super(addWorkFrame, "Dodaj pracę", true);
        this.setSize(490, 230);
        this.setLayout( new GridLayout(5, 2, 10, 10));
        this.setLocationRelativeTo(addWorkFrame);

        assignmentCombo = new JComboBox<>(
                Assignment.getAssignmentsMap().values().toArray(new Assignment[0])
        );

        this.add( new JLabel("Typ pracy:")); this.add(typeCombo);
        this.add( new JLabel("Czas trwania (s):")); this.add(timeField);
        this.add( new JLabel("Opis:")); this.add(descriptionField);
        this.add( new JLabel("Zlecenie:")); this.add(assignmentCombo);
        this.add( new JLabel()); this.add(addButton);

        addButton.addActionListener(e -> {

            try {

                Work.workType type = (Work.workType) typeCombo.getSelectedItem();
                int time = Integer.parseInt(timeField.getText().trim());
                String desc = descriptionField.getText().trim();

                Work newWork = new Work(type, time, desc, null);

                Assignment selectedAssignment = (Assignment) assignmentCombo.getSelectedItem();
                if (selectedAssignment != null) {
                    selectedAssignment.addWork(newWork);
                }

                JOptionPane.showMessageDialog(this, "Praca dodana.");
                dispose();
                cp.updateTable("Praca", cp.getWorkData());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
