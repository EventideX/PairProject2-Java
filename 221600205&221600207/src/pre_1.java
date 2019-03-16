package pregilt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pre_1 {
	
	 private static String httpRequest(String requestUrl,int num)
	    {
	    	
	    	StringBuffer buffer = null; 
	    	BufferedReader bufferedReader = null; 
	    	InputStreamReader inputStreamReader = null;
	    	InputStream inputStream = null;
	    	HttpURLConnection httpUrlConn = null;  
	    	
	    	try {
	    		
	    		URL url = new URL(requestUrl);
	    		httpUrlConn = (HttpURLConnection) url.openConnection();
	    		httpUrlConn.setDoInput(true);
	    		httpUrlConn.setRequestMethod("GET");
	    		
	    		
	    		inputStream = httpUrlConn.getInputStream();
	    		inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	    		bufferedReader = new BufferedReader(inputStreamReader);
	    		
	    		
	    		buffer = new StringBuffer();
	    		String str = null;

	    		while((str = bufferedReader.readLine())!= null) 
	    		{
	    			buffer.append(str);
	    		}
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}finally {
	    		
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
	    	pre_2 p2 = new pre_2();
	    	
	    	String str = null;
	    	Pattern p = Pattern.compile("(<div id=\"content\">)(.*)(</div>*)");    		
   		Matcher m = p.matcher(html);    		
   		if(m.find()) 
   		{
   			str = m.group(2);
   			String str1 = null;
   			String str2 = null;
   	    	Pattern p1 = Pattern.compile("(class=\"ptitle\">)(.+?)(a href=\")(content_cvpr_2018)(.+?)(_2018_paper.html\">)");    		
       		Matcher m1 = p1.matcher(str);
    
       		while(m1.find())
       		{
       	    	
       			str1 = m1.group(5);
       			str2 = ("http://openaccess.thecvf.com/content_cvpr_2018"+str1+"_2018_paper.html");
       			buffer.append(str2);
       			buffer.append("\n");
       			p2.getTodayTemperatureInfo(str2,num); 
       			num++;
       		}
   		}	 				
	    	return buffer.toString();
	    }
	    
	    public static String getTodayTemperatureInfo(String html) 
	    {
	    	int num = 0;
	    	String html1 = httpRequest(html,num);  
	    	String result = htmlFiter(html1,num);
	    	return result; 
	    }  

}
