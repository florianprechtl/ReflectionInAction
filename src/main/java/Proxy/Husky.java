package Proxy;

public class Husky implements Dog {
	public String name;
	public int size;

	@Override
	public void initialize(String name, int size) {
		this.name = name;
		this.size = size;
	}

	@Override
	public boolean retrieveStick() {
		System.out.println("I retrieve the stick");
		return Math.random() > 0.5;
	}
}
