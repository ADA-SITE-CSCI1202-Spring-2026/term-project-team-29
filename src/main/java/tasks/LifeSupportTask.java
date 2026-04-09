package tasks;

public class LifeSupportTask extends ColonyTask {
	private int oxygenRequired;
	private int spaceSuits;

	public LifeSupportTask(String name, int oxygenRequired, int spaceSuits, int requiredParts, int timeToFix,
			int crewMembersRequired, int difficulties, int suppliesRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired);
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
