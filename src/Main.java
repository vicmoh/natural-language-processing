import java.io.InputStreamReader;

/**
 * This class is the main class to run the program.
 */
public class Main {
	/**
	 * The default path of for testing a sample file.
	 */
	private static final String DEFAULT_FILE_PATH = "../assets/samples.text";

	/**
	 * Main functions to run the program
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// Check if args exist
		String fileName;
		if (args.length <= 1)
			fileName = DEFAULT_FILE_PATH;
		else
			fileName = args[0];
		// If arg exist
		System.out.println("\nRunning program with file path: \"" + fileName + "\".");
		try {
			readFile(fileName);
		} catch (Exception error) {
			System.out.println(error);
		}
	}

	private static void readFile(String fileName) throws Exception {
		if (fileName == null)
			throw new Exception("File name is not passed.");

	}
}
