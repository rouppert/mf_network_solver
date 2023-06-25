import java.util.ArrayList;
import java.util.List;

/*
Simulates the movements of the droplets in the network, and returns if the considered paths are consistent.
 */
public class Simulation {
    private final Network network;
    private final List<Path> paths;

    public Simulation(Network network) {
        this.network = network;
        this.paths = new ArrayList<>();
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    public void removePath(Path path) {
        paths.remove(path);
    }

    public boolean checkConsistency() {
        return true;
    }
}
