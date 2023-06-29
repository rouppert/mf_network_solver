import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/*
Represents a droplet-based microfluidic network.
 */
public class Network {
    private final Map<Integer, Module> modules;
    private Module origin;
    private Module wasteChamber;

    public Module getOrigin() {
        return origin;
    }

    public void setOrigin(Module origin) {
        this.origin = origin;
    }

    public Module getWasteChamber() {
        return wasteChamber;
    }

    public void setWasteChamber(Module wasteChamber) {
        this.wasteChamber = wasteChamber;
    }

    public Network() {
        this.modules = new HashMap<>();
    }

    public void addModule(int ID, Module module) {
        if(Objects.equals(module.getName(), "origin")) {
            setOrigin(module);
        } else if (Objects.equals(module.getName(), "waste chamber")) {
            setWasteChamber(module);
        }
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
                    int headerLength = Integer.parseInt(el.getElementsByTagName("hLength").item(0).getChildNodes().item(0).getNodeValue());
                    int payloadLength = Integer.parseInt(el.getElementsByTagName("pLength").item(0).getChildNodes().item(0).getNodeValue());
                    boolean def = Boolean.parseBoolean(el.getElementsByTagName("def").item(0).getChildNodes().item(0).getNodeValue());
                    Module module = new Module(name, headerLength, payloadLength, def);
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

    public Path generatePlPathFromXml(String filename) {
        File file = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList moduleNodes = document.getElementsByTagName("module");
            int nodesCount = moduleNodes.getLength();
            Path path = new Path();

            // Getting the list of the modules for the network
            for (int i = 0; i < nodesCount; i++) {
                Node node = moduleNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    int ID = Integer.parseInt(node.getAttributes().getNamedItem("ID").getNodeValue());
                    Module module = modules.get(ID);
                    path.addMember(module);
                }
            }
            return path;
        } catch (Exception e) {
            return null;
        }
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

    public List<Path> generatePathsToTarget(Module origin, Module target) {
        List<Path> possiblePaths = new ArrayList<>();
        List<Path> defintivePaths = new ArrayList<>();
        Path firstPath = new Path();
        firstPath.addMember(origin);
        possiblePaths.add(firstPath);
        while(!possiblePaths.isEmpty()) {
            int size = possiblePaths.size();
            List<Path> pathsToRemove = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Path path = possiblePaths.get(i);
                Module current = path.getHead();
                if (current.isChannel()) {
                    if (current.getSuccessors().contains(target)){
                        path.addMember(target);
                        defintivePaths.add(path);
                        pathsToRemove.add(path);
                    } else if (current == wasteChamber) {
                        pathsToRemove.add(path);
                    } else if (!current.getSuccessors().isEmpty()){
                        List<Module> successors = current.getSuccessors();
                        for (int j = 1; j < successors.size(); j++) {
                            Path pathToAdd = new Path(new ArrayList<>(path.getMembers()));
                            pathToAdd.addMember(successors.get(j));
                            possiblePaths.add(pathToAdd);
                        }
                        path.addMember(successors.get(0));
                    }
                } else {
                    pathsToRemove.add(path);
                }
            }
            for (Path pathToRemove : pathsToRemove) {
                possiblePaths.remove(pathToRemove);
            }
        }
        return defintivePaths;
    }

    public List<Integer> generateStartTimes(Path plPath, int level) {
        List<Integer> startTimes = new ArrayList<>();
        startTimes.add(0);
        List<Path> candidate = new ArrayList<>();
        candidate.add(plPath);
        int window;
        int prev_size = 0;
        List<Module> examNDef = new ArrayList<>();
        while(prev_size < candidate.size()) {
            window = prev_size;
            prev_size = candidate.size();
            int size = candidate.size();
            for (int i = window; i<size; i++) {
                List<Module> nonDefaults = candidate.get(i).getNonDefaults();
                List<Path> possiblePaths;
                Path newPath;
                Path end;
                for (Module nonDefault : nonDefaults) {
                    if(!examNDef.contains(nonDefault)) {
                        possiblePaths = generatePathsToTarget(origin, nonDefault);
                        Collections.sort(possiblePaths);
                        newPath = possiblePaths.get(Math.min(level, possiblePaths.size() - 1));
                        candidate.add(newPath);
                        int middle = plPath.timeToTarget(nonDefault, false) - nonDefault.getHeaderLength() / 2; //TODO : ajouter partie entière suppérieure
                        int startTime = middle - newPath.timeToTarget(nonDefault, true);
                        newPath.setStartTime(startTime);
                        startTimes.add(startTime);
                        List<Path> pathsToEnd = generatePathsToTarget(nonDefault, wasteChamber);
                        Collections.sort(pathsToEnd);
                        end = pathsToEnd.get(0);
                        for (int j = 1; j < end.getMembers().size(); j++) {
                            newPath.addMember(end.getMembers().get(j));
                        }
                        examNDef.add(nonDefault);
                    }
                }

            }

        }
        return startTimes;
    }

    public List<Integer> solve(String networkFile, String pathFile) {
        generateFromXml(networkFile);
        Path plPath = generatePlPathFromXml(pathFile);
        int level = 0;
        List<Integer> startTimes = generateStartTimes(plPath, level);
        int minTime = Collections.min(startTimes);
        List<Integer> newList = new ArrayList<>();
        for (Integer startTime : startTimes) {
            newList.add(startTime - minTime);
        }
        startTimes = newList;
        Collections.sort(startTimes);
        Simulation sim = new Simulation(this, startTimes, 100);
        System.out.println(startTimes);
        /*
        while (!sim.checkConsistency()) {
            level++;
            startTimes = generateStartTimes(plPath, level);
            sim = new Simulation(this, startTimes, 100);
        }
         */
        return startTimes;
    }
}
