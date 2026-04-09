package tasks;

public abstract class ColonyTask {
	protected String name;
	protected int requiredParts;
	protected int timeToFix; // IN MINUTES DECIMAL FORMAT example: 10 or 20 or 30....
	protected int crewMembersRequired;
	protected int difficulties; // easy = 0, medium = 1, hard = 2
	protected int supplies; // food, water stuff

	public ColonyTask(String name, int requiredParts, int timeToFix, int crewMembersRequired, int difficulties, int supplies) {
		this.name = name; // name of the task
		this.requiredParts = requiredParts; // this can be unique depending on the task
		this.timeToFix = timeToFix; // self explanatory
		this.crewMembersRequired = crewMembersRequired;
		this.difficulties = difficulties;
		this.supplies = supplies;
	}

	public String getName() {
		return name;
	}

	public int getRequiredParts() {
		return requiredParts;
	}

	public int getTimeToFix() {
		return timeToFix;
	}

	public int getCrewMembersRequired() {
		return crewMembersRequired;
	}

	public int getDifficulties() {
		return difficulties;
	}
	
	public int getSupplies() {
		return supplies;
	}

	public abstract String getTaskType(); // example: "LIFE_SUPPORT", "ENGINEERING_TASK"...
}
