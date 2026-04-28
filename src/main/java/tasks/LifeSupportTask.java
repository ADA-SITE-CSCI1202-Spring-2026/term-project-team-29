package tasks;
 
import resources.Resource;
import resources.ResourceManager;
 
public class LifeSupportTask extends ColonyTask {
	private int oxygenRequired;
	private int spaceSuits;
 
	public LifeSupportTask(String name, int oxygenRequired, int spaceSuits, int requiredParts, int timeToFix,
			int crewMembersRequired, int difficulties, int suppliesRequired) {
		super(name, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired);
		this.oxygenRequired = oxygenRequired;
		this.spaceSuits = spaceSuits;
	}
 
	@Override
	public boolean hasEnoughResources(ResourceManager rm) {
		return super.hasEnoughResources(rm)
			&& rm.hasEnough(Resource.OXYGEN, oxygenRequired)
			&& rm.hasEnough(Resource.SPACE_SUITS, spaceSuits);
	}
 
	@Override
	public void deductResources(ResourceManager rm) {
		super.deductResources(rm);
		rm.deduct(Resource.OXYGEN, oxygenRequired);
		rm.deduct(Resource.SPACE_SUITS, spaceSuits);
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
