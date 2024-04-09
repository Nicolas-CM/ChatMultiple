//S

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server {

  private static Set<PrintWriter> writers = new HashSet<>();

  public static void main(String[] args) {
    int PORT = 6789;
    Chatters clientes = new Chatters();
    Comunity grupos = new Comunity();
    Personals privados = new Personals();

    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      System.out.println("\nServidor iniciado. Esperando clientes...");

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("\nNuevo cliente conectado: " + clientSocket + "\n");
        // crea el objeto Runable
        ClientHandler clientHandler = new ClientHandler(
          clientSocket,
          clientes,
          grupos,
          privados
        );
        // inicia el hilo con el objeto Runnable
        new Thread(clientHandler).start();
      }
    } catch (Exception e) {
      if (e instanceof SocketException) {
        System.out.println("Cliente desconectado");
      }
    }
  }
}
