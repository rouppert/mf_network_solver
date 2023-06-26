import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Represents a droplet-based microfluidic network.
 */
public class Network {
    private final Map<Integer, Module> modules;
    private Module origin;
    private Module wasteChamber;

    public Network() {
        this.modules = new HashMap<>();
    }

    public void addModule(int ID, Module module) {
        modules.put(ID, module);
    }

    public void generateFromXml(String filename) {
        File file = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList moduleNodes = document.getElementsByTagName("module");
            int nodesCount = moduleNodes.getLength();

            // Getting the list of the modules for the network
            for (int i = 0; i < nodesCount; i++) {
                Node node = moduleNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    int ID = Integer.parseInt(node.getAttributes().getNamedItem("ID").getNodeValue());
                    String name = el.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                    int length = Integer.parseInt(el.getElementsByTagName("length").item(0).getChildNodes().item(0).getNodeValue());
                    boolean def = Boolean.parseBoolean(el.getElementsByTagName("def").item(0).getChildNodes().item(0).getNodeValue());
                    Module module = new Module(name, length, def);
                    addModule(ID, module);
                }
            }

            // For each module in the network, get the list of its successors
            for(int i = 0; i < nodesCount; i++) {
                Node node = moduleNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    int ID = Integer.parseInt(node.getAttributes().getNamedItem("ID").getNodeValue());
                    Module module = modules.get(ID);
                    System.out.println(module.getName());
                    NodeList successors = el.getElementsByTagName("successor");
                    int succCount = successors.getLength();
                    for (int j = 0; j < succCount; j++) {
                        Node succ = successors.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            int succID = Integer.parseInt(succ.getAttributes().getNamedItem("ID").getNodeValue());
                            Module successor = modules.get(succID);
                            module.addSuccessor(successor);
                        }
                    }

                }
            }
        } catch (Exception e) {}

    }

    public void printNetwork() {
        for (Map.Entry<Integer, Module> entry : modules.entrySet()) {
            Module module = entry.getValue();
            List<Module> successors = module.getSuccessors();
            System.out.println("Module : "+module.getName());
            for (Module successor : successors) {
                System.out.println("Successor : "+successor.getName());
            }
            System.out.println("-------------------------------");
        }
    }

    public Path generatePath(Module target) {
        //TODO
        return null;
    }
}
