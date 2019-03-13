import java.util.*;

public class Main {

	public static void main(String[] args) {
		/*是否加权重*/
		boolean isWeight=false;       
		int countChar;
		int countLinnes;
		int phraseLength;
		int numbers=10;
		/*输入与输出地址*/
		String in_name = "test.txt";
		String out_name = "result.txt";
		for(int i=0;i<args.length;i+=2) {
			switch(args[i]) {
				case "-i":
					/*更改输入地址*/
					in_name=args[i+1];
					break;
				case "-o":
					/*更改输出地址*/
					out_name=args[i+1];
					break;
				case "-w":
					/*由输入参数选择权重占比*/
					if(Integer.parseInt(args[i+1])==0) {
						isWeight=false;
					}else {
						isWeight=true;
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
			}
		}
		Map<String, String> wordsMap;
		Map<String, String> wordsFrequency;
		/*输出文件的字符数*/
		countChar = wordCount.count_Characters(in_name);
		/*统计单词出现次数和单词总数*/
		wordsMap = wordCount.count_Words(in_name);
		/*统计行数*/
		countLinnes = wordCount.count_Lines(in_name);
		/*初步统计的单词(或词组)和词频的Map、数字选项*/
		wordsFrequency=wordCount.count_Word_Frequency(in_name, wordsMap,isWeight);
		/*输出至文件中*/
		wordCount.writeToFile(countChar,wordsFrequency,countLinnes,out_name,numbers);
	}
	
	


}