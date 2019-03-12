import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.io.*;

/**
 * ���ܣ���ȡ�������ģ�����+ժҪ���������浽�ļ���
 * ���룺javac -cp jsoup-1.11.3.jar; Main.java
 * ���У�java -cp jsoup-1.11.3.jar; Main
 */
public class Main {
	
	public static void main(String[] args) {
		final String ROOT = "http://openaccess.thecvf.com/";
		final String papersUrl = "CVPR2018.py";
		final String outputFileName = "result.txt";
		final int TIME_OUT = 5000;
		Document doc = null;
		String aPaperTitle = null;
		String aPaperUrl = null;
		String aPaperAbstract = null;
		Document aPaperDoc = null;
		int id = 0;
		try {
			doc = Jsoup.connect(ROOT + papersUrl).timeout(TIME_OUT).get();
			File outPutFile = new File(outputFileName);
			if (! outPutFile.exists()){outPutFile.createNewFile();}
			FileWriter writter = new FileWriter(outPutFile.getName(), true);
				
			// ��ȡ����ptitle����
			Elements elements = doc.getElementsByClass("ptitle");
			for (Element e : elements) {
				aPaperTitle = e.text();
				// ��ȡ������������
				aPaperUrl = e.getElementsByTag("a").first().attr("href");
				System.out.println(ROOT + aPaperUrl);
				// ��ֹ��ȡ���죬���������ܾ�����
				// Thread.sleep(1000);
				aPaperDoc = Jsoup.connect(ROOT + aPaperUrl).timeout(TIME_OUT).get();
				Element ee = aPaperDoc.getElementById("abstract");
				aPaperAbstract = ee.text();
				writter.write(String.format(
					"%d\r\nTitle: %s\r\nAbstract: %s\r\n\r\n\r\n",
					id++, aPaperTitle, aPaperAbstract
				));
			}
			writter.close();
			System.out.println("\r\n\r\n��ȡ��ϣ�һ����ȡ" + id + "ƪ����.");
		} catch (Exception e) {
			System.out.println("��ȡ����");
			e.printStackTrace();
		}
	}
}