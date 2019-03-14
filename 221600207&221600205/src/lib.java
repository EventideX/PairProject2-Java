import java.io.*;
import java.util.*;

public class lib {
	private int fWordCount = 0;//字词数
	private int fRowCount = 0;//行数
	private int fByteCount = 0;//字节数
	private int maxWordNum = 10;//词频输出数
	private String fileInput= "cvpr/result.txt";
	private String fileOutput= "src/output.txt";
	private boolean wValue = false;//-w 值
	private HashMap<String,Integer> map = new HashMap<String,Integer>();//存放字词处
	
	
	public lib()
	{
		fileInput = "cvpr/result.txt";
	}
	public lib(String fName)
	{
		fileInput = fName;
		setWord();
	}
	public void setFileInput(String fName)
	{
		fileInput = fName;
	}
	public void setFileOutput(String fName)
	{
		fileOutput = fName;
	}
	public void setWValue(int num)
	{
		wValue = num > 0 ? true : false;
	}
	public void setMaxWordNum(int num)
	{
		if(num >= 0)	maxWordNum = num;
	}
	public int getFWordCount()
	{
		return fWordCount;
	}
	public int getFRowCount()
	{
		return fRowCount;
	}
	public int getfByteCount()
	{
		return fByteCount;
	}
	public int getMaxWordNum()
	{
		return maxWordNum;
	}
	public int getMaxWordNum(int num)
	{
		return maxWordNum = num;
	}
	public boolean isLower(char c)
	{
		return (c>='a' && c<='z');
	}
	public boolean isDigit(char c)
	{
		return c<='0' && c<='9';
	}
	
	public LinkedHashMap<String,Integer> sortMap(int num)
	{
		List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		        return o2.getValue() != o1.getValue() ? (o2.getValue() - o1.getValue()) : (o1.getKey()).toString().compareTo(o2.getKey());
		        //return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});
		LinkedHashMap<String,Integer> tmp = new LinkedHashMap<String,Integer>();
		for (int i = 0; i < list.size() && i< num; i++) {
		    String id = list.get(i).toString();
		    Integer value = list.get(i).getValue();
		    tmp.put(id, value);
		    //System.out.println(id + (value));
		}
		return tmp;
	}
	public void getWord()
	{
		LinkedHashMap<String,Integer> list  = sortMap(maxWordNum);
		
		try {
			FileOutputStream fos = new FileOutputStream(fileOutput);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter buff = new BufferedWriter(osw);
			
			String content = "characters: " + fByteCount + "\r\n";
			content += "words: "+ getFWordCount() + "\r\n";
			content += "lines: "+ fRowCount + "\r\n";
			Iterator<String> iterator = list.keySet().iterator();
			while (iterator.hasNext()) {
			    String key = iterator.next();
			    content += "<" + key.replace("=", ">: ") + "\r\n";
			    //System.out.println(key.replace("=", ">: "));
			}
			//System.out.print(content);
			buff.write(content);
			buff.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void setMap(String fContent,boolean isTitle)
	{
		String [] ch = fContent.split("\\W+");
		for(int i = 0; i< ch.length ;i++)
		{
			if(ch[i].length()>=4)
			{
				ch[i] = ch[i].toLowerCase();
				if (isLower(ch[i].charAt(0)) && isLower(ch[i].charAt(1)) && isLower(ch[i].charAt(2)) && isLower(ch[i].charAt(3)) )
				{
					//System.out.print(ch[i]);
					//新增纪录或者记录数+1
					fWordCount ++;
					if( map.containsKey(ch[i]) )
						map.put(ch[i],(wValue & isTitle) ? map.get(ch[i])+10 : map.get(ch[i])+1);
					else 
						map.put(ch[i], (wValue & isTitle) ? 10 : 1);
				}
			}
		}
	}
	public void setWord()
	{
		try {
			String fContent = "";
			FileInputStream fis = new FileInputStream(fileInput);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			fWordCount = fByteCount = fRowCount = 0;
			while ((fContent = br.readLine()) != null) {
				if(fContent.length() > 3)//排除空行和编号
				{
					fRowCount ++;
					if(fContent.charAt(0) == 'T')
					{
						fContent = fContent.substring(6, fContent.length()-1 );//remove(Title：) 
						fByteCount += fContent.length();//无换行
						setMap(fContent,true);
					}
					else if(fContent.charAt(0) == 'A')
					{
						fContent = fContent.substring(9, fContent.length()-1 );//remove(Abstract: ) 
						fByteCount += fContent.length();//无换行
						setMap(fContent,false);
					}
				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.print("文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
