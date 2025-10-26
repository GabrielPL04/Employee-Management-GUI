package View.AddDialog;

import Model.*;
import View.CentralPanel;

import javax.swing.*;
import java.awt.*;

public
    class AddDepartmentDialog
    extends JDialog {

    private JTextField nameField;
    private JButton addButton;
    private CentralPanel centralPanel;

    public AddDepartmentDialog(JFrame addDepartmentFrame, CentralPanel cp) {
        super(addDepartmentFrame, "Dodaj dział", true);
        this.centralPanel = cp;

        this.setLayout( new GridLayout(2, 2, 10, 10));
        this.setSize(400, 150);
        this.setLocationRelativeTo(addDepartmentFrame);

        nameField = new JTextField();
        addButton = new JButton("Dodaj");

        this.add( new JLabel("Nazwa działu:")); this.add(nameField);
        this.add( new JLabel()); this.add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa nie może być pusta.");
                return;
            }

            try {

                Department.createDepartment(name);
                centralPanel.updateTable("Dział pracowników", centralPanel.getDepartmentData());
                dispose();

            } catch (NotUniqueNameException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
