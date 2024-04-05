public class Private {

    private Person person1;
    private Person person2;

    private ChatHistory chatHistory;

    public Private(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
        this.chatHistory = new ChatHistory(person1.getName() + "-" + person2.getName());
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

    public String getHeadHistorial() {
        return chatHistory.getHeadHistorial();
    }

    public String getAllMessages() {
        return chatHistory.getAllMessages();
    }

}
