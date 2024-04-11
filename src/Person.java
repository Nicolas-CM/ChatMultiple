import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//
public class Person implements Cloneable {

  private String name;
  PrintWriter out;
  private InetAddress address;
  private Socket call;

  public Socket getCall() {
    return call;
  }

  public void setCall(Socket call) {
    this.call = call;
  }

  public Person(String name, PrintWriter out, InetAddress address) {
    this.name = name;
    this.out = out;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public PrintWriter getOut() {
    return out;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOut(PrintWriter out) {
    this.out = out;
  }

  public InetAddress getAddress() {
    return address;
  }

  public void setAddress(InetAddress address) {
    this.address = address;
  }

  @Override
  public Person clone() {
    try {
      Person copia = (Person) super.clone();
      return copia;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("La clonación no es compatible", e);
    }
  }
}
