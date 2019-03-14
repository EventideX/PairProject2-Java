
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
		int char_type; // ��������ÿ�ж�ȡ������
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while ((char_type = reader.read()) != -1) { // ��� line Ϊ��˵��������
			sb.append((char) char_type); // ��������������ӵ� buffer ��
		}
		text=sb.toString();
	}

	@SuppressWarnings("resource")
	public String getTitleText() throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String readtext;
		while ((readtext = br.readLine()) != null) {
			if (readtext.contains("Title: ")) {//��ȡTitle��
				lineCnt++;
				readtext = readtext.substring(7);//�޳�"Title: "
				sb.append(readtext + "\r\n");//����readLineȱ�ٵĻ���
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
			if (readtext.contains("Abstract: ")) {//��ȡAbstract��
				lineCnt++;
				readtext = readtext.substring(10);//�޳�"Abstract: "
				sb.append(readtext+ "\r\n");//����readLineȱ�ٵĻ���
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("resource")
	public void resultToFile(String result, String outputPath) {
		try {
			File file = new File(outputPath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(result);// ���ļ���д���ַ���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
