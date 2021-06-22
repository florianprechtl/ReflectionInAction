package ClassLoader;

public class ConstructOnce {
	static private int runIndex = 0;
	public ConstructOnce() {
		System.out.println(runIndex);
		runIndex++;
	}
}