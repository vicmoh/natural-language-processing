/**
 * This class is the main class to run the program.
 */
public class Main {
	/**
	 * Main functions to run the program
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("\nRunning program...");
		// Check if args exist
		if (args.length <= 1) {
			System.out.println("Argument is empty.");
			return;
		}
		// If arg exist
		System.out.println("File is \"" + args[0] + "\"");
		try {
			readFile("../assets/samples.text");
		} catch (Exception error) {
			System.out.println(error);
		}
	}

	private static void readFile(String fileName) throws Exception {
		if (fileName == null)
			throw new Exception("File name is not passed.");
		String debug = "Main.readFile():";
		System.out.println(debug + " Invoked.");
	}
}
