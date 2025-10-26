package View.EditDialog;

import Model.Work;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class EditWorkDialog
    extends JDialog {

    private final JComboBox<Work.workType> typeCombo = new JComboBox<>(Work.workType.values());
    private final JTextField timeField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditWorkDialog(JFrame editWorkFrame, CentralPanel cp, Work work) {
        super(editWorkFrame, "Edytuj pracę", true);
        setLayout( new GridLayout(4, 2, 10, 10));
        setSize(400, 200);
        setLocationRelativeTo(editWorkFrame);

        typeCombo.setSelectedItem(work.getType().name());
        timeField.setText(String.valueOf(work.getWorkTime()));
        descriptionField.setText(work.getDescription());

        this.add( new JLabel("Typ pracy:")); this.add(typeCombo);
        this.add( new JLabel("Czas trwania (s):")); this.add(timeField);
        this.add( new JLabel("Opis:")); this.add(descriptionField);
        this.add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                Work.workType type = (Work.workType) typeCombo.getSelectedItem();
                int time = Integer.parseInt(timeField.getText().trim());
                String desc = descriptionField.getText().trim();

                work.setType(type);
                work.setWorkTime(time);
                work.setDescription(desc);

                cp.updateTable("Praca", cp.getWorkData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
