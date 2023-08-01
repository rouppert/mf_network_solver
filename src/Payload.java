import java.util.List;

public class Payload extends Droplet {
    public Payload(Module startModule, Module wasteChamber) {
        super(startModule, wasteChamber, 0);
    }

    public void move (int currentStep) {
        if(currentStep - getEntryTime() > getCurrentModule().getPayloadLength()) {
            List<Module> successors = getCurrentModule().getSuccessors();
            if(!successors.isEmpty()) {
                int i = 0;
                boolean found = false;
                Module curr = null;
                while (i < successors.size()) {
                    Module successor = successors.get(i);
                    int length = successor.getPayloadLength();
                    if (length > 0 && !successor.isOccupied()) {
                        curr = successor;
                        found = true;
                        break;
                    }
                    i++;
                }

                if(found) {
                    for (Module successor : successors) {
                        int minLength = curr.getPayloadLength();
                        int length = successor.getPayloadLength();
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
