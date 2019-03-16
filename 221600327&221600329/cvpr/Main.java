
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Main {
		
		public static void main(String[] args) {
			String url = "http://openaccess.thecvf.com/CVPR2018.py";	
			getFile(url);
		}
		
		/**
		 * 从cvpr网站爬取论文数据，并写入result.txt
		 * @param URL
		 */
	    public static void getFile(String URL) {
	    	BufferedWriter bufferedWriter = null;
	        try {
	            File outputFile = new File("result.txt");
	            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
	            //get方式得到HTML数据
	            //默认设置下，jsoup超时时间为3秒，鉴于当前网络环境，修改为10秒
	            //默认设置下，jsoup最大获取的长度只有1024K，设置maxBodySize(0)，可不限长度
	            Document doc = Jsoup.connect(URL).timeout(10000).maxBodySize(0).get();
	            //从HTML中选择所有class=ptitle的节点
	            Elements paperList = doc.select("[class=ptitle]");
	            //从ptitle节点中选择a标签的href属性值
	            Elements links = paperList.select("a[href]");
	            int count = 0;
	            //分别当问每篇论文的详情页
	            for (Element link : links) {
	                //论文详情页URL
	                String url = link.attr("abs:href");
	                Document paperDoc = Jsoup.connect(url).timeout(10000).maxBodySize(0).get();
	                //获取论文title
	                Elements paperTitle = paperDoc.select("[id=papertitle]");
	                String title = paperTitle.text();
	                //获取论文Abstract
	                Elements paperAbstract = paperDoc.select("[id=abstract]");
	                String abstracts = paperAbstract.text();
	                //数据写入文件
	                bufferedWriter.write(count++ + "\r\n");
	                bufferedWriter.write("Title: " + title + "\r\n");
	                bufferedWriter.write("Abstract: " + abstracts + "\r\n\r\n\r\n");
	            }
	        } catch (Exception e) {
	        	System.out.println("获取论文数据失败");
	            e.printStackTrace();
	        }finally {
	        	try {
				if(bufferedWriter != null) {
					bufferedWriter.close();
				}
	        	}catch (Exception e) {
		        	System.out.println("获取论文数据失败");
					e.printStackTrace();
				}
			}
	    }

}
