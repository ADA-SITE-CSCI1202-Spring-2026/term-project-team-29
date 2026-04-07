package tasks;

public class ResearchTask extends ColonyTask {
	private int labEquipmentRequired;

	public ResearchTask(String name, int labEquipmentRequired, int requiredParts, int timeToFix,
			int crewMembersRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired);
		this.labEquipmentRequired = labEquipmentRequired;
	}

	public int getLabEquipmentRequired() {
		return labEquipmentRequired;
	}

	@Override
	public String getTaskType() {
		return "RESEARCH_TASK";
	}
}
