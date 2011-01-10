/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import gui.mainWindow.MainWindow;
import gui.fetchingProgressWindow.FetchingProcessWindow;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes the fetching process to extract the comments from the amazon URL.
 *
 * @author javiersalcedogomez
 */
public class FetchingProcess extends Thread {

    /**
     * Command to execute in the runtime command.
     */
    public static final String COMMAND = "python";
    /**
     * Python program for fetching the comments from the amazon.com.
     */
    public static final String ARGUMENTS = "src/commentFetcher/amazoncommentfetcher.py";
    /**
     * Name for the output file which contains the comments. To generate it will
     * take the size of the keyword list in the main window.
     *
     * So if the size is 0 then will generate the comments1.txt file and so on.
     */
    public static final String FILE = "comments";

    /**
     * Creates a new fetching process.
     */
    public FetchingProcess() {
        super();
    }

    @Override
    public void run() {

        // Creates the string buffer for reading the output from the python program
        StringBuffer message = new StringBuffer();

        // Updates the progress window
        FetchingProcessWindow.getInstance().updateProgressWindow("Initializing the fetching process...\n");

        // Clears the text pane content
        FetchingProcessWindow.getInstance().clearTextAreaContent();

        // Calls the python program to obtain the input data
        try {

            // Executes the fetching process
            Process process = Runtime.getRuntime().exec(COMMAND + " "
                    + ARGUMENTS + " "
                    + MainWindow.getInstance().getURLProductText() + " "
                    + FILE
                    + (MainWindow.getInstance().getProductKeywordList().size() + 1)
                    + ".txt");

            // Gets the input stream with the results of the commentfetcheranalyzer
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(inputStream));
            BufferedReader bufferedError = new BufferedReader(new InputStreamReader(errorStream));

            // Shows the progress window
            FetchingProcessWindow.getInstance().showProgressWindow();

            while (true) {
                if (inputStream.available() > 0) {
                    String lineIn;
                    while ((lineIn = bufferedInput.readLine()) != null) {
                        // Updates the progress window
                        FetchingProcessWindow.getInstance().updateProgressWindow(lineIn);
                        message.append(lineIn);
                        message.append(System.getProperty("line.separator"));
                    }
                }
                if (errorStream.available() > 0) {
                    String lineIn;
                    while ((lineIn = bufferedError.readLine()) != null) {
                        // Updates the progress window
                        FetchingProcessWindow.getInstance().updateProgressWindow(lineIn);
                        message.append("ERROR: ");
                        message.append(lineIn);
                        message.append(System.getProperty("line.separator"));
                    }
                }
                try {
                    process.exitValue();
                    break;
                } catch (Throwable throwable) {
                    Thread.sleep(1000);
                }
            }

            bufferedInput.close();
            bufferedError.close();

            // Enables the close button in the progress window
            FetchingProcessWindow.getInstance().enableCloseButton();

        } catch (Throwable throwable) {
            // Updates the log
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, throwable);
        }
    }
}
