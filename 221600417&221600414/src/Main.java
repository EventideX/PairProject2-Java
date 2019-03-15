import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @description: main
 * @author: hlx 2019-03-11
 **/
public class Main {

    public static void main(String[] args) throws IOException {
        // arg handle
        Map<String, String> map = Lib.argsToMap(args);
        String inFile = map.get("i");
        String outFile = map.get("o");

        Map<String, Integer> letterMap = new HashMap<>();
        int charNum = 0, lineNum = 0, wordNum = 0;
        int titleWeight = 1, phraseLen = 1, sortLen = 10;
        boolean nextSuccess = true;
        if (map.get("w").equals("1")) titleWeight = 10;
        if (map.containsKey("m")) phraseLen = Integer.parseInt(map.get("m"));
        if (map.containsKey("n")) sortLen = Integer.parseInt(map.get("n"));


        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
        while (reader.readLine() != null) {
            String titleStr = reader.readLine().substring(7);
            String abstractStr = reader.readLine().substring(10);
            wordNum += Lib.countWord(titleStr, titleWeight, phraseLen, letterMap);
            wordNum += Lib.countWord(abstractStr, 1, phraseLen, letterMap);
            lineNum += 2;
            charNum += Lib.getCharacters(titleStr) + Lib.getCharacters(abstractStr);
            nextSuccess = Lib.nextPaper(reader);
        }
        if (!nextSuccess) charNum--;


        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("characters: ").append(charNum).append("\r\n")
                .append("words: ").append(wordNum).append("\r\n")
                .append("lines: ").append(lineNum).append("\r\n");
        Lib.sortMapAndOut(letterMap, resultBuilder, sortLen);
        Lib.strOutFile(resultBuilder.toString(), outFile);
    }

}
