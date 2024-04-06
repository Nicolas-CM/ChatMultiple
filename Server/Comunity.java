import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Comunity {
    private Set<Group> grupos = new HashSet<>();

    public Comunity() {
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

    public String printMyGroups(String member) {
        List<Group> listaGrupos = new ArrayList<>(grupos);

        String gruposMensaje = "";

        if (listaGrupos.size() == 0) {
            return "No existen grupos creados";
        } else {
            for (int i = 0; i < listaGrupos.size(); i++) {
                if (listaGrupos.get(i).existeUsr(member)) {
                    gruposMensaje += ((i + 1) + ") " + listaGrupos.get(i).getName() + "\n");
                }

            }

            return gruposMensaje;
        }
    }

    public String printGroups() {
        List<Group> listaGrupos = new ArrayList<>(grupos);

        String gruposMensaje = "";

        if (listaGrupos.size() == 0) {
            return "No existen grupos creados";
        } else {
            for (int i = 0; i < listaGrupos.size(); i++) {
                gruposMensaje += ((i + 1) + ") " + listaGrupos.get(i).getName() + "\n");

            }

            return gruposMensaje;
        }
    }

}
