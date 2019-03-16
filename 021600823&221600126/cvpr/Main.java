import cvpr.PaperContent;
import cvpr.PaperSniffer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    static final String OUTPUT_FILE = "result.txt";

    public static void main(String[] args) {
        PaperSniffer sniffer = new PaperSniffer();
        try {
            writeFile(sniffer.getPaperContents());
        } catch (IOException e) {
            System.err.println("Fatal error");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Done!");
    }

    static void writeFile(List<PaperContent> paperContents) throws IOException {
        FileWriter writer = new FileWriter(new File(OUTPUT_FILE));
        int count = 0;
        for(PaperContent content : paperContents) {
            writer.write(String.format("%d%n", count++));
            writer.write(String.format("Title: %s%n", content.getPaperTitle()));
            writer.write(String.format("Abstract: %s%n", content.getPaperAbstract()));
            writer.write(String.format("%n%n"));
        }
        writer.close();
    }
}
