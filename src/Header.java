import java.util.List;

public class Header extends Droplet {

    public Header(Module startModule, Module wasteChamber, int entryTime) {
        super(startModule, wasteChamber, entryTime);
    }

    public void move (int currentStep) {
        if(currentStep - getEntryTime() > getCurrentModule().getHeaderLength()) {
            List<Module> successors = getCurrentModule().getSuccessors();
            if(!successors.isEmpty()) {
                int i = 0;
                boolean found = false;
                Module curr = null;
                while (i < successors.size()) {
                    Module successor = successors.get(i);
                    int length = successor.getHeaderLength();
                    if (length > 0 && !successor.isOccupied()) {
                        curr = successor;
                        found = true;
                        break;
                    }
                    i++;
                }

                if(found) {
                    for (Module successor : successors) {
                        int minLength = curr.getHeaderLength();
                        int length = successor.getHeaderLength();
                        if (length < minLength && !successor.isOccupied()) {
                            curr = successor;
                        }
                    }
                    setNewModule(curr);
                    setNewEntryTime(currentStep);
                }
            }
        }
    }

}
