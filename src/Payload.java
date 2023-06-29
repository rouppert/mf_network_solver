import java.util.List;

public class Payload extends Droplet {
    public Payload(Module startModule, Module wasteChamber) {
        super(startModule, wasteChamber, 0);
    }

    public void move (int currentStep) {
        if(currentStep - getEntryTime() > getCurrentModule().getPayloadLength()) {
            List<Module> successors = getCurrentModule().getSuccessors();
            if(!successors.isEmpty()) {
                int minLength = 0;
                for (Module successor : successors) {
                    int length = successor.getPayloadLength();
                    if (length < minLength && !successor.isOccupied()) {
                        minLength = length;
                        setCurrentModule(successor);
                    }
                }
            }
        }
    }

}
