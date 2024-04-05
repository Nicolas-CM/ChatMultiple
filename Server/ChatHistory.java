import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private List<Message> mensajes = new ArrayList<>();
    private String groupOrMembers;

    public ChatHistory(String groupOrMembers) {
        this.groupOrMembers = groupOrMembers;
    }

    public String getGroupOrPrivate() {
        return groupOrMembers;
    }

    public void setGroupOrMembers(String groupOrMembers) {
        this.groupOrMembers = groupOrMembers;
    }
    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }

    public void sendMessage(Message m) {
        mensajes.add(m);
    }

    public String getHeadHistorial() {
        if (mensajes.size() == 0) {
            return "\n No existen mensajes en Historial: " + this.getGroupOrPrivate();
        }
        int startIndex = Math.max(0, mensajes.size() - 10); // Indice de inicio
        int endIndex = mensajes.size(); // Indice de fin (último mensaje)
        // Se crea una sublista con los últimos 10 mensajes
        List<Message> head = mensajes.subList(startIndex, endIndex);
        String historialHead = " \n\nHISTORIAL: " + this.getGroupOrPrivate() + "\n MENSAJES: ";
        for (Message m : head) {
            historialHead += "\n " + m.getSender().getName() + ": " + m.getContent() + " ["
                    + m.getTimestamp().toString() + "]";
        }
        return historialHead + "\n";
    }

    public String getAllMessages() {
        if (mensajes.size() == 0) {
            return "\n No existen mensajes Historial: " + this.getGroupOrPrivate();
        }
        String todos = " \n\nHISTORIAL: " + this.getGroupOrPrivate() + "\n MENSAJES: ";
        for (Message m : mensajes) {
            todos += "\n " + m.getSender().getName() + ": " + m.getContent() + " [" + m.getTimestamp().toString() + "]";
        }
        return todos + "\n";
    }
}
