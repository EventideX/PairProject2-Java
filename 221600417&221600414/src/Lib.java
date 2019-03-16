import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: function library
 * @author: hlx 2019-03-11
 **/
class Lib {

    private static final Pattern NOT_DIV_RE = Pattern.compile("[^a-zA-Z0-9]+");

    private static final Pattern DIV_RE = Pattern.compile("[a-zA-Z0-9]+");

    static Map<String, String> argsToMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            map.put(args[i].substring(1), args[i + 1]);
        }
        return map;
    }

    // calculate the number of characters
    static int getCharacters(String s) {
        int num = 0, charInt;
        for (int i = 0; i < s.length(); i++) {
            charInt = s.charAt(i);
            if (charInt <= 127) {
                num++;
            }
        }
        if (s.length() == 0) return 0;
        else return (num + 1);
    }

    // determine letter
    private static boolean isLetter(int charInt) {
        return (charInt >= 65 && charInt <= 90) || (charInt >= 97 && charInt <= 122);
    }

    // determine number
    private static boolean isNumber(int charInt) {
        return charInt >= 48 && charInt <= 57;
    }

    // determine division
    private static boolean isDivision(int charInt) {
        return (charInt < 48 || (charInt > 57 && charInt < 65) || (charInt > 90 && charInt < 97) || charInt > 122);
    }

    // determine word
    private static boolean isWord(String s) {
        if (s.length() < 4) return false;
        for (int i = 0; i < s.length(); i++) {
            int charInt = s.charAt(i);
            if (!isLetter(charInt)) {
                if (i > 3) {
                    if (!isNumber(charInt)) return false;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // read the next paper
    static boolean nextPaper(BufferedReader reader) throws IOException {
        boolean nextSuccess = false;
        for (int i = 0; i < 2; i++) {
            if (reader.readLine() != null) nextSuccess = true;
        }
        return nextSuccess;
    }


    static int countWord(String s, int w, int len, Map<String, Integer> map) {
        int wordNum = 0;
        boolean isDivBegin = isDivision(s.charAt(0));
        List<String> titles = cutStr(s, DIV_RE);
        List<String> titles2 = cutStr(s, NOT_DIV_RE);
        for (int i = 0, j = i; i < titles.size(); i++) {
            if (isWord(titles.get(i))) {
                wordNum++;
                if ((i - j + 1) == len) {
                    String word = getWord(isDivBegin, titles, titles2, j, i).toLowerCase();
                    map.merge(word, w, (a, b) -> a + b);
                    j++;
                }
            } else {
                j = i + 1;
            }
        }
        return wordNum;
    }

    //cut string through regular expressions
    private static List<String> cutStr(String s, Pattern pattern) {
        List<String> strList = new ArrayList<>();
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            strList.add(m.group());
        }
        return strList;
    }


    private static String getWord(boolean isDivBegin, List<String> s, List<String> s2, int j, int i) {
        StringBuilder builder = new StringBuilder();
        int offset = isDivBegin ? 1 : 0;
        while (j <= i) {
            builder.append(s.get(j));
            if (j != i) builder.append(s2.get(j + offset));
            j++;
        }
        return builder.toString();
    }

    static void sortMapAndOut(Map<String, Integer> map, StringBuilder builder, int sortLen) {
        map.entrySet()
                .stream().sorted((e1, e2) -> {
                    int cmp = e2.getValue().compareTo(e1.getValue());
                    if (cmp == 0) return e1.getKey().compareTo(e2.getKey());
                    else return cmp;
                })
                .limit(sortLen)
                .forEach(o -> builder.append("<").append(o.getKey()).append(">").append(": ").append(o.getValue()).append("\r\n"));
    }

    static void strOutFile(String s, String f) throws IOException {
        BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(f));
        bf.write(s.getBytes());
        bf.flush();
    }

}
