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
import java.net.UnknownHostException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Lector implements Runnable {

  String message;
  BufferedReader in;
  PrintWriter out;
  BufferedReader userInput;
  String adress;
  AudioRecorderPlayer reproductor;

  TargetDataLine microphone;
  SourceDataLine speakers;
  volatile boolean stopCall;
  AudioFormat format;
  DatagramSocket callSocket;
  int serverSocketUDP;
  InetAddress ipInetAddress;

  public Lector(
    BufferedReader in,
    PrintWriter out,
    String adress,
    int serverSocketUDP,
    DatagramSocket callSocket
  ) throws LineUnavailableException, UnknownHostException {
    this.in = in;
    this.out = out;
    this.adress = adress;
    this.reproductor = new AudioRecorderPlayer();
    this.microphone = AudioSystem.getTargetDataLine(format);
    this.speakers = AudioSystem.getSourceDataLine(format);
    this.stopCall = false;
    this.callSocket = callSocket;
    this.format = new AudioFormat(24000.0f, 16, 1, true, true);
    this.serverSocketUDP = serverSocketUDP;
    this.userInput = new BufferedReader(new InputStreamReader(System.in));
    this.ipInetAddress = InetAddress.getByName(adress);
  }

  @Override
  public void run() {
    // leer la linea que envia el servidor e imprimir en pantalla
    boolean end = false;
    try {
      while ((message = in.readLine()) != null || !end) {
        switch (message) {
          case "MENU":
            mainMenu();
            System.out.println(
              "Escriba 'exit' para cerrar la terminal. Gracias por usar nuestra aplicacion"
            );
            return;
          default:
            System.out.println("Informacion desconocida recibida del Server");
            break;
        }
      }
    } catch (IOException e) {
      System.out.println();
      System.out.println(
        "Servidor desconectado \n Escriba 'exit' para cerrar la terminal. Gracias por usar nuestra aplicacion"
      );
    } catch (LineUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void call() {
    stopCall = false;
    // Crear e iniciar un hilo para enviar voz
    Thread sendVoiceThread = new Thread(() -> {
      sendVoice();
    });
    sendVoiceThread.start();

    // Crear e iniciar un hilo para recibir voz
    Thread receiveVoiceThread = new Thread(() -> {
      receiveVoice();
    });
    receiveVoiceThread.start();
  }

  public void stopCall() {
    stopCall = true;
    // Detener el envío de voz
    microphone.stop();
    microphone.close();

    // Detener la recepción de voz
    speakers.stop();
    speakers.close();

    System.out.println("Llamada detenida.");
  }

  public void sendVoice() {
    try {
      microphone.open(format);
      microphone.start();

      byte[] buffer = new byte[800];
      DatagramPacket packet;

      System.out.println("Llamando");

      while (!stopCall) {
        int bytesRead = microphone.read(buffer, 0, buffer.length);
        packet =
          new DatagramPacket(buffer, bytesRead, ipInetAddress, serverSocketUDP);
        try {
          callSocket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void receiveVoice() {
    try {
      speakers.open(format);
      speakers.start();

      byte[] buffer = new byte[800];

      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      while (!stopCall) {
        callSocket.receive(packet);
        speakers.write(packet.getData(), 0, packet.getLength());
      }
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void mainMenu() throws IOException, LineUnavailableException {
    while ((message = in.readLine()) != null) {
      switch (message) {
        case "FINISH":
          return;
        case "CREATENEWGROUP":
          createNewGroup();
          break;
        case "RECORDAUDIO":
          recordAudio(5, adress, 12345);
          break;
        case "PLAYAUDIO":
          playAudio();
          break;
        case "ENTERTOCALL":
          call();
          break;
        case "STOPCALL":
          stopCall();
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
      Socket socket = new Socket(adress, 12348); // Conéctate al servidor en el puerto 12345
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
      Socket socket = new Socket(serverAddress, serverPort); // Cambia "localhost" y 12345 por la dirección y puerto del
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
