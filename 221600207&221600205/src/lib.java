import java.io.*;
import java.util.*;

public class lib {
	private int fWordCount = 0;//字词数
	private int fRowCount = 0;//行数
	private int fByteCount = 0;//字节数
	private int maxWordNum = 10;//词频输出数
	private String fileInput= "cvpr/result.txt";
	private String fileOutput= "output.txt";
	private boolean wValue = false;//-w 值
	private int mValue = 1;//-m 值
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
	public void setMValue(int num)
	{
		mValue = num;
	}
	public void setMaxWordNum(int num)
	{
		if(num >= 0)	maxWordNum = num;
	}
	public boolean getWValue()
	{
		return wValue;
	}
	public int getFWordCount()
	{
		return fWordCount;
	}
	public int getMValue()
	{
		return mValue;
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
	public String getFileInput()
	{
		return fileInput;
	}
	public String getFileOutput()
	{
		return fileOutput;
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
	public void setMapPro(String fContent,boolean isTitle)
	{
		fContent = fContent.toLowerCase();
		String [] ch = fContent.split("\\W+");
		int [] a = new int[ch.length+1];
		a[0] = a[1] = 0;//a[0]=0方便迭代 a[1]=0防止没有单词
		/*记录单词出现的下标，坏单词置-1*/
		for(int i = 0; i < ch.length ;i++) {
			a[i+1] = fContent.indexOf(ch[i], a[i]);
			if(ch[i].length() >= 4 && isLower(ch[i].charAt(0)) && isLower(ch[i].charAt(1)) && isLower(ch[i].charAt(2)) && isLower(ch[i].charAt(3)) ) {
				fWordCount ++;
				a[i] = -1;
			}
		}

		boolean isGoodMove = false;//是否 良好移动=begin++,end++
		for(int begin = 1,end = 1; end <= ch.length ;begin++,end++) {
			if( ! isGoodMove ) {	
				int j;
				for( j = 1;j <= mValue; j++,end++) {
					if(end > ch.length || a[end] == -1)	break;//遍历完或者遇到坏单词
				}
				if(j > mValue) { //中间没有遇到坏单词
					end = begin + mValue - 1;//找到尾坐标
					isGoodMove = true;//开始良好的移动 
				}
				else {//中间遇到坏单词
					begin = end ;//开始和结束下标转移至坏单词的下一个单词，保存坏移动不变
				}
			}
			else { //良好的移动
				if(a[end] == -1) { //遇到坏单词
					begin = end;//开始和结束下标转移至坏单词的下一个单词，修改为坏移动
					isGoodMove = false;//开始坏的移动 
				}
			}
			if( isGoodMove ) { //只有良好的移动才允许记录
				String sh = fContent.substring(a[begin], a[end]+ch[end-1].length()+1);
				//System.out.print(sh);
				//新增纪录或者记录数+1
				if( map.containsKey(sh) )
					map.put(sh,(wValue & isTitle) ? map.get(sh)+10 : map.get(sh)+1);
				else 
					map.put(sh, (wValue & isTitle) ? 10 : 1);
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
						fContent = fContent.substring(7, fContent.length()-1 );//remove(Title：) 
						fByteCount += fContent.length()+1;//换行+1
						if(mValue >= 1 )
							setMap(fContent,true); 
						else 
							setMapPro(fContent,true);
					}
					else if(fContent.charAt(0) == 'A')
					{
						fContent = fContent.substring(9, fContent.length()-1 );//remove(Abstract: ) 
						fByteCount += fContent.length()+1;//换行+1
						if(mValue >= 1 )
							setMap(fContent,true); 
						else 
							setMapPro(fContent,true);
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
