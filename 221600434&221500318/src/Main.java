import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("deprecation")
public class Main {
	public static void main(String[] args) throws Exception {		
			CommandLineParser parser = new GnuParser();
			Options options = new Options();
			options.addOption("i",true,"读入文件名");
			options.addOption("o",true,"输出文件名");
			options.addOption("w",true,"单词权重");
			options.addOption("m",true,"词组词频统计");
			options.addOption("n",true,"频率最高的n行单词或词组");
			CommandLine commandLine = parser.parse(options, args);
			
			if (commandLine.hasOption("i") && commandLine.hasOption("o") && commandLine.hasOption("w")) {
				String infilename = commandLine.getOptionValue("i");
				String outfilename = commandLine.getOptionValue("o");
				String w = commandLine.getOptionValue("w");
				if (commandLine.hasOption("n")) {
					String n = commandLine.getOptionValue("n");
					if (isNumeric(n)) {						
						if (w.equals("1")) {
							writeResult(infilename,outfilename,true,Integer.valueOf(n));
						}
						else  {
							writeResult(infilename,outfilename,false,Integer.valueOf(n));
						}
					}
					else {
						System.out.println("-n [0<=number<=100]");
					}
				}
				else {
					if (w.equals("1")) {
						writeResult(infilename,outfilename,true,10);
					}
					else  {
						writeResult(infilename,outfilename,false,10);
					}
				}
			}
			else {
				System.out.print("必须有-i -o -w选项和参数");
			}
	}
/*	
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
	ArrayList group = new ArrayList<>();
	while (matcher.find()) { //提取单词
	word = matcher.group();
	group.add(word);
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
*/	
	public static boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		return pattern.matcher(str).matches(); 
	}
	
	public static void initTxt(String string) throws IOException {
        String path = new File(string).getAbsolutePath();
        FileWriter fw = new FileWriter(path, false);
        fw.write("");
        fw.flush();
        fw.close();
	}
	  public static void writeResult(String infilename,String outfilename,boolean w,int n) throws IOException {
	        File file = new File(infilename);
	        if (file.exists()) {	
	        	initTxt(outfilename);
	        	String outpath = new File(outfilename).getAbsolutePath();
	        	FileWriter fw = new FileWriter(outpath, true);
	        	int charactersNum = charactersNum(infilename);
	        	int wordsNum = wordsNum(infilename,w);
	        	int linesNum = linesNum(infilename);
	        	System.out.print("characters: " + charactersNum + '\n');
	        	System.out.print("words: " + wordsNum + '\n');
	        	System.out.print("lines: " + linesNum + '\n');
	        	fw.write("characters: " + charactersNum + '\n');
	        	fw.write("words: " + wordsNum + '\n');
	        	fw.write("lines: " + linesNum + '\n');
	        	fw.flush();
	        	writeMostWords(infilename,outfilename,w,n);
	        	if (fw != null) {
	        		fw.close();
	        	}
	        }
			else {
				System.out.println(infilename + "文件不存在!");
			}
	    }
	public static void writeMostWords(String infilename,String outfilename,boolean w,int n) throws IOException {
		String outpath = new File(outfilename).getAbsolutePath();
        FileWriter fw = new FileWriter(outpath, true);
        TreeMap<String, Integer> tm = wordMap(infilename,w);
		if(tm != null && tm.size()>=1)
		{		  
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(tm.entrySet());
			// 通过比较器来实现排序
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					//treemap默认按照键的字典序升序排列的，所以list也是排过序的，在值相同的情况下不用再给键升序排列		
					// 按照值降序排序 
					return o2.getValue().compareTo(o1.getValue());
				}
			});   
			int i = 1;
			String key = null;
			Integer value = null;
			for (Map.Entry<String, Integer> mapping : list) {
				if (n == 0) {
					break;				
				}
				key = mapping.getKey();
				value = mapping.getValue();
				System.out.print("<" + key + ">: " + value + '\n');
				fw.write("<" + key + ">: " + value + '\n');
				//只输出前n个 
				if (i == n) {
					break;
				}
				i++;
			}
		}
		fw.close();
	}
		
	public static int charactersNum(String filename) throws IOException  {		
		int num = 0;			
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String regex = "[0-9]*"; // 匹配数字编号行
		String separator = "Title: |Abstract: ";//过滤Title: 和 Abstract:
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		String line = null;
		char[] charArray = null;
		int value = -1;
		while ((line  = br.readLine()) != null) {
			num++;//readLine()漏掉的换行符也统计在内
			line = line.replaceAll(separator, ""); // 过滤Title: 和 Abstract:
			m = p.matcher(line);
			if (line.trim().length() != 0 && !m.matches()) {
				charArray = line.toCharArray();
				for (int i = 0;i < line.length();i++) {
					value = (int)charArray[i];
					if (value > 0 && value < 128 && value != 13) {
						num ++;
					}			
				}				
			}
		}
		br.close();		
		return num;	 	
	}
	
	public static int wordsNum(String filename,boolean w) throws IOException  {
		int num = 0;			
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String separator = "[^A-Za-z0-9]";//分隔符
		String regex = "^[A-Za-z]{4,}[0-9]*$"; // 正则判断每个数组中是否存在有效单词
		String titleRegex = "Title: .*";
		String abstractRegex = "Abstract: .*";
		Pattern p = Pattern.compile(regex);
		Pattern tp = Pattern.compile(titleRegex);
		Pattern ap = Pattern.compile(abstractRegex);
		Matcher m = null;
		Matcher titleMacher = null;
		Matcher abstractMacher = null;
		String line = null;
		String[] array = null;
		boolean intitle = false;
		while ((line  = br.readLine()) != null) {
			titleMacher = tp.matcher(line);
			abstractMacher = ap.matcher(line);
			if (titleMacher.matches()) {
				line = deleteSubString(line,"Title: ");
				intitle = true;
			}
			if (abstractMacher.matches()) {			
				line = deleteSubString(line,"Abstract: ");
			}
            line = line.replaceAll("[(\\u4e00-\\u9fa5)]", "");// 过滤汉字
            line = line.replaceAll(separator, " "); // 用空格替换分隔符
            array = line.split("\\s+"); // 按空格分割                     
            for (int i = 0;i<array.length;i++) { 
            	m = p.matcher(array[i]);
            	if (m.matches()) {
            		num = (w && intitle)?(num+10):(num+1);
            	}
            }
            intitle = false;
		}						
		br.close();		
		return num;
	}	
	
	public static TreeMap<String, Integer> wordMap(String filename,boolean w) throws IOException {		
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();				
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String separator = "[^A-Za-z0-9]";//分隔符
		String regex = "^[A-Za-z]{4,}[0-9]*$"; // 正则判断每个数组中是否存在有效单词
		String titleRegex = "Title: .*";
		String abstractRegex = "Abstract: .*";
		Pattern p = Pattern.compile(regex);
		Pattern tp = Pattern.compile(titleRegex);
		Pattern ap = Pattern.compile(abstractRegex);
		Matcher m = null;
		Matcher titleMacher = null;
		Matcher abstractMacher = null;
		String str = null;
		String line = null;
		String[] array = null;
		boolean intitle = false;		
		while ((line  = br.readLine()) != null) {
			titleMacher = tp.matcher(line);
			abstractMacher = ap.matcher(line);
			if (titleMacher.matches()) {
				line = deleteSubString(line,"Title: ");
				intitle = true;
			}
			if (abstractMacher.matches()) {			
				line = deleteSubString(line,"Abstract: ");
			}
            line = line.replaceAll("[(\\u4e00-\\u9fa5)]", "");// 用空格替换汉字
            line = line.replaceAll(separator, " "); // 用空格替换分隔符
            array = line.split("\\s+"); // 按空格分割                     
            for (int i = 0;i<array.length;i++) { 
            	m = p.matcher(array[i]);
            	if (m.matches()) {
            		str = array[i].toLowerCase();                
                    if (!tm.containsKey(str)) {
                    	tm.put(str, w&&intitle?10:1);
                    } else {
                        int count = tm.get(str) + (w&&intitle?10:1);
                        tm.put(str, count);
                    }
            	}
            }
            intitle = false;
		}						
		br.close();		
		return tm;
	}
	
	
	public static String deleteSubString(String str1,String str2) {
		StringBuffer sb = new StringBuffer(str1);
		while (true) {
			int index = sb.indexOf(str2);
			if(index == -1) {
				break;
			}
			sb.delete(index, index+str2.length());		
		}		
		return sb.toString();
	}
	
 	public static int linesNum(String filename) throws IOException  {
		int num = 0;			
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String regex = "[0-9]*"; // 匹配数字编号行
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		String line = null;
		while ((line  = br.readLine()) != null) {
			m = p.matcher(line);
			if (line.trim().length() != 0 && !m.matches()) {
				num ++;					
			}
		}
		br.close();		
		return num;
	}	
}
