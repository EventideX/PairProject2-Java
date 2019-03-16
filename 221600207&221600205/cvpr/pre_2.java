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
	    
	    private static String  htmlFiter(String html,int num) 
	    {
	    	StringBuffer buffer = new StringBuffer();
	      
	    	String str1= "";
	    	String str2= "";	    	
	    		// ƥ��Title(��Ŀ),��Ŀ��������<div id="papertitle"> �� </div>��
	    		Pattern p1 = Pattern.compile("(<div id=\"papertitle\">)(.+?)(</div>*)");    		
	    		Matcher m1 = p1.matcher(html);  
	    		// ƥ��Abstract(ժҪ)��ժҪ��������<div id="abstract"> �� </div>��
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
