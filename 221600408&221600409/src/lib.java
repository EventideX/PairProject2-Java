import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lib {
    public static boolean isGroup = false;
    public static int countLines = 0, countNums = 0, countAllChars = 0, countChars = 0, countWords = 0, w, m, n;
    public static String allText = new String();
    public static String inputPath, outputPath;
    public static TreeMap<String, String> orders = new TreeMap<>();
    public static TreeMap<String, Integer> records = new TreeMap<>();
    public static TreeMap<String, Integer> records2 = new TreeMap<>();
    public static LinkedList<String> wordList = new LinkedList<>();
    public static LinkedList<String> hotWordList = new LinkedList<>();
    public static LinkedList<String> wordGroupList = new LinkedList<>();
    public static LinkedList<String> hotWordGroupList = new LinkedList<>();

    //命令行输入的参数赋值
    public static void terminal(){
        inputPath = orders.get("-i");
        outputPath = orders.get("-o");
        w = Integer.valueOf(orders.get("-w"));
        if(orders.containsKey("-m")){
            m = Integer.valueOf(orders.get("-m"));
            isGroup = true;
        }
        if(orders.containsKey("-n")){
            n = Integer.valueOf(orders.get("-n"));
        }else{
            n = 10;
        }
    }
    //读取输入文件所有字符内容
    public static void readFile()throws Exception{
        File file = new File(inputPath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int val;
        while((val=br.read())!=-1){//统计所有字符数
            if((0<=val && val<=9)){
                countAllChars++;
            }else if(val == 10){
                continue;
            }else if(11<=val && val<=127){
                countAllChars++;
            }
        }
        br.close();
    }
    //获取文章标题，摘要字符内容及有效行数
    public static void dealFile()throws Exception{
        String regex = "\\d+";
        File file = new File(inputPath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line,title,abstractText;
        while((line=br.readLine())!=null){
            if(line.matches(regex)){
                countNums += line.length() + 1;//统计论文编号所在行字符数
            } else if(line.startsWith("Title: ")){
                countLines++;//统计有效行数
                countNums += 7;//统计title: 字符数
                title = line.substring(6).toLowerCase();
                if(w == 0){
                    countWords(title,1);
                }else if(w == 1){
                    countWords(title,10);
                }
                if(isGroup){
                    if(w == 0){
                        countWordGroups(title,1);
                    }else if(w == 1){
                        countWordGroups(title,10);
                    }
                }
            }else if(line.startsWith("Abstract: ")){
                countLines++;//统计有效行数
                countNums += 10;//统计abstract: 字符数
                abstractText = line.substring(9).toLowerCase();
                countWords(abstractText,1);
                if(isGroup){
                    countWordGroups(abstractText,1);
                }
            }
        }
        br.close();
        countHotWords();
        if(isGroup){
            countHotWordGroups();
        }
    }
    //将统计结果输入到output文件
    public static void writeFile()throws Exception{
        File file = new File(outputPath);
        if(!file.exists()){
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        StringBuilder sbf = new StringBuilder();
        countChars = countAllChars - countNums;
        sbf.append("characters:"+countChars+"\r\n");
        sbf.append("words:"+countWords+"\r\n");
        sbf.append("lines:"+countLines+"\r\n");
        if(isGroup){
            for(String str : hotWordGroupList){
                sbf.append(str+"\r\n");
            }
        }else {
            for(String str : hotWordList){
                sbf.append(str+"\r\n");
            }
        }
        bw.write(sbf.toString());
        bw.close();
    }
    //统计单词
    public static void countWords(String content, int weight){//weight权重的词频统计
        Pattern expression = Pattern.compile("[a-z]{4,}[a-z0-9]*");//匹配单词
        String str = content;
        Matcher matcher = expression.matcher(str);
        String word;
        while (matcher.find()) {
            word = matcher.group();
            countWords++;
            if (records.containsKey(word)) {
                records.put(word, records.get(word) + weight);
            } else {
                records.put(word, weight);
            }
        }
    }
    //统计单词词组
    public static void countWordGroups(String content, int weight){//词组权重词频统计
        StringBuffer sbf = new StringBuffer();
        int size = content.length();
        char []arr = content.toCharArray();
        for (int i = 0; i < size; i++) {//去除分隔符整合所有字符
            char val = arr[i];
            if(48<=val && val<=57){
                sbf.append(val);
            }else if(97<=val && val<=122){
                sbf.append(val);
            }
        }
        allText = sbf.toString();//获取所有字符(去分隔符后)
        Pattern expression = Pattern.compile("[a-z]{4,}[a-z0-9]*");
        String str = content;
        Matcher matcher = expression.matcher(str);
        String word;
        ArrayList<String> group = new ArrayList<>();
        while (matcher.find()) {//提取单词
            word = matcher.group();
            group.add(word);//单词归组
        }
        int len = group.size();
        for (int i = 0; i <= len-m; i++) {
            String pr = "";
            String pr2 = "";
            for (int j = i; j < i+m; j++) {//将m个单词构成字符串pr,pr2为记录单词构成的词组
                pr += group.get(j);
                pr2 += group.get(j);
                if(j < (i+m)-1){
                    pr2 +=" ";
                }
            }
            if(allText.indexOf(pr)!=-1){//在allText中匹配子字符串pr
                if (records2.containsKey(pr2)) {//词组归组
                    records2.put(pr2, records2.get(pr2) + weight);
                } else {
                    records2.put(pr2, weight);
                }
            }
        }
    }
    //统计热度单词
    public static void countHotWords(){
        sortWords();
        String str;
        int length = wordList.size();
        if(length > n){
            for(int i = 0; i < n; i++){
                str = wordList.get(i);
                hotWordList.add(str);
            }
        }
        else{
            hotWordList.addAll(wordList);
        }
    }
    //统计热度词组
    public static void countHotWordGroups(){
        sortWordGroups();
        String str;
        int length = wordGroupList.size();
        if(length > n){
            for(int i = 0; i < n; i++){
                str = wordGroupList.get(i);
                hotWordGroupList.add(str);
            }
        }
        else{
            hotWordGroupList.addAll(wordGroupList);
        }
    }
    //单词排序
    public static void sortWords(){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(records.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        String str;
        for (Map.Entry<String, Integer> e: list) {
            str = e.getKey()+":"+e.getValue();
            wordList.add(str);
        }
    }
    //词组排序
    public static void sortWordGroups(){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(records2.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        String str;
        for (Map.Entry<String, Integer> e: list) {
            str = "<"+e.getKey()+">:"+e.getValue();
            wordGroupList.add(str);
        }
    }
}
