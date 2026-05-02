package tasks;
 
import resources.Resource;
import resources.ResourceManager;
 
public class EngineeringTask extends ColonyTask {
	private int powerUnitsRequired;
 
	public EngineeringTask(String name, int powerUnitsRequired, int requiredParts, int timeToFix,
			int crewMembersRequired, int difficulties, int suppliesRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired);
		this.powerUnitsRequired = powerUnitsRequired;
	}
	
	@Override
	public String getResourceSummary() {
	    return "Power:" + powerUnitsRequired + 
	           " Parts:" + requiredParts + 
	           " Crew:" + crewMembersRequired;
	}
	
	@Override
	public boolean hasEnoughResources(ResourceManager rm) {
		return super.hasEnoughResources(rm)
			&& rm.hasEnough(Resource.POWER_UNITS, powerUnitsRequired);
	}
 
	@Override
	public void deductResources(ResourceManager rm) {
		super.deductResources(rm);
		rm.deduct(Resource.POWER_UNITS, powerUnitsRequired);
	}
 
	public int getPowerUnitsRequired() {
		return powerUnitsRequired;
	}
 
	@Override
	public String getTaskType() {
		return "ENGINEERING_TASK";
	}
}
