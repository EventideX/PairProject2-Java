import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
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

    // 是否使用-m参数
    public static boolean usePhraseWordNum = false;

    // 是否使用-n参数
    public static boolean useSortedPrintNum = false;

    // 是否使用-w参数
    public static boolean useWordWeight = false;
	
	// 论文编号
    public static String strNumber = null;
	
	// 论文标题
    public static String strTitle = null;
	
	// 论文摘要
    public static String strAbstract = null;
	
	// 论文标题单词数
	public static int strTitleNumber = 0;
	
	// 论论文摘要单词书
    public static int strAbstractNumber = 0;
    /**
     * 程序入口
     * @param args[0] 输入文件名
     */
    public static void main(String[] args) {
        // 初始化
        loadArgs(args);
		inputFileBytes = readFileToBytes(inputFileName);
        runProgress();
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
            System.out.println("未输入参数");
            System.exit(1);
        }
        //System.out.println(inputFileName);
        //System.out.println(outputFileName);
        //System.out.println(phraseWordNum);
        //System.out.println(sortedPrintNum);
        //System.out.println(useWordWeight);
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
			BufferedReader reader=new BufferedReader(new FileReader(file));
			
			strNumber=reader.readLine();
			//System.out.println(strNumber);
			strTitle=reader.readLine();
			strTitle=strTitle.substring(6).toLowerCase();
			//System.out.println(strTitle);
			strAbstract=reader.readLine();
			strAbstract=strAbstract.substring(9).toLowerCase();
			//System.out.println(strAbstract);
			strNumber=reader.readLine();
			String str=strTitle+"\r\n"+strAbstract;
			fileBytes=str.getBytes();
			
				// for (byte b : fileBytes){
				//     System.out.print((char)b);
				//     System.out.print(":");
				//     System.out.print(b);
				//     System.out.print("/");
				// }
				//reader.close();
			
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
        System.out.println("words: " + wordNum);
        System.out.println("lines: " + lineNum);
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
            "characters: %d\r\nwords: %d\r\nlines: %d\r\n",
            charNum, wordNum, lineNum
        );
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            resultString += String.format("%s: %d\r\n", entry.getKey(), entry.getValue());
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
	
	public static void runProgress(){
		Lib core = new Lib(inputFileBytes);

        // 内核处理
        core.preproccess();
		strTitleNumber=core.filterTitle(strTitle.getBytes());
		System.out.println(strTitle);
		strAbstractNumber=core.filterAbstract(strAbstract.getBytes())-strTitleNumber;
		System.out.println(strAbstract);
        core.collectWord(strTitle.trim().split(" "),1);
		core.collectWord(strAbstract.trim().split(" "),2);
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
}