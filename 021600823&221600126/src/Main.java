import com.wordCount.WordCount;
import org.apache.commons.cli.ParseException;
import util.Config;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Config.parseCommandLine(args);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            Config.printHelpMessage();
            System.exit(-1);
        }

        WordCount count = new WordCount(Config.getOutputFileName());
        if(Config.isCountWithWeight()) {
            count.setCountWithWeight();
        }

        try {
            count.countWords(Config.getInputFileName(), Config.getMaxOutputNumber(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
