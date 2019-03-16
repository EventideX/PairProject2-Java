import bean.CommandLine;

import bean.Word;
import unitl.AdvancedWordCount;
import unitl.Command;
import unitl.IOUtils;

public class Main {
	public static void main(String[] args) {
		Command command = new Command();
		CommandLine commandLine = command.ParseCommand(args);
		AdvancedWordCount advancedWordCount = new AdvancedWordCount();
		StringBuffer stringBuffer = new StringBuffer();
		String inputFileName = commandLine.getInputFileName();
		String outputFileName = commandLine.getoutputFileName();
		
		//执行命令
		if(commandLine.getWordGroup()==-1) {
			boolean isWeight = false;
			if(commandLine.getWeight()==1) {
				isWeight = true;
			}
			stringBuffer.append("characters: " + advancedWordCount.characterCount(inputFileName) + "\r\n");
			stringBuffer.append("words: " + advancedWordCount.wordCount(inputFileName) + "\r\n");
			stringBuffer.append("lines: " + advancedWordCount.lineCount(inputFileName) + "\r\n");
			Word word[] = advancedWordCount.topKWordWeighting(inputFileName, commandLine.getWordFrequency(), isWeight);
			for(int i = word.length - 1; i >= 0; i--) {
				stringBuffer.append("<"+word[i].getKey()+">: "+word[i].getCountNum()+"\r\n");
			}
		}else {
			boolean isWeight = false;
			if(commandLine.getWeight()==1) {
				isWeight = true;
			}
			stringBuffer.append("characters: " + advancedWordCount.characterCount(inputFileName) + "\r\n");
			stringBuffer.append("words: " + advancedWordCount.wordCount(inputFileName) + "\r\n");
			stringBuffer.append("lines: " + advancedWordCount.lineCount(inputFileName) + "\r\n");
			Word word[] = advancedWordCount.topKWordsWeighting(inputFileName, commandLine.getWordGroup(), commandLine.getWordFrequency(), isWeight);
			for(int i = word.length - 1; i >= 0; i--) {
				stringBuffer.append("<"+word[i].getKey()+">: "+word[i].getCountNum()+"\r\n");
			}
		}

		//写入到文件
		IOUtils.writeFile(stringBuffer.toString(),outputFileName);

	}
}
