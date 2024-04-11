import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.sound.sampled.LineUnavailableException;

public class Client {

  private static final String SERVER_IP = "192.168.93.242";
  private static final int PORT = 6789;
  private static final int PORT_UDP = 9999;

  public static void main(String[] args) {
    try {
      Socket socket = new Socket(SERVER_IP, PORT);
      System.out.println("\nConectado al servidor.");
      String message;
      BufferedReader userInput = new BufferedReader(
        new InputStreamReader(System.in)
      );
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream())
      );

      //Socket socketUDP = new Socket(SERVER_IP, PORT_UDP);

      while ((message = in.readLine()) != null) {
        // repetir el ciclo hasta que no ingrese un nombre valido
        if (message.startsWith("SUBMITNAME")) {
          System.out.print("\nIngrese nombre de usuario: \n");
          String name = userInput.readLine();
          out.println(name);
        } else if (message.startsWith("NAMEACCEPTED")) {
          out.println("sikas/9999");
          System.out.println("\nNombre aceptado!\n");
          break;
        }
      }

      // creamos el objeto lector e iniciamos el hilo
      Lector lector = new Lector(in, out, SERVER_IP, PORT_UDP);
      new Thread(lector).start();
      // estar atento a la entrada del usuario
      while ((message = userInput.readLine()) != null) {
        if (message.trim().toLowerCase().equals("exit")) {
          return;
        } else {
          out.println(message);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
