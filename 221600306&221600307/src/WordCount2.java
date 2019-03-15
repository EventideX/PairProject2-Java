package wordcount;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class WordCount2 {
	public Map<String, Integer> titleWordMap = new TreeMap<String, Integer>();
	public Map<String, Integer> abstractWordMap = new TreeMap<String, Integer>();
	public Map<String, Integer> totalWordMap = new TreeMap<String, Integer>();

	public List<Map.Entry<String, Integer>> orderedWordList;//最终用于输出的按要求排好序的单词列表
	
	public List<String> titleList = new ArrayList<String>();
	public List<String> abstractList = new ArrayList<String>();
	
	
	public int numOfChar;
	public int numOfWord;
	public int numOfLine;
	
	public int countChar() {
		numOfChar=0;
		for (int i = 0; i < titleList.size(); i++) {
			numOfChar+=titleList.get(i).toCharArray().length;
		}
		for (int i = 0; i < abstractList.size(); i++) {
			numOfChar+=abstractList.get(i).toCharArray().length;
		}
		//System.out.println(numOfChar);
		return numOfChar;
	}
	
	public int countWord() {
		numOfWord=0;
		for (int i = 0; i < titleList.size(); i++) {
			int countFourLetter = 0;
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < titleList.get(i).length(); j++) {
				//四个英文字母开头
				if (Character.isLetter(titleList.get(i).charAt(j))) {
					countFourLetter++;		
					sb.append(titleList.get(i).charAt(j));
					if (countFourLetter>=4 && j == titleList.get(i).length()-1) {
						if (titleWordMap.get(sb.toString())!=null) {
							int temp = titleWordMap.get(sb.toString());
							temp++;
							titleWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							titleWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
				}else if (Character.isDigit(titleList.get(i).charAt(j))&&countFourLetter>=4) {
					sb.append(titleList.get(i).charAt(j));
					if (countFourLetter>=4 && j == titleList.get(i).length()-1) {
						if (titleWordMap.get(sb.toString())!=null) {
							int temp = titleWordMap.get(sb.toString());
							temp++;
							titleWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							titleWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
					//System.out.println("the digit:"+sb.toString());			
				}else {
					if (countFourLetter>=4) {
						if (titleWordMap.get(sb.toString())!=null) {
							int temp = titleWordMap.get(sb.toString());
							temp++;
							titleWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							titleWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
					countFourLetter=0;
					sb.delete(0, sb.length());
				}
			}
		}
		for (int i = 0; i < abstractList.size(); i++) {
			int countFourLetter = 0;
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < abstractList.get(i).length(); j++) {
				//四个英文字母开头
				if (Character.isLetter(abstractList.get(i).charAt(j))) {
					countFourLetter++;		
					sb.append(abstractList.get(i).charAt(j));
					if (countFourLetter>=4 && j == abstractList.get(i).length()-1) {
						if (abstractWordMap.get(sb.toString())!=null) {
							int temp = abstractWordMap.get(sb.toString());
							temp++;
							abstractWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							abstractWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
				}else if (Character.isDigit(abstractList.get(i).charAt(j))&&countFourLetter>=4) {
					sb.append(abstractList.get(i).charAt(j));
					if (countFourLetter>=4 && j == abstractList.get(i).length()-1) {
						if (abstractWordMap.get(sb.toString())!=null) {
							int temp = abstractWordMap.get(sb.toString());
							temp++;
							abstractWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							abstractWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
					//System.out.println("the digit:"+sb.toString());			
				}else {
					if (countFourLetter>=4) {
						if (abstractWordMap.get(sb.toString())!=null) {
							int temp = abstractWordMap.get(sb.toString());
							temp++;
							abstractWordMap.put(sb.toString().toLowerCase(), temp);
						}else {
							abstractWordMap.put(sb.toString().toLowerCase(), 1);
						}
						numOfWord++;
					}
					countFourLetter=0;
					sb.delete(0, sb.length());
				}
			}
		}
//		System.out.println(titleWordMap);
//		System.out.println(abstractWordMap);
		return numOfWord;
	}
	
	public int countLine() {
		numOfLine = 0;
		numOfLine=titleList.size()+abstractList.size();
		return numOfLine;
	}
	
	public void orderWord(int weightOfTitle, int weightOfAbstract) {
		//title和abstract中的单词的并集
		Set<String> titleSet = titleWordMap.keySet();
		Set<String> abstractSet = abstractWordMap.keySet();
		Set<String> totalSet = new HashSet<String>();
		totalSet.addAll(titleSet);
		totalSet.addAll(abstractSet);
		
		//带上权重重新计算词频
		Iterator<String> setIterator = totalSet.iterator();
		while(setIterator.hasNext()) {
			String key = setIterator.next();
			int titleValue = titleWordMap.get(key)!=null ? titleWordMap.get(key):0;
			int abstractValue = abstractWordMap.get(key)!=null ? abstractWordMap.get(key):0;
			totalWordMap.put(key,titleValue*weightOfTitle+abstractValue*weightOfAbstract);
		}
		
		//按照词频排个序
		//通过ArrayList构造函数把map.entrySet()转换成list
        orderedWordList = new ArrayList<Map.Entry<String, Integer>>(totalWordMap.entrySet());
        //通过比较器实现比较排序
        Collections.sort(orderedWordList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> mapping1, Map.Entry<String, Integer> mapping2) {
                return mapping2.getValue().compareTo(mapping1.getValue());
            }
        });
	}
	
	public WordCount2(String filePath) {
		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
			String string;
			
			while((string=bufferedReader.readLine()) != null) {
				if (string.matches("^\\d+")) {//论文编号
					char[] cbuf = new char[10];
					bufferedReader.read(cbuf,0,7);
					
					//title里的单词
					String titleString = bufferedReader.readLine();
					titleList.add(titleString);
					//System.out.println(titleString);
					
					//abstract里的单词
					bufferedReader.read(cbuf,0,10);
					String abstractString = bufferedReader.readLine();
					abstractList.add(abstractString);
					//System.out.println(abstractString);
					
					//空格两行不计
					bufferedReader.readLine();
					bufferedReader.readLine();
					
				}
			}
			dataInputStream.close();
			bufferedReader.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String filePath = "input.txt";
		WordCount2 wordCount2 = new WordCount2(filePath);
		System.out.println("characters: "+wordCount2.countChar());
		System.out.println("words: "+wordCount2.countWord());
		System.out.println("lines: "+wordCount2.countLine());

		wordCount2.orderWord(10,1);
		int topN = 10;
		
		Iterator<Entry<String, Integer>> iterator = wordCount2.orderedWordList.iterator();
		for (int i=0; i< topN &&iterator.hasNext();i++) {
		    Entry<String, Integer> Entry = iterator.next();
		    System.out.println("<"+Entry.getKey()+">: "+ Entry.getValue());
		}

	}
	
}
