import Model.*;
import View.LoginFrame;

import javax.swing.*;
import java.awt.*;

public
    class Main {

    public static void main(String[] args) {

        AppState loaded = DataManager.load();
        if (loaded != null) {
            Assignment.getAssignmentsMap().putAll(loaded.assignments);
            Department.getAllDepartments().addAll(loaded.departments);
            Employee.getAllEmployees().putAll(loaded.employees);
            Team.getAllTeams().addAll(loaded.teams);
            Work.getWorkMap().putAll(loaded.works);

            IDManager.updateAllCounters();
        }

        SwingUtilities.invokeLater(() -> {

            JWindow splash = new JWindow();
            JLabel label = new JLabel("Wczytywanie aplikacji...", SwingConstants.CENTER);
            label.setFont( new Font("Arial", Font.BOLD, 20));
            splash.add(label);
            splash.setSize(300, 150);

            splash.setLocationRelativeTo(null);
            splash.setVisible(true);

            Timer timer = new Timer(2000, e -> {

                splash.dispose();
                new LoginFrame();

            });

            timer.setRepeats(false);
            timer.start();

        });
    }
}