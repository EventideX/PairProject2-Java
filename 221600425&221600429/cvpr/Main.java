import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main {
	static int i=0;

	static FileWriter fw;

	public static void main(String[] args) {
		try {
			fw=new FileWriter(new File("result.txt"));
			Document doc = Jsoup.connect("http://openaccess.thecvf.com/CVPR2018.py")//爬取网页的URL
								.maxBodySize(0)//不限制大小
								.timeout(1000*60)//连接超时时间
								.get();
			Elements elems = doc.select(".ptitle a");

			System.out.println(elems.size());
			ExecutorService threadPool = Executors.newScheduledThreadPool(8);//开启可以容纳8个线程的线程池
			
			for (Element elem : elems) {
				String url=elem.attr("href");
				threadPool.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Document doc = Jsoup.connect("http://openaccess.thecvf.com/" + url)
												.maxBodySize(0)
												.timeout(1000*60*5) // 设置连接超时时间
												.get();
							
							System.out.println(i+"\n"+doc.select("#papertitle").text() + "\n" + doc.select("#abstract").text() + "\n");
							synchronized (fw) {//对字符写入流进行加速
								fw.append(
										(i++)+"\r\n"+
										"Title: "+
										doc.select("#papertitle").text() + "\r\n" +
										"Abstract: " +
										doc.select("#abstract").text() + "\r\n"
										+"\r\n\r\n");											
								fw.flush();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			if (!threadPool.isShutdown()) {
				threadPool.shutdown();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
