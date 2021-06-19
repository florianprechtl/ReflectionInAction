package XMLSerialization;

public class Zoo {
	public String city;
	public String name;
	public Animal[] animals;

	public Zoo(String city, String name) {
		this.city = city;
		this.name = name;
		animals = new Animal[0];
	}

	public void add(Animal animal) {
		Animal[] newAnimals = new Animal[animals.length + 1];
		for (int i = 0; i < animals.length; i++) {
			newAnimals[i] = animals[i];
		}
		newAnimals[animals.length] = animal;

		animals = newAnimals;
	}
}
