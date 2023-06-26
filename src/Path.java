import java.util.ArrayList;
import java.util.List;

/*
Represents a path between two elements of the network.
 */
public class Path {
    private final List<Module> members;
    private final List<Module> nonDefaults;
    private int StartTime;

    public List<Module> getNonDefaults() {
        return nonDefaults;
    }

    public Path(List<Module> members) {
        this.members = members;
        this.nonDefaults = new ArrayList<>();
        for (Module current : members) {
            if (current.isDef()) {
                nonDefaults.add(current);
            }
        }
    }

    public void addMember(Module newMember) {
        members.add(newMember);
    }

    public void determineTiming(int earliest, int latest) {

    }
}
