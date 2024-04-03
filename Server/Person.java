import java.io.PrintWriter;

//
public class Person {
    private String name;
    PrintWriter out;

    public Person(String name, PrintWriter out){
        this.name = name;
        this.out  = out;
    }
   
    public String getName() {
        return name;
    }
    
    public PrintWriter getOut() {
        return out;
    }

    
}