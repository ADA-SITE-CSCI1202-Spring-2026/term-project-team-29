package tasks;

public class EngineeringTask extends ColonyTask {
	private int powerUnitsRequired;

	public EngineeringTask(String name, int powerUnitsRequired, int requiredParts, int timeToFix,
			int crewMembersRequired, int difficulties, int suppliesRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired);
		this.powerUnitsRequired = powerUnitsRequired;
	}

	public int getPowerUnitsRequired() {
		return powerUnitsRequired;
	}

	@Override
	public String getTaskType() {
		return "ENGINEERING_TASK";
	}
}
