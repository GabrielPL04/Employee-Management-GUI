package View;

import Controller.TableChangeListener;
import Controller.TableChangedEvent;
import Model.*;
import View.AddDialog.*;
import View.EditDialog.*;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
    class TopPanel
    extends JPanel
    implements ActionListener, TableChangeListener {

    private final User loggedUser;
    private CentralPanel centralPanel;
    private ViewType currentViewType = ViewType.DEPARTMENTS;

    private JButton textButton;
    private JButton myAssignmentsButton;
    private JButton userButton;

    private JButton[] jButtons;
    private final String[] buttons = {
            "Nowy",
            "Edycja",
            "Usuń"
    };

    public TopPanel(User loggedUser) {
        this.loggedUser = loggedUser;
        this.setBackground(Color.GRAY);
        this.setPreferredSize( new Dimension(0, 60));
        this.setLayout( new FlowLayout(FlowLayout.CENTER, 20, 10));
        this.add(Box.createHorizontalStrut(50));

        jButtons = new JButton[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            jButtons[i] = new RoundedButton(buttons[i]);
            jButtons[i].setPreferredSize(new Dimension(140, 40));
            jButtons[i].setFont( new Font("SansSerif", Font.BOLD, 11));
            jButtons[i].setForeground(Color.WHITE);
            jButtons[i].setBackground( new Color(25, 118, 210));
            jButtons[i].setFocusPainted(false);
            jButtons[i].addActionListener(this);
            this.add(jButtons[i]);
        }

        textButton = new RoundedButton("Pracownicy działu");
        textButton.setPreferredSize( new Dimension(140, 40));
        textButton.setFont( new Font("SansSerif", Font.BOLD, 11));
        textButton.setForeground(Color.WHITE);
        textButton.setBackground( new Color(25, 118, 210));
        textButton.setFocusPainted(false);
        textButton.addActionListener(this);
        this.add(textButton);

        if (loggedUser instanceof Foreman) {
            myAssignmentsButton = new RoundedButton("Moje zlecenia");
            myAssignmentsButton.setPreferredSize( new Dimension(140, 40));
            myAssignmentsButton.setFont( new Font("SansSerif", Font.BOLD, 11));
            myAssignmentsButton.setForeground(Color.WHITE);
            myAssignmentsButton.setBackground( new Color(25, 118, 210));
            myAssignmentsButton.setFocusPainted(false);
            myAssignmentsButton.addActionListener(this);
            this.add(myAssignmentsButton);
        }

        String initials = (loggedUser != null) ? loggedUser.getInitials() : "Gość";
        userButton = new RoundedButton("Witaj, " + initials, true);
        userButton.setPreferredSize( new Dimension(100, 40));
        userButton.setFont( new Font("SansSerif", Font.BOLD, 11));
        userButton.addActionListener(this);
        this.add(userButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == textButton) {
            switch (currentViewType) {
                case DEPARTMENTS -> handleShowDepartmentEmployees();
                case ASSIGNMENTS -> handleShowAssignmentWorks();
                default -> JOptionPane.showMessageDialog(this, "Brak akcji dla tego widoku.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        if (source == myAssignmentsButton) {
            showMyAssignments();
            return;
        }

        if (source == userButton) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem changePassword = new JMenuItem("Zmień hasło");
            JMenuItem logout = new JMenuItem("Wyloguj się");

            changePassword.addActionListener(evt -> new ChangePasswordDialog(getFrame(), loggedUser).setVisible(true));
            logout.addActionListener(evt -> {
                MyFrame.saveAppState();
                getFrame().dispose();
                new LoginFrame();
            });

            menu.add(changePassword);
            menu.add(logout);
            menu.show(userButton, 0, userButton.getHeight());
            return;
        }

        for (int i = 0; i < jButtons.length; i++) {
            if (source == jButtons[i]) {
                String selected = buttons[i];

                switch (selected) {
                    case "Nowy" -> {
                        switch (currentViewType) {
                            case DEPARTMENTS -> new AddDepartmentDialog(getFrame(), centralPanel).setVisible(true);
                            case EMPLOYEES -> new AddEmployeeDialog(getFrame(), centralPanel).setVisible(true);
                            case TEAMS -> new AddTeamDialog(getFrame(), centralPanel).setVisible(true);
                            case ASSIGNMENTS -> new AddAssignmentDialog(getFrame(), centralPanel).setVisible(true);
                            case WORKS -> new AddWorkDialog(getFrame(), centralPanel).setVisible(true);
                            case USERS -> new AddUserDialog(getFrame(), centralPanel).setVisible(true);
                            case FOREMENS -> new AddForemanDialog(getFrame(), centralPanel).setVisible(true);
                        }
                    }
                    case "Edycja" -> handleEdit();
                    case "Usuń" -> handleDelete();
                }
                break;
            }
        }
    }

    private TableChangeListener tcl;

    public void addTableChangeListener(TableChangeListener tcl) {
        this.tcl = tcl;
    }

    @Override
    public void tableChanged(TableChangedEvent evt) {
        this.currentViewType = mapTitleToViewType(evt.getTitle());
    }

    public void setCurrentViewType(ViewType type) {
        this.currentViewType = type;

        if (type == ViewType.ASSIGNMENTS) {
            textButton.setText("Prace zlecenia");
        } else {
            textButton.setText("Pracownicy działu");
        }
    }

    public void setCentralPanel(CentralPanel cp) {
        this.centralPanel = cp;
    }

    private JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    private boolean isLoggedUser(Object item) {
        return item instanceof String str && loggedUser != null && str.contains(loggedUser.toString());
    }

    private void handleDelete() {
        List<Object> selectedItems = centralPanel.getSelectedItems();

        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie zaznaczono żadnego wiersza.", "Uwaga", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Object item : selectedItems) {
            if (isLoggedUser(item)) {
                JOptionPane.showMessageDialog(this,
                        "Nie możesz usunąć samego siebie!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int confirmRemove = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz usunąć zaznaczone elementy?",
                "Potwierdzenie usunięcia", JOptionPane.YES_NO_OPTION);

        if (confirmRemove != JOptionPane.YES_OPTION) return;

        selectedItems.forEach(this::deleteItemByViewType);

        switch (currentViewType) {
            case EMPLOYEES, USERS, FOREMENS -> {
                if (Employee.getAllEmployees().isEmpty()) {
                    Employee.resetCounter();
                }
            }
            case DEPARTMENTS -> {
                if (Department.getAllDepartments().isEmpty()) {
                    Department.resetCounter();
                }
            }
            case TEAMS -> {
                if (Team.getAllTeams().isEmpty()) {
                    Team.resetCounter();
                }
            }
            case ASSIGNMENTS -> {
                if (Assignment.getAssignmentsMap().isEmpty()) {
                    Assignment.resetCounter();
                }
            }
            case WORKS -> {
                if (Work.getWorkMap().isEmpty()) {
                    Work.resetCounter();
                }
            }
        }
        refreshCurrentView();
    }

    private void handleEdit() {
        List<Object> selected = centralPanel.getSelectedItems();

        if (selected.size() != 1) {
            JOptionPane.showMessageDialog(this, "Zaznacz dokładnie jeden wiersz do edycji.", "Uwaga", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object selectedItem = selected.get(0);

        if (isLoggedUser(selectedItem)) {
            JOptionPane.showMessageDialog(this,
                    "Nie możesz edytować własnych danych. Możesz jedynie zmienić hasło.",
                    "Brak dostępu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = editItemByViewType(selectedItem);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Edycja nieobsługiwana dla tego widoku.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleShowDepartmentEmployees() {
        if (currentViewType != ViewType.DEPARTMENTS) {
            JOptionPane.showMessageDialog(this, "Ten przycisk działa tylko w widoku działów.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Object> selectedItems = centralPanel.getSelectedItems();
        if (selectedItems.size() != 1 || !(selectedItems.get(0) instanceof Department department)) {
            JOptionPane.showMessageDialog(this, "Zaznacz dokładnie jeden dział.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Object[][] data = department.getEmployees().stream()
                .sorted()
                .map(e -> new Object[]{false, e.toString()})
                .toArray(Object[][]::new);

        centralPanel.updateTable("Pracownicy działu: " + department.getDepartmentName(), data);
        setCurrentViewType(ViewType.EMPLOYEES);
    }

    private void handleShowAssignmentWorks() {
        List<Object> selectedItems = centralPanel.getSelectedItems();

        if (selectedItems.size() != 1) {
            JOptionPane.showMessageDialog(this, "Zaznacz dokładnie jedno zlecenie.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Object selected = selectedItems.get(0);

        Assignment assignment = Assignment.getAssignmentsMap().values().stream()
                .filter(a -> a.toString().equals(selected.toString()))
                .findFirst()
                .orElse(null);

        if (assignment == null) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono zlecenia.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[][] data = assignment.getWorks().stream()
                .map(w -> new Object[]{false, w.toString()})
                .toArray(Object[][]::new);

        centralPanel.updateTable("Prace zlecenia: " + assignment.getAssignmentId(), data);
        setCurrentViewType(ViewType.WORKS);
    }

    private void showMyAssignments() {
        if (!(loggedUser instanceof Foreman foreman)) {
            return;
        }

        List<Assignment> unfinished = Assignment.getAssignmentsMap().values().stream()
                .filter(a -> a.getTeam() != null && foreman.getTeams().contains(a.getTeam()))
                .filter(a -> {
                    String status = a.getStatus();
                    return status.equals("Created") || status.equals("Started");
                })
                .sorted(Comparator.comparingInt(Assignment::getAssignmentId))
                .toList();

        Object[][] data = unfinished.stream()
                .map(a -> new Object[]{false, a.toString()})
                .toArray(Object[][]::new);

        centralPanel.updateTable("Moje niezakończone zlecenia", data);
        setCurrentViewType(ViewType.ASSIGNMENTS);
    }

    private void deleteItemByViewType(Object item) {
        switch (currentViewType) {
            case EMPLOYEES, USERS, FOREMENS ->
                    Employee.getAllEmployees().values().removeIf(e -> e.toString().equals(item));

            case DEPARTMENTS -> {
                if (item instanceof Department d) {
                    d.delete();
                }
            }
            case TEAMS ->
                    Employee.getAllEmployees().values().stream()
                            .filter(e -> e instanceof Foreman)
                            .map(e -> (Foreman) e)
                            .forEach(f -> f.getTeams().removeIf(t -> t.toString().equals(item)));

            case ASSIGNMENTS ->
                    Assignment.getAssignmentsMap().values().removeIf(a -> a.toString().equals(item));

            case WORKS ->
                    Work.getWorkMap().values().removeIf(w -> w.toString().equals(item));
        }
    }

    private boolean editItemByViewType(Object item) {
        switch (currentViewType) {
            case EMPLOYEES -> {
                Employee emp = Employee.getAllEmployees().values().stream()
                        .filter(e -> e.toString().equals(item))
                        .findFirst().orElse(null);
                if (emp != null) {
                    new EditEmployeeDialog(getFrame(), centralPanel, emp).setVisible(true);
                    return true;
                }
            }
            case USERS -> {
                if (item instanceof String s) {
                    Employee emp = Employee.getAllEmployees().values().stream()
                            .filter(e -> e instanceof User && !(e instanceof Foreman))
                            .filter(e -> e.toString().equals(s))
                            .findFirst().orElse(null);
                    if (emp instanceof User user) {
                        new EditUserDialog(getFrame(), centralPanel, user).setVisible(true);
                        return true;
                    }
                }
            }
            case FOREMENS -> {
                if (item instanceof String s) {
                    Employee emp = Employee.getAllEmployees().values().stream()
                            .filter(e -> e instanceof Foreman)
                            .filter(e -> e.toString().equals(s))
                            .findFirst().orElse(null);
                    if (emp instanceof Foreman f) {
                        new EditForemanDialog(getFrame(), centralPanel, f).setVisible(true);
                        return true;
                    }
                }
            }
            case TEAMS -> {
                if (item instanceof String s) {
                    Team team = Team.getAllTeams().stream()
                            .filter(t -> t.toString().equals(s))
                            .findFirst().orElse(null);
                    if (team != null) {
                        new EditTeamDialog(getFrame(), centralPanel, team).setVisible(true);
                        return true;
                    }
                }
            }
            case WORKS -> {
                if (item instanceof String s) {
                    Work work = Work.getWorkMap().values().stream()
                            .filter(w -> w.toString().equals(s))
                            .findFirst().orElse(null);
                    if (work != null) {
                        new EditWorkDialog(getFrame(), centralPanel, work).setVisible(true);
                        return true;
                    }
                }
            }
            case DEPARTMENTS -> {
                if (item instanceof Department d) {
                    new EditDepartmentDialog(getFrame(), centralPanel, d).setVisible(true);
                    return true;
                }
            }
            case ASSIGNMENTS -> {
                if (item instanceof String s) {
                    Assignment a = Assignment.getAssignmentsMap().values().stream()
                            .filter(as -> as.toString().equals(s))
                            .findFirst().orElse(null);
                    if (a != null) {
                        new EditAssignmentDialog(getFrame(), centralPanel, a).setVisible(true);
                        return true;
                    }
                }
            }
        }
        return false;
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
            default -> ViewType.DEPARTMENTS;
        };
    }

    private void refreshCurrentView() {
        switch (currentViewType) {
            case EMPLOYEES -> centralPanel.updateTable("Pracownik", centralPanel.getEmployeeData());
            case USERS -> centralPanel.updateTable("Użytkownik", centralPanel.getUserData());
            case FOREMENS -> centralPanel.updateTable("Brygadzista", centralPanel.getForemanData());
            case DEPARTMENTS -> centralPanel.updateTable("Dział pracowników", centralPanel.getDepartmentData());
            case TEAMS -> centralPanel.updateTable("Brygada", centralPanel.getTeamData());
            case ASSIGNMENTS -> centralPanel.updateTable("Zlecenie", centralPanel.getAssignmentData());
            case WORKS -> centralPanel.updateTable("Praca", centralPanel.getWorkData());
        }
    }
}