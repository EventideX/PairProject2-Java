import java.io.*;
import java.util.*;

public class Lib{

    // ����
    private static int lineNum = 0;

    // �ַ���
    private static int charNum = 0;

    // �������������ĸ�Ӣ����ĸ��ͷ�������ִ�Сд
    private static int wordNum = 0;

    // ���ʼ��ϣ�<����, ��Ŀ>
    private static Map<String, Integer> wordMap  = new TreeMap<String, Integer>();;

    // ����õĵ��ʼ���
    private static List<Map.Entry<String, Integer>> wordList = null;

    // �ֽ�����
    private static byte[] bytes = null;

    // ���ȣ��ֽ����鳤��
    private static int bytesLength = 0;

    public static int getCharNum(){return charNum;}
    public static int getWordNum(){return wordNum;}
    public static int getLineNum(){return lineNum;}
    public static List<Map.Entry<String, Integer>> getSortedList(){return wordList;}



    public static void main(String[] args) {
        System.out.println(isSeparator((byte)'&'));
    }

    /**
     * ��ʼ������
    **/
    public Lib(byte[] bytes){
        this.bytes = bytes;
        this.bytesLength = bytes.length;
    }

    /**
     * ���ܣ�Ԥ����
     *      ����д��ĸתΪСд��ĸ
     *      �����ֽ������е��ַ������������ַ���//r//n����һ���ַ�
     *      �����ֽ��������������
     *
     * ������byte[] bytes �ֽ�����
     */
    public static void preproccess(){
        // �����ַ���������
        for (int i = 0; i < bytesLength; i ++){
            // Ԥ������д��ĸͳһתΪСд��ĸ
            //if (bytes[i] >= 65 && bytes[i] <= 90){
            //   bytes[i] += 32;
            //}
            if (bytes[i] == 13&&bytes[i+1]==10){
                // ��������
                if (checkLine(bytes, i)){
                    lineNum ++;
                }
				i++;
            }else{
                charNum ++;
            }
        }
        // ע�����һ�в��Իس���β�������ͬ������һ��
        if (bytes[bytesLength-1] != 13){
            lineNum ++;
        }
    }
	/**
     * ���ܣ����˵�ժҪ�еķ���ĸ����
     *
     * ������byte[] characters ժҪ�ַ�����
     *
     * ���أ�int ժҪ������
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
                    // ��ת����ĩβ
                    i = checkWordResult;
                } else{
                    // ���ǵ��ʣ�����ͬ����ת����ĩβ
                    i = - checkWordResult;
                }
                // System.out.println(checkWordResult);
            }
        }
		return wordNum;
	  }
	  /**
     * ���ܣ����˵������еķ���ĸ����
     *
     * ������byte[] characters �����ַ�����
     *
     * ���أ�int ���ⵥ����
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
                    // ��ת����ĩβ
                    i = checkWordResult;
                } else{
                    // ���ǵ��ʣ�����ͬ����ת����ĩβ
                    i = - checkWordResult;
                }
                // System.out.println(checkWordResult);
            }
        }
		return wordNum;
	  }
    /**
     * ���ܣ�������װ�뼯�ϡ�ͳ�Ƹ���
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
     * ���ܣ����յ���Ƶ������
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
     * ���ܣ�ȡ���ֽ������е�ĳһ��ת��String����
     *
     * ������byte[] bytes �ֽ�����
     *      int start ��ʼ�±�
     *      int end ��ֹ�±�
     *
     * ���أ�String aWordString ��ȡת�ɵ��ַ���
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
     * ���ܣ��жϸû����ַ��������Ƿ��Ƿǿհ���
     *
     * ������byte[] bytes �ֽ�����
     *      int lineEnd ���з��±꣨��ĩβ��
     *
     * ���أ�boolean true �ǿ���
     *      boolean fasle �ǿ���
     */
    static boolean checkLine(byte[] bytes, int lineEnd){
        int notBlankCharNum = 0;
        for (int i = lineEnd-1; i >= 0; i --){
            if (bytes[i] == 10){
                // ����ǰһ�з���
                break;
            } else if (!isBlankChar(bytes[i])){
                // ��ǰ��ĸ���ǿո���Ʊ��
                notBlankCharNum ++;
            }
        }
        return (notBlankCharNum > 0);
    }

    /**
     * ���ܣ��ж�byte�ֽ��Ƿ�����ĸ
     *
     * ������byte b �ֽ�
     *
     * ���أ�boolean true ����ĸ
     *      boolean false ������ĸ
    **/
    static boolean isLetter(byte b){
        return (b >= 97 && b <= 122) || (b >= 65 && b <= 90);
    }

    /**
     * ���ܣ��ж�Byte�ֽ��Ƿ�������
     *
     * ������byte b �ֽ�
     *
     * ���أ�boolean true ������
     *      boolean false ��������
     */
    static boolean isNum(byte b){
        return (b >= 48 && b <= 57);
    }

    /**
     * ���ܣ��ж�byte�ֽ��Ƿ��ǿհ��ַ�
     *
     * ������byte b �ֽ�
     *
     * ���أ�boolean true �ǿհ��ַ�
     *      boolean false ���ǿհ��ַ�
    **/
    static boolean isBlankChar(byte b){
        // return (b == 32 || b == 10 || b == 9 || b == 13);
        return (b <= 32);
    }

    /**
     * ���ܣ��ж�Byte�ֽ��Ƿ��Ƿָ���
     *
     * ������byte b �ֽ�
     *
     * ���أ�boolean true �Ƿָ���
     *      boolean false ���Ƿָ���
     */
    static boolean isSeparator(byte b){
        return !(isLetter(b)|| isNum(b));
    }

    /**
     * ���ܣ��жϴ�ĳ���±꿪ʼ��һ�γ����Ƿ��ǵ���
     *
     * ������byte[] bytes �ֽ�����
     *      int start ��ʼ�±�
     *      int minWordLength ������С����Ŀ�ͷ��ĸ��
     *
     * ���أ�int < 0 ���ǵ��ʣ����Ĵ�ĩβ�ָ������±�
     *      int > 0 �ǵ��ʣ�����ĩβ�ָ������±�
    **/
    static int checkWord(byte[] bytes, int start, int minWordLength){
        int bytesLength = bytes.length;
        int i = start;
        int checkWordResult = 0;

        if (start > 0 && ! isSeparator(bytes[start-1])){
            checkWordResult = -1;
        } else{
            for (; i < start + minWordLength && i < bytesLength; i++){
                // ������С����Ŀ�ͷ��ĸ��
                if (! isLetter(bytes[i])){
                    checkWordResult = -2;
                    break;
                }
            }
            // �ѵ���β����������С��ͷ��ĸ��
            if (i == bytesLength && i - start < minWordLength){
                checkWordResult = -3;
            }
        }
        for (; i < bytesLength; i++){
            // �������ʽ��������ش�ĩβ���±�
            if (isSeparator(bytes[i])){
                // �ַ����Ƿָ���
                break;
            }
        }

        return checkWordResult < 0 ? -i : i;
    }
}