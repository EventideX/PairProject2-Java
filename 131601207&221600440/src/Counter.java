
import java.util.*;
import java.util.regex.*;

class Counter {
	private int charCnt = 0;
	private int wordCnt = 0;
	private Map<String, Integer> cntMap = new HashMap<String, Integer>(); // Map<单词，数量>
	private Map<String, Integer> weightMap = new HashMap<String, Integer>(); // Map<单词，词频>
	private Map<String, Integer> phraseMap = new HashMap<String, Integer>(); // Map<词组，词频>
	private String text;

	// 记录分隔符
	public int getCharCnt() {
		return charCnt;
	}

	public int getWordCnt() {
		return wordCnt;
	}

	public Map<String, Integer> getCntMap() {
		return cntMap;
	}

	public Map<String, Integer> getPhraseMap() {
		return phraseMap;
	}

	public Map<String, Integer> getWeightMap() {
		return weightMap;
	}

	public String getText() {
		return text;
	}

	public Counter(String text) {
		text = text.replaceAll("\r\n", "\n");
		this.text = text.toLowerCase();
	}

	public void charCount() {
		String charRegex = "[\\p{ASCII}]";// [\p{ASCII}] [\\x00-\\x7F]
		Pattern p = Pattern.compile(charRegex);
		Matcher m = p.matcher(text);
		while (m.find()) {
			charCnt++;
		}
	}

	public void phraseCount(int size) {
		String splittext = text.replaceAll("[a-z0-9]", "0");// 将字母数字替换为0
		String splits[] = splittext.split("[0]+");// 剔除0，得到单词跟着的分隔符
		String splitRegex = "[^a-z0-9]";// 分隔符
		String lowerText = text.replaceAll(splitRegex, " ");// 将非字母数字替换为空格
		String words[] = lowerText.split("\\s+");// 利用空白分割所有单词
		String wordRegex = "[a-z]{4,}[a-z0-9]*";// 单词匹配正则表达式
		for (int i = 0; i < words.length; i++) {
			boolean canPhrase = true;
			if (i + size <= words.length) {//当前单词的第后size个单词不超过单词总数
				for (int j = i; j < i + size; j++) {
					if (!Pattern.matches(wordRegex, words[j])) {//单词的后size个单词均要符合单词定义
						canPhrase = false;
						break;
					}
				}
				for (int k = i + 1; k < i + size; k++) {
					if (Pattern.matches("\n", splits[k])) {//不同篇论文的title与abstract不能组成词组，用回车符区分
						canPhrase = false;
					}
				}
			} else
				canPhrase = false;
			if (canPhrase) {
				String phrase = new String();
				for (int m = 0; m < size; m++) {
					int pos = i + m;
					if (m == size - 1)
						phrase += words[pos];
					else
						phrase += (words[pos] + splits[pos + 1]);
				}
				Integer num = phraseMap.get(phrase);
				if (num == null || num == 0) {
					phraseMap.put(phrase, 1);
				} else if (num > 0) {
					phraseMap.put(phrase, num + 1);
				}
			}
		}
	}

	public void wordCount() {
		String splitRegex = "[^a-z0-9]";// 分隔符
		String lowerText = text.replaceAll(splitRegex, " ");// 将非字母数字替换为空格
		String words[] = lowerText.split("\\s+");// 利用空白分割所有单词
		String wordRegex = "[a-z]{4,}[a-z0-9]*";// 单词匹配正则表达式
		for (int i = 0; i < words.length; i++) {
			Pattern p = Pattern.compile(wordRegex);
			Matcher m = p.matcher(words[i]);
			if (m.find()) {// 符合单词定义
				wordCnt++;
				Integer num = cntMap.get(words[i]);
				if (num == null || num == 0) {
					cntMap.put(words[i], 1); // map中无该单词，数量置1
				} else if (num > 0) {
					cntMap.put(words[i], num + 1); // map中有该单词，数量加1
				}
			}
		}
		cntMap = sortMap(cntMap);
	}

	// 按键值对排序
	public <K extends Comparable<? super K>, V extends Comparable<? super V>> Map<K, V> sortMap(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int re = o2.getValue().compareTo(o1.getValue());
				if (re != 0)
					return re;
				else
					return o1.getKey().compareTo(o2.getKey());
			}
		});
		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	// 权重计算
	public void weightCount(int weight, String type, int Size) {
		if (Size == 1) {// 单词词频计算
			if (weight == 1) {
				if (type.equals("title")) {
					for (Map.Entry<String, Integer> word : cntMap.entrySet()) {
						weightMap.put(word.getKey(), word.getValue() * 10);
					}
				}
				if (type.equals("abstract")) {
					for (Map.Entry<String, Integer> word : cntMap.entrySet()) {
						weightMap.put(word.getKey(), word.getValue());
					}
				}
			} else if (weight == 0) {
				for (Map.Entry<String, Integer> word : cntMap.entrySet()) {
					weightMap.put(word.getKey(), word.getValue());
				}
			} else {
				System.out.println("w参数只能与数字 0|1 搭配使用");
			}
		} else {// 词组词频计算
			if (weight == 1) {
				if (type.equals("title")) {
					for (Map.Entry<String, Integer> phrase : phraseMap.entrySet()) {
						weightMap.put(phrase.getKey(), phrase.getValue() * 10);
					}
				}
				if (type.equals("abstract")) {
					for (Map.Entry<String, Integer> phrase : phraseMap.entrySet()) {
						weightMap.put(phrase.getKey(), phrase.getValue());
					}
				}
			} else if (weight == 0) {
				for (Map.Entry<String, Integer> phrase : phraseMap.entrySet()) {
					weightMap.put(phrase.getKey(), phrase.getValue());
				}
			} else {
				System.out.println("w参数只能与数字 0|1 搭配使用");
			}
		}
	}

	// 合并map，value值叠加
	public void mergeMap(Map<String, Integer> map) {
		Set<String> set = map.keySet();
		for (String key : set) {
			if (weightMap.containsKey(key)) {
				weightMap.put(key, weightMap.get(key) + map.get(key));
			} else {
				weightMap.put(key, map.get(key));
			}
		}
		weightMap = sortMap(weightMap);
	}

}
