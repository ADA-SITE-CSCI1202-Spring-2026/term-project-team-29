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
	private Random random;

	private Timer taskTimer;
	private Timer clockTimer;
	private int gameMinutes = 0;
	private boolean paused = false;
	private int speed = 1;
	private String lastLog = "";

	private static final int CLOCK_INTERVAL_MS = 3000;

	// Credits awarded per difficulty level (easy=0, medium=1, hard=2)
	private static final int[] CREDIT_REWARDS = { 150, 250, 400 };

	private Runnable onTick;

	public SimulationEngine(Runnable onTick) {
		taskQueue = new LinkedList<>();
		resourceManager = new ResourceManager();
		random = new Random();
		this.onTick = onTick;

		processors = new ArrayList<>();
		processors.add(new MedicalWard()); // handles LIFE_SUPPORT
		processors.add(new EngineeringBay()); // handles ENGINEERING_TASK
		processors.add(new Hydroponics()); // handles RESEARCH_TASK
	}

	public String getLastLog() {
		return lastLog;
	}

	public void clearLastLog() {
		lastLog = "";
	}

	public void start() {
		startTaskTimer();
		startClockTimer();
	}

	private void startTaskTimer() {
		if (taskTimer != null)
			taskTimer.stop();
		// divide interval by speed so fast forward generates tasks faster too
		int base = 2000 + random.nextInt(3001);
		int interval = base / speed;
		taskTimer = new Timer(interval, e -> {
			generateTask();
			startTaskTimer();
		});
		taskTimer.setRepeats(false);
		taskTimer.start();
	}

	private void startClockTimer() {
		if (clockTimer != null)
			clockTimer.stop();
		clockTimer = new Timer(CLOCK_INTERVAL_MS / speed, e -> {
			gameMinutes += 1;
			if (onTick != null)
				onTick.run();
		});
		clockTimer.start();
	}

	private void generateTask() {
		// Build a fresh task each time so the same object isn't added to the
		// queue multiple times (which would allow one poll to affect another entry).
		ColonyTask task = TaskLibrary.getRandomTask(random);
		lastLog = "⚠️ New Task arrived: " + task.getName();
		taskQueue.add(task);
		if (onTick != null)
			onTick.run();
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

	public boolean isPaused() {
		return paused;
	}

	public int getSpeed() {
		return speed;
	}

	// ========== CLOCK DISPLAY ==========

	public String getFormattedGameTime() {
		int hours = gameMinutes / 60;
		int minutes = gameMinutes % 60;
		return String.format("%02d:%02d", hours, minutes);
	}

	public int getGameMinutes() {
		return gameMinutes;
	}

	// ========== TASK PROCESSING ==========

	public String executeNextTask() {
		if (taskQueue.isEmpty()) {
			return "WARNING: No tasks in queue!";
		}

		ColonyTask task = taskQueue.poll();

		for (IProcessor processor : processors) {
			if (processor.canProcess(task)) {
				if (!task.hasEnoughResources(resourceManager)) {
					// HP penalty based on difficulty
					int penalty = getPenalty(task.getDifficulties());
					resourceManager.damageHp(penalty);
					return "ERROR: Cannot resolve " + task.getName() + " - Insufficient resources! | -" + penalty
							+ " HP";
				}
				task.deductResources(resourceManager);
				int reward = CREDIT_REWARDS[Math.min(task.getDifficulties(), CREDIT_REWARDS.length - 1)];
				resourceManager.addCredits(reward);
				return processor.processTask(task) + " | +" + reward + " credits!";
			}
		}

		return "ERROR: No processor available for " + task.getName();
	}

	private int getPenalty(int difficulty) {
		switch (difficulty) {
		case 0:
			return 5; // easy
		case 1:
			return 10; // medium
		case 2:
			return 20; // hard
		default:
			return 5;
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

	public void setGameMinutes(int minutes) {
		this.gameMinutes = minutes;
	}

	public void saveGame(String filePath) throws IOException {
		if (!filePath.toLowerCase().endsWith(".txt")) {
			filePath = filePath + ".txt";
		}
		SaveLoadManager slm = new SaveLoadManager();
		slm.save(filePath, resourceManager, taskQueue, gameMinutes);
	}

	public void loadGame(String filePath) throws IOException {
		SaveLoadManager slm = new SaveLoadManager();
		Queue<ColonyTask> loadedQueue = slm.load(filePath, resourceManager);
		taskQueue.clear();
		taskQueue.addAll(loadedQueue);
		this.gameMinutes = slm.getLoadedGameMinutes();

	}

	// ========== GETTERS ==========

	public Queue<ColonyTask> getTaskQueue() {
		return taskQueue;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}
