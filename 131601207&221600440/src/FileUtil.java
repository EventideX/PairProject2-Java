
import java.io.*;

class FileUtil {
	private String filePath;
	private InputStream is;
	private String text;
	private int lineCnt = 0;

	public int getLineCnt() {
		return lineCnt;
	}
	public String getText() {
		return text;
	}

	public FileUtil(String path) {
		filePath = path;
		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void FiletoText() throws IOException {
		StringBuffer sb = new StringBuffer();
		int char_type; // 用来保存每行读取的内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while ((char_type = reader.read()) != -1) { // 如果 line 为空说明读完了
			sb.append((char) char_type); // 将读到的内容添加到 buffer 中
		}
		text=sb.toString();
	}

	@SuppressWarnings("resource")
	public String getTitleText() throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String readtext;
		while ((readtext = br.readLine()) != null) {
			if (readtext.contains("Title: ")) {//提取Title行
				lineCnt++;
				readtext = readtext.substring(7);//剔除"Title: "
				sb.append(readtext + "\r\n");//补上readLine缺少的换行
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("resource")
	public String getAbstractText() throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String readtext;
		while ((readtext = br.readLine()) != null) {
			if (readtext.contains("Abstract: ")) {//提取Abstract行
				lineCnt++;
				readtext = readtext.substring(10);//剔除"Abstract: "
				sb.append(readtext+ "\r\n");//补上readLine缺少的换行
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("resource")
	public void resultToFile(String result, String outputPath) {
		try {
			File file = new File(outputPath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// 往文件里写入字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
