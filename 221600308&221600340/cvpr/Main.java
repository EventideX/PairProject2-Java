import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {

	/**
	 * ��ȡ������Ϣ
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuilder sBuilder = new StringBuilder();
		try {
			CloseableHttpClient chclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet("http://openaccess.thecvf.com/CVPR2018.py");
			String response = chclient.execute(httpget, new BasicResponseHandler());

			Document document = Jsoup.parse(response);
			Elements ptitles = document.getElementsByClass("ptitle");
			ArrayList<String> links = new ArrayList<>();
			String link = "http://openaccess.thecvf.com/";
			//����ƴ�� url�ַ���
			StringBuilder str = new StringBuilder(); 
														
			for (Element ptitle : ptitles) {
				str.append(link);
				str.append(ptitle.getElementsByTag("a").attr("href"));
				links.add(str.toString());
				//���StringBuilder
				str.delete(0, str.length());
			}

			int i = 0;
			for (String paper : links) {
				Document doc = Jsoup.connect(paper).get();
				String title = doc.select("div#papertitle").first().text();
				String abstr = doc.select("div#abstract").first().text();
				sBuilder.append(i);
				sBuilder.append("\r\n");
				sBuilder.append("Title: ");
				sBuilder.append(title);
				sBuilder.append("\r\n");
				sBuilder.append("Abstract: ");
				sBuilder.append(abstr);
				//ƴ��pdf����
//                sBuilder.append("\r\n");
//                sBuilder.append("PDF_Link: ");
//                sBuilder.append(paper.replaceAll("/html","/papers").replaceAll(".html",".pdf"));
				if (i < links.size() - 1) {
					sBuilder.append("\r\n");
					sBuilder.append("\r\n");
					sBuilder.append("\r\n");
				}
				System.out.println("����ȡ:"+i);
				//title��Abstract
//				System.out.println("Title:" + title);
//				System.out.println("Abstract:" + abstr);
				i++;
			}
			//��ȡ��ɣ�׼��д���ļ�
			System.out.println("��ȡ��ɣ���ʼд���ļ���");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("result.txt"));
			bos.write(sBuilder.toString().getBytes());
			bos.close();
			System.out.println("д���ļ����!");
		} catch (Exception e) {
			//����
		}
	}
}
