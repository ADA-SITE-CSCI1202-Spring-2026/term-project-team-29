package main;

import tasks.*;
import resources.*;
import processors.*;
import fileio.SaveLoadManager;

import javax.swing.Timer;
import java.io.IOException;
import java.util.*;

public class SimulationEngine {

    private Queue<ColonyTask> taskQueue;
    private ResourceManager resourceManager;
    private List<IProcessor> processors;
    private List<ColonyTask> allTasks;
    private Random random;

    private Timer taskTimer;
    private Timer clockTimer;

    private int gameMinutes = 0;
    private boolean paused = false;
    private int speed = 1;

    private static final int CLOCK_INTERVAL_MS = 3000;
    private int taskInterval = 3000;

    private Runnable onTick;

    public SimulationEngine(Runnable onTick) {
        taskQueue = new LinkedList<>();
        resourceManager = new ResourceManager();
        random = new Random();
        allTasks = TaskLibrary.getAllTasks();
        this.onTick = onTick;

        processors = new ArrayList<>();
        processors.add(new MedicalWard());
        processors.add(new EngineeringBay());
        processors.add(new Hydroponics());
    }

    public void start() {
        startTaskTimer();
        startClockTimer();
    }

    private void startTaskTimer() {
        if (taskTimer != null) taskTimer.stop();
        taskTimer = new Timer(taskInterval, e -> generateTask());
        taskTimer.start();
    }

    private void startClockTimer() {
        if (clockTimer != null) clockTimer.stop();
        clockTimer = new Timer(CLOCK_INTERVAL_MS / speed, e -> {
            gameMinutes += 1;
            if (onTick != null) onTick.run();
        });
        clockTimer.start();
    }

    private void generateTask() {
        ColonyTask task = allTasks.get(random.nextInt(allTasks.size()));
        taskQueue.add(task);
        if (onTick != null) onTick.run();
    }

    // ========== CLOCK CONTROLS ==========

    public void pause() {
        if (!paused) {
            taskTimer.stop();
            clockTimer.stop();
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            startTaskTimer();
            startClockTimer();
            paused = false;
        }
    }

    public void fastForward() {
        speed = 2;
        taskInterval = Math.max(500, taskInterval / 2);
        if (!paused) {
            startTaskTimer();
            startClockTimer();
        }
    }

    public void normalSpeed() {
        speed = 1;
        taskInterval = 3000;
        if (!paused) {
            startTaskTimer();
            startClockTimer();
        }
    }

    public boolean isPaused() { return paused; }
    public int getSpeed() { return speed; }

    // ========== CLOCK DISPLAY ==========

    public String getFormattedGameTime() {
        int hours   = gameMinutes / 60;
        int minutes = gameMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public int getGameMinutes() { return gameMinutes; }

    // ========== TASK PROCESSING ==========

    public String executeNextTask() {
        if (taskQueue.isEmpty()) {
            return "WARNING: No tasks in queue!";
        }

        ColonyTask task = taskQueue.poll();

        for (IProcessor processor : processors) {
            if (processor.canProcess(task)) {
                if (!hasEnoughResources(task)) {
                    return "ERROR: Cannot resolve " + task.getName()
                         + " - Insufficient resources!";
                }
                deductResources(task);
                resourceManager.addCredits(200);
                return processor.processTask(task)
                     + " | Est. " + task.getTimeToFix() + " mins | +200 credits!";
            }
        }

        return "ERROR: No processor available for " + task.getName();
    }

    private boolean hasEnoughResources(ColonyTask task) {
        ResourceManager rm = resourceManager;
        if (!rm.hasEnough(Resource.SPARE_PARTS, task.getRequiredParts())) return false;
        if (!rm.hasEnough(Resource.CREW_MEMBERS, task.getCrewMembersRequired())) return false;

        if (task instanceof LifeSupportTask lst) {
            if (!rm.hasEnough(Resource.OXYGEN, lst.getOxygenRequired())) return false;
            if (!rm.hasEnough(Resource.SPACE_SUITS, lst.getSpaceSuits())) return false;
        } else if (task instanceof EngineeringTask et) {
            if (!rm.hasEnough(Resource.POWER_UNITS, et.getPowerUnitsRequired())) return false;
        } else if (task instanceof ResearchTask rt) {
            if (!rm.hasEnough(Resource.LAB_EQUIPMENTS, rt.getLabEquipmentRequired())) return false;
        }

        return true;
    }

    private void deductResources(ColonyTask task) {
        ResourceManager rm = resourceManager;
        rm.deduct(Resource.SPARE_PARTS, task.getRequiredParts());
        rm.deduct(Resource.CREW_MEMBERS, task.getCrewMembersRequired());

        if (task instanceof LifeSupportTask lst) {
            rm.deduct(Resource.OXYGEN, lst.getOxygenRequired());
            rm.deduct(Resource.SPACE_SUITS, lst.getSpaceSuits());
        } else if (task instanceof EngineeringTask et) {
            rm.deduct(Resource.POWER_UNITS, et.getPowerUnitsRequired());
        } else if (task instanceof ResearchTask rt) {
            rm.deduct(Resource.LAB_EQUIPMENTS, rt.getLabEquipmentRequired());
        }
    }

    // ========== RESTOCK ==========

    public String restock(Resource r) {
        if (resourceManager.getCredits() < 100) {
            return "ERROR: Not enough credits to synthesize " + r.name() + "!";
        }
        resourceManager.deductCredits(100);
        resourceManager.restock(r, 10);
        return "Synthesized 10 " + r.name() + " | -100 credits";
    }

    // ========== SAVE / LOAD ==========

    public void saveGame(String filePath) throws IOException {
        SaveLoadManager slm = new SaveLoadManager();
        slm.save(filePath, resourceManager, taskQueue);
    }

    public void loadGame(String filePath) throws IOException {
        SaveLoadManager slm = new SaveLoadManager();
        Queue<ColonyTask> loadedQueue = slm.load(filePath, resourceManager);
        taskQueue.clear();
        taskQueue.addAll(loadedQueue);
    }

    // ========== GETTERS ==========

    public Queue<ColonyTask> getTaskQueue() { return taskQueue; }
    public ResourceManager getResourceManager() { return resourceManager; }
}
