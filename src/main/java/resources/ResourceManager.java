package resources;

import java.util.HashMap;

public class ResourceManager {
	private HashMap<Resource, Integer> inventory;  //private so gui can never directly change this
	private int credits;

	public ResourceManager() {
		inventory = new HashMap<>();
		// setup the starting values
		inventory.put(Resource.OXYGEN, 100);
		inventory.put(Resource.SPARE_PARTS, 20);
		inventory.put(Resource.RATIONS, 40);
		credits = 1000;
	}

	public int getAmount(Resource r) {
		return inventory.getOrDefault(r, 0);
	}

	public boolean hasEnough(Resource r, int amount) {
		return getAmount(r) >= amount;
	}

	public void deduct(Resource r, int amount) {
		inventory.put(r, getAmount(r) - amount);
	}

	public int getCredits() {
		return credits;
	}

	public void addCredits(int amount) {
		credits += amount;
	}

	public void deductCredits(int amount) {
		credits -= amount;
	}

	public HashMap<Resource, Integer> getInventory() {
		return new HashMap<>(inventory); // return the copy, not the original
	}
}
