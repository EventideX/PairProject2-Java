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
public class file {
	
	/*
	 * ȥ��������ȡ����ļ��еġ�Title: ������Abstract: �������ı�ż�������ŵĻ��з��Լ��ָ����ĵ��������з���д��һ�����ļ���new_Txt.txt��
	 * ���룺�ļ�·��
	 * ������޸ĺ���ļ����ļ�·��
	 */
	public static String rewrite_Txt(String file_path)
	{
		try {
			File input_file = new File(file_path);
			File output_file = new File("new_Txt.txt");
			if (input_file.isFile() && input_file.exists()) { // �ж��ļ��Ƿ����
				InputStreamReader reader = new InputStreamReader(new FileInputStream(input_file));
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output_file));
				BufferedReader bufferedReader = new BufferedReader(reader);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					
					//�жϸö��Ƿ�Ϊ�������
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
					// ȥ����Ascii����ַ�
					str = clear_String(str);
					// ȥ��"title: "��"abstract: "
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
				System.out.println("�Ҳ���ָ�����ļ�");
				return null;
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*
	 * ȥ���ַ����еķ�ASCII��
	 * ���룺�ַ���
	 * �����ȥ����ASCII����ַ���
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

}
