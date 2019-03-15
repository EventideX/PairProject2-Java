import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		int count_char = 0;
		int count_rows = 0;
		int count_words_num = 0;
		int phraseLength = 3;  //一个词组的包含的单词数
		boolean is_weight=false;  //是否加权重
		int numbers=11;
		
		//输入与输出地址
		String in_name = "test.txt";
		String out_name = "result.txt";
		for(int i=0;i<args.length;i+=2) {
			switch(args[i]) {
				case "-i":
					
					in_name=args[i+1];//更改输入文件路径
					break;
				case "-o":
					
					out_name=args[i+1];//更改输出文件路径
					break;
				case "-w":
					//由输入参数选择权重占比
					if(Integer.parseInt(args[i+1])==0) {
						is_weight=false;
					}else {
						is_weight=true;
					}
					break;
				case "-m":
					/*统计文件夹中指定长度的词组的词频
					 * 出现-m时只统计词组词频
					 * 未出现-m时只统计单词词频
					 * */
					phraseLength=Integer.parseInt(args[i+1]);
					break;
				case "-n":
					/*用户指定输出前number多的单词(词组)与其频数
					 * 表示输出频数最多的前 [number] 个单词(词组)
					 * */
					numbers=Integer.parseInt(args[i+1]);
					break;
				default:
						System.out.println("指令错误！！！");
			}
		}
		
		Map<String, String> wordsMap;
		Map<String, String> wordsFrequency;

		String new_file = lib.rewrite_Txt(in_name);//去掉论文爬取结果文件中的“Title: ”、“Abstract: ”、论文编号及其紧跟着的换行符以及分隔论文的两个换行符并写入一个新文件“new_Txt.txt”
		
		count_char = lib.count_Characters(new_file)-1;//统计文件的字符数
		
		wordsMap = lib.count_Words(new_file);//从文件提取单词并且统计单词出现次数和单词总数放入Map
		count_words_num = Integer.parseInt(wordsMap.get("count_words_num"));//统计文件的单词数
		
		count_rows = lib.count_Lines(new_file);//统计文件的行数
		
		wordsFrequency=lib.count_Word_Frequency(in_name, wordsMap,is_weight);//统计单词的权重词频
		
		
		
		if( phraseLength > 0 ) {
			Map<String, String> phraseFrequency = lib.count_Phrase_frequency(in_name, phraseLength, is_weight);//统计词组的权重词频
			lib.writeToFile(phraseFrequency,count_char,count_words_num,count_rows,out_name,numbers);//输出至文件中
		}
		else
			lib.writeToFile(wordsFrequency,count_char,count_words_num,count_rows,out_name,numbers);//输出至文件中
	}
	
	


}