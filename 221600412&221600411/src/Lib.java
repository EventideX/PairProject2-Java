import java.util.*;


/**
 * @program: java8
 * @description:
 * @author: ChenYu
 * @create: 2019-03-11 16:09
 **/
public class Lib {

    private Map<String, Integer> map = new HashMap<>();  // For the word
    private int wordNum = 0;
    private long charNum = 0;
    private long lines = 0;


    private int titleWeight = 0;
    private int abstractWeight = 0;
    private int m = 1;
    private int n = 10;

    public void countChar(String content) {
        for(int i = 0; i< content.length() ; i++){
            int ch = content.charAt(i);
            if(ch>=0 && ch<=127){
                charNum++;
            }
        }
    }

    public String printChar() {
        if(charNum == 0){
            return "characters: 0";
        }
        return "characters: " + String.valueOf(charNum - 1);
    }

    public String printWord() {
        return "words: " + wordNum;
    }

    public String printLines() {
        return "lines: " + lines;
    }

    public void countWordOrWordGroupAndFrequency(String content, int w1, int w2, int m1, int n1) {
        titleWeight = w1;
        abstractWeight = w2;
        m = m1;
        n = n1;
        boolean isWordGroup = false;
        if (m >= 2) {
            isWordGroup = true;
        }
        String[] attr = content.split("\r\n\r\n");
        String[] str = null;
        String t1 = null;
        String t2 = null;
        for (int i = 0; i < attr.length; i++) {
            if (attr[i] != null && !attr[i].equals("")) {
                if (i == 0) {
                    str = attr[i].split("\r\n");
                } else {
                    str = attr[i].replaceFirst("\r\n", "").split("\r\n");
                }

                if (str.length == 3) {
                    t1 = str[1].substring(7);
                    t2 = str[2].substring(10);
                    lines += 2;   // add line
                    charNum += 2; // add line
                    countChar(t1 + t2);
                    if (isWordGroup) {
                        countWordGroup(t1, true);  // Calculate the title
                        countWordGroup(t2, false); // Calculated in this paper,
                    } else {
                        countWord(t1, true);  // Calculate the title
                        countWord(t2, false); // Calculated in this paper
                    }
                }
            }
        }
    }

    // Count words
    public void countWord(String content, boolean isTitle) {
        char[] ch = content.toCharArray();
        int begin = 0, end = 0, len = content.toCharArray().length;
        String str = null;
        for (int i = 0; i < len; i++) {
            boolean flag = !((ch[i] >= 65 && ch[i] <= 90) || (ch[i] >= 97 && ch[i] <= 122) || (ch[i] >= 48 && ch[i] <= 57)); // 判断是否是分隔符
            if (flag || i == 0) {  // If it is a delimiter or the beginning of the calculation
                if (flag) {
                    begin = end = i + 1;
                } else {
                    begin = end = i;
                }
                // Find two delimiters
                while (end < len && ((ch[end] >= 65 && ch[end] <= 90) ||
                        (ch[end] >= 97 && ch[end] <= 122) || (ch[end] >= 48 && ch[end] <= 57))) {
                    end++;
                }
                if (begin != end) {
                    i = end - 1;
                    if (end - begin >= 4) {
                        str = content.substring(begin, end).toLowerCase();
                        boolean isWord = true;
                        for (int j = 0; j < 4; j++) { // If the first four are letters
                            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                                isWord = false;
                                break;
                            }
                        }
                        if (isWord) {
                            wordNum++;
                            if (map.containsKey(str)) {
                                if (isTitle) {
                                    map.put(str, map.get(str) + titleWeight);
                                } else {
                                    map.put(str, map.get(str) + abstractWeight);
                                }
                            } else {
                                if (isTitle) {
                                    map.put(str,titleWeight);
                                } else {
                                    map.put(str,abstractWeight);
                                }
                            }
                        }
                    }
                }
            } else {
                continue;
            }
        }
    }


    // Computing phrase
    public void countWordGroup(String content, boolean isTitle) {
        char[] ch = content.toCharArray();
        int wordGroupBegin = 0, wordGroupEnd = 0;
        int firstWordEnd = 0;
        int begin = 0, end = 0, len = content.toCharArray().length;
        String str = null;
        int wordGroupNum = 0;
        for (int i = 0; i < len; i++) {
            boolean flag = !((ch[i] >= 65 && ch[i] <= 90) || (ch[i] >= 97 && ch[i] <= 122) || (ch[i] >= 48 && ch[i] <= 57)); // Determines if it is a delimiter
            if (flag || i == 0) {  // If it is a delimiter or the beginning of the calculation
                if (flag) {
                    begin = end = i + 1;
                } else {
                    begin = end = i;
                }
                // Find two delimiters
                while (end < len && ((ch[end] >= 65 && ch[end] <= 90) ||
                        (ch[end] >= 97 && ch[end] <= 122) || (ch[end] >= 48 && ch[end] <= 57))) {
                    end++;
                }
                if (begin != end) {
                    i = end - 1;
                    if (end - begin < 4) {
                        wordGroupNum = 0;
                    }
                    if (end - begin >= 4) {
                        str = content.substring(begin, end).toLowerCase();
                        boolean isWord = true;
                        for (int j = 0; j < 4; j++) { // If the first four are letters
                            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                                isWord = false;
                                wordGroupNum = 0;
                                break;
                            }
                        }
                        if (isWord) {
                            wordNum++;
                            wordGroupNum++;
                            if (wordGroupNum == 1) {
                                wordGroupBegin = begin;
                                firstWordEnd = end;
                            }
                            if (wordGroupNum == m) {
                                wordGroupEnd = end;
                                str = content.substring(wordGroupBegin, wordGroupEnd).toLowerCase();
                                if (map.containsKey(str)) {
                                    if (isTitle) {
                                        map.put(str, map.get(str) + titleWeight);
                                    } else {
                                        map.put(str, map.get(str) + abstractWeight);
                                    }
                                } else {
                                    if (isTitle) {
                                        map.put(str,titleWeight);
                                    } else {
                                        map.put(str,abstractWeight);
                                    }
                                }
                                wordGroupNum = 0;
                                i = firstWordEnd - 1;
                                wordNum = wordNum - m +1  ;
                            }
                        } else {
                            wordGroupNum = 0;
                        }
                    }
                }
            } else {
                continue;
            }
        }
    }


    // Computing word frequency
    public String printCountMostWord(int n){
        //Sort the keys in the HashMap and display the sorted results
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        // Sort the keys in the HashMap
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (o1.getValue() == o2.getValue()) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return -(o1.getValue() - o2.getValue());
            }
        });
        StringBuilder sb = new StringBuilder();
        // The output frequency
        for (int i = 0; i < infoIds.size() && i < n; i++) {
            sb.append("<" + infoIds.get(i).getKey() + ">: " + infoIds.get(i).getValue() + "\r\n");
        }
        return sb.toString();
    }


}
