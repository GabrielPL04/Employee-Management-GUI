package View;

import Controller.TableChangeListener;
import Controller.TableChangedEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public
    class CentralPanel
    extends JPanel
    implements TableChangeListener {

    private JTable jTable;
    private JScrollPane jScrollPane;

    public CentralPanel() {
        this.setLayout( new BorderLayout());
        updateTable("Dział pracowników", getEmployeeData());
    }

    public void updateTable(String title, Object[][] data) {
        this.removeAll();

        JLabel headerLabel = new JLabel(title, SwingConstants.CENTER);
        headerLabel.setFont( new Font("SansSerif", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(headerLabel, BorderLayout.NORTH);

        String[] columnNames = {"", ""};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        jTable = new JTable(model);
        jTable.setRowHeight(30);
        jTable.setShowGrid(true);
        jTable.setGridColor(Color.LIGHT_GRAY);
        jTable.setTableHeader(null);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        jTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTable.getColumnModel().getColumn(0).setMaxWidth(50);

        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        jTable.getColumnModel().getColumn(1).setCellRenderer(textRenderer);

        jScrollPane = new JScrollPane(jTable);
        this.add(jScrollPane, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    public Object[][] getAssignmentData() {
        return Model.Assignment.getAssignmentsMap().values().stream()
                .sorted(Comparator.comparingInt(Model.Assignment::getAssignmentId))
                .map(a -> new Object[]{false, a.toString()})
                .toArray(Object[][]::new);
    }

    public Object[][] getDepartmentData() {
        return Model.Department.getAllDepartments().stream()
                .sorted(Comparator.comparingInt(Model.Department::getId))
                .map(d -> new Object[]{false, d})
                .toArray(Object[][]::new);
    }

    public Object[][] getEmployeeData() {
        return Model.Employee.getAllEmployees().values().stream()
                .sorted(Comparator.comparingInt(Model.Employee::getId))
                .map(e -> new Object[]{false, e.toString()})
                .toArray(Object[][]::new);
    }

    public Object[][] getForemanData() {
        return Model.Employee.getAllEmployees().values().stream()
                .filter(e -> e instanceof Model.Foreman)
                .sorted(Comparator.comparingInt(Model.Employee::getId))
                .map(e -> new Object[]{false, e.toString()})
                .toArray(Object[][]::new);
    }

    public Object[][] getTeamData() {
        return Model.Employee.getAllEmployees().values().stream()
                .filter(e -> e instanceof Model.Foreman)
                .flatMap(e -> ((Model.Foreman) e).getTeams().stream())
                .sorted(Comparator.comparingInt(Model.Team::getId))
                .map(team -> new Object[]{false, team.toString()})
                .toArray(Object[][]::new);
    }

    public Object[][] getUserData() {
        return Model.Employee.getAllEmployees().values().stream()
                .filter(e -> e instanceof Model.User && !(e instanceof Model.Foreman))
                .sorted(Comparator.comparingInt(Model.Employee::getId))
                .map(e -> new Object[]{false, e.toString()})
                .toArray(Object[][]::new);
    }

    public Object[][] getWorkData() {
        return Model.Work.getWorkMap().values().stream()
                .sorted(Comparator.comparingInt(Model.Work::getWorkId))
                .map(w -> new Object[]{false, w.toString()})
                .toArray(Object[][]::new);
    }

    public java.util.List<Object> getSelectedItems() {

        java.util.List<Object> selected = new ArrayList<>();
        for (int i = 0; i < jTable.getRowCount(); i++) {
            Boolean checked = (Boolean) jTable.getValueAt(i, 0);
            if (checked != null && checked) {
                Object value = jTable.getValueAt(i, 1);
                if (value != null) {
                    selected.add(value);
                }
            }
        }
        return selected;
    }

    @Override
    public void tableChanged(TableChangedEvent evt) {
        updateTable(evt.getTitle(), evt.getData());
    }
}
