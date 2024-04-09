import java.io.PrintWriter;
import java.net.InetAddress;

//
public class Person implements Cloneable {

  private String name;
  PrintWriter out;
  private int port;
  private InetAddress address;

  public Person(String name, PrintWriter out, int port, InetAddress address) {
    this.name = name;
    this.out = out;
    this.port = port;
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

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
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
