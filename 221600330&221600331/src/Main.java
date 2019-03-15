import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		int count_char = 0;
		int count_rows = 0;
		int count_words_num = 0;
		int phraseLength = 0;  //һ������İ����ĵ�����
		boolean is_weight=false;  //�Ƿ��Ȩ��
		int numbers=10;
		
		//�����������ַ
		String in_name = "test.txt";
		String out_name = "result.txt";
		for(int i=0;i<args.length;i+=2) {
			switch(args[i]) {
				case "-i":
					
					in_name=args[i+1];//���������ļ�·��
					break;
				case "-o":
					
					out_name=args[i+1];//��������ļ�·��
					break;
				case "-w":
					//���������ѡ��Ȩ��ռ��
					if(Integer.parseInt(args[i+1])==0) {
						is_weight=false;
					}else {
						is_weight=true;
					}
					break;
				case "-m":
					/*ͳ���ļ�����ָ�����ȵĴ���Ĵ�Ƶ
					 * ����-mʱֻͳ�ƴ����Ƶ
					 * δ����-mʱֻͳ�Ƶ��ʴ�Ƶ
					 * */
					phraseLength=Integer.parseInt(args[i+1]);
					break;
				case "-n":
					/*�û�ָ�����ǰnumber��ĵ���(����)����Ƶ��
					 * ��ʾ���Ƶ������ǰ [number] ������(����)
					 * */
					numbers=Integer.parseInt(args[i+1]);
					break;
				default:
						System.out.println("ָ����󣡣���");
			}
		}
		
		Map<String, String> wordsMap;
		Map<String, String> wordsFrequency;

		String new_file = file.rewrite_Txt(in_name);//ȥ��������ȡ����ļ��еġ�Title: ������Abstract: �������ı�ż�������ŵĻ��з��Լ��ָ����ĵ��������з���д��һ�����ļ���new_Txt.txt��
		
		count_char = lib.count_Characters(new_file)-1;//ͳ���ļ����ַ���
		
		wordsMap = lib.count_Words(new_file);//���ļ���ȡ���ʲ���ͳ�Ƶ��ʳ��ִ����͵�����������Map
		count_words_num = Integer.parseInt(wordsMap.get("count_words_num"));//ͳ���ļ��ĵ�����
		
		count_rows = lib.count_Lines(new_file);//ͳ���ļ�������
		
		wordsFrequency=lib.count_Word_Frequency(in_name, wordsMap,is_weight);//ͳ�Ƶ��ʵ�Ȩ�ش�Ƶ
		
		
		
		if( phraseLength > 0 ) {
			Map<String, String> phraseFrequency = lib.count_Phrase_frequency(in_name, phraseLength, is_weight);//ͳ�ƴ����Ȩ�ش�Ƶ
			file.writeToFile(phraseFrequency,count_char,count_words_num,count_rows,out_name,numbers);//������ļ���
		}
		else
			file.writeToFile(wordsFrequency,count_char,count_words_num,count_rows,out_name,numbers);//������ļ���
	}
	
	


}