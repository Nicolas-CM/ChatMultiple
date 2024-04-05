import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {

    private String name;
    private Set<Person> miembros = new HashSet<>();
    private Person creator;
    private ChatHistory chatHistory;

    public Group(String name, Person creator) {
        this.name = name;
        this.creator = creator;
        this.miembros.add(creator);
        this.chatHistory = new ChatHistory(name);
    }

    public void sendMessage(Message m) {
        chatHistory.sendMessage(m);
    }

    public String getHeadHistorial() {
        return chatHistory.getHeadHistorial();
    }

    public String getAllMessages() {
        return chatHistory.getAllMessages();
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

    public String printMembers() {

        String mensajeMiembros = "";

        List<Person> listaPersonas = new ArrayList<>(miembros);

        if (listaPersonas.size() == 0) {
            return "El grupo esta vacio";
        } else {

            for (int i = 0; i < listaPersonas.size(); i++) {
                int counterPerson = 0;
                mensajeMiembros += ("Miembro #" + (counterPerson + 1) + ": " + listaPersonas.get(i).getName());
            }

            return mensajeMiembros;

        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
