import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<6) {
			System.out.println("参数个数不能少于6个");
			System.exit(-1);
		}
		Option option=new Option();
		option.getOption(args);
		WordsHandler wordsHandler=new WordsHandler();
		wordsHandler.readFile(Option.inputFile);
		wordsHandler.writeFile(Option.outputFile);
	}
}

class Option{
	static String inputFile="",outputFile="";
	static boolean isWeight=false;
	static int m=1,n=10;
	
	public void getOption(String[] args) {
		if(args.length%2!=0) {
			System.out.println("参数个数必须为偶数");
			System.exit(-1);
		}
		for(int i=0;i<args.length;i+=2) {
			if(args[i].charAt(1)=='i') {
				inputFile=args[i+1];
			}else if(args[i].charAt(1)=='o') {
				outputFile=args[i+1];
			}else if(args[i].charAt(1)=='w') {
				isWeight=args[i+1].charAt(0)=='1'?true:false;
			}else if(args[i].charAt(1)=='m') {
				m=args[i+1].charAt(0)-'0';
			}else if(args[i].charAt(1)=='n') {
				n=args[i+1].charAt(0)-'0';
			}
		}
	}
	
	
}

class WordsHandler{
	int wordCnt=0;//单词总数
	int charCnt=0;//字符数
	int lineCnt=0;//有效行数

	Map<String, Integer> wordCntMap=new HashMap<String,Integer>();//每个单词的数量
	
	public WordsHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public void readFile(String filename) {
		String string;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			while ((string=reader.readLine())!=null) {
				if(standard(string).equals(""))
					continue;
				if(string.matches("[0-9]*"))
					continue;
				lineCnt++;
				String newLine="";
				if(string.contains("Title: ")) {
					newLine=string.substring(7)+"\n";
				}
				if(string.contains("Abstract: ")) {
					newLine=string.substring(10);
				}
				charCnt(newLine);
				wordCnt(string);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//每行去开头和结尾的所有空白字符
	public String standard(String line) {
		String newLine=line.trim();
		return newLine;
	}
	
	//字符数统计
	public void charCnt(String line) {
		for(int i=0;i<line.length();++i) {
			if(line.charAt(i)<128)
				charCnt++;
		}	
	}
	
	//将单词转为小写
	public void tolowerCase(String arr[]) {
		for(int i=0;i<arr.length;++i)
			arr[i]=arr[i].toLowerCase();
	}
	
	//单词（词组）数统计
	public void wordCnt(String line) {
		if(Option.m==1) {//单词统计
			if(Option.isWeight) {//启用权值
				if(line.contains("Title: ")) {
					line=line.substring(7);
					String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
					tolowerCase(arr);
					for(int i=0;i<arr.length;++i) {
						//System.out.println(arr[i]);
						if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
							wordCnt++;
							if(!wordCntMap.containsKey(arr[i])) {
								wordCntMap.put(arr[i], 10);
							}else {
								wordCntMap.put(arr[i], wordCntMap.get(arr[i])+10);
							}
							
						}
					}
				}
				if(line.contains("Abstract: ")) {
					line=line.substring(10);
					String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
					tolowerCase(arr);
					for(int i=0;i<arr.length;++i) {
						//System.out.println(arr[i]);
						if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
							wordCnt++;
							if(!wordCntMap.containsKey(arr[i])) {
								wordCntMap.put(arr[i], 1);
							}else {
								wordCntMap.put(arr[i], wordCntMap.get(arr[i])+1);
							}
							
						}
					}
				}
			}else {//不启用权值
				if(line.contains("Title: ")) {
					line=line.substring(7)+"\n";
				}
				if(line.contains("Abstract: ")) {
					line=line.substring(10);
				}
				
				String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
				tolowerCase(arr);
				for(int i=0;i<arr.length;++i) {
					//System.out.println(arr[i]);
					if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
						wordCnt++;
						if(!wordCntMap.containsKey(arr[i])) {
							wordCntMap.put(arr[i], 1);
						}else {
							wordCntMap.put(arr[i], wordCntMap.get(arr[i])+1);
						}
						
					}
				}
			}
		}else {//词组统计
			if(Option.isWeight) {//启用权值
				if(line.contains("Title: ")) {
					line=line.substring(7);
					String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
					String arr2[]=new String[arr.length];//词组
					tolowerCase(arr);
					for(int i=0;i<arr.length;++i) {
						//System.out.println(arr[i]);
						if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
							arr2[wordCnt++]=arr[i];
						}
					}
				}
				if(line.contains("Abstract: ")) {
					line=line.substring(10);
					String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
					String arr2[]=new String[arr.length];//词组
					tolowerCase(arr);
					for(int i=0;i<arr.length;++i) {
						//System.out.println(arr[i]);
						if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
							arr2[wordCnt++]=arr[i];
						}
					}
				}
			}else {//不启用权值
				if(line.contains("Title: ")) {
					line=line.substring(7)+"\n";
				}
				if(line.contains("Abstract: ")) {
					line=line.substring(10);
				}	
				String arr[]=line.split("[^a-zA-Z0-9]"); //分割出潜在的单词
				String arr2[]=new String[arr.length];//词组
				tolowerCase(arr);
				for(int i=0;i<arr.length;++i) {
					//System.out.println(arr[i]);
					if(arr[i].matches("[a-z]{4}[a-z0-9]*")) {//判断是否是单词
						arr2[wordCnt++]=arr[i];
					}
				}
			}
		}
		
	}
	
	//输出信息
	public void writeFile(String filename) {
		//单词排序
		ArrayList<Map.Entry<String, Integer>> list=new ArrayList<Map.Entry<String,Integer>>(wordCntMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
					@Override
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						// TODO Auto-generated method stub
						if(o1.getValue()>o2.getValue())
							return -1;
						if(o1.getValue()==o2.getValue()) {
							return o1.getKey().compareTo(o2.getKey());
						}
						return 1;
					}
		});
		try {
			FileWriter writer=new FileWriter(filename);
			String string="";
			string+="characters: "+charCnt+"\r\n"+"words: "+wordCnt+"\r\n"+"lines: "+lineCnt+"\r\n";
			int n=0;
			//输出top n单词
			for(Map.Entry<String, Integer> item:list) {
				n++;
				if(n>Option.n)
					break;
				string+="<"+item.getKey()+">: "+item.getValue()+"\r\n";	
			}
			writer.write(string);
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
