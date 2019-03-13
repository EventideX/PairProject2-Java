import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Handler handler=new Handler();
		ExecutorService cachedPool=Executors.newCachedThreadPool();
		try {
			System.out.println("开始链接");
			Document document=Jsoup.connect("http://openaccess.thecvf.com/CVPR2018.py").maxBodySize(0).timeout(1000*60).get();
			System.out.println("开始爬取");
			handler.writer=new BufferedWriter(new FileWriter("result.txt"));
			//System.out.println(document.toString());
			Elements links=document.getElementsByTag("a");
			int cnt=0;
			for(Element link:links) {
				String href=link.attr("href");
				//System.out.println(href);
				if(href.contains("content_cvpr_2018/html/")) {//获取论文
					cnt++;
					cachedPool.execute(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								//Thread.sleep(100);
								Document document=Jsoup.connect("http://openaccess.thecvf.com/"+href).maxBodySize(0).timeout(1000*60*5).get();
								handler.writeFile(document);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}	
					});
					
				}
			}
			System.out.println(cnt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

class Handler{
	BufferedWriter writer;
	int cnt=0;
	synchronized void writeFile(Document document) {
		try {
			System.out.println(cnt+" "+document.getElementById("abstract").text());
			String string=cnt+"\r\n";
			string+="Title: "+document.getElementById("papertitle").text()+"\r\n";
			string+="Abstract: "+document.getElementById("abstract").text()+"\r\n\r\n\r\n";
			writer.write(string);
			writer.flush();
			cnt++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		System.out.println("close");
		writer.close();
	}
	
	
}