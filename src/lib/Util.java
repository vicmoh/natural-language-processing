package lib;

import lib.Debugger;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Util {
    /**
     * Setup debugger for debug printing.
     */
    private static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Parser");

    /**
     * Read the string and return whole file into one string, without new line.
     * 
     * @param fileName to be read.
     * @return the string builder.
     * @throws Exception.
     */
    public static String readFile(String fileName) throws Exception {
        debug.setFunctionName("readString").print("Invoked.");
        // Try to read the whole string
        try {
            InputStream inFile = new FileInputStream(fileName);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inFile));
            StringBuilder sb = new StringBuilder();
            String line = buffer.readLine();
            while (line != null) {
                sb.append(line + " ");
                line = buffer.readLine();
            }
            buffer.close();
            writeFile(sb.toString());
            return sb.toString();
        } catch (Exception exception) {
            throw new Exception("Exception ocurred. Could not read file.");
        }
    }

    /**
     * Use Streams when you are dealing with raw data to write the data to
     * <name>.splitted.
     * 
     * @param data is the data to written.
     */
    private static void writeFile(String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream("../output/data.splitted");
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}