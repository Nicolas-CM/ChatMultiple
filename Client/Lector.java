import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Lector implements Runnable {
    String message;
    BufferedReader in;
    PrintWriter out;
    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

    public Lector(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
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
                        System.out.println("Información extraña recibida del Server");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void mainMenu() throws IOException {
        while ((message = in.readLine()) != null) {
            switch (message) {
                case "CREATENEWGROUP":
                    createNewGroup();
                    break;
                case "CALLSTARTED":
                    callToGroup();
                    break;
                default:
                    System.out.println(message);
                    break;
            }

        }
    }

    private void callToGroup() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 1234;

            AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 2, true, false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while (true) {
                bytesRead = targetDataLine.read(buffer, 0, buffer.length);

                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, serverAddress, serverPort);
                datagramSocket.send(packet);

                if (System.in.available() > 0 && System.in.read() == '\n') {
                    datagramSocket.close();
                    break;
                }

            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewGroup() throws IOException {
        try {
            while ((message = in.readLine()) != null) {
                // repetir el ciclo hasta que no ingrese un nombre valido
                System.out.println("Se recibió...." + message);
                if (message.startsWith("SUBMITNAME")) {
                    System.out.print("Ingrese nombre del grupo: ");

                } else if (message.startsWith("NAMEACCEPTED")) {
                    System.out.println("Nombre aceptado del grupo!!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}