public abstract class Droplet {

    private Module currentModule;
    private Module newModule;
    private Module wasteChamber;
    private int currEntryTime;
    private int newEntryTime;

    public Module getCurrentModule() {
        return currentModule;
    }

    public int getEntryTime() {
        return currEntryTime;
    }

    public void setEntryTime(int entryTime) {
        this.currEntryTime = entryTime;
    }

    public void setNewEntryTime(int newEntryTime) {
        this.newEntryTime = newEntryTime;
    }

    public boolean isArrived() {
        return currentModule == wasteChamber;
    }

    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    public void setNewModule(Module newModule) {
        this.newModule = newModule;
    }

    public Droplet(Module startModule, Module wasteChamber, int entryTime) {
        this.currentModule = startModule;
        this.newModule = startModule;
        this.wasteChamber = wasteChamber;
        this.currEntryTime = entryTime;
        this.newEntryTime = entryTime;
    }

    public void actualizePos() {
        currentModule.setOccupied(false);
        this.currEntryTime = this.newEntryTime;
        this.currentModule = this.newModule;
        currentModule.setOccupied(true);
    }

    public abstract void move(int currentStep);
}
