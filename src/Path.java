import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Represents a path between two elements of the network.
 */
public class Path implements Comparable<Path> {
    private final List<Module> members;
    private final List<Module> nonDefaults;
    private int StartTime;

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getStartTime() {
        return StartTime;
    }

    public List<Module> getNonDefaults() {
        return nonDefaults;
    }

    public List<Module> getMembers() {
        return members;
    }

    public Path() {
        this.members = new ArrayList<>();
        this.nonDefaults = new ArrayList<>();
    }

    public Path(List<Module> members) {
        this.members = members;
        this.nonDefaults = new ArrayList<>();
        for (Module current : members) {
            if (!current.isDef()) {
                nonDefaults.add(current);
            }
        }
    }

    public void addMember(Module newMember) {
        members.add(newMember);
        if (!newMember.isDef()) {
            nonDefaults.add(newMember);
        }
    }

    public Module getHead() {
        return members.get(members.size()-1);
    }

    public int timeToTarget(Module target, boolean isHeader) {
        if (!members.contains(target)) {
            return -1;
        } else {
            int time = 0;
            int i = 0;
            Module current = members.get(i);
            while (current != target) {
                i++;
                if(isHeader) {
                    time += current.getHeaderLength();
                } else {
                    time += current.getPayloadLength();
                }
                current = members.get(i);
            }
            return time;
        }
    }

    @Override
    public int compareTo(Path path) {
        return members.size() - path.members.size();
    }
}
