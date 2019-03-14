package pregilt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pre_2 {
	 private static String httpRequest(String requestUrl,int num)
	    {
	    	
	    	StringBuffer buffer = null; //将指定字符串添加到指定字符序列中
	    	BufferedReader bufferedReader = null; //缓冲区
	    	InputStreamReader inputStreamReader = null;
	    	InputStream inputStream = null;
	    	HttpURLConnection httpUrlConn = null;  //与网页链接
	    	
	    	try {
	    		//建立get请求
	    		URL url = new URL(requestUrl);
	    		httpUrlConn = (HttpURLConnection) url.openConnection();
	    		httpUrlConn.setDoInput(true);
	    		httpUrlConn.setRequestMethod("GET");
	    		
	    		//获取输入流
	    		inputStream = httpUrlConn.getInputStream();
	    		inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	    		bufferedReader = new BufferedReader(inputStreamReader);
	    		
	    		//从输入流中读取结果
	    		buffer = new StringBuffer();
	    		String str = null;
	    		//当缓冲区的字符不为空，把字符存到字符序列中
	    		while((str = bufferedReader.readLine())!= null) 
	    		{
	    			buffer.append(str);
	    		}//while结束
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}finally {
	    		//释放资源
	    		if(bufferedReader != null)
	    		{
	    			try {
	    				bufferedReader.close();
	    			}catch(IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		if(inputStreamReader != null)
	    			try {
	    				inputStreamReader.close();
	    			}catch(IOException e) {
	    				e.printStackTrace();
	    			}
	    		if(inputStream != null)
	    		{
	    			try {
	    				inputStream.close();
	    				
	    			}catch(IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		if(httpUrlConn != null){
	    			 httpUrlConn.disconnect();  
	    		}    		
	        } 
	        return buffer.toString();
	     }
	    
	    private static String  htmlFiter(String html,int num) 
	    {
	    	StringBuffer buffer = new StringBuffer();
	      
	    	String str1= "";
	    	String str2= "";	    	
	    		// 匹配Title(题目),题目被包含在<div id="papertitle"> 和 </div>中
	    		Pattern p1 = Pattern.compile("(<div id=\"papertitle\">)(.+?)(</div>*)");    		
	    		Matcher m1 = p1.matcher(html);  
	    		// 匹配Abstract(摘要)，摘要被包含在<div id="abstract"> 和 </div>中
	    		Pattern p2 = Pattern.compile("(\"abstract\")(.+?)(</div>*)");
	    		Matcher m2 = p2.matcher(html);
	    		if(m1.find() && m2.find()) 
	    		{
	    			
	    			str1 = m1.group(2);
	    			str1 = num+"\r\n"+"Title: "+str1+"\r\n";
	    	//		buffer.append("\nTitle: ");
	    //			buffer.append(str1);
	    			str2 = m2.group(2);
	    			str2 = str2.replace(">","");
	    			str2 = "Abstract:"+str2+"\r\n"+"\r\n";
	   // 			buffer.append("\nAbstract: ");
	    //			buffer.append(str2);
	    //			buffer.append("\n");
	    			
  			try
  			{
      			 File file = new File("D:\\result.txt");               			 
      			 FileWriter fw = new FileWriter(file,true);
      			 String str5 = str1;
      			 fw.write(str5);
      			 String str6 = str2 + System.getProperty("line.separator");
      			 fw.write(str6);
                   
                   fw.close();
  			}catch(Exception e) {
  				e.printStackTrace();
  			}
	    		}   	
	    	return buffer.toString();
	    }
	    
	    public static String getTodayTemperatureInfo(String html,int num) 
	    {
	    	String html1 = httpRequest(html,num);  
	    	String result = htmlFiter(html1,num);
	    	return result; 
	    	
	    } 

}
