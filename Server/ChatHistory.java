import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private List<Message> mensajes = new ArrayList<>();

    public String getGroupOrPrivate() {
        return groupOrMembers;
    }

    public void setGroupOrMembers(String groupOrMembers) {
        this.groupOrMembers = groupOrMembers;
    }

    private String groupOrMembers;

    public ChatHistory(String groupOrMembers) {
        this.groupOrMembers = groupOrMembers;
    }

    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }

    public String getHeadHistorial() {
        if (mensajes.size() == 0) {
            return "\n No existen mensajes en Historial: " + this.getGroupOrPrivate();
        }
        int startIndex = Math.max(0, mensajes.size() - 10); // Índice de inicio
        int endIndex = mensajes.size(); // Índice de fin (último mensaje)
        // Se crea una sublista con los últimos 10 mensajes
        List<Message> head = mensajes.subList(startIndex, endIndex);
        String historialHead = " HISTORIAL: " + this.getGroupOrPrivate() + "\n MENSAJES: ";
        for (Message m : head) {
            historialHead += "\n " + m.getSender() + ": " + m.getContent() + " [" + m.getTimestamp().toString() + "]";
        }
        return historialHead;
    }

    public String getAllMessages() {
        if (mensajes.size() == 0) {
            return "\n No existen mensajes Historial: " + this.getGroupOrPrivate();
        }
        String todos = " HISTORIAL: " + this.getGroupOrPrivate() + "\n MENSAJES: ";
        for (Message m : mensajes) {
            todos += "\n " + m.getSender() + ": " + m.getContent() + " [" + m.getTimestamp().toString() + "]";
        }
        return todos;
    }
}
