import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Lector implements Runnable {

  String message;
  BufferedReader in;
  PrintWriter out;
  BufferedReader userInput = new BufferedReader(
    new InputStreamReader(System.in)
  );
  String adress;
  AudioRecorderPlayer reproductor;

  public Lector(BufferedReader in, PrintWriter out, String adress) {
    this.in = in;
    this.out = out;
    this.adress = adress;
    this.reproductor = new AudioRecorderPlayer();
  }

  @Override
  public void run() {
    // leer la linea que envia el servidor e imprimir en pantalla
    try {
      while ((message = in.readLine()) != null) {
        switch (message) {
          case "MENU":
            mainMenu();
          default:
            System.out.println("Informacion desconocida recibida del Server");
            break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void mainMenu() throws IOException, LineUnavailableException {
    while ((message = in.readLine()) != null) {
      switch (message) {
        case "CREATENEWGROUP":
          createNewGroup();
          break;
        case "CALLSTARTED":
          try {
            callToGroup();
            System.out.println("VENECOS");
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          break;
        case "RECORDAUDIO":
          recordAudio(5, "localhost", 7000);
          break;
        case "PLAYAUDIO":
          playAudio();
          break;
        default:
          System.out.println(message);
          break;
      }
    }
  }

  private void playAudio() {
    try {
      System.out.println("\nEstoy en Play Audio\n");
      Socket socket = new Socket("localhost", 12348); // Conéctate al servidor en el puerto 12345
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

      // Leer el objeto Audio enviado por el servidor
      Audio audio = (Audio) in.readObject();

      // Cerrar los flujos de entrada y el socket
      in.close();
      socket.close();

      reproductor.play(audio);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void recordAudio(int duration, String serverAddress, int serverPort)
    throws LineUnavailableException, IOException {
    System.out.println("\nVa para el metodo de grabar\n");
    reproductor.record();
    System.out.println("\nYa grabo el audio\n");

    try {
      Socket socket = new Socket("localhost", 12345); // Cambia "localhost" y 12345 por la dirección y puerto del
      // servidor
      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("\nVa a enviar el audio\n");
      out.writeObject(reproductor.getAudioToSend());
      System.out.println("\nEnvio el audio\n");
      out.close();
      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void callToGroup() throws Exception {
    DatagramSocket clientSocket = new DatagramSocket();
    AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 2, true, false);
    DataLine.Info dataLineInfo = new DataLine.Info(
      TargetDataLine.class,
      audioFormat
    );
    TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(
      dataLineInfo
    );
    targetDataLine.open(audioFormat);
    targetDataLine.start();

    byte[] sendData = new byte[1024];

    InetAddress serverAddress = InetAddress.getByName("localhost");

    int serverPort = 6789;

    while (true) {
      int bytesRead = targetDataLine.read(sendData, 0, sendData.length);

      DatagramPacket sendPacket = new DatagramPacket(
        sendData,
        bytesRead,
        serverAddress,
        serverPort
      );
      clientSocket.send(sendPacket);
    }
  }

  private void createNewGroup() throws IOException {
    try {
      while ((message = in.readLine()) != null) {
        // repetir el ciclo hasta que no ingrese un nombre valido
        System.out.println("\nSe recibio..." + message);
        if (message.startsWith("SUBMITNAME")) {
          System.out.print("\nIngrese nombre del grupo: \n");
        } else if (message.startsWith("NAMEACCEPTED")) {
          System.out.println("\nNombre del grupo aceptado!\n");
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
