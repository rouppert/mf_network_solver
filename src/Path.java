import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path {
    private final List<Element> members;
    private final List<Element> nonDefaults;
    private int StartTime;

    public List<Element> getNonDefaults() {
        return nonDefaults;
    }

    public Path(List<Element> members) {
        this.members = members;
        this.nonDefaults = new ArrayList<>();
        for (Element current : members) {
            if (current.isDef()) {
                nonDefaults.add(current);
            }
        }
    }

    public void addMember(Element newMember) {
        members.add(newMember);
    }

    public void determineTiming(int earliest, int latest) {

    }
}
