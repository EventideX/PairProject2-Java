package util;

import org.apache.commons.cli.*;

public class Config {
    private static CommandLine commandLine;
    private static final Options options;
    private static String inputFileName;
    private static String outputFileName;
    private static boolean countWithWeight;
    private static int wordGroupSize;
    private static int maxOutputNumber;

    static {
        options = new Options();
        wordGroupSize = 0;
        maxOutputNumber = 10;
        addOptions();
    }

    private static void addOptions() {
        Option inputFile = Option.builder("i")
                .argName("filename")
                .desc("the input file")
                .required()
                .hasArg()
                .build();
        options.addOption(inputFile);

        Option outputFile = Option.builder("o")
                .argName("filename")
                .required()
                .hasArg()
                .desc("the file that stores the result")
                .build();
        options.addOption(outputFile);

        Option countWithWeight = Option.builder("w")
                .hasArg()
                .required()
                .argName("[0 | 1]")
                .desc("determine if count words with weight")
                .build();
        options.addOption(countWithWeight);

        Option wordGroupCount = Option.builder("m")
                .hasArg()
                .argName("number")
                .desc("determine if count words with word group, disabled by default")
                .build();
        options.addOption(wordGroupCount);

        Option maxOutputNumber = Option.builder("n")
                .hasArg()
                .argName("number")
                .desc("only display the most `number` frequent words, 10 by default")
                .build();
        options.addOption(maxOutputNumber);
    }

    public static void parseCommandLine(String[] args) throws ParseException {
        DefaultParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);

        inputFileName = commandLine.getOptionValue("i");
        outputFileName = commandLine.getOptionValue("o");

        if(commandLine.getOptionValue("w").equals("0"))
            countWithWeight = false;
        else if(commandLine.getOptionValue("w").equals("1"))
            countWithWeight = true;
        else
            throw new ParseException("the -w option only accepts 0 or 1");

        if(commandLine.hasOption("m")) {
            try {
                maxOutputNumber = Integer.parseInt(commandLine.getOptionValue("m"));
            } catch (NumberFormatException e) {
                throw new ParseException("the -m option only accepts a number");
            }
        }

        if(commandLine.hasOption("n")) {
            try {
                wordGroupSize = Integer.parseInt(commandLine.getOptionValue("n"));
            } catch (NumberFormatException e) {
                throw new ParseException("the -n option only accepts a number");
            }
        }
    }

    public static void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("WordCount", options);
    }
}
