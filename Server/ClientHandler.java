import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

  private Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;
  private String clientName;
  private String groupName;
  Chatters clientes;
  Groups grupos;


  public ClientHandler(Socket socket, Chatters clientes, Groups grupos) {
    this.clientes = clientes;
    this.clientSocket = socket;
    this.grupos = grupos;
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true); // Se inicializa el flujo de salida aquí
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  @Override
  public void run() {
    try {

      // Se dirige para que cree su usuario con nombre único
      createUser();

      // Se abre el menú principal
      mainMenu();

      String message;
      // esperar mensajes de cada cliente y enviarlo a todos los clientes
      // si el mensaje es dirijido a un cliente en especial, se debe separar el
      // destinatario del mensaje y enviarlo unicamente a esa persona
      while ((message = in.readLine()) != null) {
        String[] parts = message.split(":", 2);
        if (parts.length == 2) {
          String recipient = parts[0].trim();
          String content = parts[1].trim();
          clientes.sendPrivateMessage(clientName, recipient, content);
        } else {
          clientes.broadcastMessage(clientName + ": " + message);
        }
      }
    } catch (IOException e) {
      // e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
        System.out.println(clientName + " ha abandonado el chat.");
        clientes.broadcastMessage(clientName + " ha abandonado el chat.");
        clientes.removeUsr(clientName);
      } catch (IOException e) {
        // e.printStackTrace();
      }
    }
  }

  public void createUser() throws IOException {
    while (true) {
      // out.println("Ingrese su nombre:");
      out.println("SUBMITNAME");
      clientName = in.readLine();
      if (clientName == null) {
        return;
      }
      synchronized (clientName) {
        if (!clientName.isBlank() && !clientes.existeUsr(clientName)) {
          clientes.broadcastMessage(clientName + " se ha unido al chat.");
          out.println("NAMEACCEPTED " + clientName);
          clientes.addUsr(clientName, out);
          break;
        }
      }
    }
  }

  public void mainMenu() throws IOException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenú principal\n---------- Seleccione una opción:\n 0) Salir del programa\n 1) Grupos" +
              "\n 2) Privados" +
              "\n-------------------");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          groupMenu();
          break;
        case 2:
          privateMenu();
          break;
        default:
          out.println("------------------\nOpción incorrecta!");
          break;
      }
    } while (exit == false);
  }

  public int validateIntegerOption() {
    int option = 0;
    try {
      String input = in.readLine();
      option = Integer.parseInt(input);
    } catch (IOException | NumberFormatException e) {
      // Si ocurre una excepción al leer o convertir el número,
      // establecer la opción como -1
      option = -1;
    }

    return option;
  }

  private void groupMenu() throws IOException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenú Grupos\n---------- Seleccione una opción:\n 0) Salir del menú\n 1) Crear un nuevo grupo"
              +
              "\n 2) Ingresar a un grupo" +
              "\n 3) Escribir a un grupo" +
              "\n 4) Ver miembros de un grupo" +
              "\n-------------------");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          out.println("CREATENEWGROUP");
          createNewGroup();
          break;
        case 2:
          out.println("JOINTOGROUP");
          joinToGroup();
          break;
        case 3:
          writeToGroup();
          break;
        case 4:
          membersOfAGroup();
          break;
        default:
          out.println("------------------\nOpción incorrecta!");
          break;
      }
    } while (exit == false);
  }

  private void writeToGroup() {

  }

  private void joinToGroup() throws IOException {
    int optionMenu = 0;

    out.println(
        "MENU\n----------\nGrupos Registrados\n---------- Seleccione un grupo para ingresar:\n 0) Salir del menú");

    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());

    int counter = 0;
    for (int i = 0; i < listaGrupos.size(); i++) {
      out.println((counter + 1) + ") " + listaGrupos.get(i).getName());

    }
    optionMenu = validateIntegerOption();

    if (optionMenu == 0) {
      groupMenu();
    }

    /*
     * while (!(optionMenu <= listaGrupos.size() + 1) & !(optionMenu >= 0)) {
     * out.println("Ingrese una opcion valida");
     * optionMenu = validateIntegerOption();
     * 
     * }
     * /
     **/

    if (listaGrupos.get(optionMenu - 1).existeUsr(listaGrupos.get(optionMenu - 1).getCreator().getName())) {
      out.println("El usuario creador ya pertenece al grupo");

    } else if (listaGrupos.get(optionMenu - 1).existeUsr(clientName)) {
      out.println("El usuario ya esta en el grupo no puede ingresar");
    } else {
      out.println("Añadiendo a " + clientName);
      out.println("\n al grupo: " + listaGrupos.get(optionMenu - 1).getName());
      listaGrupos.get(optionMenu - 1).getMiembros().add(clientes.getPerson(clientName));
    }

  }

  private void createNewGroup() throws IOException {
    while (true) {
      out.println("SUBMITNAME");
      groupName = in.readLine().trim();
      System.out.println("Llego: " + groupName);
      if (groupName == null) {
        return;

      }
      System.out.println("Va a sincronizzar");// -------------------------
      synchronized (groupName) {
        if (!groupName.isBlank() && !grupos.existeGroup(groupName)) {
          // grupos.broadcastMessage(groupName + " se ha unido al chat.");
          System.out.println("Entró a la condición");// -------------------
          out.println("NAMEACCEPTED");
          System.out.println("Se envió");
          grupos.addGroup(groupName, out, clientes.getPerson(clientName));
          break;
        }
      }
    }
  }

  private void membersOfAGroup() throws IOException {
    int optionMenu = 0;

    out.println(
        "MENU\n----------\nVer miembros de un grupo\n---------- Seleccione un grupo para ver sus miembros:\n 0) Salir del menú");

    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());

    int counter = 0;
    for (int i = 0; i < listaGrupos.size(); i++) {
      out.println((counter + 1) + ") " + listaGrupos.get(i).getName());

    }
    optionMenu = validateIntegerOption();

    if (optionMenu == 0) {
      groupMenu();
    }

    /*
     * while (!(optionMenu <= listaGrupos.size() + 1) & !(optionMenu >= 0)) {
     * out.println("Ingrese una opcion valida");
     * optionMenu = validateIntegerOption();
     * 
     * }
     * /
     **/

    List<Person> listaPersonas = new ArrayList<>(listaGrupos.get(optionMenu - 1).getMiembros());
    if (listaPersonas.size() == 0) {
      out.println("El grupo esta vacio");
    } else {

      for (int i = 0; i < listaPersonas.size(); i++) {
        int counterPerson = 0;
        out.println("Miembro #" + (counterPerson + 1) + ": " + " " + listaPersonas.get(i).getName());
      }

    }

  }

  private void privateMenu() throws IOException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenú Privados\n---------- Seleccione una opción:\n 0) Salir del menú\n 1) Crear un nuevo chat"
              +
              "\n 2) Escribir a un chat" +
              "\n-------------------");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          createNewGroup();
          break;
        case 2:
          joinToGroup();
          break;
        default:
          out.println("------------------\nOpción incorrecta!");
          break;
      }
    } while (exit == false);
  }
}
