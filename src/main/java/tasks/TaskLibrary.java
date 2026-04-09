package tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskLibrary {

	public static List<ColonyTask> getAllTasks() {
		List<ColonyTask> tasks = new ArrayList<>();

		/*
		 * Azim i made a new property called supplies as you asked from me, you can
		 * check from ColonyTask.java It is the current last parameter for every
		 * constructor: ColonyTask,LifeSupportTask,EngineeringTask,ResearchTask
		 * oh yeah i also added 0 supplies to all scenarios cuz i was lazy lol!!!!
		 * -Qardem
		 */

		// Engineering Task
		// name, powerUnitsRequired, requiredParts, timeToFix, crewMembersRequired,
		// difficulties, supplies
		tasks.add(new EngineeringTask("Fix Loose Solar Panel Cable", 5, 2, 10, 1, 0, 0));
		tasks.add(new EngineeringTask("Calibrate Power Generator", 4, 1, 20, 1, 0, 0));
		tasks.add(new EngineeringTask("Replace Broken Circuit Fuse", 3, 1, 10, 1, 0, 0)); // easy

		tasks.add(new EngineeringTask("Repair Solar Array Damage", 12, 5, 20, 2, 1, 0));
		tasks.add(new EngineeringTask("Restore Power Grid Stability", 15, 6, 20, 2, 1, 0));
		tasks.add(new EngineeringTask("Fix Water Recycling Pump", 10, 4, 20, 2, 1, 0)); // medium

		tasks.add(new EngineeringTask("Rebuild Reactor Cooling System", 25, 10, 30, 4, 2, 0));
		tasks.add(new EngineeringTask("Emergency Reactor Shutdown", 30, 12, 40, 5, 2, 0));
		tasks.add(new EngineeringTask("Repair Meteor-Damaged Power Core", 28, 11, 40, 4, 2, 0)); // hard

		// LifeSupport Task
		// name, oxygenRequired, spaceSuits, requiredParts, timeToFix,
		// crewMembersRequired, difficulties, supplies
		tasks.add(new LifeSupportTask("Fix Minor Oxygen Leak", 5, 1, 2, 10, 1, 0, 0));
		tasks.add(new LifeSupportTask("Clean Air Filters", 3, 0, 1, 2, 20, 0, 0));
		tasks.add(new LifeSupportTask("Refill Water Condenser", 4, 0, 1, 10, 1, 0, 0)); // easy

		tasks.add(new LifeSupportTask("Repair CO2 Scrubber Failure", 10, 1, 4, 30, 2, 1, 0));
		tasks.add(new LifeSupportTask("Stabilize Cabin Pressure Drop", 12, 2, 5, 20, 2, 1, 0));
		tasks.add(new LifeSupportTask("Replace Oxygen Tank Valve", 8, 1, 3, 20, 2, 1, 0)); // medium

		tasks.add(new LifeSupportTask("Seal Hull Breach", 20, 3, 8, 30, 3, 2, 0));
		tasks.add(new LifeSupportTask("Emergency Oxygen System Reboot", 25, 2, 10, 50, 4, 2, 0));
		tasks.add(new LifeSupportTask("Toxic Air Contamination Purge", 18, 2, 7, 30, 3, 2, 0)); // hard

		// Research Task
		// name, labEquipmentRequired, requiredParts, timeToFix, crewMembersRequired,
		// difficulties, supplies
		tasks.add(new ResearchTask("Analyze Soil Sample", 3, 1, 10, 1, 0, 0));
		tasks.add(new ResearchTask("Run Basic Water Test", 2, 1, 10, 1, 0, 0));
		tasks.add(new ResearchTask("Calibrate Lab Sensors", 2, 1, 20, 1, 0, 0)); // easy

		tasks.add(new ResearchTask("Study Microbial Life", 8, 3, 20, 2, 1, 0));
		tasks.add(new ResearchTask("Develop Plant Growth Formula", 7, 3, 20, 2, 1, 0));
		tasks.add(new ResearchTask("Analyze Radiation Levels", 9, 4, 30, 2, 1, 0)); // medium

		tasks.add(new ResearchTask("Create Synthetic Oxygen Generator", 15, 6, 40, 3, 2, 0));
		tasks.add(new ResearchTask("Alien Virus Containment Study", 18, 7, 40, 4, 2, 0));
		tasks.add(new ResearchTask("Terraforming Experiment", 20, 8, 30, 4, 2, 0)); // hard

		return tasks;
	}

}
