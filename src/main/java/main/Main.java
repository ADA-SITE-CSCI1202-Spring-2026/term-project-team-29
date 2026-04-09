package main;

//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.Queue;
//
//import fileio.SaveLoadManager;
//import resources.Resource;
//import resources.ResourceManager;
//import tasks.ColonyTask;
//import tasks.EngineeringTask;
//import tasks.LifeSupportTask;
//import tasks.ResearchTask;
import ui.MainFrame;

public class Main {
	public static void main(String[] args) {
		new MainFrame();

		
		
		// this is for testing the SaveLoadManager.java

//		ResourceManager rm = new ResourceManager();
//		Queue<ColonyTask> queue = new LinkedList<>();
//
//		queue.add(new LifeSupportTask("Oxygen Leak", 10, 2, 3, 30, 2, 0, 2));
//		queue.add(new EngineeringTask("Solar Array", 5, 3, 45, 4, 0, 2));
//		queue.add(new ResearchTask("Lab Study", 2, 1, 20, 1, 2, 3));
//
//		SaveLoadManager slm = new SaveLoadManager();
//		slm.save("test_save.txt", rm, queue);
//
//		System.out.println("Saved!");
//
//		ResourceManager rm2 = new ResourceManager();
//
//		Queue<ColonyTask> loadedQueue = slm.load("test_save.txt", rm2);
//
//		System.out.println("Credits: " + rm2.getCredits());
//		System.out.println("Oxygen: " + rm2.getAmount(Resource.OXYGEN));
//		System.out.println("Queue size: " + loadedQueue.size());
//		for (ColonyTask t : loadedQueue) {
//			System.out.println("Task: " + t.getName() + " | Type: " + t.getTaskType());
//		}
	}
}
