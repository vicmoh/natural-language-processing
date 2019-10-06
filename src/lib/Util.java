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
    private static Debugger debug = Debugger.init().showDebugPrint(false).setClassName("Parser");

    /**
     * Read the string and return whole file into one string, without new line.
     * 
     * @param filePath to be read.
     * @return the string builder.
     * @throws Exception.
     */
    public static String readFile(String filePath) throws Exception {
        debug.setFunctionName("readString").print("Invoked.");
        try {
            InputStream inFile = new FileInputStream(filePath);
            InputStreamReader in = new InputStreamReader(inFile);
            BufferedReader buffer = new BufferedReader(in);
            StringBuilder sb = new StringBuilder();
            String line = buffer.readLine();
            while (line != null) {
                sb.append(line + " ");
                line = buffer.readLine();
            }
            if (buffer != null)
                buffer.close();
            if (inFile != null)
                inFile.close();
            if (in != null)
                in.close();
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
    public static void writeFile(String filePath, String data) throws Exception {
        debug.setFunctionName("writeFile").print("Invoked.");
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to write file to the path. Path might be invalid.");
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}