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
		int cnt = 0;
		String fileName = "result.txt";
		String url = "http://openaccess.thecvf.com/CVPR2018.py";

		File resultFile = new File(fileName);
		resultFile.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(resultFile));
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.timeout(2000000);
		connection.maxBodySize(0);
		// jsoup����ȡ����Ӧ����������1M��ֻҪ���� connection.maxBodySize(0)������Ϊ0���Ϳ��Եõ�������Ӧ���ȵ������ˡ�
		Document document = connection.get();
		Elements ptitle = document.select(".ptitle");
		// ͨ��ѡ�����õ���ptitle��Elements�б�
		Elements links = ptitle.select("a[href]");
		// ͨ��ѡ������һ���õ�����href���Ե�a��ǩElements�б�
		for (Element link : links) {
			out.write(cnt + "\r\n");
			cnt++;
			String eachUrl = link.attr("abs:href");
			// ��������ǰ�� abs: ǰ׺�������Ϳ��Է��ذ�����·����URL��ַattr("abs:href")
			Connection eachConnection = Jsoup.connect(eachUrl).ignoreContentType(true);
			eachConnection.timeout(2000000);
			eachConnection.maxBodySize(0);
			// jsoup����ȡ����Ӧ����������1M��ֻҪ���� connection.maxBodySize(0)������Ϊ0���Ϳ��Եõ�������Ӧ���ȵ������ˡ�
			Document eachDocument = eachConnection.get();
			Elements eachTitle = eachDocument.select("#papertitle");
			// ��������ͨ��ѡ�����ҵ�Title
			String paperTitle = eachTitle.text();
			out.write("Title: " + paperTitle + "\r\n");
			Elements eachAbstract = eachDocument.select("#abstract");
			// ��������ͨ��ѡ�����ҵ�Abstract
			String paperAbstract = eachAbstract.text();
			out.write("Abstract: " + paperAbstract + "\r\n");
			out.write("\r\n\r\n");
			out.flush();
		}
		out.close(); // �ر��ļ�
	}
}
