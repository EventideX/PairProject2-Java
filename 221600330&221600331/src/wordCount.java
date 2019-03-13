import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class wordCount {

	/*
	 * 将统计结果输出到文件"result.txt" 
	 * 输入：统计的文件字符总数、统计单词和词频的Map、统计的文件有效行数
	 * 输出：无
	 */
	public static void writeToFile(int countChar, Map<String, String> wordsMap, int countLinnes,String out_name,int numbers) {
		try {
			int countWords = Integer.parseInt(wordsMap.get("count_words"));
			File output_file = new File(out_name);
			OutputStreamWriter writer;
			writer = new OutputStreamWriter(new FileOutputStream(output_file));
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write("characters: " + countChar + "\r\n");
			bufferedWriter.write("words: " + countWords + "\r\n");
			bufferedWriter.write("lines: " + countLinnes + "\r\n");
			bufferedWriter.flush();

			System.out.println("characters: " + countChar);
			System.out.println("words: " + countWords);
			System.out.println("lines: " + countLinnes);

			if (countWords <= 0) {
				writer.close();
				return;
			}

			
			while (countWords > 0 && numbers-- > 0) {
				String temp = "";
				int maxNum = -1;
				Iterator<Map.Entry<String, String>> iterator = wordsMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					if (Integer.parseInt(entry.getValue()) >= maxNum && !entry.getKey().equals("count_words")) {
						if (Integer.parseInt(entry.getValue()) == maxNum && entry.getKey().compareTo(temp) > 0) {// 相同频率的单词选字典序靠前的单词
							continue;
						}
						temp = entry.getKey();
						maxNum = Integer.parseInt(entry.getValue());
					}
				}
				bufferedWriter.write("<" + temp + ">: " + maxNum + "\r\n");
				bufferedWriter.flush();
				System.out.println("<" + temp + ">: " + maxNum);

				wordsMap.remove(temp);
				//countWords = countWords - maxNum;
			}

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 统计文件的有效行数包含非空白字符的行） 
	 * 输入：文件路径 
	 * 输出：文件的有效行数（包含非空白字符的行）
	 */
	public static int count_Lines(String filePath) {
		try {
			File input_file = new File(filePath);
			if (input_file.isFile() && input_file.exists())// 判断文件是否存在
			{
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;
				int countLinnes = 0;
				while ((str = bufferedReader.readLine()) != null) {
					for (int i = 0; i < str.length(); i++) {
						if (32 < str.charAt(i) && str.charAt(i) < 127) {
							countLinnes++;
							break;
						}
					}
				}
				reader.close();
				return countLinnes;
			} else {
				System.out.println("找不到指定的文件");
				return -1;
			}
		} catch (IOException e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return -1;
		}
	}
	
	/*
	 * 按权重统计文件的单词(或词组)词频(0表示属于 Title、Abstract 的单词(或词组)权重相同均为 1; 1 表示属于 Title
	 * 的单词(或词组)权重为10，属于Abstract 单词(或词组)权重为1) 输入：初步统计的单词(或词组)和词频的Map、数字选项
	 * 输出：按权重统计的单词(或词组)和词频的Map
	 */
	public static Map<String, String> count_Word_Frequency(String input_file_path, Map<String, String> wordsMap,
			boolean isWeight) {

		try {
			File input_file = new File(input_file_path);
			InputStreamReader reader;
			BufferedReader bufferedReader;
			String str = null;

			if (input_file.isFile() && input_file.exists())// 判断文件是否存在
			{
				
				if (wordsMap.containsKey("title"))// 如果是单词的Map
				{
					// 先去除wordsMap初步统计中“title: ”和“abstract: ”所占用的词频
					reader = new InputStreamReader(new FileInputStream(input_file));
					bufferedReader = new BufferedReader(reader);
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
				}
				// if (wordsMap.get("title").equals("0"))
				// wordsMap.remove("title");
				// if (wordsMap.get("abstract").equals("0"))
				// wordsMap.remove("abstract");

				if (!isWeight) {
					// 权重1:1
					return wordsMap;
				} else if (isWeight) {
					// 权重10:1
					reader = new InputStreamReader(new FileInputStream(input_file));
					bufferedReader = new BufferedReader(reader);
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
													n += 9;
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

	/*
	 * 统计文件中的单词总数 
	 * 输入：文件路径
	 *  输出：文件中的单词总数
	 */
	public static int get_Words_Num(String filePath) {
		Map<String, String> wordsMap = wordCount.count_Words(filePath);
		int countWords = Integer.parseInt(wordsMap.get("count_words"));
		return countWords;
	}

	/*
	 * 从文件提取单词并且统计单词出现次数和单词总数
	 *  输入：文件路径 
	 *  输出：包涵单词总数和各个单词出现次数的Map
	 */
	public static Map<String, String> count_Words(String filePath) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("count_words", "0");

		try {
			File input_file = new File(filePath);
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;

				while ((str = bufferedReader.readLine()) != null) {
					str = str.toLowerCase();
					for (int i = 0; i < (str.length() - 4); i++) {
						if (i > 0) {
							if (('a' <= str.charAt(i - 1) && str.charAt(i - 1) <= 'z')
									|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// 如果前一个字符是字符或数字
								continue;
							}
						}
						if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
							if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
								if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
									if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {// 找到单词
										int j;
										for (j = i + 4; j < str.length(); j++) {// 看单词是否结束
											if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
												if (48 > str.charAt(j) || str.charAt(j) > 57)// 不是数字
													break;
											}
										}
										String temp = str.substring(i, j);// 截取字符串索引号i到j区域（包括i，不包括j）
										// 加到图里去
										if (map.containsKey(temp)) {
											int n = Integer.parseInt(map.get(temp));
											n++;
											map.put(temp, n + "");
										} else
											map.put(temp, "1");

										int n = Integer.parseInt(map.get("count_words"));
										n++;// 总单词个数加一
										map.put("count_words", n + "");

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
				reader.close();
				return map;

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

	/*
	 * 统计文件总的字符数 输入：文件路径 输出：文件的字符数（包括空白字符）
	 */
	public static int count_Characters(String filePath) {
		try {
			File input_file = new File(filePath);
			File output_file = new File("result.txt");
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output_file));
				int countChar = 0;
				int temp;
				int last_char = -1;
				while ((temp = reader.read()) != -1) {
					countChar++;
					if (last_char == '\r' && temp == '\n') {
						countChar--;
					}
					last_char = temp;
				}

				reader.close();
				writer.close();
				return countChar;
			} else {
				System.out.println("找不到指定的文件");
				return -1;
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return -1;
		}

	}

}