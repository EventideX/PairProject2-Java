import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lib {
	
	public int num = 0;
	//ÍøÒ³ÐÅÏ¢ÅÀÈ¡
	public void GetInfo()
	{
		String strurl = "http://openaccess.thecvf.com/CVPR2018.py";
		try
		{
			URL url = new URL(strurl);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			//System.out.println(con.getContentEncoding());
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "Ascii"));
			String line=null;
			String href = null;
			File file = new File("result.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			Pattern p = Pattern.compile("ptitle");
			//Pattern ps = Pattern.compile("papertitle");
			Pattern pa = Pattern.compile("abstract");
			while((line=br.readLine()) != null)
			{
				//System.out.println(line);				
				Matcher m = p.matcher(line);
				if(m.find())
				{
					String arr[] = line.split("<|>|\"");
					//for(int i = 0; i < arr.length; i++)
					//{
					//	System.out.print(arr[i]+"  ");  //href=arr[8]
					//}
					//System.out.println("");
					href= "http://openaccess.thecvf.com/" + arr[8];
					URL surl = new URL(href);
					URLConnection scon = surl.openConnection();
					InputStream os = scon.getInputStream();
					BufferedReader sbw = new BufferedReader(new InputStreamReader(os));
					String abs = null;
					while((abs=sbw.readLine()) != null)
					{
						Matcher sm = pa.matcher(abs);
						if(sm.find())
						{
							abs=sbw.readLine();
							String sarr[] = abs.split("<|>");
							//(int i=0; i<sarr.length; i++) 
							//{
								//System.out.print(sarr[i]+"  ");
							//}
							//System.out.println("");
							//System.out.println(sarr[0]);
							//System.out.println("");
							//System.out.println(num+"\r\nTitle: "+arr[10]+"\r\nAbstract: "+sarr[0]+"\r\n");
							bw.write(num+"\r\nTitle: "+arr[10]+"\r\nAbstract: "+sarr[0]+"\r\n\r\n\r\n");
							num++;
							break;
						}
					
					}
					//bw.write(num+"\r\ntitle: "+arr[10]+"\r\n");
					//num++;
					//System.out.println(num+"\r\ntitle: "+arr[10]);
					//bw.write(line+"\r\n");
					
				}
			}
			//bw.write(br.toString());
			bw.flush();
			bw.close();
			br.close();
			System.out.println("Finished!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
