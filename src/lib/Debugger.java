package lib;

public class Debugger {
    /**
     * A global debug setting. If this is false All debugger initialize will not
     * show any debug prints.
     */
    private static final boolean SHOW_DEBUG = true;

    /**
     * The state wether the debugger should be print when it is called.
     */
    private boolean showDebugPrints = true;

    /**
     * The class name of that is being debugged.
     */
    private String className = "";

    /**
     * The function name that is being debugged.
     */
    private String functionName = "";

    /**
     * Initialize the debugger.
     */
    public static Debugger init() {
        return new Debugger();
    }

    /**
     * A debug print statement, if IS_DEBUG is set to true it will print the output
     * to the standard out.
     * 
     * @param value to be printed.
     */
    public void print(String value) {
        if (!SHOW_DEBUG)
            return;
        // Determine wether to show the class and function name
        String section = "";
        if (!className.equalsIgnoreCase("") && !functionName.equalsIgnoreCase(""))
            section = className + "." + functionName + "(): ";
        else if (!className.equalsIgnoreCase(""))
            section = className + ": ";
        // Show the debug print
        if (showDebugPrints) {
            System.out.println("\t[DEBUG]: " + section + value);
        }
    }

    /**
     * Set whether debug print should be displayed.
     * 
     * @param value is true then show the debug print.
     */
    public Debugger showDebugPrint(boolean value) {
        this.showDebugPrints = value;
        return this;
    }

    /**
     * Set the class name whether to show what class it is debugging.
     * 
     * @param value of the class name string.
     */
    public Debugger setClassName(String value) {
        if (value == null)
            value = "";
        this.className = value;
        return this;
    }

    /**
     * Set the function name whether to show what function it is debugging.
     * 
     * @param value of the function name string.
     */
    public Debugger setFunctionName(String value) {
        if (value == null)
            value = "";
        this.functionName = value;
        return this;
    }
}
