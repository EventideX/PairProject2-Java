import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: SuanFa
 * @description:
 * @author: ChenYu
 * @create: 2019-03-11 21:26
 **/
public class Main {

    public String inputFileName = null;  // The file name to read
    public String outputFileName = null;  // The file name to output
    public int titleWeight = 10;  // title weight
    public int abstractWeight = 1;   // abstract weight
    public int m = 1; // The length of the phrase
    public int n = 10; // Output the number of words


    public static void main(String args[]) throws IOException, InterruptedException {
        Main instance = new Main();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    instance.I(args[i + 1]);
                    break;
                case "-o":
                    instance.O(args[i + 1]);
                    break;
                case "-w":
                    instance.W(args[i + 1]);
                    break;
                case "-m":
                    instance.M(args[i + 1]);
                    break;
                case "-n":
                    instance.N(args[i + 1]);
                    break;
                default:
                    break;
            }
        }
        String content = instance.readFile(instance.inputFileName);
        Lib lib = new Lib();
        lib.countWordOrWordGroupAndFrequency(content, instance.titleWeight, instance.abstractWeight, instance.m, instance.n);  // 计算单词总数
        String str1 = lib.printChar();  // calculate Number of characters
        String str2 = lib.printWord();  // Count words
        String str3 = lib.printLines(); // Calculate number of lines
        String str4 = lib.printCountMostWord(instance.n);// Computing word frequency
        instance.writeFile(instance.outputFileName,str1 + "\r\n" + str2 + "\r\n" + str3 + "\r\n" + str4);
    }


    public void I(String str) {
        inputFileName = str;
    }

    public void O(String str) {
        outputFileName = str;
    }

    public void W(String str) {
        if (Integer.valueOf(str) == 0) {
            titleWeight = abstractWeight = 1;
        } else if (Integer.valueOf(str) == 1) {
            titleWeight = 10;
            abstractWeight = 1;
        }
    }


    public void M(String str) {
        m = Integer.valueOf(str);
    }

    public void N(String str) {
        n = Integer.valueOf(str);
    }


    // Reads the contents of the file and returns
    public String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileInputStream in = new FileInputStream(new File(fileName));
        int length = 0;
        byte[] buf = new byte[1024];
        while ((length = in.read(buf)) != -1) {
            sb.append(new String(buf, 0, length));
        }
        in.close();
        return sb.toString();
    }

    // Reads the contents of the file and writes to the file
    public void writeFile(String fileName, String content) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(f, false);
        fileWriter.write(content);
        fileWriter.flush();
    }

}
