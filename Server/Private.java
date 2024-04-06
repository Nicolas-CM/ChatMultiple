public class Private {

    private Person person1;
    private Person person2;

    private ChatHistory chatHistory;

    public Private(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
        this.chatHistory = new ChatHistory(person1.getName() + "-" + person2.getName());
    }

    public boolean existePrivate(String me, String other) {
        if (me.equals(person1.getName()) && other.equals(person2.getName())) {
            return true;
        } else if (other.equals(person1.getName()) && me.equals(person2.getName())) {
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

    public String getHistorial(boolean lastTen) {
        return chatHistory.getHistorial(lastTen);
    }

}
