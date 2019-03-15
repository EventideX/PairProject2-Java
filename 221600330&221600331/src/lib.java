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
	 * ͳ���ļ�����Ч���������ǿհ��ַ����У� ���룺�ļ�·�� ������ļ�����Ч�����������ǿհ��ַ����У�
	 */
	public static int count_Lines(String file_path) {
		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists())// �ж��ļ��Ƿ����
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
				System.out.println("�Ҳ���ָ�����ļ�");
				return -1;
			}
		} catch (IOException e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return -1;
		}
	}


	/*
	 * ͳ���ļ��еĵ������� 
	 * ���룺�ļ�·�� 
	 * ������ļ��еĵ�������
	 */
	public static int get_Words_Num(String file_path) {
		Map<String, String> wordsMap = lib.count_Words(file_path);
		int count_words_num = Integer.parseInt(wordsMap.get("count_words_num"));
		return count_words_num;
	}

	
	/*
	 * ���ļ���ȡ���ʲ���ͳ�Ƶ��ʳ��ִ����͵�����������Map
	 * ���룺�ļ�·�� 
	 * ������������������͸������ʳ��ִ�����Map
	 */
	public static Map<String, String> count_Words(String file_path) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("count_words_num", "0");// ��¼��������

		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // �ж��ļ��Ƿ����
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;

				while ((str = bufferedReader.readLine()) != null) {
					str = str.toLowerCase();
					for (int i = 0; i < (str.length() - 3); i++) {
						if (i > 0) {
							if (('a' <= str.charAt(i - 1) && str.charAt(i - 1) <= 'z')
									|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// ���ǰһ���ַ����ַ�������
								continue;
							}
						}
						if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
							if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
								if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
									if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {// �ҵ�����
										int j;
										for (j = i + 4; j < str.length(); j++) {// �������Ƿ����
											if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
												if (48 > str.charAt(j) || str.charAt(j) > 57)// ��������
													break;
											}
										}
										String temp = str.substring(i, j);// ��ȡ�ַ���������i��j���򣨰���i��������j��
										
										// �ӵ�Map��ȥ
										if (map.containsKey(temp)) {
											int n = Integer.parseInt(map.get(temp));
											n++;
											map.put(temp, n + "");
										} else
											map.put(temp, "1");

										int n = Integer.parseInt(map.get("count_words_num"));
										n++;// �ܵ��ʸ�����һ
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
				System.out.println("�Ҳ���ָ�����ļ�");
				return null;
			}
		} catch (IOException e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return null;
		}
	}

	
	/*
	 * ͳ���ļ��ܵ��ַ��� 
	 * ���룺�ļ�·�� 
	 * ������ļ����ַ����������հ��ַ���
	 */
	public static int count_Characters(String file_path) {
		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // �ж��ļ��Ƿ����
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
				System.out.println("�Ҳ���ָ�����ļ�");
				return -1;
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return -1;
		}

	}

	
	/*
	 * ���ļ���ȡm�����ʹ��ɵĴ��鲢��ͳ�ƴ��鼰�����Ȩ�ش�Ƶ 
	 * ���룺�ļ�·����һ������ĵ��������Ƿ�10:1��Ȩ��ͳ�ƴ�Ƶ��booleanֵ
	 * ������������������͸��������Ƶ��Map
	 */
	public static Map<String, String> count_Phrase_frequency(String file_path, int phraseLength, boolean is_weight) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("count_words_num", "0");// ע�⣡��wordsMap�м�����һ����count_words_num���ļ�����ͳ�ƴ�����������������Ŷ��

		try {
			File input_file = new File(file_path);
			if (input_file.isFile() && input_file.exists()) { // �ж��ļ��Ƿ����
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				String str = null;
				boolean is_title = false;

				while ((str = bufferedReader.readLine()) != null) {
					str = file.clear_String(str);
					str = str.toLowerCase();

					// ȥ��"title: "��"abstract: "
					if (str.contains("title: ")) {
						is_title = true;
						str = str.substring(0, str.indexOf("title: ")) + str.substring(str.indexOf("title: ") + 7);
					} else
						is_title = false;
					if (str.contains("abstract: "))
						str = str.substring(0, str.indexOf("abstract: ")) + str.substring(str.indexOf("abstract: ") + 10);

					int count = 0;// �����������ֵĵ�����
					String[] words = new String[101];// �洢�������ֵĵ���

					for (int i = 0; i < (str.length() - 3); i++) {
						if (i > 0) {
							if (('a' <= str.charAt(i - 1) && str.charAt(i - 1) <= 'z')
									|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// ���ǰһ���ַ����ַ�������
								continue;
							}
						}
						if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
							if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
								if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
									if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {// �ҵ�һ������

										int j;
										for (j = i + 4; j < str.length(); j++) {// �������Ƿ����
											if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
												if (48 > str.charAt(j) || str.charAt(j) > 57)// �������֣������ָ���
													break;
											}
										}
										String temp = str.substring(i, j);// ��ȡ�ַ���������i��j���򣨰���i��������j+1��---��ȡ����
										if (j == str.length())// һ�����Ե��ʽ�β
											temp = temp + " ";
										else
											temp = temp + str.charAt(j);// �ѵ��ʺ����һ���ָ����ӵ�������ȥ
										count++;
										words[count] = temp;
										if (count >= phraseLength) {
											temp = words[count - phraseLength + 1];
											for (int k = phraseLength; k > 1; k--) {
												temp = temp + words[count - k + 2];
											}
											temp = temp.substring(0, temp.length() - 1);// �Ͳ���ȥ��ĩβ�ķָ��

											// �ӵ�Map��ȥ
											if (is_weight && is_title)// ����ȨֵΪ10:1,���Ҹô�����title����
											{
												if (map.containsKey(temp)) {
													int n = Integer.parseInt(map.get(temp));
													n += 10;
													map.put(temp, n + "");
												} else
													map.put(temp, "10");
											} else {
												// ����Ҫ����Ȩֵ
												if (map.containsKey(temp)) {
													int n = Integer.parseInt(map.get(temp));
													n++;
													map.put(temp, n + "");
												} else
													map.put(temp, "1");
											}

											int n = Integer.parseInt(map.get("count_words_num"));
											n++;// �ܴ��������һ
											map.put("count_words_num", n + "");
										}
										i = j;
									} else {
										count = 0;
										i = i + 3;
									} // ��������4����ĸ�ĵ��ʣ�����count
								} else {
									count = 0;
									i = i + 2;
								} // ��������4����ĸ�ĵ��ʣ�����count
							} else {
								count = 0;
								i = i + 1;
							} // ��������4����ĸ�ĵ��ʣ�����count
						} else {
							if ((48 > str.charAt(i) || str.charAt(i) > 57)) {// �����ָ����ӵ��ӵ���һ������ĩβ
								words[count] += str.charAt(i);
							} else {
								// �������֣�����count
								count = 0;
							}
						}

					}
				}
				reader.close();
				return map;

			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
				return null;
			}
		} catch (IOException e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return null;
		}
	}

	
	/*
	 * ��Ȩ��ͳ���ļ��ĵ��ʴ�Ƶ(0��ʾ���� Title��Abstract �ĵ���Ȩ����ͬ��Ϊ 1; 1 ��ʾ����Title�ĵ���Ȩ��Ϊ10������Abstract ����Ȩ��Ϊ1) 
	 * ���룺�ļ�·��������ͳ�Ƶĵ��ʺʹ�Ƶ��Map���Ƿ�ʹ�ð�10:1��Ȩ�ص�boolean
	 * �������Ȩ��ͳ�Ƶĵ��ʺʹ�Ƶ��Map
	 */
	public static Map<String, String> count_Word_Frequency(String input_file_path, Map<String, String> wordsMap,boolean is_weight) {

		try {
			File input_file = new File(input_file_path);
			if (input_file.isFile() && input_file.exists())// �ж��ļ��Ƿ����
			{
				if (!is_weight) {
					// Ȩ��1:1
					return wordsMap;
				} else if (is_weight) {
					// Ȩ��10:1
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
										|| (48 <= str.charAt(i - 1) && str.charAt(i - 1) <= 57)) {// ���ǰһ���ַ����ַ�������
									continue;
								}
								if ('a' <= str.charAt(i) && str.charAt(i) <= 'z') {
									if ('a' <= str.charAt(i + 1) && str.charAt(i + 1) <= 'z') {
										if ('a' <= str.charAt(i + 2) && str.charAt(i + 2) <= 'z') {
											if ('a' <= str.charAt(i + 3) && str.charAt(i + 3) <= 'z') {
												// �ҵ�����
												int j;
												for (j = i + 4; j < str.length(); j++) {
													// �������Ƿ����
													if ('a' > str.charAt(j) || str.charAt(j) > 'z') {
														if (48 > str.charAt(j) || str.charAt(j) > 57)
															// �����ַ������֣����ʽ���
															break;
													}
												}
												String temp = str.substring(i, j);// ��ȡ�ַ���������i��j���򣨰���i��������j��
												// ��Ƶ��9
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
					System.out.println("������󣡣�");
					return null;
				}
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
				return null;
			}
		} catch (IOException e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return null;
		}
	}
	

}

	






