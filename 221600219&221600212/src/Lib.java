import java.io.*;
import java.util.*;

public class Lib{

    // 行数
    private static int lineNum = 0;

    // 字符数
    private static int charNum = 0;

    // 单词数：至少四个英文字母开头，不区分大小写
    private static int wordNum = 0;

    // 单词集合：<单词, 数目>
    private static Map<String, Integer> wordMap  = new TreeMap<String, Integer>();;

    // 排序好的单词集合
    private static List<Map.Entry<String, Integer>> wordList = null;

    // 字节数组
    private static byte[] bytes = null;

    // 长度：字节数组长度
    private static int bytesLength = 0;

    public static int getCharNum(){return charNum;}
    public static int getWordNum(){return wordNum;}
    public static int getLineNum(){return lineNum;}
    public static List<Map.Entry<String, Integer>> getSortedList(){return wordList;}



    public static void main(String[] args) {
        System.out.println(isSeparator((byte)'&'));
    }

    /**
     * 初始化构造
     */
    public Lib(byte[] bytes){
        this.bytes = bytes;
        this.bytesLength = bytes.length;
    }

    /**
     * 功能：预处理
     *      将大写字母转为小写字母
     *      计算字节数组中的字符数、包括空字符、//r//n算作一个字符
     *      计算字节数组包含的行数
     *
     * 参数：byte[] bytes 字节数组
     */
    public static void preproccess(){
        // 计算字符数、行数
        for (int i = 0; i < bytesLength; i ++){
            // 预处理，大写字母统一转为小写字母
            //if (bytes[i] >= 65 && bytes[i] <= 90){
            //   bytes[i] += 32;
            //}
            if (bytes[i] == 13&&bytes[i+1]==10){
                // 计算行数
                if (checkLine(bytes, i)){
                    lineNum ++;
                }
				i++;
            }else{
                charNum ++;
            }
        }
        // 注意最后一行不以回车结尾的情况，同样算作一行
        if (bytes[bytesLength-1] != 13){
            lineNum ++;
        }
    }
	
	/**
     * 功能：过滤掉摘要中的非字母数字
     *
     * 参数：byte[] characters 摘要字符数组
     *
     * 返回：int 摘要单词数
     */
	 public static int filterAbstract(byte[] characters){
		Main.strAbstract="";
		int checkWordResult = -1;
		int length=characters.length;
        for (int i = 0; i < length; i ++){
            if (isLetter(characters[i])){
                checkWordResult = checkWord(characters, i, 4);
                if (checkWordResult > 0){
                    Main.strAbstract=Main.strAbstract+" "+subBytesToString(characters, i, checkWordResult);
                    wordNum ++;
                    // 跳转单词末尾
                    i = checkWordResult;
                } else{
                    // 不是单词，但是同样跳转到词末尾
                    i = - checkWordResult;
                }
                // System.out.println(checkWordResult);
            }
        }
		return wordNum;
	  }
	  
	 /**
     * 功能：过滤掉标题中的非字母数字
     *
     * 参数：byte[] characters 标题字符数组
     *
     * 返回：int 标题单词数
     */
	   public static int filterTitle(byte[] characters){
		Main.strTitle="";  
		int checkWordResult = -1;
		int length=characters.length;
        for (int i = 0; i < length; i ++){
            if (isLetter(characters[i])){
                checkWordResult = checkWord(characters, i, 4);
                if (checkWordResult > 0){
                    Main.strTitle =Main.strTitle+" "+subBytesToString(characters, i, checkWordResult);
                    wordNum ++;
                    // 跳转单词末尾
                    i = checkWordResult;
                } else{
                    // 不是单词，但是同样跳转到词末尾
                    i = - checkWordResult;
                }
                // System.out.println(checkWordResult);
            }
        }
		return wordNum;
	  }
	  
    /**
     * 功能：将单词装入集合、统计个数
     */
    public static void collectWord(String[] wordArray,int mode){
		int weight=1;
		int length=wordArray.length;
		String phraseWord="";
		//System.out.println("length:"+length);  
		boolean arriveEnd=false;
        for (int i = 0; i < length-Main.phraseWordNum+1; i ++){
			phraseWord=wordArray[i];
			for(int j=1;j<Main.phraseWordNum;++j){
				phraseWord=phraseWord+" "+wordArray[i+j];
			}
			if(mode==1&&Main.useWordWeight){
					weight=10;
			}
			else{
				weight=1;
			}
			if (wordMap.containsKey(phraseWord)){
				wordMap.put(phraseWord, wordMap.get(phraseWord)+weight);
			} 
			else{
				wordMap.put(phraseWord, weight);
			}	
		}   
    }

    /**
     * 功能：按照单词频率排序
     */
    public static void sortWordMap(){
        wordList = new ArrayList<Map.Entry<String,Integer>>(wordMap.entrySet());
        Collections.sort(wordList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> word1, Map.Entry<String, Integer> word2) {
                return word2.getValue() - word1.getValue();
            }
        });
    }

    /**
     * 功能：取出字节数组中的某一段转成String返回
     *
     * 参数：byte[] bytes 字节数组
     *      int start 开始下标
     *      int end 截止下标
     *
     * 返回：String aWordString 截取转成的字符串
     */
    static String subBytesToString(byte[] bytes, int start, int end){
        if (end >= start){
            byte[] aWordByte = new byte[end-start];
            System.arraycopy(bytes, start, aWordByte, 0, end-start);
            return new String(aWordByte);
        }
        return null;
    }

    /**
     * 功能：判断该换行字符所在行是否是非空白行
     *
     * 参数：byte[] bytes 字节数组
     *      int lineEnd 换行符下标（行末尾）
     *
     * 返回：boolean true 非空行
     *      boolean fasle 是空行
     */
    static boolean checkLine(byte[] bytes, int lineEnd){
        int notBlankCharNum = 0;
        for (int i = lineEnd-1; i >= 0; i --){
            if (bytes[i] == 10){
                // 遇到前一行返回
                break;
            } else if (!isBlankChar(bytes[i])){
                // 当前字母不是空格或制表符
                notBlankCharNum ++;
            }
        }
        return (notBlankCharNum > 0);
    }

    /**
     * 功能：判断byte字节是否是字母
     *
     * 参数：byte b 字节
     *
     * 返回：boolean true 是字母
     *      boolean false 不是字母
    **/
    static boolean isLetter(byte b){
        return (b >= 97 && b <= 122) || (b >= 65 && b <= 90);
    }

    /**
     * 功能：判断Byte字节是否是数字
     *
     * 参数：byte b 字节
     *
     * 返回：boolean true 是数字
     *      boolean false 不是数字
     */
    static boolean isNum(byte b){
        return (b >= 48 && b <= 57);
    }

    /**
     * 功能：判断byte字节是否是空白字符
     *
     * 参数：byte b 字节
     *
     * 返回：boolean true 是空白字符
     *      boolean false 不是空白字符
    **/
    static boolean isBlankChar(byte b){
        // return (b == 32 || b == 10 || b == 9 || b == 13);
        return (b <= 32);
    }

    /**
     * 功能：判断Byte字节是否是分隔符
     *
     * 参数：byte b 字节
     *
     * 返回：boolean true 是分隔符
     *      boolean false 不是分隔符
     */
    static boolean isSeparator(byte b){
        return !(isLetter(b)|| isNum(b));
    }

    /**
     * 功能：判断从某个下标开始的一段长度是否是单词
     *
     * 参数：byte[] bytes 字节数组
     *      int start 开始下标
     *      int minWordLength 满足最小需求的开头字母数
     *
     * 返回：int < 0 不是单词，负的词末尾分隔符的下标
     *      int > 0 是单词，单词末尾分隔符的下标
    **/
    static int checkWord(byte[] bytes, int start, int minWordLength){
        int bytesLength = bytes.length;
        int i = start;
        int checkWordResult = 0;

        if (start > 0 && ! isSeparator(bytes[start-1])){
            checkWordResult = -1;
        } else{
            for (; i < start + minWordLength && i < bytesLength; i++){
                // 满足最小需求的开头字母数
                if (! isLetter(bytes[i])){
                    checkWordResult = -2;
                    break;
                }
            }
            // 已到结尾，不满足最小开头字母数
            if (i == bytesLength && i - start < minWordLength){
                checkWordResult = -3;
            }
        }
        for (; i < bytesLength; i++){
            // 遍历到词结束，返回词末尾的下标
            if (isSeparator(bytes[i])){
                // 字符不是分隔符
                break;
            }
        }

        return checkWordResult < 0 ? -i : i;
    }
}
