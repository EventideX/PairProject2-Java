import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url = "http://openaccess.thecvf.com/CVPR2018.py";
		Document doc = Jsoup.connect(url).maxBodySize(0).get();
		Elements links = doc.select("dt.ptitle > a[href]");
		String secondUrl = null;
		Document secondDoc = null;
		Element title = null;
		Element contentAbstract = null;
		String outfilename = "result.txt";
		initTxt(outfilename);
		String outpath = new File(outfilename).getAbsolutePath();
		FileWriter fw = new FileWriter(outpath, true);
		int num = 0;
		for (Element link : links) {
			secondUrl = link.attr("abs:href");
			secondDoc = Jsoup.connect(secondUrl).get();
			title = secondDoc.select("div#papertitle").first();
			contentAbstract = secondDoc.select("div#abstract").first();
	    	fw.write((Integer.toString(num)) + '\n');
	    	fw.write("Title: " + StringFilter(title.text()) + '\n');
	    	fw.write("Abstract: " + StringFilter(contentAbstract.text()) + "\n\n\n");
	    	fw.flush();
			num++;
		}
		System.out.println("Links:" + links.size());
		if (fw != null) {
			fw.close();
		}
	}
	
	public static void initTxt(String string) throws IOException {
        String path = new File(string).getAbsolutePath();
        FileWriter fw = new FileWriter(path, false);
        fw.write("");
        fw.flush();
        fw.close();
	}
	
	// ���˷�ASCII�ַ� 
	public static String StringFilter(String str) throws PatternSyntaxException { 
	String regEx="[^\\x00-\\xff]"; 
	Pattern p = Pattern.compile(regEx); 
	Matcher m = p.matcher(str);
	return m.replaceAll(" ").trim();
	} 
}
