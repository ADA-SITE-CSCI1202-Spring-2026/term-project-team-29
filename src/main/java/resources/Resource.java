package resources;

public enum Resource {
	OXYGEN, // Consumed by LifeSupportTask
	SPACE_SUITS, // Consumed by LifeSupportTask
	SPARE_PARTS, // Consumed by all tasks ( base class )
	CREW_MEMBERS, // Consumed by all tasks ( base class )
	SUPPLIES, // Consumed by  all tasks ( base class )
	LAB_EQUIPMENTS, // Consumed by ResearchTask
	POWER_UNITS, // Consumed by EngineeringTask

}
