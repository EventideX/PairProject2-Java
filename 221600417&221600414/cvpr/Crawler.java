import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @description: crawler
 * @author: hlx 2019-03-10
 **/
public class Crawler {

    private static final String OUT_FILE = "result.txt";

    private static final String DOMAIN = "http://openaccess.thecvf.com/";

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect(DOMAIN + "CVPR2018.py").maxBodySize(0).get();
        StringBuilder builder = new StringBuilder();
        Element dls = doc.select("dl").first();
        Elements papers = dls.children();
        for (int i = 0; i < papers.size(); i = i + 3) {
            builder.append(i / 3).append("\n");
            Element link = papers.get(i).getElementsByTag("a").first();
            String titleStr = link.text();
            String absUrl = link.attr("href");
            System.out.println(titleStr);
            builder.append("Title: ").append(titleStr).append("\n");
            builder.append("Abstract: ").append(getAbstract(absUrl)).append("\n");
            builder.append("\n\n");
        }
        outFile(builder.toString());
    }

    private static String getAbstract(String url) throws IOException {
        Document doc = Jsoup.connect(DOMAIN + url).timeout(60 * 1000).get();
        return doc.getElementById("abstract").text();
    }

    private static void outFile(String str) throws IOException {
        BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(OUT_FILE));
        bf.write(str.getBytes());
        bf.flush();
    }

}
