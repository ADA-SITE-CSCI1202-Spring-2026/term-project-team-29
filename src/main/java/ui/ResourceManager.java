package ui;

import tasks.ColonyTask;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private Map<String, Integer> resources;
    private int credits;

    public ResourceManager() {
        resources = new HashMap<>();
        resources.put("Oxygen", 200);
        resources.put("SpareParts", 50);
        resources.put("Rations", 100);
        credits = 500;
    }

    public boolean hasEnoughResources(ColonyTask task) {
        if (getSpareParts() < task.getRequiredParts()) {
            return false;
        }

        if (getRations() < task.getSuppliesRequired()) {
            return false;
        }
        return true;
    }

    public boolean consumeResources(ColonyTask task) {
        if (!hasEnoughResources(task)) {
            return false;
        }

        setSpareParts(getSpareParts() - task.getRequiredParts());
        setRations(getRations() - task.getSuppliesRequired());

        return true;
    }

    public int getOxygen() { return resources.getOrDefault("Oxygen", 0); }
    public void setOxygen(int value) { resources.put("Oxygen", Math.max(0, value)); }
    public void addOxygen(int amount) { setOxygen(getOxygen() + amount); }

    public int getSpareParts() { return resources.getOrDefault("SpareParts", 0); }
    public void setSpareParts(int value) { resources.put("SpareParts", Math.max(0, value)); }
    public void addSpareParts(int amount) { setSpareParts(getSpareParts() + amount); }

    public int getRations() { return resources.getOrDefault("Rations", 0); }
    public void setRations(int value) { resources.put("Rations", Math.max(0, value)); }
    public void addRations(int amount) { setRations(getRations() + amount); }

    public int getCredits() { return credits; }
    public void setCredits(int value) { credits = Math.max(0, value); }
    public void addCredits(int amount) { setCredits(credits + amount); }

    public Map<String, Integer> getAllResources() {
        return new HashMap<>(resources);
    }
}