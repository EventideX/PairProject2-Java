
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.io.PrintStream; 
import java.io.File;
import java.io.FileWriter; 
import java.io.IOException;

public class paChong_1 
{

	 private static String httpRequest(String requestUrl)
	    {
	    	
	    	StringBuffer buffer = null; //��ָ���ַ�����ӵ�ָ���ַ�������
	    	BufferedReader bufferedReader = null; //������
	    	InputStreamReader inputStreamReader = null;
	    	InputStream inputStream = null;
	    	HttpURLConnection httpUrlConn = null;  //����ҳ����
	    	
	    	try {
	    		//����get����
	    		URL url = new URL(requestUrl);
	    		httpUrlConn = (HttpURLConnection) url.openConnection();
	    		httpUrlConn.setDoInput(true);
	    		httpUrlConn.setRequestMethod("GET");
	    		
	    		//��ȡ������
	    		inputStream = httpUrlConn.getInputStream();
	    		inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	    		bufferedReader = new BufferedReader(inputStreamReader);
	    		
	    		//���������ж�ȡ���
	    		buffer = new StringBuffer();
	    		String str = null;
	    		//�����������ַ���Ϊ�գ����ַ��浽�ַ�������
	    		while((str = bufferedReader.readLine())!= null) 
	    		{
	    			buffer.append(str);
	    		}//while����
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}finally {
	    		//�ͷ���Դ
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
	    
	    private static String  htmlFiter(String html) 
	    {
	    	StringBuffer buffer = new StringBuffer();

	    	String str = null;
	    	Pattern p = Pattern.compile("(<div id=\"content\">)(.*)(</div>*)");    		
    		Matcher m = p.matcher(html);    		
    		if(m.find()) 
    		{
    			str = m.group(2);
    			String str1 = null;
    			String str2 = null;
    	    	Pattern p1 = Pattern.compile("(class=\"ptitle\">)(.+?)(a href=\")(content_cvpr_2018)(.+?)(CVPR_2018_paper.html\">)(.+?)(</a>)(</dt>)");    		
        		Matcher m1 = p1.matcher(str);
     
        		while(m1.find())
        		{
        	    	
        			str1 = m1.group(5);
        			str2 = ("http://openaccess.thecvf.com/content_cvpr_2018"+str1+"CVPR_2018_paper.html");
        			buffer.append(str2);
        			buffer.append("\n");

        	
            			try
            			{
            				
                			 File file = new File("D:\\1.txt");               			 
                			 FileWriter fw = new FileWriter(file,true);
                			 String str5 = str2 + System.getProperty("line.separator");
                             fw.write(str5);
                             fw.close();
            			}catch(Exception e) {
            				e.printStackTrace();
            			}
   		
        		}
    		}	 				
	    	return buffer.toString();
	    }
	    
	    public static String getTodayTemperatureInfo(String html) {
	    	String html1 = httpRequest(html);  
	    	String result = htmlFiter(html1);
	    	return result; 
	    	
	    }
	    
	    
	    public static void main(String[] args)
	    {
	    	 String info = getTodayTemperatureInfo("http://openaccess.thecvf.com/CVPR2018.py");
	    	 System.out.println(info);	
	    }
	    
}

