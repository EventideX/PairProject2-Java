import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * java Main -i input.txt -w 1 -o output.txt
 */
public class Main{
	//�ļ���ȡ����
	public static boolean fileEnd = false;
	
	//����������ʽ
	public static Pattern pattern = Pattern.compile("[\\d]*$");  
    // ����
    public static int lineNum = 0;

    // �ַ���
    public static int charNum = 0;

    // �������������ĸ�Ӣ����ĸ��ͷ�������ִ�Сд
    public static int wordNum = 0;

    // ����õĵ��ʼ���
    public static List<Map.Entry<String, Integer>> wordList = null;

    // �����ļ��ֽ�����
    public static byte[] inputFileBytes = null;

    // �����ӡ��ǰ���ĸ���
    public static int sortedPrintNum = 10;

    // �����ļ���
    public static String inputFileName = null;

    // ����ļ���
    public static String outputFileName = null;

    // ����Ĵ���
    public static int phraseWordNum = 1;

    // �Ƿ�ʹ��-m����
    public static boolean usePhraseWordNum = false;

    // �Ƿ�ʹ��-n����
    public static boolean useSortedPrintNum = false;

    // �Ƿ�ʹ��-w����
    public static boolean useWordWeight = false;
	
	// ���ı��
    public static String strNumber = null;
	
	// ���ı���
    public static String strTitle = null;
	
	// ����ժҪ
    public static String strAbstract = null;
	
	// ���ı��ⵥ����
	public static int strTitleNumber = 0;
	
	// ������ժҪ������
    public static int strAbstractNumber = 0;
	
	public static File file=null;
	
	public static BufferedReader reader=null;
	
    /**
     * �������
     * @param args[0] �����ļ���
     */
    public static void main(String[] args) {
        // ��ʼ��
        loadArgs(args);
		try {
			file = new File(inputFileName);
			reader=new BufferedReader(new FileReader(file));
			
        }
        catch(Exception e){
            System.out.println("��ȡ�ļ�����");
            System.exit(1);
        }
		
		while(!fileEnd){
			inputFileBytes = readFileToBytes();
			runProgress();
		}
    }

    /**
     * ���ܣ����������в���
     *
     * ������String[] args �����в�������
     */
    static void loadArgs(String[] args){
        if (args.length > 0 && args != null){
            for (int i = 0; i < args.length; i++){
                switch (args[i]){
                    case "-i":
                        inputFileName = args[++i];
                        break;
                    case "-o":
                        outputFileName = args[++i];
                        break;
                    case "-m":
                        phraseWordNum = Integer.valueOf(args[++i]);
                        usePhraseWordNum=true;
                        break;
                    case "-n":
                        sortedPrintNum = Integer.valueOf(args[++i]);
                        useSortedPrintNum=true;
                        break;
                    case "-w":
                        useWordWeight = (args[i+1].equals("1"));
                        break;
                    default:
                        break;
                }
            }
        } else{
            System.out.println("δ�������");
            System.exit(1);
        }
        //System.out.println(inputFileName);
        //System.out.println(outputFileName);
        //System.out.println(phraseWordNum);
        //System.out.println(sortedPrintNum);
        //System.out.println(useWordWeight);
    }


    /**
     * ���ܣ���ȡ�ļ����ֽ�������
     *
     * ������String fileName �ļ���
     *
     * ���أ�byte[] bytes �ֽ�����
     */
    public static byte[] readFileToBytes(){
        byte[] fileBytes = null;
		
        try {
			
			
			if(pattern.matcher(strNumber=reader.readLine()).matches()){//�����
				//System.out.println(strNumber);
				strTitle=reader.readLine();//������
				strTitle=strTitle.substring(6).toLowerCase();
				//System.out.println(strTitle);
				strAbstract=reader.readLine();//��ժҪ
				strAbstract=strAbstract.substring(9).toLowerCase();
				//System.out.println(strAbstract);
				String str=strTitle+"\r\n"+strAbstract;
				fileBytes=str.getBytes();
				
					// for (byte b : fileBytes){
					//     System.out.print((char)b);
					//     System.out.print(":");
					//     System.out.print(b);
					//     System.out.print("/");
					// }
					//reader.close();
					if(reader.readLine()==null){
						//System.out.println("aaaa");
						fileEnd=true;
						reader.close();
						return fileBytes;
					}
			}
			else{
				fileEnd=true;
			}
        }
        catch(Exception e){
            System.out.println("��ȡ�ļ�����");
			e.printStackTrace();
            System.exit(1);
        }

        return fileBytes;
    }

    /**
     * ���ܣ���ӡ���������̨
     */
    public static void printResult(){
        System.out.println("characters: " + charNum);
        System.out.println("words: " + wordNum);
        System.out.println("lines: " + lineNum);
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            System.out.println("<"+entry.getKey()+">" + ": " + entry.getValue());
            if (++ i >= sortedPrintNum){
                break;
            }
        }
    }

    /**
     * ���ܣ����������ļ���
     */
    public static void writeResult(){
        String resultString = String.format(
            "characters: %d\r\nwords: %d\r\nlines: %d\r\n",
            charNum, wordNum, lineNum
        );
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            resultString += String.format("<"+"%s"+">"+": %d\r\n", entry.getKey(), entry.getValue());
            if (++ i >= sortedPrintNum){
                break;
            }
        }
        try{
            File outPutFile = new File(outputFileName);
            if (! outPutFile.exists()){
                outPutFile.createNewFile();
            }
            FileWriter writter = new FileWriter(outPutFile.getName(), false);
            writter.write(resultString);
            writter.close();
        }catch(Exception e){
            System.out.println("д���ļ�����");
            System.exit(1);
        }
    }
	/**
     * ���ܣ�������ͳ�ƹ���
     *
     * 
     */
	public static void runProgress(){
		Lib core = new Lib(inputFileBytes);

        // �ں˴���
        core.preproccess();
		strTitleNumber=core.filterTitle(strTitle.getBytes());
		// System.out.println(strTitle);
		strAbstractNumber=core.filterAbstract(strAbstract.getBytes())-strTitleNumber;
		// System.out.println(strAbstract);
        core.collectWord(strTitle.trim().split(" "),1);
		core.collectWord(strAbstract.trim().split(" "),2);
        core.sortWordMap();

        // ��ȡ���
        charNum = core.getCharNum();
        wordNum = core.getWordNum();
        lineNum = core.getLineNum();
        wordList = core.getSortedList();

        // ��ӡ���
        printResult();

        // ������
        writeResult();
	}
}