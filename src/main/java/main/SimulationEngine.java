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
 
    // Credits awarded per difficulty level (easy=0, medium=1, hard=2)
    private static final int[] CREDIT_REWARDS = { 150, 250, 400 };
 
    private Runnable onTick;
 
    public SimulationEngine(Runnable onTick) {
        taskQueue = new LinkedList<>();
        resourceManager = new ResourceManager();
        random = new Random();
        allTasks = TaskLibrary.getAllTasks();
        this.onTick = onTick;
 
        processors = new ArrayList<>();
        processors.add(new MedicalWard());    // handles LIFE_SUPPORT
        processors.add(new EngineeringBay()); // handles ENGINEERING_TASK
        processors.add(new Hydroponics());    // handles RESEARCH_TASK
    }
 
    public void start() {
        startTaskTimer();
        startClockTimer();
    }
 
    private void startTaskTimer() {
        if (taskTimer != null) taskTimer.stop();
        // Random interval between 2000-5000 ms as required by the spec
        int interval = 2000 + random.nextInt(3001);
        taskTimer = new Timer(interval, e -> {
            generateTask();
            // Reschedule with a new random interval after each tick
            startTaskTimer();
        });
        taskTimer.setRepeats(false); // one-shot; reschedule in handler for true randomness
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
        // Build a fresh task each time so the same object isn't added to the
        // queue multiple times (which would allow one poll to affect another entry).
        ColonyTask task = TaskLibrary.getRandomTask(random);
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
        if (!paused) {
            startTaskTimer();
            startClockTimer();
        }
    }
 
    public void normalSpeed() {
        speed = 1;
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
                // Ask the task itself whether there are enough resources.
                // This keeps instanceof chains OUT of the engine and inside the task hierarchy.
                if (!task.hasEnoughResources(resourceManager)) {
                    return "ERROR: Cannot resolve " + task.getName()
                         + " - Insufficient resources!";
                }
                // Deduct resources and reward credits scaled by difficulty.
                task.deductResources(resourceManager);
                int reward = CREDIT_REWARDS[Math.min(task.getDifficulties(), CREDIT_REWARDS.length - 1)];
                resourceManager.addCredits(reward);
                return processor.processTask(task)
                     + " | Est. " + task.getTimeToFix() + " mins | +" + reward + " credits!";
            }
        }
 
        return "ERROR: No processor available for " + task.getName();
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
        // Enforce .txt extension so saves are always human-readable text files
        if (!filePath.toLowerCase().endsWith(".txt")) {
            filePath = filePath + ".txt";
        }
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
