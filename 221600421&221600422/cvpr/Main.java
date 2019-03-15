import java.io.BufferedWriter;
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
	static final String CVPR_URL = "http://openaccess.thecvf.com/CVPR2018.py";
	static final String FILE_NAME = "result.txt";

	static BufferedWriter writer;

	public static void main(String[] args) {
		try {
			writer=new BufferedWriter(new FileWriter(new File(FILE_NAME)));
//			get menu.page.html
			Document doc = Jsoup.connect(CVPR_URL)
					.userAgent("Mozilla/5.0 (Windows NT 10.0").timeout(3000)
					.get();
			Elements elements = doc.select(".ptitle a");

			System.out.println(elements.size());

			ExecutorService pool = Executors.newScheduledThreadPool(24);
			
			for (Element element : elements) {
//				MyThread thread = new MyThread(element.attr("href"));
//				pool.submit(thread);
				String url=element.attr("href");
				pool.submit(new Runnable() {
					static final String HOST_URL = "http://openaccess.thecvf.com/";
					@Override
					public void run() {
						try {
							Document doc = Jsoup.connect(HOST_URL + url).userAgent("Mozilla/5.0 (Windows NT 10.0").timeout(3000) // 设置连接超时时间
									.get();
							System.out.println(doc.select("#papertitle").text() + "\n" + doc.select("#abstract").text() + "\n");
							
							writer.append(doc.select("#papertitle").text() + "\n" + doc.select("#abstract").text() + "\n\n");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			if (!pool.isShutdown()) {
				pool.shutdown();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


/**
 * get links in menu.html then create thread to get concreate article title and
 * abstract
 * 
 * @author KWM
 *
 */
//class MyThread extends Thread {
//	String url;
//
//	static final String HOST_URL = "http://openaccess.thecvf.com/";
//
//	public MyThread(String url) {
//		this.url = url;
//	}
//
//	@Override
//	public void run() {
//		try {
//			Document doc = Jsoup.connect(HOST_URL + url).userAgent("Mozilla/5.0 (Windows NT 10.0").timeout(3000) // 设置连接超时时间
//					.get();
//			System.out.println(doc.select("#papertitle").text() + "\n" + doc.select("#abstract").text() + "\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void saveToFile() {
//		
//	}
//}