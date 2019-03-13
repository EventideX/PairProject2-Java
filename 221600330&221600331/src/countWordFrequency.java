import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class countWordFrequency {
	
	/*
	 * 按权重统计文件的单词(或词组)词频(0表示属于 Title、Abstract 的单词(或词组)权重相同均为 1;
	 * 1 表示属于 Title 的单词(或词组)权重为10，属于Abstract 单词(或词组)权重为1)
	 * 输入：初步统计的单词(或词组)和词频的Map、数字选项
	 * 输出：按权重统计的单词(或词组)和词频的Map
	 */
	public static Map<String, String> count_Word_Frequency(String input_file_path, Map<String, String> wordsMap, int choose) {

		try {
			File input_file = new File(input_file_path);
			InputStreamReader reader;
			BufferedReader bufferedReader;
			String str = null;

			if (input_file.isFile() && input_file.exists())// 判断文件是否存在
			{
				reader = new InputStreamReader(new FileInputStream(input_file));
				bufferedReader = new BufferedReader(reader);

				// 先去除wordsMap初步统计中“title: ”和“abstract: ”所占用的词频
				while ((str = bufferedReader.readLine()) != null) {
					str = str.toLowerCase();
					if (str.contains("title: ")) {
						// title的词频减一
						int value = Integer.parseInt(wordsMap.get("title"));
						value--;
						wordsMap.put("title", value + "");

						// 总单词个数减一
						int n = Integer.parseInt(wordsMap.get("count_words"));
						n--;
						wordsMap.put("count_words", n + "");
					}
					if (str.contains("abstract: ")) {
						// abstract的词频减一
						int value = Integer.parseInt(wordsMap.get("abstract"));
						value--;
						wordsMap.put("abstract", value + "");

						// 总单词个数减一
						int n = Integer.parseInt(wordsMap.get("count_words"));
						n--;
						wordsMap.put("count_words", n + "");
					}
				}
				if (wordsMap.get("title").equals("0"))
					wordsMap.remove("title");
				if (wordsMap.get("abstract").equals("0"))
					wordsMap.remove("abstract");
				
				
				if (choose == 0) {
					// 权重1:1
					reader.close();
					return wordsMap;
				} else if (choose == 1) {
					// 权重10:1
					while ((str = bufferedReader.readLine()) != null) {
						str = str.toLowerCase();
						if (str.contains("title: ")) {
							for (int i = 5; i < (str.length() - 4); i++) {
								if (('a' <= str.charAt(i - 1) && str.charAt(i - 1) <= 'z')
										|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// 如果前一个字符是字符或数字
									continue;
								}
								if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
									if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
										if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
											if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {
												// 找到单词
												int j;
												for (j = i + 4; j < str.length(); j++) {
													// 看单词是否结束
													if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
														if (48 > str.charAt(j) || str.charAt(j) > 57)
															// 不是字符或数字，单词结束
															break;
													}
												}
												String temp = str.substring(i, j);// 截取字符串索引号i到j区域（包括i，不包括j）
												// 词频加9或第一次录入词频10
												if (wordsMap.containsKey(temp)) {
													int n = Integer.parseInt(wordsMap.get(temp));
													n+=9;
													wordsMap.put(temp, n + "");
												} else
													wordsMap.put(temp, "10");
												i = j;
											} else
												i = i + 3;
										} else
											i = i + 2;
									} else
										i = i + 1;
								}
							}
						}
					}
					reader.close();
					return wordsMap;
				} else {
					reader.close();
					System.out.println("命令错误！！");
					return null;
				}
			} else {
				System.out.println("找不到指定的文件");
				return null;
			}
		} catch (IOException e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return null;
		}

	}
}
