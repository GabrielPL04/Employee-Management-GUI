package View;

import Controller.TableChangeListener;
import Controller.TableChangedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
    class LeftPanel
    extends JPanel
    implements ActionListener {

    private CentralPanel centralPanel;

    private JButton[] jButtons;
    private final String[] buttons = {
            "Dział pracowników",
            "Pracownik",
            "Użytkownik",
            "Brygadzista",
            "Brygada",
            "Zlecenie",
            "Praca",
            "Wyloguj"
    };

    public LeftPanel(CentralPanel cp) {
        this.centralPanel = cp;

        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize( new Dimension(160, 0));
        this.setLayout( new GridLayout(8, 1, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jButtons = new JButton[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            jButtons[i] = new RoundedButton(buttons[i]);
            jButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            jButtons[i].setMaximumSize( new Dimension(140, 40));
            jButtons[i].setFont( new Font("SansSerif", Font.BOLD, 11));
            jButtons[i].setForeground(Color.WHITE);
            jButtons[i].setBackground( new Color(25, 118, 210));
            jButtons[i].setFocusPainted(false);
            jButtons[i].addActionListener(this);
            this.add(jButtons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < jButtons.length; i++) {
            if (e.getSource() == jButtons[i]) {
                String selected = jButtons[i].getText();

                Object[][] data;
                String title = selected;

                switch (selected) {
                    case "Pracownik" -> data = centralPanel.getEmployeeData();
                    case "Dział pracowników" -> data = centralPanel.getDepartmentData();
                    case "Użytkownik" -> data = centralPanel.getUserData();
                    case "Brygadzista" -> data = centralPanel.getForemanData();
                    case "Brygada" -> data = centralPanel.getTeamData();
                    case "Zlecenie" -> data = centralPanel.getAssignmentData();
                    case "Praca" -> data = centralPanel.getWorkData();
                    case "Wyloguj" -> {
                        MyFrame.saveAppState();
                        SwingUtilities.getWindowAncestor(this).dispose();
                        SwingUtilities.invokeLater(LoginFrame::new);
                        return;
                    }
                    default -> data = new Object[][]{{false, "Brak danych"}};
                }

                for (TableChangeListener listener : listeners) {
                    listener.tableChanged( new TableChangedEvent(title, data));
                    if (listener instanceof TopPanel topPanel) {
                        topPanel.setCurrentViewType(mapTitleToViewType(title));
                    }
                }
            }
        }
    }

    private final java.util.List<TableChangeListener> listeners = new java.util.ArrayList<>();

    public void addTableChangeListener(TableChangeListener listener) {
        listeners.add(listener);
    }

    private ViewType mapTitleToViewType(String title) {
        return switch (title) {
            case "Dział pracowników" -> ViewType.DEPARTMENTS;
            case "Pracownik" -> ViewType.EMPLOYEES;
            case "Użytkownik" -> ViewType.USERS;
            case "Brygadzista" -> ViewType.FOREMENS;
            case "Brygada" -> ViewType.TEAMS;
            case "Zlecenie" -> ViewType.ASSIGNMENTS;
            case "Praca" -> ViewType.WORKS;
            default -> ViewType.EMPLOYEES;
        };
    }
}