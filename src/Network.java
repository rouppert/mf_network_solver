import java.util.ArrayList;
import java.util.List;

/*
Represents a droplet-based microfluidic network.
 */
public class Network {
    private final List<Element> elements;
    private Element origin;
    private Element wasteChamber;

    public Network() {
        this.elements = new ArrayList<>();
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void setOrigin(Element origin) {
        if (elements.contains(origin)) {
            this.origin = origin;
        } else {
            System.err.println(origin.getName() + " : this element doesn't belong to the network.");
        }
    }

    public void setWasteChamber(Element wasteChamber) {
        if (elements.contains(wasteChamber)) {
            this.wasteChamber = wasteChamber;
        } else {
            System.err.println(wasteChamber.getName() + " : this element doesn't belong to the network.");
        }
    }

    public void generateFromXml() {}

    public Path generatePath(Element target) {
        return null;
    }
}
