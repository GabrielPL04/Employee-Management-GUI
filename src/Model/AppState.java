package Model;

import java.io.Serializable;
import java.util.*;

public
    class AppState
    implements Serializable {

    private static final long serialVersionUID = 1L;

    public Map<Integer, Assignment> assignments = new HashMap<>();
    public Set<Department> departments = new HashSet<>();
    public Map<Integer, Employee> employees = new HashMap<>();
    public Set<Team> teams = new HashSet<>();
    public Map<Integer, Work> works = new HashMap<>();
}

