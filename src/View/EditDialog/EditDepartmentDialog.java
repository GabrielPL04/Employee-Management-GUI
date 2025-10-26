package View.EditDialog;

import Model.Department;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class EditDepartmentDialog
    extends JDialog {

    private final JTextField nameField = new JTextField();
    private final JButton saveButton = new JButton("Zapisz");

    public EditDepartmentDialog(JFrame editDepartmentFrame, CentralPanel cp, Department dept) {
        super(editDepartmentFrame, "Edytuj dział", true);
        setLayout( new GridLayout(2, 2, 10, 10));
        setSize(400, 150);
        setLocationRelativeTo(editDepartmentFrame);

        nameField.setText(dept.getDepartmentName());

        add( new JLabel("Nazwa działu:")); this.add(nameField);
        add( new JLabel()); this.add(saveButton);

        saveButton.addActionListener(e -> {

            try {
                String newName = nameField.getText().trim();
                if (!newName.isEmpty()) {
                    dept.setDepartmentName(newName);
                }
                cp.updateTable("Dział pracowników", cp.getDepartmentData());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
            }
        });
    }
}
