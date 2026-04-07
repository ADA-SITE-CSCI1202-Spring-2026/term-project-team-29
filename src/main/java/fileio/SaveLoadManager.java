package fileio;

import resources.Resource;
import resources.ResourceManager;
import tasks.ColonyTask;
import tasks.EngineeringTask;
import tasks.LifeSupportTask;
import tasks.ResearchTask;
import java.io.*;
import java.util.*;

public class SaveLoadManager {

	// filePath where it will be saved the "save_state.txt" file
	// rm to read credits and resources
	// taskQueue is the list of pending tasks
	public void save(String filePath, ResourceManager rm, Queue<ColonyTask> taskQueue) throws IOException {
		// using Buffered Writer to make writing faster
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			bw.write("CREDITS: " + rm.getCredits());
			bw.newLine();

			HashMap<Resource, Integer> inventory = rm.getInventory();
			for (Resource r : Resource.values()) {
				bw.write("RESOURCE:" + r.name() + ":" + inventory.getOrDefault(r, 0));
				bw.newLine();
			}

			bw.write("QUEUE_START");
			bw.newLine();

			for (ColonyTask task : taskQueue) {
				bw.write(serializeTask(task));
				bw.newLine();
			}

			bw.write("QUEUE_END");
			bw.newLine();
		}
	}

	private String serializeTask(ColonyTask task) {
		String base = "TASK:" + task.getTaskType() + ":" + task.getName() + ":r_parts:" + task.getRequiredParts()
				+ ":time:" + task.getTimeToFix() + ":r_crew:" + task.getCrewMembersRequired();
		if (task instanceof LifeSupportTask lst) {
			return base + ":r_oxygen:" + lst.getOxygenRequired() + ":r_suits:" + lst.getSpaceSuits();
		} else if (task instanceof EngineeringTask et) {
			return base + ":r_powerunits:" + et.getPowerUnitsRequired();
		} else if (task instanceof ResearchTask rt) {
			return base + ":r_lab:" + rt.getLabEquipmentRequired();
		}
		return base; // fallback scenario, shouldn't happen
	}

	public Queue<ColonyTask> load(String filePath, ResourceManager rm) throws IOException {
		Queue<ColonyTask> restoredQueue = new LinkedList<>();
		Map<Resource, Integer> loadedInventory = new HashMap<>();
		boolean inQueue = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim(); // deletes accidental spaces
				if (line.isEmpty())
					continue;

				if (line.startsWith("CREDITS:")) {
					// parse the credits ex: "1000" to 1000
					int credits = Integer.parseInt(line.split(":")[1].trim());
					rm.setCredits(credits);

				} else if (line.startsWith("RESOURCE:")) {
					// parse "RESOURCE:OXYGEN" > OXYGEN, 100
					String[] parts = line.split(":");
					Resource r = Resource.valueOf(parts[1].trim());
					int amount = Integer.parseInt(parts[2].trim());
					loadedInventory.put(r, amount);
				} else if (line.startsWith("QUEUE_START")) {
					inQueue = true;
				} else if (line.startsWith("QUEUE_END")) {
					inQueue = false;
				} else if (inQueue == true && line.startsWith("TASK:")) {
					// deserialize the task and add it to the queue
					ColonyTask task = deserializeTask(line);
					if (task != null)
						restoredQueue.add(task);
				}

			}
		}
		// restore inventory to the resource manager
		rm.setInventory(loadedInventory);
		return restoredQueue;
	}

	private ColonyTask deserializeTask(String line) {

		// example line:
		// TASK:LIFE_SUPPORT:Oxygen
		// Leak:r_parts:2:time:30:r_crew:3:r_oxygen:10:r_suits:2
		// we split by ":" and read each field by position
		String[] parts = line.split(":");

		// p[0] = "TASK"
		// p[1] = task type example:LIFE_SUPPORT
		// p[2] = task name
		// p[3] = "r_parts", p[4] = value
		// p[5] = "time", p[6] = value
		// p[7] = "r_crew", p[8] = value
		// p[9] = unique key, p[10] = unique value ...

		String type = parts[1].trim();
		String name = parts[2].trim();
		int requiredParts = Integer.parseInt(parts[4].trim());
		int timeToFix = Integer.parseInt(parts[6].trim());
		int crewMembersRequired = Integer.parseInt(parts[8].trim());

		switch (type) {

		case "LIFE_SUPPORT":
			// p[9]="r_oxygen", p[10]=value, p[11]="r_suits", p[12]=value
			int oxygen = Integer.parseInt(parts[10].trim());
			int suits = Integer.parseInt(parts[12].trim());
			return new LifeSupportTask(name, oxygen, suits, requiredParts, timeToFix, crewMembersRequired);

		case "ENGINEERING_TASK":
			// p[9]="r_powerunits", p[10]=value
			int powerUnitsRequired = Integer.parseInt(parts[10].trim());
			return new EngineeringTask(name, powerUnitsRequired, requiredParts, timeToFix, crewMembersRequired);

		case "RESEARCH_TASK":
			// p[9]="r_lab", p[10]=value
			int labEquipmentRequired = Integer.parseInt(parts[10].trim());
			return new ResearchTask(name, labEquipmentRequired, requiredParts, timeToFix, crewMembersRequired);

		default:
			System.err.println("WARNING: unknown task type -> " + type);
			return null;
		}

	}

}
