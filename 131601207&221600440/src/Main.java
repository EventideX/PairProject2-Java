import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Main {
	public static void main(String args[]) throws IOException {
		if (args.length > 0) {
			Analysis analysis = new Analysis(args);
			analysis.analyse();
			String path = analysis.getInputFilePath();
			FileUtil fileutil = new FileUtil(path);// 文件过滤为字符串
			String titletext = fileutil.getTitleText();// 提取标题文本
			Counter titleCounter = new Counter(titletext);// 利用标题文本创建标题文本统计分析
			String abstracttext = fileutil.getAbstractText();// 提取摘要文本
			Counter abstractCounter = new Counter(abstracttext);// 利用摘要文本创建摘要文本统计分析
			titleCounter.charCount();// 统计标题字符数
			abstractCounter.charCount();// 统计摘要字符数
			titleCounter.wordCount();// 统计标题单词
			abstractCounter.wordCount();// 统计摘要单词
			String result = new String();// 结果字符串
			result = "characters:" + (titleCounter.getCharCnt() + abstractCounter.getCharCnt()) + "\r\n";
			result += "words:" + (titleCounter.getWordCnt() + abstractCounter.getWordCnt()) + "\r\n";
			result += "lines:" + fileutil.getLineCnt() + "\r\n";
			// 单词词频统计
			int weight = analysis.getWeight();// 获取权重计算方式标志位
			int phraseSize = analysis.getPhraseSize();
			int n = analysis.getResultCnt();
			if (phraseSize == 1) {// 进行单词词频统计
				titleCounter.weightCount(weight, "title", phraseSize);// 统计标题单词词频
				abstractCounter.weightCount(weight, "abstract", phraseSize);// 统计摘要单词词频
				titleCounter.mergeMap(abstractCounter.getWeightMap());// 将摘要词频表合并到标题词频表中
				Map<String, Integer> mergeMap = titleCounter.getWeightMap();
				// 输出单词词频
				Set<Entry<String, Integer>> entry = mergeMap.entrySet();

				if (entry.size() <= n) {
					for (Entry<String, Integer> ent : entry) {
						result += "<" + ent.getKey() + ">:" + ent.getValue() + "\r\n";
					}
				} else {
					int i = 0;
					for (Entry<String, Integer> ent : entry) {
						result += "<" + ent.getKey() + ">:" + ent.getValue() + "\r\n";
						i++;
						if (i >= n)
							break;
					}
				}
			} else {// 词组词频统计
				titleCounter.phraseCount(phraseSize);//收集统计标题词组
				abstractCounter.phraseCount(phraseSize);//收集统计摘要词组
				titleCounter.weightCount(weight, "title", phraseSize);//标题词组词频统计
				abstractCounter.weightCount(weight, "abstract", phraseSize);//摘要词组词频统计
				titleCounter.mergeMap(abstractCounter.getWeightMap());// 将摘要词频表合并到标题词频表中
				Map<String, Integer> mergePhraseMap = titleCounter.getWeightMap();
				Set<Entry<String, Integer>> phraseEntry = mergePhraseMap.entrySet();
				if (phraseEntry.size() <= n) {
					for (Entry<String, Integer> ent : phraseEntry) {
						result += "<" + ent.getKey() + ">:" + ent.getValue() + "\r\n";
					}
				} else {
					int i = 0;
					for (Entry<String, Integer> ent : phraseEntry) {
						result += "<" + ent.getKey() + ">:" + ent.getValue() + "\r\n";
						i++;
						if (i >= n)
							break;
					}
				}
			}
			String outputfilepath = analysis.getOutputFilePath();
			fileutil.resultToFile(result, outputfilepath);
		}
	}
}
