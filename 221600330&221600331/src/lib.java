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

public class lib {

	/*
	 * 将统计结果输出到文件"result.txt" 输入：统计的文件字符总数、统计单词和词频的Map、统计的文件有效行数 输出：无
	 */
	public static void writeToFile( Map<String, String> wordsMap, int count_char, int count_words_num, int countLinnes, String output_file_path,int output_num) {
		try {
			File output_file = new File(output_file_path);
			OutputStreamWriter writer;
			writer = new OutputStreamWriter(new FileOutputStream(output_file));
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write("characters: " + count_char + "\r\n");
			bufferedWriter.write("words: " + count_words_num + "\r\n");
			bufferedWriter.write("lines: " + countLinnes + "\r\n");
			bufferedWriter.flush();

			System.out.println("characters: " + count_char);
			System.out.println("words: " + count_words_num);
			System.out.println("lines: " + countLinnes);

			if (count_words_num <= 0) {
				writer.close();
				return;
			}

			while (count_words_num > 0 && output_num-- > 0) {
				String temp = "";
				int maxNum = -1;
				Iterator<Map.Entry<String, String>> iterator = wordsMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					if (Integer.parseInt(entry.getValue()) >= maxNum && !entry.getKey().equals("count_words_num")) {
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
				// count_words_num = count_words_num - maxNum;
			}

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/*
	 * 统计文件的有效行数包含非空白字符的行） 输入：文件路径 输出：文件的有效行数（包含非空白字符的行）
	 */
	public static int count_Lines(String file_path) {
		try {
			File input_file = new File(file_path);
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
	 * 统计文件中的单词总数 
	 * 输入：文件路径 
	 * 输出：文件中的单词总数
	 */
	public static int get_Words_Num(String file_path) {
		Map<String, String> wordsMap = lib.count_Words(file_path);
		int count_words_num = Integer.parseInt(wordsMap.get("count_words_num"));
		return count_words_num;
	}

	
	/*
	 * 从文件提取单词并且统计单词出现次数和单词总数放入Map
	 * 输入：文件路径 
	 * 输出：包涵单词总数和各个单词出现次数的Map
	 */
	public static Map<String, String> count_Words(String file_path) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("count_words_num", "0");// 记录单词总数

		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;

				while ((str = bufferedReader.readLine()) != null) {
					str = str.toLowerCase();
					for (int i = 0; i < (str.length() - 3); i++) {
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
										
										// 加到Map里去
										if (map.containsKey(temp)) {
											int n = Integer.parseInt(map.get(temp));
											n++;
											map.put(temp, n + "");
										} else
											map.put(temp, "1");

										int n = Integer.parseInt(map.get("count_words_num"));
										n++;// 总单词个数加一
										map.put("count_words_num", n + "");

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
	 * 统计文件总的字符数 
	 * 输入：文件路径 
	 * 输出：文件的字符数（包括空白字符）
	 */
	public static int count_Characters(String file_path) {
		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				int count_char = 0;
				int temp;
				int last_char = -1;
				while ((temp = reader.read()) != -1) {
					count_char++;
					if (last_char == '\r' && temp == '\n') {
						count_char--;
					}
					last_char = temp;
				}

				reader.close();
				return count_char;
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

	
	/*
	 * 从文件提取m个单词构成的词组并且统计词组及词组的权重词频 
	 * 输入：文件路径，一个词组的单词数，是否按10:1的权重统计词频的boolean值
	 * 输出：包涵词组总数和各个词组词频的Map
	 */
	public static Map<String, String> count_Phrase_frequency(String file_path, int phraseLength, boolean is_weight) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("count_words_num", "0");// 注意！！wordsMap中加入了一个“count_words_num”的键用于统计词组总数（不是种数哦）

		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;
				boolean is_title = false;

				while ((str = bufferedReader.readLine()) != null) {
					str = clear_String(str);
					str = str.toLowerCase();

					// 去掉"title: "和"abstract: "
					if (str.contains("title: ")) {
						is_title = true;
						str = str.substring(0, str.indexOf("title: ")) + str.substring(str.indexOf("title: ") + 7);
					} else
						is_title = false;
					if (str.contains("abstract: "))
						str = str.substring(0, str.indexOf("abstract: ")) + str.substring(str.indexOf("abstract: ") + 10);

					int count = 0;// 计算连续出现的单词数
					String[] words = new String[101];// 存储连续出现的单词

					for (int i = 0; i < (str.length() - 3); i++) {
						if (i > 0) {
							if (('a' <= str.charAt(i - 1) && str.charAt(i - 1) <= 'z')
									|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// 如果前一个字符是字符或数字
								continue;
							}
						}
						if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
							if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
								if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
									if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {// 找到一个单词

										int j;
										for (j = i + 4; j < str.length(); j++) {// 看单词是否结束
											if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
												if (48 > str.charAt(j) || str.charAt(j) > 57)// 不是数字，遇到分隔符
													break;
											}
										}
										String temp = str.substring(i, j);// 截取字符串索引号i到j区域（包括i，不包括j+1）---截取单词
										if (j == str.length())// 一段中以单词结尾
											temp = temp + " ";
										else
											temp = temp + str.charAt(j);// 把单词后面的一个分隔符加到单词中去
										count++;
										words[count] = temp;
										if (count >= phraseLength) {
											temp = words[count - phraseLength + 1];
											for (int k = phraseLength; k > 1; k--) {
												temp = temp + words[count - k + 2];
											}
											temp = temp.substring(0, temp.length() - 1);// 和并后去掉末尾的分割符

											// 加到Map里去
											if (is_weight && is_title)// 计算权值为10:1,并且该词组在title段中
											{
												if (map.containsKey(temp)) {
													int n = Integer.parseInt(map.get(temp));
													n += 10;
													map.put(temp, n + "");
												} else
													map.put(temp, "10");
											} else {
												// 不需要计算权值
												if (map.containsKey(temp)) {
													int n = Integer.parseInt(map.get(temp));
													n++;
													map.put(temp, n + "");
												} else
													map.put(temp, "1");
											}

											int n = Integer.parseInt(map.get("count_words_num"));
											n++;// 总词组个数加一
											map.put("count_words_num", n + "");
										}
										i = j;
									} else {
										count = 0;
										i = i + 3;
									} // 遇到少于4个字母的单词，结束count
								} else {
									count = 0;
									i = i + 2;
								} // 遇到少于4个字母的单词，结束count
							} else {
								count = 0;
								i = i + 1;
							} // 遇到少于4个字母的单词，结束count
						} else {
							if ((48 > str.charAt(i) || str.charAt(i) > 57)) {// 遇到分隔符加到加到上一个单词末尾
								words[count] += str.charAt(i);
							} else {
								// 遇到数字，结束count
								count = 0;
							}
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
	 * 按权重统计文件的单词词频(0表示属于 Title、Abstract 的单词权重相同均为 1; 1 表示属于Title的单词权重为10，属于Abstract 单词权重为1) 
	 * 输入：文件路径、初步统计的单词和词频的Map、是否使用按10:1的权重的boolean
	 * 输出：按权重统计的单词和词频的Map
	 */
	public static Map<String, String> count_Word_Frequency(String input_file_path, Map<String, String> wordsMap,boolean is_weight) {

		try {
			File input_file = new File(input_file_path);
			if (input_file.isFile() && input_file.exists())// 判断文件是否存在
			{
				if (!is_weight) {
					// 权重1:1
					return wordsMap;
				} else if (is_weight) {
					// 权重10:1
					InputStreamReader reader;
					BufferedReader bufferedReader;
					String str = null; 
					
					reader = new InputStreamReader(new FileInputStream(input_file));
					bufferedReader = new BufferedReader(reader);
					while ((str = bufferedReader.readLine()) != null) {
						str = str.toLowerCase();
						if (str.contains("title: ")) {
							for (int i = 5; i < (str.length() - 3); i++) {
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
												// 词频加9
												if (wordsMap.containsKey(temp)) {
													int n = Integer.parseInt(wordsMap.get(temp));
													n += 9;
													wordsMap.put(temp, n + "");
												} 
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
	 * 去掉论文爬取结果文件中的“Title: ”、“Abstract: ”、论文编号及其紧跟着的换行符以及分隔论文的两个换行符并写入一个新文件“new_Txt.txt”
	 * 输入：文件路径
	 * 输出：修改后的文件的文件路径
	 */
	public static String rewrite_Txt(String file_path)
	{
		try {
			File input_file = new File(file_path);
			File output_file = new File("new_Txt.txt");
			if (input_file.isFile() && input_file.exists()) { // 判断文件是否存在
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					
					//判断该段是否为论文序号
					boolean is_Num = true;
					for(int i = 0;i < str.length() ; i++){
						int chr=str.charAt(i);
						if(chr<48 || chr>57) {
							is_Num = false;
							break;
						}
					}
					if( is_Num || str.equals(""))
						continue;
					// 去掉非Ascii码的字符
					str = clear_String(str);
					// 去掉"title: "和"abstract: "
					if (str.contains("Title: ")) {
						str = str.substring(0, str.indexOf("Title: ")) + str.substring(str.indexOf("Title: ") + 7);
						bufferedWriter.write(str+"\r\n");
						bufferedWriter.flush();
						continue;
					} 
					if (str.contains("Abstract: ")) {
						str = str.substring(0, str.indexOf("Abstract: ")) + str.substring(str.indexOf("Abstract: ") + 10);
						bufferedWriter.write(str+"\r\n");
						bufferedWriter.flush();
						continue;
					}
					bufferedWriter.write(str+"\r\n");
					bufferedWriter.flush();
					
				}
				
				reader.close();
				writer.close();
				return "new_Txt.txt";
			} else {
				System.out.println("找不到指定的文件");
				return null;
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*
	 * 去除字符串中的非ASCII码
	 * 输入：字符串
	 * 输出：去除非ASCII码的字符串
	 */
	public static String clear_String(String str)
	{
		String new_str = "";
		for( int i = 0 ; i < str.length() ; i++ )
		{
			if( str.charAt(i) <= 127 )
				new_str +=str.charAt(i);
		}
		
		return new_str;
	}

}

	






