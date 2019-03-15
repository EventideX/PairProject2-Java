import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class Main2 {

    public static final String URL="http://openaccess.thecvf.com/CVPR2018.py";
    public static final String URL_PRE="http://openaccess.thecvf.com/";
    public static final String STORE_NAME="cvpr/result.txt";

    public static void main(String args[]) throws IOException {
        Document doc=Jsoup.connect(URL).maxBodySize(0).get();
        Elements elements=doc.getElementsByClass("ptitle").select("a");
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(STORE_NAME))));
        int index=0;
        System.out.println("start");
        for(Element e : elements){
            String href=e.attr("href");
            System.out.println(index+":"+href);
            String title=e.text();
            String text=Jsoup.connect(URL_PRE+href).get().getElementById("abstract").text();
            bw.write(String.valueOf(index));
            bw.write("\r\n");
            bw.write("Title: "+title+"\r\n");
            bw.write("Abstract: "+text+"\r\n\r\n");
            ++index;
        }
        bw.flush();
        bw.close();
        System.out.println("end");
    }
}
