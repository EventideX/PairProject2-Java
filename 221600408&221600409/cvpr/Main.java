import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class Main{
    public static int index = 0;
    public static String path = "result.txt";
    public static LinkedList<String> links = new LinkedList<>();

    //线程爬取CVPR2018官网
    public static void crawler(){
        try{
            String url = "http://openaccess.thecvf.com/CVPR2018.py";
            crawlerLink(url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!links.isEmpty()){
                        String link = links.getFirst();
                        links.removeFirst();
                        crawlerContent(link);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //爬取CVPR2018官网所有文章链接
    public static void crawlerLink(String url){
        try{
            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("content");
            Elements ptitles = content.getElementsByClass("ptitle");
            for(Element item : ptitles){
                Element link = item.selectFirst("a");
                String href = "http://openaccess.thecvf.com/"+link.attr("href");
                links.add(href);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //获取CVPR2018所有文章的标题和摘要
    public static void crawlerContent(String url){
        try{
            StringBuilder results = new StringBuilder();
            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("content");
            results.append(index+"\r\n");
            String title = content.getElementById("papertitle").text();
            results.append("title: "+title+"\r\n");
            String abstractText = content.getElementById("abstract").text();
            results.append("abstract: "+abstractText+"\r\n"+"\r\n"+"\r\n");
            saveArticle(results);
            index++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //将CVPR2018官网爬取的内容保存到result文本
    public static void saveArticle(StringBuilder content)throws Exception{
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content.toString());
        bw.flush();
        bw.close();
    }

    public static void main(String[] args){
        try{
            crawler();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
