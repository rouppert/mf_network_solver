import java.util.ArrayList;
import java.util.List;

/*
Represents an element, channel or module, of a microfluidic network.
 */
public class Module {
    private final List<Module> successors;
    private final String name;
    private final int headerLength;
    private final int payloadLength;
    private final boolean def;
    private boolean occupied;

    // ---- Getters ----
    public List<Module> getSuccessors() {
        return successors;
    }

    public String getName() {
        return name;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public boolean isDef() {
        return def;
    }

    public boolean isChannel() {
        return headerLength > 0;
    }

    public boolean isSensor() {
        return headerLength == 0;
    }

    public boolean isOccupied() {
        return occupied;
    }
    // ------------

    public Module(String name, int headerLength, int payloadLength, boolean def) {
        this.name = name;
        this.headerLength = headerLength;
        this.payloadLength = payloadLength;
        this.def = def;
        this.successors = new ArrayList<>();
        this.occupied = false;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void addSuccessor(Module successor) {
        successors.add(successor);
    }
}
