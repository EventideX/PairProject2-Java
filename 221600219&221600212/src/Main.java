import java.io.*;
import java.util.*;

public class Main{

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
	
	// �Ƿ��ƵȨ��
	public static boolean useWordWeight = false;
	

    /**
     * �������
     * @param args[0] �����ļ���
     */
    public static void main(String[] args) {
        // ��ʼ��
		loadArgs(args);
        inputFileBytes = readFileToBytes(inputFileName);
        Lib core = new Lib(inputFileBytes);
        
		// �ں˴���
        core.preproccess();
        core.collectWord();
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
						break;
					case "-n":
						sortedPrintNum = Integer.valueOf(args[++i]);
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
		System.out.println(inputFileName);
		System.out.println(outputFileName);
		System.out.println(phraseWordNum);
		System.out.println(sortedPrintNum);
		System.out.println(useWordWeight);
    }


    /**
     * ���ܣ���ȡ�ļ����ֽ�������
     *
     * ������String fileName �ļ���
     *
     * ���أ�byte[] bytes �ֽ�����
     */
    public static byte[] readFileToBytes(String fileName){
        byte[] fileBytes = null;

        try {
            File file = new File(fileName);
			FileInputStream reader = new FileInputStream(file);
			Long fileLength = file.length();
			fileBytes = new byte[fileLength.intValue()];
			reader.read(fileBytes);
			for (byte b : fileBytes){
				System.out.print((char)b);
				System.out.print(":");
				System.out.print(b);
				System.out.print("/");
			}
			reader.close();
        }
        catch(Exception e){
            System.out.println("��ȡ�ļ�����");
			System.exit(1);
        }

        return fileBytes;
    }

    /**
     * ���ܣ���ӡ���������̨
     */
    public static void printResult(){
        System.out.println("characters: " + charNum);
        System.out.println("words: " + lineNum);
        System.out.println("lines: " + wordNum);
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
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
            "characters: %d\nwords: %d\nlines: %d\n",
            charNum, wordNum, lineNum
        );
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            resultString += String.format("%s: %d\n", entry.getKey(), entry.getValue());
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

}