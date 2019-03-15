package wordcount;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map.Entry;

public class Main {

	public static String inputFilePath;// 读入文件的存储路径
	public static String outputFilePath;// 生成文件的存储路径

	public static String diffWeight;// 是否采用不同权重
	public static int titleWeight;// Title的单词权重
	public static int abstractWeight;// Abstract的单词权重

	public static int wordLength;// 设定统计的词组长度

	public static int topN = 10;// 设定输出的单词数量,默认为10

	public static void main(String[] args) {
		// 参数处理
		for (int i = 0; i + 1 < args.length; i++) {
			switch (args[i]) {
			case "-i":// 获取读入文件路径
				inputFilePath = args[i + 1];
				break;
			case "-o":// 获取生成文件路径
				outputFilePath = args[i + 1];
				break;
			case "-w":// 获取权重设置
				diffWeight = args[i + 1];
				abstractWeight = 1;
				if (diffWeight.equals("0")) {// 表示属于Title、Abstract的单词权重相同均为 1
					titleWeight = 1;
				} else if (diffWeight.equals("1")) {// 表示属于Title的单词权重为10，属于Abstract单词权重为1
					titleWeight = 10;
				}
				break;
			case "-m":// 获取统计词组大小
				wordLength = Integer.valueOf(args[i + 1]);
				break;
			case "-n":// 获取统计词组个数
				topN = Integer.valueOf(args[i + 1]);
				break;
			default:
				break;
			}
		}

		try {
			/* 输出流重定向，用于将统计结果输出到生成文件中 */
			System.setOut(new PrintStream(outputFilePath));

			/* 接收命令行参数文件路径，构造Main类 */
			WordCount2 wordCount2 = new WordCount2(inputFilePath);
			System.out.println("characters: "+wordCount2.countChar());// 统计文件的字符数
			System.out.println("words: "+wordCount2.countWord());// 统计文件的单词数 
			System.out.println("lines: "+wordCount2.countLine());// 统计文件的有效行数
			
			// 统计各个单词的出现次数 KEY-VALUE映射
			wordCount2.orderWord(titleWeight,abstractWeight);
			// 输出频率最高的n个单词，频率相同输出字典序靠前的单词，String key排序
			Iterator<Entry<String, Integer>> iterator = wordCount2.orderedWordList.iterator();
			for (int i=0; i< topN &&iterator.hasNext();i++) {
			    Entry<String, Integer> Entry = iterator.next();
			    System.out.println("<"+Entry.getKey()+">: "+ Entry.getValue());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
