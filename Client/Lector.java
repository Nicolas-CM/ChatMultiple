import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
                    case "CREATENEWGROUP":
                        createNewGroup();
                        break;
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
            if (message.equals("FINISH")) {
                createNewGroup();
            } else {
                System.out.println(message);
            }

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