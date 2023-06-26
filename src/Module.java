import java.util.ArrayList;
import java.util.List;

/*
Represents an element, channel or module, of a microfluidic network.
 */
public class Module {
    private final List<Module> successors;
    private final String name;
    private final int length;
    private final boolean def;

    // ---- Getters ----
    public List<Module> getSuccessors() {
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

    public Module(String name, int length, boolean def) {
        this.name = name;
        this.length = length;
        this.def = def;
        this.successors = new ArrayList<>();
    }

    public void addSuccessor(Module successor) {
        successors.add(successor);
    }
}
