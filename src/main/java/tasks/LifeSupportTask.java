package tasks;

public class LifeSupportTask extends ColonyTask {
	private int oxygenRequired;
	private int spaceSuits;

	public LifeSupportTask(String name, int oxygenRequired, int spaceSuits, int requiredParts, int timeToFix,
			int crewMembersRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired); // we send the name,parts and time of the task
		this.oxygenRequired = oxygenRequired;
		this.spaceSuits = spaceSuits;
	}

	public int getOxygenRequired() {
		return oxygenRequired;
	}
	
	public int getSpaceSuits() {
		return spaceSuits;
	}

	@Override
	public String getTaskType() {
		return "LIFE_SUPPORT";
	}
}
