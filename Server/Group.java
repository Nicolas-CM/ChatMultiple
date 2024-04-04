import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {

    private String name;
    private List<Message> mensajes = new ArrayList<>();
    private Set<Person> miembros = new HashSet<>();
    private Person creator;

    public Group(String name, Person creator) {
        this.name = name;
        this.creator = creator;
        this.miembros.add(creator);
    }

    public Person getPerson(String name) {
        if (existeUsr(name)) {
            for (Person person : miembros) {
                if (person.getName().equals(name)) {
                    return person;
                }
            }
        }
        return null;
    }

    public boolean existeUsr(String name) {
        boolean response = false;
        for (Person p : miembros) {
            if (name.equals(p.getName())) {
                response = true;
                break;
            }
        }
        return response;
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

    /**
     * @return Person return the creator
     */
    public Person getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(Person creator) {
        this.creator = creator;
    }

}
