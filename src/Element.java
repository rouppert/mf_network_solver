import java.util.ArrayList;
import java.util.List;

/*
Represents an element, channel or module, of a microfluidic network.
 */
public class Element {
    private final List<Element> successors;
    private final String name;
    private final int length;
    private final boolean def;

    // ---- Getters ----
    public List<Element> getSuccessors() {
        return successors;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public boolean isDef() {
        return def;
    }
    // ------------

    public Element(String name, int length, boolean def) {
        this.name = name;
        this.length = length;
        this.def = def;
        this.successors = new ArrayList<>();
    }

    public void addSuccessor(Element successor) {
        successors.add(successor);
    }
}
