package tasks;

public abstract class ColonyTask {
	protected String name;
	protected int requiredParts;
	protected int timeToFix; // IN SECONDS (IMPORTANTTTT)
	protected int crewMembersRequired;
	
	public ColonyTask(String name, int requiredParts, int timeToFix, int crewMembersRequired) {
		this.name = name; // name of the task
		this.requiredParts = requiredParts; // this can be unique depending on the task
		this.timeToFix = timeToFix; // self explanatory
		this.crewMembersRequired = crewMembersRequired;
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
