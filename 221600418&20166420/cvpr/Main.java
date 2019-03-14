package cvpr;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // 加载论文类型的 CSV 表格
        Map<String, String> map = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("cvpr/cvpr2018-paper-list.csv")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] list = line.split(",");
                map.put(list[2].toLowerCase(), list[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = getHtmlContent("CVPR2018.py");
        /*
         首页HTML样式
         <dt class="ptitle"><br><a href="content_cvpr_2018/html/Das_Embodied_Question_Answering_CVPR_2018_paper.html">Embodied Question Answering</a></dt>
         */
        /*
         详情页HTML样式
         <div id="papertitle">title</div><div id="authors"><br><b><i>authors</i></b>; where</div><font size="5"><br><b>Abstract</b></font><br><br><div id="abstract" >abstract</div><font size="5"><br><b>Related Material</b></font><br><br>[<a href="url">pdf</a>]
         */
        String pattern = "<dt class=\"ptitle\"><br><a href=\"(.*?)\">(.*?)</a></dt>";
        String pattern2 = "<div id=\"papertitle\">(.*?)</div><div id=\"authors\"><br><b><i>(.*?)</i></b>; (.*?)</div><font size=\"5\"><br><b>Abstract</b></font><br><br><div id=\"abstract\" >(.*?)</div><font size=\"5\"><br><b>Related Material</b></font><br><br>\\[<a href=\"(.*?)\">pdf</a>]";
        Pattern r = Pattern.compile(pattern);
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m = r.matcher(result);
        StringBuilder res = new StringBuilder();
        int i = 0;

        while (m.find()) {
            String html = getHtmlContent(m.group(1));
            System.out.println(i);
            System.out.println("URL: " + m.group(1));
            Matcher m2 = r2.matcher(html);
            if (m2.find()) {
                System.out.println("Title: " + m2.group(1));
                System.out.println("authors: " + m2.group(2));
                System.out.println("Type: " + map.get(m2.group(1).split(",")[0].toLowerCase()));
                System.out.println("Where: " + m2.group(3));
                System.out.println("Abstract: " + m2.group(4));
                System.out.println("PDF: " + m2.group(5).replace("../../", "http://openaccess.thecvf.com/"));
                res.append(i).append("\r\n");
//                res.append("Type: ").append(map.get(m2.group(1).split(",")[0].toLowerCase())).append("\r\n");
                res.append("Title: ").append(m2.group(1)).append("\r\n");
//                res.append("Authors: ").append(m2.group(2)).append("\r\n");
                res.append("Abstract: ").append(m2.group(4)).append("\r\n");
//                res.append("PDF: ").append(m2.group(5).replace("../../", "http://openaccess.thecvf.com/")).append("\r\n\r\n\r\n");
            }
            System.out.println();
            System.out.println();
            i++;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("cvpr/result.txt")))) {
            bufferedWriter.write(String.valueOf(res));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getHtmlContent(String uri) {
        StringBuilder result = new StringBuilder();
        try {
            String baseURL = "http://openaccess.thecvf.com";
            URL url = new URL(baseURL + "/" + uri);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }
}
