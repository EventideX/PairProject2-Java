package org.jsoup.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Crawler {
	public static void main(String[] args) throws IOException {
		int cnt=0;
		String fileName="result.txt";
		String url="http://openaccess.thecvf.com/CVPR2018.py";
		
		File resultFile = new File(fileName);   
        resultFile.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(resultFile));         
		Connection connection=Jsoup.connect(url).ignoreContentType(true);
		connection.timeout(2000000);
		connection.maxBodySize(0);
		Document document=connection.get();
		Elements ptitle=document.select(".ptitle");
		Elements links=ptitle.select("a[href]");  
		for (Element link : links) {
			out.write(cnt+"\r\n");
			cnt++;
			String eachUrl=link.attr("abs:href");
			Connection eachConnection=Jsoup.connect(eachUrl).ignoreContentType(true);
			eachConnection.timeout(2000000);
			eachConnection.maxBodySize(0);
			//jsoup最大获取的响应长度正好时1M。只要设置 connection.maxBodySize(0)，设置为0，就可以得到不限响应长度的数据了。
			Document eachDocument=eachConnection.get();
			Elements eachTitle=eachDocument.select("#papertitle");
			String paperTitle=eachTitle.text();
			out.write("Title: "+paperTitle+"\r\n");
			Elements eachAbstract=eachDocument.select("#abstract");
			String paperAbstract=eachAbstract.text();
			out.write("Abstract: "+paperAbstract+"\r\n");
			out.write("\r\n\r\n");
			out.flush();
		}
		out.close(); //关闭文件
		System.out.print("end");//以后要删掉
	}
}
