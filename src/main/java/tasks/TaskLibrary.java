package tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskLibrary {

	public static List<ColonyTask> getAllTasks() {
		List<ColonyTask> tasks = new ArrayList<>();


		// Engineering Task
		// name, powerUnitsRequired, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired
		tasks.add(new EngineeringTask("Fix Loose Solar Panel Cable", 5, 2, 10, 1, 0, 1));  
        // "NOTICE: Power generation slightly reduced. Ignoring may lead to gradual energy shortages."
		tasks.add(new EngineeringTask("Calibrate Power Generator", 4, 1, 20, 1, 0, 1));
        // "WARNING: Generator efficiency dropping. Long-term neglect increases risk of overload."
		tasks.add(new EngineeringTask("Replace Broken Circuit Fuse", 3, 1, 10, 1, 0, 1)); // easy
        // "NOTICE: Minor electrical instability detected. Could escalate into system-wide outages."

		tasks.add(new EngineeringTask("Repair Solar Array Damage", 12, 5, 20, 2, 1, 3));
        // "WARNING: Solar array damaged. Energy production significantly reduced."
		tasks.add(new EngineeringTask("Restore Power Grid Stability", 15, 6, 20, 2, 1, 4));
        // "ALERT: Power grid unstable. Random system shutdowns may occur."
		tasks.add(new EngineeringTask("Fix Water Recycling Pump", 10, 4, 20, 2, 1, 3)); // medium
        // "WARNING: Water recycling efficiency reduced. Supplies will drain faster."

		tasks.add(new EngineeringTask("Rebuild Reactor Cooling System", 25, 10, 30, 4, 2, 6));
        // "CRITICAL: Reactor overheating. Ignoring may result in catastrophic meltdown."
		tasks.add(new EngineeringTask("Emergency Reactor Shutdown", 30, 12, 40, 5, 2, 7));
        // "EMERGENCY: Reactor instability detected. Immediate shutdown required to prevent explosion."
		tasks.add(new EngineeringTask("Repair Meteor-Damaged Power Core", 28, 11, 40, 4, 2, 6)); // hard
        // "CRITICAL DAMAGE: Meteor strike compromised power core. Total blackout imminent."

		// LifeSupport Task
		// name, oxygenRequired, spaceSuits, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired
		tasks.add(new LifeSupportTask("Fix Minor Oxygen Leak", 5, 1, 2, 10, 1, 0, 2));
        // "WARNING: Oxygen levels slowly dropping. If ignored, crew fatigue will increase and minor leaks may escalate into critical failures."
		tasks.add(new LifeSupportTask("Clean Air Filters", 3, 0, 1, 2, 20, 0, 1));
        // "NOTICE: Air quality degrading. Ignoring this will reduce crew efficiency and increase risk of system contamination."
		tasks.add(new LifeSupportTask("Refill Water Condenser", 4, 0, 1, 10, 1, 0, 2)); // easy
        // "WARNING: Water reserves declining. If not restored, supplies will drain faster and crew survival time will shorten."

		tasks.add(new LifeSupportTask("Repair CO2 Scrubber Failure", 10, 1, 4, 30, 2, 1, 3));
        // "CRITICAL: CO2 levels rising. Crew may experience dizziness and system-wide inefficiency if not handled soon."
		tasks.add(new LifeSupportTask("Stabilize Cabin Pressure Drop", 12, 2, 5, 20, 2, 1, 4));
        // "ALERT: Cabin pressure unstable. Ignoring this may trigger emergency evacuation protocols."
		tasks.add(new LifeSupportTask("Replace Oxygen Tank Valve", 8, 1, 3, 20, 2, 1, 3)); // medium
        // "WARNING: Oxygen distribution irregular. Failure to act may cause uneven supply across modules."

		tasks.add(new LifeSupportTask("Seal Hull Breach", 20, 3, 8, 30, 3, 2, 5));
        // "CRITICAL FAILURE: Hull breach detected. Ignoring this will result in rapid oxygen loss and potential base collapse."
		tasks.add(new LifeSupportTask("Emergency Oxygen System Reboot", 25, 2, 10, 50, 4, 2, 6));
        // "SYSTEM FAILURE: Oxygen systems offline. Immediate action required or total crew loss is inevitable."
		tasks.add(new LifeSupportTask("Toxic Air Contamination Purge", 18, 2, 7, 30, 3, 2, 5)); // hard
        // "BIOHAZARD ALERT: Toxic particles detected. If ignored, crew health will rapidly deteriorate."

		// Research Task
		// name, labEquipmentRequired, requiredParts, timeToFix, crewMembersRequired, difficulties, suppliesRequired
		tasks.add(new ResearchTask("Analyze Soil Sample", 3, 1, 10, 1, 0, 1));
        // "INFO: Soil composition unknown. Ignoring slows future terraforming progress."
		tasks.add(new ResearchTask("Run Basic Water Test", 2, 1, 10, 1, 0, 1));
        // "NOTICE: Water purity uncertain. Contaminated water may affect crew health."
		tasks.add(new ResearchTask("Calibrate Lab Sensors", 2, 1, 20, 1, 0, 1)); // easy
        // "INFO: Sensor accuracy degraded. Research results may become unreliable."

		tasks.add(new ResearchTask("Study Microbial Life", 8, 3, 20, 2, 1, 3));
        // "WARNING: Unknown microorganisms detected. Potential biological threat."
		tasks.add(new ResearchTask("Develop Plant Growth Formula", 7, 3, 20, 2, 1, 3));
        // "INFO: Food production efficiency limited. Ignoring delays sustainability."
		tasks.add(new ResearchTask("Analyze Radiation Levels", 9, 4, 30, 2, 1, 4)); // medium
        // "WARNING: Radiation spikes detected. Long-term exposure risk increasing."

		tasks.add(new ResearchTask("Create Synthetic Oxygen Generator", 15, 6, 40, 3, 2, 5));
        // "CRITICAL PROJECT: Alternative oxygen source required. Failure limits survival options."
		tasks.add(new ResearchTask("Alien Virus Containment Study", 18, 7, 40, 4, 2, 6));
        // "BIOHAZARD: Unknown virus detected. Unchecked spread could wipe out crew."
		tasks.add(new ResearchTask("Terraforming Experiment", 20, 8, 30, 4, 2, 7)); // hard
        // "MISSION CRITICAL: Terraforming progress halted. Colony long-term survival at risk."

		return tasks;
	}

}
