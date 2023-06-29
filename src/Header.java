import java.util.List;

public class Header extends Droplet {

    public Header(Module startModule, Module wasteChamber, int entryTime) {
        super(startModule, wasteChamber, entryTime);
    }

    public void move (int currentStep) {
        if(currentStep - getEntryTime() > getCurrentModule().getHeaderLength()) {
            getCurrentModule().setOccupied(false);
            List<Module> successors = getCurrentModule().getSuccessors();
            if(!successors.isEmpty()) {
                int minLength = 0;
                for (Module successor : successors) {
                    int length = successor.getHeaderLength();
                    if (length < minLength && !successor.isOccupied()) {
                        minLength = length;
                        setCurrentModule(successor);
                    }
                }
                getCurrentModule().setOccupied(true);
            }
        }
    }

}
