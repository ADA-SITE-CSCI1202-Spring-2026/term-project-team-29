package tasks;

import resources.Resource;
import resources.ResourceManager;

public class ResearchTask extends ColonyTask {
	private int labEquipmentRequired;

	public ResearchTask(String name, int labEquipmentRequired, int requiredParts, int crewMembersRequired,
			int difficulties, int suppliesRequired) {
		super(name, requiredParts, crewMembersRequired, difficulties, suppliesRequired);
		this.labEquipmentRequired = labEquipmentRequired;
	}

	@Override
	public String getResourceSummary() {
		return "Lab:" + labEquipmentRequired + " Parts:" + requiredParts + " Crew:" + crewMembersRequired;
	}

	@Override
	public boolean hasEnoughResources(ResourceManager rm) {
		return super.hasEnoughResources(rm) && rm.hasEnough(Resource.LAB_EQUIPMENTS, labEquipmentRequired);
	}

	@Override
	public void deductResources(ResourceManager rm) {
		super.deductResources(rm);
		rm.deduct(Resource.LAB_EQUIPMENTS, labEquipmentRequired);
	}

	public int getLabEquipmentRequired() {
		return labEquipmentRequired;
	}

	@Override
	public String getTaskType() {
		return "RESEARCH_TASK";
	}
}
