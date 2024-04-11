//S

import java.util.HashSet;
import java.util.Set;

public class Private {

  private Person person1;
  private Person person2;
  private Call call;

  private ChatHistory chatHistory;

  public ChatHistory getChatHistory() {
    return chatHistory;
  }

  public void setChatHistory(ChatHistory chatHistory) {
    this.chatHistory = chatHistory;
  }

  public Set<Person> getMiembros() {
    Set<Person> miemmbros = new HashSet<>();
    miemmbros.add(person1);
    miemmbros.add(person2);
    return miemmbros;
  }

  public Set<Person> getCloneMiembros() {
    Set<Person> miembrosClone = new HashSet<>();
    miembrosClone.add(((Person) person1.clone()));
    miembrosClone.add(((Person) person2.clone()));
    return miembrosClone;
  }

  public Private(Person person1, Person person2) {
    this.person1 = person1;
    this.person2 = person2;
    this.chatHistory =
      new ChatHistory(person1.getName() + "-" + person2.getName());
  }

  public void notificationPrivateWithoutMe(
    String typeMessage,
    String clientName
  ) {
    if (this.person1.getName().equals(clientName)) {
      this.person2.out.println(
          typeMessage + " privado recibido desde: " + clientName
        );
    } else {
      this.person1.out.println(
          "Mensaje privado recibido desde: " + this.person2.getName()
        );
    }
  }

  public boolean existePrivate(String me, String other) {
    if (me.equals(person1.getName()) && other.equals(person2.getName())) {
      return true;
    } else if (
      other.equals(person1.getName()) && me.equals(person2.getName())
    ) {
      return true;
    } else {
      return false;
    }
  }

  public boolean areYouHereInThisPrivate(String name) {
    return (name.equals(person1.getName()) || name.equals(person2.getName()));
  }

  public Person getPerson1() {
    return person1;
  }

  public void setPerson1(Person person1) {
    this.person1 = person1;
  }

  public Person getPerson2() {
    return person2;
  }

  public void setPerson2(Person person2) {
    this.person2 = person2;
  }

  public void sendMessage(Message m) {
    chatHistory.sendMessage(m);
  }

  public void sendAudio(Audio audio) {
    chatHistory.sendAudio(audio);
  }

  public String getHistorial(boolean lastTen) {
    return chatHistory.getHistorial(lastTen);
  }

  public Person getOtherPerson(String name) {
    if (name.equals(person1.getName())) {
      return person2;
    } else {
      return person1;
    }
  }

  public Call getCall() {
    return call;
  }

  public void setCall(Call call) {
    this.call = call;
  }
}
