//S

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;

public class ClientHandler implements Runnable {

  private Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;
  private String clientName;
  private String groupName;
  Chatters clientes;
  Comunity grupos;
  Personals privados;

  public ClientHandler(
    Socket socket,
    Chatters clientes,
    Comunity grupos,
    Personals privados
  ) {
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
        System.out.println("Se salio del menuu main");
        out.println("FINISH");
      }
    } catch (IOException | LineUnavailableException e) {
      if (e instanceof SocketException) {
        //
      } else {
        e.printStackTrace();
      }
    } finally {
      clean();
    }
  }

  private void clean() {
    try {
      clientSocket.close();
      System.out.println(clientName + " ha abandonado el chat. en clean\n");
      clientes.broadcastMessage(clientName + " ha abandonado la aplicacion.\n");
      clientes.removeUsr(clientName);
    } catch (Exception e) {
      e.printStackTrace();
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
          clientes.broadcastMessage(
            clientName + " se ha unido a la aplicacion.\n"
          );
          out.println("NAMEACCEPTED " + clientName);
          String userInfo = in.readLine();
          String[] parts = getUserInfo(userInfo);
          clientes.addUsr(
            clientName,
            out,
            Integer.parseInt(parts[1]),
            clientSocket.getInetAddress()
          );
          break;
        }
      }
    }
  }

  public String[] getUserInfo(String userInfo) {
    String[] parts = userInfo.split("/");
    return parts;
  }

  public void mainMenu() throws IOException, LineUnavailableException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
        "MENU\n----------\nMenu principal\n---------- \n\nSeleccione una opcion:\n 0) Salir del programa\n 1) Grupos" +
        "\n 2) Privados" +
        "\n-------------------\n"
      );
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
          out.println("------------------\nOpcion incorrecta!");
          break;
      }
    } while (exit == false);
  }

  public int validateIntegerOption() throws IOException {
    int option = 0;
    String input = in.readLine();
    try {
      option = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      // Si ocurre una excepcion al leer o convertir el numero,
      // establecer la opcion como -1
      option = -1;
    }

    return option;
  }

  private void groupMenu() throws IOException, LineUnavailableException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
        "MENU\n----------\nMenu Grupos\n---------- \n\nSeleccione una opcion:\n 0) Salir del menu\n 1) Crear un nuevo grupo" +
        "\n 2) Ingresar a un grupo" +
        "\n 3) Escribir a un grupo" +
        "\n 4) Ver miembros de un grupo" +
        "\n-------------------\n"
      );
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

  private void chatMenu(Group group, Private privado)
    throws IOException, LineUnavailableException {
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
        "MENU\n----------\nCHAT\n----------" +
        lastTen +
        "\n \nSeleccione una opcion:\n 0) Salir del menu\n 1) Mas Mensajes" +
        "\n 2) Escuchar un audio" +
        "\n 3) Enviar Mensajes / Llamar" +
        "\n-------------------\n"
      );
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
          listenAudio(group, privado);
          break;
        case 3:
          sendMenu(group, privado);
          break;
        default:
          out.println("\n------------------\nOpcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void listenAudio(Group group, Private privado) throws IOException {
    if (privado == null) {
      if (group.getChatHistory().getAudios().isEmpty()) {
        out.println("\nNo existen audios en este Chat\n");
        return;
      }

      /// PRuebaaaaaaaaaaaaa
      int optionMenu = 0;
      out.println(
        "MENU\n----------\nAudios en: " +
        group.getChatHistory().getGroupOrPrivate() +
        "\n---------- \nSeleccione un audio para escuchar:\n 0) Salir del menu\n"
      );

      // Imprimir los audios para seleccionar
      out.println(group.getChatHistory().printAudios());

      optionMenu = validateRange(group.getChatHistory().getAudios());

      if (optionMenu == 0) {
        return;
      }

      ServerSocket serverSocket = new ServerSocket(12348);
      out.println("PLAYAUDIO");
      Socket socket = serverSocket.accept();
      ObjectOutputStream outputStream = new ObjectOutputStream(
        socket.getOutputStream()
      );
      outputStream.writeObject(
        group.getChatHistory().getAudios().get(optionMenu - 1)
      );
      outputStream.flush();
      outputStream.close();
      socket.close();
      serverSocket.close();
    } else {
      if (privado.getChatHistory().getAudios().isEmpty()) {
        out.println("\nNo existen audios en este Chat\n");
        return;
      }

      /// PRuebaaaaaaaaaaaaa
      int optionMenu = 0;
      out.println(
        "MENU\n----------\nAudios en: " +
        privado.getChatHistory().getGroupOrPrivate() +
        "\n---------- \nSeleccione un audio para escuchar:\n 0) Salir del menu\n"
      );

      // Imprimir los audios para seleccionar
      out.println(privado.getChatHistory().printAudios());

      optionMenu = validateRange(privado.getChatHistory().getAudios());

      if (optionMenu == 0) {
        return;
      }

      ServerSocket serverSocket = new ServerSocket(12348);
      out.println("PLAYAUDIO");
      Socket socket = serverSocket.accept();
      ObjectOutputStream outputStream = new ObjectOutputStream(
        socket.getOutputStream()
      );
      outputStream.writeObject(
        privado.getChatHistory().getAudios().get(optionMenu - 1)
      );
      outputStream.flush();
      outputStream.close();
      socket.close();
      serverSocket.close();
    }
  }

  private void sendAudio(Group group, Private privado) {
    try {
      ServerSocket serverSocket = new ServerSocket(12345); // Puerto donde el servidor escucha para recibir audio
      out.println("RECORDAUDIO");
      Socket socket = serverSocket.accept(); // Espera a que un cliente se conecte
      new Thread(() -> {
        try {
          ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
          Audio audio = (Audio) in.readObject();
          // Aquí puedes procesar el objeto Audio recibido según tus necesidades
          if (privado == null) {
            int sizeAudios = group.getChatHistory().getAudios().size();
            Message m = new Message(
              clientes.getPerson(clientName),
              "Audio " + (sizeAudios + 1),
              LocalDateTime.now()
            );
            group.sendMessage(m);
            group.sendAudio(audio);
            group.notificationGroupWithoutMe("Audio", clientName);
          } else {
            int sizeAudios = privado.getChatHistory().getAudios().size();
            Message m = new Message(
              clientes.getPerson(clientName),
              "Audio " + (sizeAudios + 1),
              LocalDateTime.now()
            );
            privado.sendMessage(m);
            privado.sendAudio(audio);
            privado.notificationPrivateWithoutMe("Audio", clientName);
          }
          in.close();
          socket.close();
          serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
      })
        .start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendMenu(Group group, Private privado)
    throws IOException, LineUnavailableException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
        "MENU\n----------\nMenu Enviar\n----------" +
        "\n Seleccione una opcion de lo que dese enviar:\n 0) Salir del menu\n 1) Mensaje" +
        "\n 2) Audio" +
        "\n 3) Hacer Llamada" +
        "\n-------------------\n"
      );
      optionMenu = validateIntegerOption();

      switch (optionMenu) {
        case 0:
          exit = true;
          break;
        case 1:
          sendMessage(group, privado);
          break;
        case 2:
          sendAudio(group, privado);
          break;
        case 3:
          if (group != null) {
            if (group.getCall() != null) {
              // Existe una llamada
              out.println("ENTERTOCALL");
              System.out.println("Se fue pa llamada");
              groupCall(group, group.getCall().getPort());
            } else {
              // No existe una llamada
              playCallToGroup(group);
            }
          } else {
            if (privado.getCall() != null) {
              // Existe una llamada
              out.println("ENTERTOCALL");
              System.out.println("Se fue pa llamada");
              privateCall(privado, privado.getCall().getPort());
            } else {
              // No existe una llamada
              playCallToPrivate(privado);
            }
          }
          break;
        default:
          out.println("\n------------------\nOpcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void playCallToGroup(Group group)
    throws LineUnavailableException, IOException {
    Call actualCall = new Call(clientName, clientes.getPerson(clientName));
    actualCall.setMiembros(group.getMiembros());
    actualCall.setMiembrosBorrables(group.getCloneMiembros());
    group.setCall(actualCall);
    int port = actualCall.getPort();
    out.println("ENTERTOCALL");
    group
      .getCall()
      .broadcastMessageWithOutUser(
        clientName,
        "LLamada Grupal en: " +
        group.getName() +
        " de: " +
        clientName +
        " porfavor ingrese a la opcion 'Escribir a un grupo' y luego presione la opcion 'Hacer llamada"
      );

    groupCall(group, port);
  }

  private void playCallToPrivate(Private privado)
    throws LineUnavailableException, IOException {
    Call actualCall = new Call(clientName, clientes.getPerson(clientName));
    actualCall.setMiembros(privado.getMiembros());
    actualCall.setMiembrosBorrables(privado.getCloneMiembros());
    privado.setCall(actualCall);
    int port = actualCall.getPort();
    out.println("ENTERTOCALL");
    privado
      .getCall()
      .broadcastMessageWithOutUser(
        clientName,
        "LLamada Privada de: " +
        clientName +
        " porfavor ingrese a la opcion 'Escribir a un chat' y luego presione la opcion 'Hacer llamada"
      );

    privateCall(privado, port);
  }

  private void privateCall(Private privado, int port)
    throws LineUnavailableException, IOException {
    Call actualCall = privado.getCall();
    DatagramSocket serverSocket = actualCall.getSocket_UDP();
    Thread callThread = new Thread(() -> {
      try {
        byte[] receiveData = new byte[800];
        Person p = privado.getOtherPerson(clientName);
        // Enviar Datagramas a todos menos a si mismo
        while (true) {
          DatagramPacket receivePacket = new DatagramPacket(
            receiveData,
            receiveData.length
          );
          serverSocket.receive(receivePacket);

          DatagramPacket sendPacket = new DatagramPacket(
            receivePacket.getData(),
            receivePacket.getLength(),
            p.getAddress(),
            p.getPort()
          );
          try {
            serverSocket.send(sendPacket);
          } catch (IOException e) {
            if (e instanceof SocketException) {
              System.out.println("Fin de la llamada");
            } else {
              e.printStackTrace();
            }
          }
        }
      } catch (Exception e) {
        if (e instanceof SocketException) {
          System.out.println("Fin de la llamada");
        } else {
          e.printStackTrace();
        }
      }
    });
    callThread.start();
    out.println("Para colgar la llamada presione '0'");
    while (true) {
      if (in.readLine().equals("0")) {
        out.println("STOPCALL");
        callThread.interrupt();
        serverSocket.close();
        privado.setCall(null);
        return;
      }
    }
  }

  private void groupCall(Group group, int port)
    throws LineUnavailableException, IOException {
    Call actualCall = group.getCall();
    DatagramSocket serverSocket = actualCall.getSocket_UDP();
    Thread callThread = new Thread(() -> {
      try {
        byte[] receiveData = new byte[800];

        // Enviar Datagramas a todos menos a si mismo
        while (true) {
          DatagramPacket receivePacket = new DatagramPacket(
            receiveData,
            receiveData.length
          );
          serverSocket.receive(receivePacket);
          for (Person member : group.getMiembros()) {
            if (!member.getName().equals(clientName)) {
              DatagramPacket sendPacket = new DatagramPacket(
                receivePacket.getData(),
                receivePacket.getLength(),
                member.getAddress(),
                member.getPort()
              );
              try {
                serverSocket.send(sendPacket);
              } catch (IOException e) {
                if (e instanceof SocketException) {
                  System.out.println("Fin de la llamada");
                } else {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      } catch (Exception e) {
        if (e instanceof SocketException) {
          System.out.println("Fin de la llamada");
        } else {
          e.printStackTrace();
        }
      }
    });
    callThread.start();
    out.println("Para colgar la llamada presione '0'");
    while (true) {
      if (in.readLine().equals("0")) {
        out.println("STOPCALL");
        callThread.interrupt();
        serverSocket.close();
        group.setCall(null);
        return;
      }
    }
  }

  private void sendMessage(Group group, Private privado) throws IOException {
    out.println("\n --------- Ingrese su mensaje: --------- \n");
    String message = in.readLine();
    Message m = new Message(
      clientes.getPerson(clientName),
      message,
      LocalDateTime.now()
    );
    // Si es privado o al grupo lo manda
    if (privado == null) {
      group.sendMessage(m);
      group.notificationGroupWithoutMe("Mensaje", clientName);
    } else {
      privado.sendMessage(m);
      privado.notificationPrivateWithoutMe("Mensaje", clientName);
    }
    out.println("\n --------- Mensaje enviado --------- \n");
  }

  private void writeToGroup() throws IOException, LineUnavailableException {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());
    int optionMenu = 0;

    if (listaGrupos.isEmpty()) {
      out.println(
        "No existen grupos creados, intenta la opcion 'Crear un nuevo grupo'"
      );
      return;
    }

    out.println(
      "MENU\n----------\nGrupos Registrados\n---------- \n\nSeleccione un grupo para ingresar:\n 0) Salir del menu\n"
    );

    // Imprimir grupos
    out.println(grupos.printMyGroups(clientName));

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
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

  private void joinToGroup() throws IOException {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());
    int optionMenu = 0;

    if (listaGrupos.isEmpty()) {
      out.println(
        "No existen grupos creados, intenta la opcion 'Crear un nuevo grupo'"
      );
      return;
    }

    out.println(
      "MENU\n----------\nGrupos Registrados\n---------- \n\nSeleccione un grupo para ingresar:\n 0) Salir del menu\n"
    );

    // Imprimir grupos
    out.println(grupos.printGroups() + "\n");

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
      return;
    }

    // Agregando usuario al grupo
    if (listaGrupos.get(optionMenu - 1).existeUsr(clientName)) {
      out.println("\nEl usuario ya esta en el grupo no puede ingresar\n");
    } else {
      out.println(
        "\nIngresando al usuario " +
        clientName +
        " al grupo: " +
        listaGrupos.get(optionMenu - 1).getName() +
        "\n"
      );
      listaGrupos
        .get(optionMenu - 1)
        .getMiembros()
        .add(clientes.getPerson(clientName));
    }
  }

  private int validateRange(List listToValidate) throws IOException {
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
      if (groupName == null) {
        return;
      }
      // sincronizando
      synchronized (groupName) {
        if (!groupName.isBlank() && !grupos.existeGroup(groupName)) {
          clientes.broadcastMessage("Nuevo grupo creado: " + groupName);
          out.println("NAMEACCEPTED");
          grupos.addGroup(groupName, out, clientes.getPerson(clientName));
          break;
        }
      }
    }
  }

  private void membersOfAGroup() throws IOException {
    List<Group> listaGrupos = new ArrayList<>(grupos.getGroups());
    int optionMenu = 0;

    if (listaGrupos.isEmpty()) {
      out.println(
        "No existen grupos creados, intenta la opcion 'Crear un nuevo grupo'"
      );
      return;
    }

    out.println(
      "MENU\n----------\nVer miembros de un grupo\n---------- \n\nSeleccione un grupo para ver sus miembros:\n 0) Salir del menu\n"
    );

    // Imprimir grupos
    out.println(grupos.printGroups() + "\n");

    optionMenu = validateRange(listaGrupos);

    if (optionMenu == 0) {
      return;
    }

    // Imprimir personas de los grupos
    out.println(listaGrupos.get(optionMenu - 1).printMembers() + "\n");
  }

  private void privateMenu() throws IOException, LineUnavailableException {
    int optionMenu = 0;
    boolean exit = false;
    do {
      out.println(
        "MENU\n----------\nMenu Privados\n---------- \n\nSeleccione una opcion:\n 0) Salir del menu\n 1) Crear un nuevo chat" +
        "\n 2) Escribir a un chat" +
        "\n-------------------\n"
      );
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
          out.println("\n------------------\nOpcion incorrecta!\n");
          break;
      }
    } while (exit == false);
  }

  private void writeToPrivate() throws IOException, LineUnavailableException {
    List<Private> listaPrivados = new ArrayList<>(
      privados.getMyPrivates(clientName)
    );
    int optionMenu = 0;

    if (listaPrivados.isEmpty()) {
      out.println(
        "\nNo tienes chat privados, intenta la opcion 'Crear un nuevo chat'\n"
      );
      return;
    }

    out.println(
      "MENU\n----------\nChat Privados\n---------- \n\nSeleccione un chat privado para ingresar:\n 0) Salir del menu\n"
    );

    // Imprimir privados
    out.println(printListMyPrivates(listaPrivados, clientName));

    optionMenu = validateRange(listaPrivados);

    if (optionMenu == 0) {
      privateMenu();
      return;
    }

    if (listaPrivados.get(optionMenu - 1).areYouHereInThisPrivate(clientName)) {
      // El usuario si está en el privado y puede entrar al menú del Chat
      chatMenu(null, listaPrivados.get(optionMenu - 1));
    } else {
      out.println("\nNo estas en este privado, bye bye\n");
    }
  }

  private String printListMyPrivates(List<Private> list, String client) {
    String m = "";
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPerson1().getName().equals(client)) {
        m += ((i + 1) + ") " + list.get(i).getPerson2().getName() + "\n");
      } else {
        m += ((i + 1) + ") " + list.get(i).getPerson1().getName() + "\n");
      }
    }
    return m;
  }

  private void createChatPrivate()
    throws IOException, LineUnavailableException {
    List<Person> listaClientes = new ArrayList<>(clientes.getClientes());
    int optionMenu = 0;

    if (listaClientes.isEmpty()) {
      out.println("\nNo existen mas usuarios por ahora! \n");
      return;
    }

    out.println(
      "MENU\n----------\nPersonas de la aplicacion\n---------- \n\nSeleccione una persona para iniciar un Chat Privado:\n 0) Salir del menu\n"
    );

    // Imprimir los clientes para crear el nuevo Privado
    out.println(clientes.printClientesWithoutMe(clientName));

    optionMenu = validateRange(listaClientes);

    if (
      optionMenu == 0 ||
      listaClientes.get(optionMenu - 1).equals(clientes.getPerson(clientName))
    ) {
      out.println("\n ¿Para que quieres un grupo contigo mismo? \n");
      privateMenu();
      return;
    }

    // Creando el nuevo privado
    String other = listaClientes.get(optionMenu - 1).getName();
    if (privados.existePrivate(clientName, other)) {
      out.println(
        "\nYa tienes un Chat Privado con esta persona, intenta desde la opcion 'Escribir a un chat'\n"
      );
    } else {
      out.println(
        "\n Creando un nuevo Chat privado entre " +
        other +
        " y " +
        clientName +
        "\n"
      );
      Private p = new Private(
        clientes.getPerson(clientName),
        clientes.getPerson(other)
      );
      privados.addPrivate(p);
      p.notificationPrivateWithoutMe("Nuevo Privado creado ", clientName);
    }
  }
}
