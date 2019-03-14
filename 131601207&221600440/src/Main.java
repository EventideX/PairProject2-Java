import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Main {
	public static void main(String args[]) throws IOException {
		if (args.length > 0) {
			Analysis analysis = new Analysis(args);
			analysis.analyse();
			String path = analysis.getInputFilePath();
			FileUtil fileutil = new FileUtil(path);// �ļ�����Ϊ�ַ���
			String titletext = fileutil.getTitleText();// ��ȡ�����ı�
			Counter titleCounter = new Counter(titletext);// ���ñ����ı����������ı�ͳ�Ʒ���
			String abstracttext = fileutil.getAbstractText();// ��ȡժҪ�ı�
			Counter abstractCounter = new Counter(abstracttext);// ����ժҪ�ı�����ժҪ�ı�ͳ�Ʒ���
			titleCounter.charCount();// ͳ�Ʊ����ַ���
			abstractCounter.charCount();// ͳ��ժҪ�ַ���
			titleCounter.wordCount();// ͳ�Ʊ��ⵥ��
			abstractCounter.wordCount();// ͳ��ժҪ����
			String result = new String();// ����ַ���
			result = "characters:" + (titleCounter.getCharCnt() + abstractCounter.getCharCnt()) + "\r\n";
			result += "words:" + (titleCounter.getWordCnt() + abstractCounter.getWordCnt()) + "\r\n";
			result += "lines:" + fileutil.getLineCnt() + "\r\n";
			// ���ʴ�Ƶͳ��
			int weight = analysis.getWeight();// ��ȡȨ�ؼ��㷽ʽ��־λ
			int phraseSize = analysis.getPhraseSize();
			int n = analysis.getResultCnt();
			if (phraseSize == 1) {// ���е��ʴ�Ƶͳ��
				titleCounter.weightCount(weight, "title", phraseSize);// ͳ�Ʊ��ⵥ�ʴ�Ƶ
				abstractCounter.weightCount(weight, "abstract", phraseSize);// ͳ��ժҪ���ʴ�Ƶ
				titleCounter.mergeMap(abstractCounter.getWeightMap());// ��ժҪ��Ƶ��ϲ��������Ƶ����
				Map<String, Integer> mergeMap = titleCounter.getWeightMap();
				// ������ʴ�Ƶ
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
			} else {// �����Ƶͳ��
				titleCounter.phraseCount(phraseSize);//�ռ�ͳ�Ʊ������
				abstractCounter.phraseCount(phraseSize);//�ռ�ͳ��ժҪ����
				titleCounter.weightCount(weight, "title", phraseSize);//��������Ƶͳ��
				abstractCounter.weightCount(weight, "abstract", phraseSize);//ժҪ�����Ƶͳ��
				titleCounter.mergeMap(abstractCounter.getWeightMap());// ��ժҪ��Ƶ��ϲ��������Ƶ����
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
