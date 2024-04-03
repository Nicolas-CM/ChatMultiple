import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;


public class Chatters{


    private Set<Person> clientes = new HashSet<>();
     
    public Chatters() {
    }

    public Person getPerson(String name) {
        if (existeUsr(name)) {
            for (Person person : clientes) {
                if (person.getName().equals(name)) {
                    return person;
                }
            }
        }
        return null;
    }

    public boolean existeUsr(String name){
        boolean response = false;
        for(Person p: clientes){
            if(name.equals(p.getName())){
            response=true;
            break;}
        }
        return response;
    }

    public void addUsr(String name, PrintWriter out){
        if (!name.isBlank() && !existeUsr(name)) {
            Person p = new Person(name, out);
            clientes.add(p);
        }
    } 
    
    public void removeUsr(String name){
        for(Person p: clientes){
            if(name.equals(p.getName())){
                clientes.remove(p);
                break;}
        }
    }


   public void broadcastMessage(String message){
        
        for (Person p: clientes) {
            p.getOut().println(message);
        }
   }

   //enviar un mensaje privado a la persona con un nombre dado nameDest
   public void sendPrivateMessage(String nameSrc,String nameDest, String message){
        for(Person p: clientes){
            if(nameDest.equals(p.getName())){
                p.getOut().println("[Chat privado de "+nameSrc + "]: "+message);
                break;}
        }


   }

}