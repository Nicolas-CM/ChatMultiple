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
                        System.out.println("Informacion desconocida recibida del Server");
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
                    try {
                        callToGroup();
                        System.out.println("VENECOS");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println(message);
                    break;
            }

        }
    }

    private void callToGroup() throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 2, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();

        byte[] sendData = new byte[1024];

        InetAddress serverAddress = InetAddress.getByName("localhost");

        int serverPort = 6789;

        while (true) {
            int bytesRead = targetDataLine.read(sendData, 0, sendData.length);

            DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, serverAddress, serverPort);
            clientSocket.send(sendPacket);
        }
    }

    private void createNewGroup() throws IOException {
        try {
            while ((message = in.readLine()) != null) {
                // repetir el ciclo hasta que no ingrese un nombre valido
                System.out.println("Se recibio...." + message);
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