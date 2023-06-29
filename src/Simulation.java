import java.util.ArrayList;
import java.util.List;

/*
Simulates the movements of the droplets in the network, and returns if the considered paths are consistent.
 */
public class Simulation {
    private final Network network;
    private final List<Integer> timingList;
    private final List<Droplet> droplets;
    private final int maxSteps;

    public Simulation(Network network, List<Integer> timingList, int maxSteps) {
        this.network = network;
        this.timingList = timingList;
        this.droplets = new ArrayList<>();
        this.maxSteps = maxSteps;
    }

    public boolean checkConsistency() {
        int step = 0;
        Droplet payload = new Payload(network.getOrigin(), network.getWasteChamber());
        while(step < maxSteps) {
            if (isOverlapping(droplets, step)) {
                return false;
            }
            for (int i = 0; i < timingList.size()-1; i++) {
                int timing = timingList.get(i);
                if(step == timing) {
                    Droplet header = new Header(network.getOrigin(), network.getWasteChamber(), step);
                    droplets.add(header);
                }
            }

            if(step == timingList.get(timingList.size()-1)) {
                droplets.add(payload);
            }

            for(Droplet droplet : droplets) {
                if(droplet.isArrived()) {
                    droplets.remove(droplet);
                }
                droplet.move(step);
            }
            step += 1;
            if (isOverlapping(droplets, step)) {
                return false;
            }
        }
        return payload.isArrived();
    }

    public boolean isOverlapping(List<Droplet> droplets, int step) {
        for (Droplet droplet1 : droplets) {
            for (Droplet droplet2 : droplets) {
                if (droplet1!=droplet2) {
                    if (droplet1.getCurrentModule() != droplet2.getCurrentModule()) {
                        if (droplet1.getCurrentModule().getSuccessors().contains(droplet2.getCurrentModule())
                            || droplet2.getCurrentModule().getSuccessors().contains(droplet1.getCurrentModule())
                            ) {
                            Droplet first = droplet1.getCurrentModule().getSuccessors().contains(droplet2.getCurrentModule()) ? droplet1 : droplet2;
                            Droplet second = droplet2.getCurrentModule().getSuccessors().contains(droplet2.getCurrentModule()) ? droplet1 : droplet2;
                            if (step - second.getEntryTime() + first.getCurrentModule().getHeaderLength() - (step - first.getEntryTime()) < Const.T_DELTA) {
                                return true;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    if (droplet1.getCurrentModule() == droplet2.getCurrentModule()) {
                        int maxEntryTime = Math.max(droplet1.getEntryTime(), droplet2.getEntryTime());
                        int minEntryTime = Math.min(droplet1.getEntryTime(), droplet2.getEntryTime());
                        if (maxEntryTime - minEntryTime < Const.T_DELTA) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
