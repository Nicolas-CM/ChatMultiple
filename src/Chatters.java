//S

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chatters {

    private Set<Person> clientes = new HashSet<>();

    public Set<Person> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Person> clientes) {
        this.clientes = clientes;
    }

    public Chatters() {
    }

    public String printClientesWithoutMe(String me) {
        if (clientes.size() == 1) {
            return "No hay mas personas en el servidor";
        }

        List<Person> listaClientes = new ArrayList<>(clientes);

        String client = "";

        for (int i = 0; i < listaClientes.size(); i++) {
            if (!listaClientes.get(i).getName().equals(me)) {
                client += ((i + 1) + ") " + listaClientes.get(i).getName() + "\n");
            }

        }

        return client;

    }

    public Person getPerson(String name) {
        if (existeUsr(name)) {
            for (Person person : clientes) {
                if (person.getName().equals(name)) {
                    return person;
                }
            }
        }
        return null;
    }

    public boolean existeUsr(String name) {
        boolean response = false;
        for (Person p : clientes) {
            if (name.equals(p.getName())) {
                response = true;
                break;
            }
        }
        return response;
    }

    public void addUsr(String name, PrintWriter out) {
        if (!name.isBlank() && !existeUsr(name)) {
            Person p = new Person(name, out);
            clientes.add(p);
        }
    }

    public void removeUsr(String name) {
        for (Person p : clientes) {
            if (name.equals(p.getName())) {
                clientes.remove(p);
                break;
            }
        }
    }

    public void broadcastMessage(String message) {

        for (Person p : clientes) {
            p.getOut().println(message);
        }
    }

    // enviar un mensaje privado a la persona con un nombre dado nameDest
    public void sendPrivateMessage(String nameSrc, String nameDest, String message) {
        for (Person p : clientes) {
            if (nameDest.equals(p.getName())) {
                p.getOut().println("[Chat privado de " + nameSrc + "]: " + message);
                break;
            }
        }

    }

}