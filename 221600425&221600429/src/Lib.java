

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lib {
	public static void solveInput(String fileName) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(new File(fileName)));
		FileWriter fw=new FileWriter(new File("inputTemp.txt"));
		
		String lineText=null;
		while((lineText=br.readLine())!=null) {
			if(lineText.contains("Title: ")) {
				lineText=lineText.replaceAll("Title: ", "");
				lineText+="\r\n";
				//System.out.println(lineText);
			}
			else if(lineText.contains("Abstract: ")) {
				lineText=lineText.replaceAll("Abstract: ", "");
				lineText+="\r\n";
				//System.out.println(lineText);
			}
			else {
				continue;
			}
			fw.write(lineText);
			fw.flush();
		}
		br.close();
		fw.close();
	}

	public static int charNum(String fileName) throws IOException {
		File srcFile=new File(fileName);
		int count=0;
		try {
			FileReader fileReader=new FileReader(srcFile);
			int len=-1;
			char[] buffer=new char[1024];
			while((len=fileReader.read(buffer))!=-1) {
				for(int i=0;i<len;i++) {
					if((int)buffer[i]>=0&&(int)buffer[i]<=127) {
						if((i<len-1)&&(int)buffer[i]=='\r'&&(int)buffer[i+1]=='\n') {
							count--;
						}
							
						count++;
					}
				}
			}
			fileReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count-1;
	}
	public static int lineNum(String fileName) {
		int count=0;
		File srcFile=new File(fileName);
		try {
			FileReader fileReader=new FileReader(srcFile);
			BufferedReader bufferedReader=new BufferedReader(fileReader);
			String lineText=null;
			while((lineText=bufferedReader.readLine())!=null) {
				if(!(lineText==null||lineText.trim().length()==0)) {
					count++;
				}
			}
			fileReader.close();
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  count;
	}
	public static boolean isLetter(String letter) {
		String regex="[a-zA-Z]{4}[a-zA-Z0-9]*";
		return letter.matches(regex);
	}
	public static int letterNum(String fileName) {
		int count=0;
        Map<String,Integer> map = new HashMap();
        String regex="[^a-zA-Z0-9]";
		File srcFile=new File(fileName);
		try {
			FileReader fileReader=new FileReader(srcFile);
			BufferedReader bufferedReader=new BufferedReader(fileReader);
			String lineText=null;
			while((lineText=bufferedReader.readLine())!=null) {
				 String[] strs=lineText.split(regex);
				 for(int i=0;i<strs.length;i++) {
					 if(isLetter(strs[i])) {
						 count++;
					 }
				 }
			}
			fileReader.close();
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public static void getWordFrequency(String fileName,List<String> list,List<Integer> list1,int Weightflag,int numFlag) {
		int value=0;
		if(Weightflag==0) {
			value=1;
		}
		else {
			value=10;
		}
		Map<String,Integer> map = new HashMap();
		String regex="[^a-zA-Z0-9]+";
		File srcFile=new File(fileName);
		try {	
			FileReader fileReader=new FileReader(srcFile);
			BufferedReader bufferedReader=new BufferedReader(fileReader);
			String lineText=null;
			while((lineText=bufferedReader.readLine())!=null) {
				if(lineText.contains("Title: ")) {
					String temp=lineText.replaceFirst("Title: ", "");
					//System.out.println(temp);
					String[] strs=temp.split(regex);
					for(int i=0;i<strs.length;i++) {
						 if(isLetter(strs[i])) {
							 strs[i]=strs[i].toLowerCase();
							 if(map.containsKey(strs[i])) {
								 map.put(strs[i], map.get(strs[i])+value);
							 }
							 else {
								 map.put(strs[i], value);
							 }
						 }
					 }
				}
				else if(lineText.contains("Abstract: ")){
					String temp=lineText.replaceFirst("Abstract: ", "");
					//System.out.println(temp);
					String[] strs=temp.split(regex);
					for(int i=0;i<strs.length;i++) {
						 if(isLetter(strs[i])) {
							 strs[i]=strs[i].toLowerCase();
							 if(map.containsKey(strs[i])) {
								 map.put(strs[i], map.get(strs[i])+1);
							 }
							 else {
								 map.put(strs[i], 1);
							 }
						 }
					 }
				}
				else {
					continue;
				}
			}
			String[] words=new String[map.size()];
			Integer[] counts=new Integer[map.size()];
		
			map.keySet().toArray(words);
			map.values().toArray(counts);
			
			for(int i=0;i<map.size();i++) {
				if(i<numFlag) {
					for(int j=i;j<map.size();j++) {
						if(counts[i]<counts[j]) {
							Integer temp=null;
							temp=counts[i];
							counts[i]=counts[j];
							counts[j]=temp;
							String temp1=null;
							temp1=words[i];
							words[i]=words[j];
							words[j]=temp1;
						}
						else if(counts[i]==counts[j]) {
							if(words[i].compareTo(words[j])<0) {
								Integer temp=null;
								temp=counts[i];
								counts[i]=counts[j];
								counts[j]=temp;
								String temp1=null;
								temp1=words[i];
								words[i]=words[j];
								words[j]=temp1;
							}
						}
					}
				}
			}
			for(int i=0;i<words.length;i++) {
				if(i>numFlag-1) {
					break;
				}
				else {
					list.add(words[i]);
				}
			}
			for(int i=0;i<counts.length;i++) {
				if(i>numFlag-1) {
					break;
				}
				else {
					list1.add(counts[i]);
				}
			}
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
