import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main{
    public static int index = 0;
    public static boolean isGroup = false;
    public static int countLines = 0, countNums = 0, countAllChars = 0, countChars = 0, countWords = 0, w, m, n;
    public static String allText = new String();
    public static String path = "C:\\Users\\win\\IdeaProjects\\Crawler\\src\\";
    public static String inputPath, outputPath;
    public static LinkedList<String> links = new LinkedList<>();
    public static TreeMap<String, String> orders = new TreeMap<>();
    public static TreeMap<String, Integer> records = new TreeMap<>();
    public static TreeMap<String, Integer> records2 = new TreeMap<>();
    public static LinkedList<String> wordList = new LinkedList<>();
    public static LinkedList<String> hotWordList = new LinkedList<>();
    public static LinkedList<String> wordGroupList = new LinkedList<>();
    public static LinkedList<String> hotWordGroupList = new LinkedList<>();

    //线程爬取CVPR2018官网
    public static void crawler(){
        try{
            String url = "http://openaccess.thecvf.com/CVPR2018.py";
            crawlerLink(url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!links.isEmpty()){
                        String link = links.getFirst();
                        links.removeFirst();
                        crawlerContent(link);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //爬取CVPR2018官网所有文章链接
    public static void crawlerLink(String url){
        try{
            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("content");
            Elements ptitles = content.getElementsByClass("ptitle");
            for(Element item : ptitles){
                Element link = item.selectFirst("a");
                String href = "http://openaccess.thecvf.com/"+link.attr("href");
                links.add(href);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //获取CVPR2018所有文章的标题和摘要
    public static void crawlerContent(String url){
        try{
            StringBuilder results = new StringBuilder();
            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("content");
            results.append(index+"\r\n");
            String title = content.getElementById("papertitle").text();
            results.append("title: "+title+"\r\n");
            String abstractText = content.getElementById("abstract").text();
            results.append("abstract: "+abstractText+"\r\n"+"\r\n"+"\r\n");
            saveArticle(results);
            index++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //将CVPR2018官网爬取的内容保存到result文本
    public static void saveArticle(StringBuilder content)throws Exception{
        File file = new File(path+"result.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content.toString());
        bw.flush();
        bw.close();
    }
    //命令行输入的参数赋值
    public static void terminal(){
        inputPath = path + orders.get("-i");
        outputPath = path + orders.get("-o");
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
        if(!file.exists()){
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        int val;
        while((val=br.read())!=-1){
            if((0<=val && val<=9)){
                countAllChars++;
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
        if(!file.exists()){
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line,title,abstractText;
        while((line=br.readLine())!=null){
            if(line.matches(regex)){
                countNums += line.length() + 1;
            } else if(line.startsWith("title: ")){
                countLines++;
                countNums += 7;
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
            }else if(line.startsWith("abstract: ")){
                countLines++;
                countNums += 10;
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
        StringBuilder sb = new StringBuilder();
        countChars = countAllChars - countNums;
        sb.append("characters:"+countChars+"\r\n");
        sb.append("words:"+countWords+"\r\n");
        sb.append("lines:"+countLines+"\r\n");
        if(isGroup){
            for(String str : hotWordGroupList){
                sb.append(str+"\r\n");
            }
        }else {
            for(String str : hotWordList){
                sb.append(str+"\r\n");
            }
        }
        bw.write(sb.toString());
        bw.close();
    }
    //统计单词
    public static void countWords(String content, int weight){
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
    public static void countWordGroups(String content, int weight){
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
        allText = sbf.toString();
        Pattern expression = Pattern.compile("[a-z]{4,}[a-z0-9]*");
        String str = content;
        Matcher matcher = expression.matcher(str);
        String word;
        ArrayList<String> group = new ArrayList<>();
        while (matcher.find()) {
            word = matcher.group();
            group.add(word);//提取单词
        }
        int len = group.size();
        for (int i = 0; i <= len-m; i++) {
            String pr = "";
            String pr2 = "";
            for (int j = i; j < i+m; j++) {//将m个单词构成字符串
                pr += group.get(j);
                pr2 += group.get(j);
                if(j < (i+m)-1){
                    pr2 +=" ";
                }
            }
            if(allText.indexOf(pr)!=-1){//在allText中匹配子字符串
                if (records2.containsKey(pr2)) {
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
    public static void main(String[] args){
        try{
//            crawler();
            if(args.length == 0){
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < args.length; i++) {
                if(args[i].equals("-i")){
                    orders.put("-i",args[i+1]);
                    i++;
                }else if(args[i].equals("-o")){
                    orders.put("-o",args[i+1]);
                    i++;
                }else if(args[i].equals("-w")){
                    orders.put("-w",args[i+1]);
                    i++;
                }else if(args[i].equals("-m")){
                    orders.put("-m",args[i+1]);
                    i++;
                }else if(args[i].equals("-n")){
                    orders.put("-n",args[i+1]);
                    i++;
                }
            }
            terminal();
            readFile();
            dealFile();
            writeFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
