public abstract class Droplet {

    private Module currentModule;
    private Module wasteChamber;
    private int entryTime;

    public Module getCurrentModule() {
        return currentModule;
    }

    public int getEntryTime() {
        return entryTime;
    }

    public boolean isArrived() {
        return currentModule == wasteChamber;
    }

    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    public Droplet(Module startModule, Module wasteChamber, int entryTime) {
        this.currentModule = startModule;
        this.wasteChamber = wasteChamber;
        this.entryTime = entryTime;
    }

    public abstract void move(int currentStep);
}
