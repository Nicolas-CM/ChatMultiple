import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private Person sender;
    private String content;
    private String time;

    public Message(Person sender, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");

        // Formatear la fecha y hora según el formateador personalizado
        String fechaHoraFormateada = timestamp.format(formatter);
        this.time = fechaHoraFormateada;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return time;
    }

    public void setTimestamp(String time) {
        this.time = time;
    }

}
