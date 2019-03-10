import java.io.*;
import java.util.*;

public class Main{

    // 行数
    public static int lineNum = 0;

    // 字符数
    public static int charNum = 0;

    // 单词数：至少四个英文字母开头，不区分大小写
    public static int wordNum = 0;

    // 排序好的单词集合
    public static List<Map.Entry<String, Integer>> wordList = null;

    // 输入文件字节数组
    public static byte[] inputFileBytes = null;

    // 排序打印出前几的个数
    public static int sortedPrintNum = 10;
	
	// 输入文件名
	public static String inputFileName = null;
	
	// 输出文件名
	public static String outputFileName = null;
	
	// 词组的词数
	public static int phraseWordNum = 1;
	
	// 是否词频权重
	public static boolean useWordWeight = false;
	

    /**
     * 程序入口
     * @param args[0] 输入文件名
     */
    public static void main(String[] args) {
        // 初始化
		loadArgs(args);
        inputFileBytes = readFileToBytes(inputFileName);
        Lib core = new Lib(inputFileBytes);
        
		// 内核处理
        core.preproccess();
        core.collectWord();
        core.sortWordMap();

        // 获取结果
        charNum = core.getCharNum();
        wordNum = core.getWordNum();
        lineNum = core.getLineNum();
        wordList = core.getSortedList();

        // 打印结果
        printResult();

        // 保存结果
        writeResult();
    }
	
	/**
     * 功能：解析命令行参数
	 *
	 * 参数：String[] args 命令行参数数组
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
            System.out.println("未输入参数");
			System.exit(1);
        }
		System.out.println(inputFileName);
		System.out.println(outputFileName);
		System.out.println(phraseWordNum);
		System.out.println(sortedPrintNum);
		System.out.println(useWordWeight);
    }


    /**
     * 功能：读取文件到字节数组中
     *
     * 参数：String fileName 文件名
     *
     * 返回：byte[] bytes 字节数组
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
            System.out.println("读取文件出错");
			System.exit(1);
        }

        return fileBytes;
    }

    /**
     * 功能：打印结果到控制台
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
     * 功能：输出结果到文件中
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
            System.out.println("写入文件出错");
			System.exit(1);
        }
    }

}