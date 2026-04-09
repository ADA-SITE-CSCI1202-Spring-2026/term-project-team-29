package tasks;

public abstract class ColonyTask {
	protected String name;
	protected int requiredParts;
	protected int timeToFix; // IN SECONDS (IMPORTANTTTT)
	protected int crewMembersRequired;
	protected int difficulties; // easy = 0, medium = 1, hard = 2
	
	
	
	public ColonyTask(String name, int requiredParts, int timeToFix, int crewMembersRequired, int difficulties) {
		this.name = name; // name of the task
		this.requiredParts = requiredParts; // this can be unique depending on the task
		this.timeToFix = timeToFix; // self explanatory
		this.crewMembersRequired = crewMembersRequired;
		this.difficulties = difficulties;
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

	public abstract String getTaskType(); // example: "LIFE_SUPPORT", "ENGINEERING_TASK"...
}
