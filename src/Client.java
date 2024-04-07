import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

  private static final String SERVER_IP = "127.0.0.1";
  private static final int PORT = 6789;

  public static void main(String[] args) {
    try {
      Socket socket = new Socket(SERVER_IP, PORT);
      System.out.println("\nConectado al servidor.\n");
      String message;
      BufferedReader userInput = new BufferedReader(
        new InputStreamReader(System.in)
      );
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream())
      );

      while ((message = in.readLine()) != null) {
        // repetir el ciclo hasta que no ingrese un nombre valido
        if (message.startsWith("SUBMITNAME")) {
          System.out.print("\nIngrese nombre de usuario: \n");
          String name = userInput.readLine();
          out.println(name);
        } else if (message.startsWith("NAMEACCEPTED")) {
          System.out.println("\n¡Nombre aceptado!\n");
          break;
        }
      }

      // creamos el objeto lector e iniciamos el hilo
      Lector l = null;
      Lector lector = new Lector(in, out, SERVER_IP);
      new Thread(lector).start();
      lector.getClass();
      // estar atento a la entrada del usuario
      while ((message = userInput.readLine()) != null) {
        out.println(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
