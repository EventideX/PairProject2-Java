import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCount {
	//System.out.println(System.getProperty("user.dir"));
	//String str = System.getProperty("user.dir");  //获取当前目录
	int charCount = 0;
	int lineCount = 0;
	int wordCount = 0;
	List<String> listt = new ArrayList<String>(); //title单词统计
	List<String> lista = new ArrayList<String>(); //abstract单词统计
	Map<String, Integer> words = new TreeMap<String, Integer>();
	static ArrayList<Map.Entry<String, Integer>> maplist=null;
	
	public void CountChar(String inpath)
	{		
		try
		{
			InputStreamReader r = new InputStreamReader(new FileInputStream(inpath));
			BufferedReader br = new BufferedReader(r);
			//int a=0;
			//int cnt=0;
			//int csnt=0;
			String str=null;
			Pattern psa = Pattern.compile("Title");
			Pattern psb = Pattern.compile("Abstract");
			
			//int lines=0;
			while((str=br.readLine()) != null)
			{			
				Matcher msa = psa.matcher(str);
				Matcher msb = psb.matcher(str);
				if(msa.find())	
				{
					int index=str.length()-1;
					//System.out.println(str.charAt(10));
					while(index >= 0)
					{
						char o = str.charAt(index);
						if(o>=0 || o <=127 )
						{
							charCount++;
						}
						index--;
					}
					charCount-=7; //减去“Title: ”
					charCount++;  //加上换行符
					//charCount += str.length()-7;
				}
				else if(msb.find()) 
				{
					//charCount+=str.length()-10;			
					int index=str.length()-1;
					while(index >= 0)
					{
						char o = str.charAt(index);
						if(o>=0 || o <=127 )
						{
							charCount++;
							//System.out.println(o);
						}
						index--;
					}
					charCount-=10; //减去“Abstract: ”
					charCount++;
				}
				
			}
			//if(a != 0) charCount--;
			//charCount = charCount - (csnt/2)*17;//有效行减去"\r\n", "Title: ", "Abstract: "
			//System.out.println(csnt);	
			r.close();
			System.out.println("characters: "+charCount);
		}
		catch(IOException e)
		{
			System.out.println("文件读取出错！");
		}
	}
	
	public void CountLine(String inpath)
	{
		try
		{
			InputStreamReader r = new InputStreamReader(new FileInputStream(inpath));
			BufferedReader br = new BufferedReader(r);
			String s= br.readLine();
			int cnt=0;
			while(s != null)
			{
				//charCount+=s.length();
				//System.out.println(s);
				if(cnt%5 != 0 && !s.trim().equals(""))
				{
					lineCount++;
				}
				cnt++;
				s=br.readLine();
			}	
			r.close();	
			
		}
		catch(IOException e)
		{
			System.out.println("文件读取出错！");
		}		
		
	}
	
	public void CountWord(int xx, int yy, int zz, String inpath, String outpath)
	{
		try
		{
			InputStreamReader r = new InputStreamReader(new FileInputStream(inpath));
			BufferedReader br = new BufferedReader(r);
			String s= br.readLine();
			while(s != null)
			{
				s=s.toLowerCase();			
				String wordstr[] = s.split("[^a-zA-Z0-9]");
				int x = wordstr.length;
				if(x>=yy)
				{
					for(int i=1; i<(x-yy+1); i++)
					{					
						if(wordstr[i].matches("[a-z]{4}[a-z0-9]*"))
						{
							++wordCount;
							//System.out.println(wordstr[0]);
							String phrase = "";
							if(wordstr[0].equals("title"))  
							{
								//System.out.println("Title--");
								int j=0;
								for(j=0; j<yy; j++)
								{
									if(!wordstr[i+j].matches("[a-z]{4}[a-z0-9]*")) break;
									else{
										phrase += (wordstr[i+j]+" ");
									}
								}
								phrase = phrase.substring(0, phrase.length()-1);
								if(j == yy) listt.add(phrase);								
								
							}
							else  
							{
								//System.out.println("Abstract--");
								int j=0;
								for(j=0; j<yy; j++)
								{
									if(!wordstr[i+j].matches("[a-z]{4}[a-z0-9]*")) break;
									else{
										phrase += (wordstr[i+j]+" ");
									}
								}
								phrase = phrase.substring(0, phrase.length()-1);
								if(j == yy) lista.add(phrase);								
							}
						}
					}				
					for(int k=(x-yy+1); k<x; k++)
					{
						if(wordstr[k].matches("[a-z]{4}[a-z0-9]*"))
						{	
							
							++wordCount;
						}
					}
								
				}
				else
				{
					for(int k=1; k<x; k++)
					{
						if(wordstr[k].matches("[a-z]{4}[a-z0-9]*"))
						{	
							++wordCount;
						}
					}					
				}
				
				s=br.readLine();
			}	
			r.close();		
			System.out.println("words: "+wordCount);
			System.out.println("lines: "+lineCount);
		}
		catch(IOException e)
		{
			System.out.println("文件读取出错！");
		}	
		
		
		
		if(xx == 0)
		{
			for(String li: listt)
			{
				if(words.get(li) != null)
				{
					words.put(li, words.get(li)+1);
				}
				else words.put(li, 1);
			}		
		}
		else
		{
			for(String li: listt)
			{
				if(words.get(li) != null)
				{
					words.put(li, words.get(li)+10);
				}
				else words.put(li, 10);
			}				
		}

		for(String li: lista)
		{
			if(words.get(li) != null)
			{
				words.put(li, words.get(li)+1);
			}
			else words.put(li, 1);
		}
		maplist = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
		Collections.sort(maplist, new Comparator<Map.Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o2.getValue() - o1.getValue(); //降序
			}
		});
		
		//打印前十结果
		for(int i=0; i<maplist.size(); i++)
		{
			if(i>=zz) break;
			System.out.println("<"+maplist.get(i).getKey()+">: "+maplist.get(i).getValue());
		}			
		//将词频统计输入result.txt文件中
		try
		{
			File file = new File(outpath);
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write("characters: "+charCount+"\r\n");
			br.write("words: "+wordCount+"\r\n");
			br.write("lines: "+lineCount+"\r\n");
			for(int i=0; i<maplist.size(); i++)
			{
				if(i>=10) break;
				br.write("<"+maplist.get(i).getKey()+">: "+maplist.get(i).getValue()+"\r\n");
			}	
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("文件读取出错！");
		}
		
			
	}
	
	//Scanner input = new Scanner(System.in);
	//System.out.print("请输入路径:");
	//String path = input.next();
	//path = "src/"+path;
	//System.out.println(path); //获取文件路径
	
	/*
	public void print()
	{
		System.out.println("characters: "+charCount);	
		System.out.println("words: "+wordCount);
		System.out.println("lines: "+lineCount);
	}
	*/
	
}
