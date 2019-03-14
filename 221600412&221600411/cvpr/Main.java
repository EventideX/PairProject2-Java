import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: java8
 * @description:
 * @author: ChenYu
 * @create: 2019-03-11 19:11
 **/
public class Main {

    public static String url = "http://openaccess.thecvf.com/CVPR2018.py";
    public static String urlprifex = "http://openaccess.thecvf.com/";
    public static String fileName = "G:\\DubboProject\\SuanFa\\src\\main\\java\\soft\\jinjie\\result.txt";

    public static void main(String args[]) throws IOException {
        Document doc = Jsoup.connect(url).maxBodySize(0).get(); //  数据可能不全，加上缓冲大小
        Elements elements = doc.getElementsByClass("ptitle").select("a");
        FileWriter fileWriter = new FileWriter(new File(fileName),true);
        int i = 0;
        System.out.println("爬虫开始 =========");
        for (Element e : elements){
             String href = e.attr("href");
             String title = e.text();
             String text = Jsoup.connect(urlprifex + href).get().getElementById("abstract").text();
             fileWriter.write(String.valueOf(i));  // 编号
             fileWriter.write("\r\n");
             fileWriter.write("Title: " + title + "\r\n");
             fileWriter.write("Abstract: " + text + "\r\n\r\n");
             fileWriter.write("\r\n");
             fileWriter.flush();
             System.out.println("第" + i + "条论文信息已爬取");
             i++;
        }
        System.out.println(i);
        System.out.println("爬虫结束 ：=========");
       // fileWriter.close();
    }

}
