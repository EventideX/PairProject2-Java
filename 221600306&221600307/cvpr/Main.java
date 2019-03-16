package cvrp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static final String BASIC_URL = "http://openaccess.thecvf.com/";
	
	public static final String CVRP_URL = "http://openaccess.thecvf.com/CVPR2018.py";
	
	public static List<String> urList = new ArrayList<String>();

	
	public static void main(String[] args) {
		try {
			/*重定向标准输出流*/
			System.setOut(new PrintStream("result.txt"));
			getPaperUrl();
			getPaperDetail();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void getPaperDetail() {
		int size;
		size = urList.size();
//		size = 10;
		for (int i = 0; i < size; i++) {
			try {
				Document doc = Jsoup.connect(urList.get(i)).get();
				Element content = doc.getElementById("content");
				Element paperTitle = content.getElementById("papertitle");
				Element paperAbstract = content.getElementById("abstract");
				System.out.println(i);
				System.out.println("Title: "+paperTitle.text());
				System.out.println("Abstract: "+paperAbstract.text()+"\n\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void getPaperUrl() {
		try {
			Document doc = Jsoup.connect(CVRP_URL).get();
//			System.out.println(doc);
			Element content = doc.getElementById("content");
			Elements ptitles = content.getElementsByClass("ptitle");
			for (int i = 0; i < ptitles.size(); i++) {
				String link = ptitles.get(i).getElementsByTag("a").attr("href");
				urList.add(BASIC_URL+link);
//				System.out.println(BASIC_URL+link);
			}
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
