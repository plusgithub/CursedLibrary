package com.cursedplanet.cursedlibrary.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/* How to use:
 Create a new instance of ItemPicker in your class
 ItemPicker picker = new ItemPicker();
 picker.addChance(itemStack, chance as %);
*/
public class ItemPicker {
	private class Chance {
		private int upperLimit;
		private int lowerLimit;
		private Object element;

		public Chance(Object element, int lowerLimit, int upperLimit) {
			this.element = element;
			this.upperLimit = upperLimit;
			this.lowerLimit = lowerLimit;
		}

		public int getUpperLimit() {
			return this.upperLimit;
		}

		public int getLowerLimit() {
			return this.lowerLimit;
		}

		public Object getElement() {
			return this.element;
		}

		@Override
		public String toString() {
			return "[" + Integer.toString(this.lowerLimit) + "|" + Integer.toString(this.upperLimit) + "]: " + this.element.toString();
		}
	}

	private List<Chance> chances;
	private int sum;
	private Random random;

	public ItemPicker() {
		this.random = new Random();
		this.chances = new ArrayList<>();
		this.sum = 0;
	}

	public ItemPicker(long seed) {
		this.random = new Random(seed);
		this.chances = new ArrayList<>();
		this.sum = 0;
	}

	public void addChance(Object element, int chance) {
		if (!this.chances.contains(element)) {
			this.chances.add(new Chance(element, this.sum, this.sum + chance));
			this.sum = this.sum + chance;
		} else {
			// not sure yet, what to do, when the element already exists, since a list can't contain 2 equal entries. Right now a second, identical chance (element + chance must be equal) will be ignored
		}
	}

	public Object getRandomElement() {
		int index = this.random.nextInt(this.sum);
		// debug: System.out.println("Amount of chances: " + Integer.toString(this.chances.size()) + ", possible options: " + Integer.toString(this.sum) + ", chosen option: " + Integer.toString(index));
		for (Chance chance : this.chances) {
			// debug: System.out.println(chance.toString());
			if (chance.getLowerLimit() <= index && chance.getUpperLimit() > index) {
				return chance.getElement();
			}
		}
		return null; // this should never happen, because the random can't be out of the limits
	}

	public int getOptions() { // might be needed sometimes
		return this.sum;
	}

	public int getChoices() { // might be needed sometimes
		return this.chances.size();
	}
}