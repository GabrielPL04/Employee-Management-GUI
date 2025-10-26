package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public
    class MyFrame
    extends JFrame {

    public MyFrame(User loggedUser) {
        super("Employee Management");

        CentralPanel cp = new CentralPanel();
        LeftPanel lp = new LeftPanel(cp);
        TopPanel tp = new TopPanel(loggedUser);

        lp.addTableChangeListener(tp);
        lp.addTableChangeListener(cp);

        tp.addTableChangeListener(cp);
        tp.setCentralPanel(cp);

        this.setLayout( new BorderLayout());
        this.add(lp, BorderLayout.LINE_START);
        this.add(tp, BorderLayout.PAGE_START);
        this.add(cp, BorderLayout.CENTER);

        this.setSize( 1024, 768);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addWindowListener( new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveAppState();
            }
        });

        if (loggedUser instanceof Foreman foreman) {
            java.util.List<Assignment> unfinished = Assignment.getAssignmentsMap().values().stream()
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

            cp.updateTable("Moje niezakończone zlecenia", data);
            tp.setCurrentViewType(ViewType.ASSIGNMENTS);
        }

        this.setVisible(true);
    }

    public static void saveAppState() {
        AppState state = new AppState();
        state.employees.putAll(Employee.getAllEmployees());
        state.departments.addAll(Department.getAllDepartments());
        state.teams.addAll(Team.getAllTeams());
        state.assignments.putAll(Assignment.getAssignmentsMap());
        state.works.putAll(Work.getWorkMap());

        DataManager.save(state);
    }
}
