//S

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private List<Message> mensajes = new ArrayList<>();
    private List<Audio> audios = new ArrayList<>();
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

    public List<Audio> getAudios() {
        return audios;
    }

    public void setAudios(List<Audio> audios) {
        this.audios = audios;
    }

    public void sendMessage(Message m) {
        mensajes.add(m);
    }

    public void sendAudio(Audio audio) {
        audios.add(audio);
    }

    public String getHistorial(boolean lastTen) {
        if (mensajes.size() == 0) {
            return "\n No existen mensajes en Historial: " + this.getGroupOrPrivate();
        }

        String historial = "\n\nHISTORIAL: " + this.getGroupOrPrivate() + "\n MENSAJES: ";

        if (lastTen) {
            int startIndex = Math.max(0, mensajes.size() - 10); // Indice de inicio
            int endIndex = mensajes.size(); // Indice de fin (último mensaje)
            // Se crea una sublista con los últimos 10 mensajes
            List<Message> head = mensajes.subList(startIndex, endIndex);
            for (Message m : head) {
                historial += "\n " + m.getSender().getName() + ": " + m.getContent() + " ["
                        + m.getTimestamp().toString() + "]";
            }
        } else {
            for (Message m : mensajes) {
                historial += "\n " + m.getSender().getName() + ": " + m.getContent() + " ["
                        + m.getTimestamp().toString() + "]";
            }
        }

        return historial + "\n";
    }

    public String printAudios() {
        String m = "";
        for (int i = 0; i < audios.size(); i++) {
            m += ((i + 1) + ") Audio " + (i + 1) + "\n");
        }
        return m;
    }

}
