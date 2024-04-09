import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.Random;

public class Call {

    public final static int PORT_UDP = 8888;
    private String name;
    private Set<Person> miembros = new HashSet<>();
    private Set<Person> miembrosBorrables = new HashSet<>();
    private int port;
    private DatagramSocket socket_UDP;

    private Person creator;

    public Call(String name, Person creator) {
        this.name = name;
        this.creator = creator;
        this.miembros.add(creator);
        try {
            this.socket_UDP = new DatagramSocket(PORT_UDP);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        generateRandomPort();
    }

    private void generateRandomPort() {
        Random random = new Random();
        // Definir un rango de puertos válidos
        int minPort = 8000; // Puertos reservados generalmente para servicios del sistema
        int maxPort = 49151; // Puertos registrados
        int range = maxPort - minPort + 1;

        // Generar un número de puerto aleatorio dentro del rango
        port = random.nextInt(range) + minPort;

        // Verificar si el puerto está en uso
        while (isPortInUse(port)) {
            port = random.nextInt(range) + minPort;
        }
    }

    private boolean isPortInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            // Si la creación del ServerSocket es exitosa, significa que el puerto no está
            // en uso
            return false;
        } catch (Exception e) {
            // Si se produce una excepción, significa que el puerto está en uso
            return true;
        }
    }

    /*
     * public void callToGroup(String name, int serverPort) {
     * for (Person p : miembros) {
     * if (!name.equals(p.getName())) {
     * try {
     * if (p.getLector().answerTheCall().equals("1")) {
     * p.getLector().callToGroup(serverPort);
     * }
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * break;
     * } else {
     * try {
     * 
     * p.getLector().callToGroup(serverPort);
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * }
     * verifyCall(name);
     * }
     * 
     * 
     * public void verifyCall(String name) {
     * new Thread(() -> {
     * while (true) {
     * for (Person p : miembros) {
     * if (p.getLector().getIsInCall() == 0) {
     * deleteUser(p.getName());
     * }
     * }
     * if (miembros.isEmpty()) {
     * break;
     * }
     * }
     * }).start();
     * }
     */

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

    public boolean deleteUser(String name) {
        boolean response = false;
        for (Person p : miembros) {
            if (name.equals(p.getName())) {
                miembros.remove(p);
                response = true;
                break;
            }
        }
        return response;
    }

    public void deleteRandomUser() {
        List<Person> list = new ArrayList<>(miembrosBorrables);
        int index = (list.size() - 1);
        miembrosBorrables.remove(list.get(index));
    }

    public void broadcastMessageWithOutUser(String name, String message) {
        for (Person p : miembros) {
            if (!p.getName().equals(name)) {
                p.getOut().println(message);
            }
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

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public Set<Person> getMiembrosBorrables() {
        return miembrosBorrables;
    }

    public void setMiembrosBorrables(Set<Person> miembrosBorrables) {
        this.miembrosBorrables = miembrosBorrables;
    }

    public static int getPortUdp() {
        return PORT_UDP;
    }

    public DatagramSocket getSocket_UDP() {
        return socket_UDP;
    }

    public void setSocket_UDP(DatagramSocket socket_UDP) {
        this.socket_UDP = socket_UDP;
    }

}
