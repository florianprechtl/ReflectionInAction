package CodeGeneration.C2C;

import Mopex.UQueue;

public class Args {
	private String[] args;
	private boolean[] argProcessed;

	Args(String[] cmdArgs) {
		args = cmdArgs;
		argProcessed = new boolean[args.length];
	}

	public String getLast() {
		if (args[args.length - 1].charAt(0) == '-' )
		return null;
		String returnValue = args[args.length - 1];
		argProcessed[args.length - 1] = true;
		return returnValue;
	}

	public UQueue getFlagValues(String flag) {
		UQueue values = new UQueue(String.class);
		String value = getFlagValue(flag);
		while (value != null) {
			values.add(value);
			value = getFlagValue(flag);
		}
		return values;
	}

	public String getFlagValue( String flag ) {
		for ( int i = 0; i < args.length-1; i++ )
			if ( !argProcessed[i] && !argProcessed[i+1] && args[i].equals(flag)
					&& args[i].charAt(0) == '-' && args[i+1].charAt(0) != '-' ) {
				String returnValue = args[i+1];
				argProcessed[i] = true;
				argProcessed[i+1] = true;
				return returnValue;
			}
		return null;
	}

	public boolean hasFlag(String flag) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null && args[i].equals(flag)
					&& args[i].charAt(0) == '-') {
				argProcessed[i] = true;
				return true;
			}
		}
		return false;
	}

	public boolean complete() {
		for (int i = 0; i < argProcessed.length; i++) {
			if (!argProcessed[i]) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < args.length; i++) {
			if (!argProcessed[i]) {
				result += args[i] + " ";
			}
		}
		return result;
	}
}
