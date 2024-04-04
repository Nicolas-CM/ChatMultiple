import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Groups {
    private Set<Group> grupos = new HashSet<>();

    public Groups() {
    }

    public boolean existeGroup(String name) {
        boolean response = false;
        for (Group g : grupos) {
            if (name.equals(g.getName())) {
                response = true;
                break;
            }
        }
        return response;
    }

    public void addGroup(String name, PrintWriter out, Person creator) {
        if (!name.isBlank() && !existeGroup(name)) {
            Group g = new Group(name, creator);
            grupos.add(g);
        }
    }

    public void removeGroup(String name) {
        for (Group g : grupos) {
            if (name.equals(g.getName())) {
                grupos.remove(g);
                break;
            }
        }
    }

    public Set<Group> getGroups() {
        return grupos;
    }

}
