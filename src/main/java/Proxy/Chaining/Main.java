package Proxy.Chaining;

import Proxy.Dog;
import Proxy.Husky;
import Proxy.TracingIH;
import java.io.PrintWriter;

public class Main {
	public static void main(String[] args) {
		Dog rover = (Dog) OtherIH.createProxy(
				OtherIH.createProxy(
						OtherIH.createProxy(
								TracingIH.createProxy(new Husky(), new PrintWriter(System.out, true)
								)
						)
				)
		);
		rover.retrieveStick();
	}
}
