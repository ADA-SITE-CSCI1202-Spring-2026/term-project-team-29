package tasks;

public abstract class ColonyTask {
	protected String name;

	public ColonyTask(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract String getTaskType(); // example: "LIFESUPPORT"
}
