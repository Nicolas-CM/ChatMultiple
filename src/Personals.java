//S

import java.util.HashSet;
import java.util.Set;

public class Personals {

    private Set<Private> privados = new HashSet<>();

    public Personals() {
    }

    public void addPrivate(Person me, Person other) {
        privados.add(new Private(me, other));
    }

    public boolean existePrivate(String me, String other) {
        for (Private p : privados) {
            if (p.existePrivate(me, other)) {
                return true;
            }
        }
        return false;
    }

    public Set<Private> getMyPrivates(String name) {
        Set<Private> myPrivates = new HashSet<Private>();
        for (Private p : privados) {
            if (p.areYouHereInThisPrivate(name)) {
                myPrivates.add(p);
            }
        }

        return myPrivates;
    }

    public Set<Private> getPrivados() {
        return privados;
    }

    public void setPrivados(Set<Private> privados) {
        this.privados = privados;
    }

}
