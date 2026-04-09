package tasks;

public class ResearchTask extends ColonyTask {
	private int labEquipmentRequired;

	public ResearchTask(String name, int labEquipmentRequired, int requiredParts, int timeToFix,
			int crewMembersRequired, int difficulties, int suppliesRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired);
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
