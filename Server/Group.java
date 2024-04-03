import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {

    private String name;
    private List<Message> mensajes = new ArrayList<>();
    private Set<Person> miembros = new HashSet<>();

    public Group(String name, Person creator) {
        this.name = name;
        this.miembros.add(creator);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }

    public Set<Person> getMiembros() {
        return miembros;
    }

    public void setMiembros(Set<Person> miembros) {
        this.miembros = miembros;
    }

}
