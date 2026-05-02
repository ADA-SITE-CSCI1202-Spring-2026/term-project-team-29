package tasks;
 
import resources.Resource;
import resources.ResourceManager;
 
public abstract class ColonyTask {
	protected String name;
	protected int requiredParts;
	protected int timeToFix; // IN MINUTES DECIMAL FORMAT example: 10 or 20 or 30....
	protected int crewMembersRequired;
	protected int difficulties; // easy = 0, medium = 1, hard = 2
	protected int suppliesRequired; // food, water stuff
	
	
	public abstract String getResourceSummary();
 
	public ColonyTask(String name, int requiredParts, int timeToFix, int crewMembersRequired, int difficulties, int suppliesRequired) {
		this.name = name; // name of the task
		this.requiredParts = requiredParts; // this can be unique depending on the task
		this.timeToFix = timeToFix; // self explanatory
		this.crewMembersRequired = crewMembersRequired;
		this.difficulties = difficulties;
		this.suppliesRequired = suppliesRequired;
	}
 
	// Checks whether the ResourceManager holds enough of every resource this task requires.
	// Subclasses override and call super to add their own extra checks.
	// No instanceof chains needed in the engine - polymorphism handles it here.
	public boolean hasEnoughResources(ResourceManager rm) {
		return rm.hasEnough(Resource.SPARE_PARTS, requiredParts)
			&& rm.hasEnough(Resource.CREW_MEMBERS, crewMembersRequired)
			&& rm.hasEnough(Resource.SUPPLIES, suppliesRequired);
	}
 
	// Deducts all base resources. Subclasses override and call super
	// to also deduct their specialised resources.
	// Must only be called AFTER hasEnoughResources() returns true.
	public void deductResources(ResourceManager rm) {
		rm.deduct(Resource.SPARE_PARTS, requiredParts);
		rm.deduct(Resource.CREW_MEMBERS, crewMembersRequired);
		rm.deduct(Resource.SUPPLIES, suppliesRequired);
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
 
	public int getDifficulties() {
		return difficulties;
	}
	
	public int getSuppliesRequired() {
		return suppliesRequired;
	}
 
	public abstract String getTaskType(); // example: "LIFE_SUPPORT", "ENGINEERING_TASK"...
}
