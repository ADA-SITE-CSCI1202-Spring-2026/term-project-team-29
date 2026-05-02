package resources;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
	private HashMap<Resource, Integer> inventory; // private so gui can never directly change this
	private int credits;
	private int hp;

	public ResourceManager() {
		inventory = new HashMap<>();
		// setup the starting values
		// ALL TEMP VALUES , SUBJECT TO CHANGE
		inventory.put(Resource.OXYGEN, 100);
		inventory.put(Resource.SPACE_SUITS, 5);
		inventory.put(Resource.SPARE_PARTS, 25);
		inventory.put(Resource.CREW_MEMBERS, 10);
		inventory.put(Resource.SUPPLIES, 30);
		inventory.put(Resource.LAB_EQUIPMENTS, 10);
		inventory.put(Resource.POWER_UNITS, 20);
		credits = 1000;
		hp = 100;
		// ALL TEMP VALUES, SUBJECT TO CHANGE
	}

	public int getAmount(Resource r) {
		return inventory.getOrDefault(r, 0);
	}

	public void restock(Resource r, int amount) {
		inventory.put(r, getAmount(r) + amount);
	}

	public boolean hasEnough(Resource r, int amount) {
		return getAmount(r) >= amount;
	}

	public void deduct(Resource r, int amount) {
		inventory.put(r, Math.max(0, getAmount(r) - amount));
	}

	public void setResource(Resource resource, int amount) {
		inventory.put(resource, amount);
	}

	public int getCredits() {
		return credits;
	}

	public void addCredits(int amount) {
		credits += amount;
	}

	public void deductCredits(int amount) {
	    credits = Math.max(0, credits - amount);
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void damageHp(int amount) {
		hp = Math.max(0, hp - amount);
	}

	public void setInventory(Map<Resource, Integer> loadedInventory) {
		inventory.clear();
		inventory.putAll(loadedInventory);
	}

	public HashMap<Resource, Integer> getInventory() {
		return new HashMap<>(inventory); // return the copy, not the original
	}
}
