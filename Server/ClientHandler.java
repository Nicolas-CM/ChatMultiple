import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class ClientHandler implements Runnable {

  private Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;
  private String clientName;
  private String groupName;
  Chatters clientes;
  Comunity grupos;
  Personals privados;

  public ClientHandler(Socket socket, Chatters clientes, Comunity grupos, Personals privados) {
    this.clientes = clientes;
    this.clientSocket = socket;
    this.grupos = grupos;
    this.privados = privados;
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

    } catch (Exception e) {
      // e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
        System.out.println(clientName + " ha abandonado el chat.");
        clientes.broadcastMessage(clientName + " ha abandonado la aplicación.");
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
          clientes.broadcastMessage(clientName + " se ha unido a la aplicacion.");
          out.println("NAMEACCEPTED " + clientName);
          clientes.addUsr(clientName, out);
          break;
        }
      }
    }
  }

  public void mainMenu() throws IOException, Exception {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenu principal\n---------- \n\nSeleccione una opcion:\n 0) Salir del programa\n 1) Grupos"
              +
              "\n 2) Privados" +
              "\n-------------------\n");
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
          out.println("------------------\n¡Opcion incorrecta!\n");
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

  private void groupMenu() throws IOException, Exception {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenu Grupos\n---------- \n\nSeleccione una opcion:\n 0) Salir del menu\n 1) Crear un nuevo grupo"
              +
              "\n 2) Ingresar a un grupo" +
              "\n 3) Escribir a un grupo" +
              "\n 4) Ver miembros de un grupo" +
              "\n-------------------\n");
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
          // out.println("JOINTOGROUP");
          joinToGroup();
          break;
        case 3:
          writeToGroup();
          break;
        case 4:
          membersOfAGroup();
          break;
        default:
          out.println("\n------------------\n¡Opcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void chatMenu(Group group, Private privado) throws IOException, Exception {
    System.out.println("\nDentro de ChatMenu\n");
    int optionMenu = 0;
    boolean exit = false;
    String lastTen = "";
    String allHistorial = "";
    do {
      if (privado == null) {
        lastTen = group.getHistorial(true);
      } else {
        lastTen = privado.getHistorial(true);
      }

      out.println(
          "MENU\n----------\nCHAT\n----------" + lastTen
              + "\n \nSeleccione una opcion:\n 0) Salir del menu\n 1) Mas Mensajes" +
              "\n 2) Escuchar un audio" +
              "\n 3) Enviar Mensajes / Llamar" +
              "\n-------------------\n");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          // Dependiendo si es un grupo o privado se realizan tareas diferentes
          if (privado == null) {
            allHistorial = group.getHistorial(false);
          } else {
            allHistorial = privado.getHistorial(false);
          }
          out.println(allHistorial);
          break;
        case 2:
          // listenAudio();
          break;
        case 3:
          sendMenu(group, privado);
          break;
        default:
          out.println("\n------------------\n¡Opcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void sendMenu(Group group, Private privado) throws IOException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenu Enviar\n----------"
              + "\n \nSeleccione una opcion de lo que dese enviar:\n 0) Salir del menu\n 1) Mensaje" +
              "\n 2) Audio" +
              "\n 3) Hacer Llamada" +
              "\n-------------------\n");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          sendMessage(group, privado);
          break;
        case 2:
          // joinToGroup();
          break;
        case 3:
          out.println("CALLSTARTED");
          new Thread(() -> {
            try {
              playCallToGroup();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }).start();
          break;
        default:
          out.println("\n------------------\n¡Opcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void sendMessage(Group group, Private privado) throws IOException {
    out.println("\n --------- Ingrese su mensaje: --------- \n");
    String message = in.readLine();
    Message m = new Message(clientes.getPerson(clientName), message, LocalDateTime.now());
    // Si es privado o al grupo lo manda
    if (privado == null) {
      group.sendMessage(m);
    } else {
      privado.sendMessage(m);
    }
    out.println("\n --------- Mensaje enviado --------- \n");
  }

  private static void playCallToGroup() throws Exception {
    DatagramSocket serverSocket = new DatagramSocket(6789);
    System.out.println("\nServer started. Waiting for clients...\n");

    // Configurar la línea de audio para reproducir el audio recibido
    AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 2, true, false);
    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
    SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
    sourceDataLine.open(audioFormat);
    sourceDataLine.start();

    byte[] receiveData = new byte[1024];

    while (true) {
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      serverSocket.receive(receivePacket);

      // Reproducir audio
      sourceDataLine.write(receivePacket.getData(), 0, receivePacket.getLength());
    }
  }

  private void writeToGroup() throws IOException, Exception {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());
    int optionMenu = 0;

    out.println(
        "MENU\n----------\nGrupos Registrados\n---------- \n\nSeleccione un grupo para ingresar:\n 0) Salir del menu\n");

    // Imprimir grupos
    out.println(grupos.printMyGroups(clientName) + "\n");

    if (grupos.printMyGroups(clientName).equals("No existen grupos creados")) {
      groupMenu();
      return;
    }

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
      groupMenu();
      return;
    }

    // Iniciando chat con el grupo y enviando los mensajes al historial del grupo
    // selecccionado
    if (listaGrupos.get(optionMenu - 1).existeUsr(clientName)) {
      // El usuario si está en el grupo y puede entrar al menú del Chat
      chatMenu(listaGrupos.get(optionMenu - 1), null);
    } else {
      out.println("\nNo estas en este grupo, bye bye\n");
    }

  }

  private void joinToGroup() throws IOException, Exception {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());
    int optionMenu = 0;

    out.println(
        "MENU\n----------\nGrupos Registrados\n---------- \n\nSeleccione un grupo para ingresar:\n 0) Salir del menu\n");

    // Imprimir grupos
    out.println(grupos.printGroups() + "\n");

    if (grupos.printGroups().equals("No existen grupos creados")) {
      groupMenu();
    }

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
      groupMenu();
      return;
    }

    // Agregando usuario al grupo
    if (listaGrupos.get(optionMenu - 1).existeUsr(clientName)) {
      out.println("\nEl usuario ya esta en el grupo no puede ingresar\n");
    } else {
      out.println("\nAñadiendo a " + clientName + " al grupo: " + listaGrupos.get(optionMenu - 1).getName() + "\n");
      listaGrupos.get(optionMenu - 1).getMiembros().add(clientes.getPerson(clientName));
    }

  }

  private int validateRange(List listToValidate) {

    int option = validateIntegerOption();

    while ((option >= listToValidate.size() + 1) || (option < 0)) {
      out.println("\nIngrese una opcion valida\n");
      option = validateIntegerOption();
    }

    return option;

  }

  private void createNewGroup() throws IOException {
    while (true) {
      out.println("SUBMITNAME");
      groupName = in.readLine().trim();
      System.out.println("\nLlego: " + groupName + "\n");
      if (groupName == null) {
        return;

      }
      System.out.println("\nVa a sincronizar\n");// -------------------------
      synchronized (groupName) {
        if (!groupName.isBlank() && !grupos.existeGroup(groupName)) {
          // grupos.broadcastMessage(groupName + " se ha unido al chat.");
          System.out.println("\nEntro a la condicion\n");// -------------------
          out.println("NAMEACCEPTED");
          System.out.println("\nSe envio\n");
          grupos.addGroup(groupName, out, clientes.getPerson(clientName));
          break;
        }
      }
    }
  }

  private void membersOfAGroup() throws IOException, Exception {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());

    int optionMenu = 0;

    out.println(
        "MENU\n----------\nVer miembros de un grupo\n---------- \n\nSeleccione un grupo para ver sus miembros:\n 0) Salir del menu");

    // Imprimir grupos
    out.println(grupos.printGroups());

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
      groupMenu();
      return;
    }

    // Imprimir personas de los grupos
    out.println(listaGrupos.get(optionMenu - 1).printMembers());

  }

  private void privateMenu() throws IOException, Exception {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
          "MENU\n----------\nMenu Privados\n---------- \n\nSeleccione una opcion:\n 0) Salir del menu\n 1) Crear un nuevo chat"
              +
              "\n 2) Escribir a un chat" +
              "\n-------------------\n");
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          createChatPrivate();
          break;
        case 2:
          writeToPrivate();
          break;
        default:
          out.println("\n------------------\n¡Opcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void writeToPrivate() throws IOException, Exception {

    List<Private> listaPrivados = new ArrayList<>(privados.getMyPrivates(clientName));
    int optionMenu = 0;

    if (listaPrivados.isEmpty()) {
      out.println("\nNo tienes chat privados, intenta la opcion 'Crear un nuevo chat'\n");
      return;
    }

    out.println(
        "MENU\n----------\nChat Privados\n---------- \n\nSeleccione un chat privado para ingresar:\n 0) Salir del menu");

    // Imprimir privados
    out.println(printListMyPrivates(listaPrivados, clientName));

    optionMenu = validateRange(listaPrivados);

    if (optionMenu == 0) {
      privateMenu();
      return;
    }

    // Iniciando chat con el privado y enviando los mensajes al historial del grupo
    // selecccionado
    System.out.println("\nEstoy antes del are you\n");
    if (listaPrivados.get(optionMenu - 1).areYouHereInThisPrivate(clientName)) {
      // El usuario si está en el privadp y puede entrar al menú del Chat
      System.out.println("\nDentro de are you\n");
      chatMenu(null, listaPrivados.get(optionMenu - 1));
    } else {
      out.println("\nNo estas en este privado, bye bye\n");
    }
  }

  private String printListMyPrivates(List<Private> list, String client) {
    String m = "";
    for (int i = 0; i < list.size(); i++) {
      System.out.println("\nSoy yooooooooooo:" + list.get(i).getPerson1().getName() + ":vs:" + client + ":");
      System.out.println(list.get(i).getPerson1().getName().equals(client));
      if (list.get(i).getPerson1().getName().equals(client)) {
        m += ((i + 1) + ") " + list.get(i).getPerson2().getName() + "\n");
      } else {
        m += ((i + 1) + ") " + list.get(i).getPerson1().getName() + "\n");
      }
    }
    return m;
  }

  private void createChatPrivate() throws IOException, Exception {
    List<Person> listaClientes = new ArrayList<>(clientes.getClientes());

    int optionMenu = 0;

    out.println(
        "MENU\n----------\nPersonas de la aplicación\n---------- \n\nSeleccione una persona para iniciar un Chat Privado:\n 0) Salir del menu");

    // Imprimir los clientes para crear el nuevo Privado
    if (clientes.printClientesWithoutMe(clientName).equals("No hay mas personas en el servidor")) {
      out.println("\n ¡No existen mas usuarios por ahora!\n");
      privateMenu();
      return;
    }
    out.println(clientes.printClientesWithoutMe(clientName));

    optionMenu = validateRange(listaClientes);

    if (optionMenu == 0 || listaClientes.get(optionMenu - 1).equals(clientes.getPerson(clientName))) {
      out.println("\n ¿¿¿Pa que quieres un grupo contigo mismo???\n");
      privateMenu();
      return;
    }

    // Creando el nuevo privado
    String other = listaClientes.get(optionMenu - 1).getName();
    if (privados.existePrivate(clientName, other)) {
      out.println("\nYa tienes un Chat Privado con esta persona, intenta desde la opcion 'Escribir a un chat'\n");
    } else {
      out.println("\n Creando un nuevo Chat privado entre " + other + " y " + clientName);
      privados.addPrivate(clientes.getPerson(clientName), clientes.getPerson(other));
    }

  }
}
