import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main{
    public static void main(String[] args) {
    	//����Ĭ��ֵ
        String inFile = null;
        String outFile = null;
        int weight = 0;
        int m = 1;
        int n = 10;
        //��ȡ����
        for(int i=0;i<args.length;i++){
            switch(args[i]){
                case "-i":
                    inFile = args[++i];
                    break;
                case "-o":
                    outFile = args[++i];
                    break;
                case "-w":
                    if(args[++i].equals("1")){
                        weight=1;
                    }
                    break;
                case "-m":
                    m = Integer.parseInt(args[++i]);
                    break;
                case "-n":
                    n = Integer.parseInt(args[++i]);
                    break;
            }
        }
        //�ж���������ļ��Ƿ�ָ��
        if(inFile!=null && outFile!=null){
            BaseFunction bf = new BaseFunction();
            FileOperate fo = new FileOperate();
            fo.ReadFile(new File(inFile));
            int characters = 0;
            int wordCounts = 0;
            //Ȩ��Ϊtrue title��1 abstract��0 Ȩ��Ϊfalse title��0 abstract��0
            for(String title: fo.getTitles()) {
                title = title.replaceAll("\r\n","\n");
                title = title.replaceAll("[^(\\x00-\\x7f)]","");
                characters += title.length();
                wordCounts += bf.CountWord(title,weight);
                bf.CountPhrase(title,m,weight);

            }
            for(String abs: fo.getAbstracts()){
                abs = abs.replaceAll("\r\n","\n");
                abs = abs.replaceAll("[^(\\x00-\\x7f)]","");
                characters += abs.length();
                wordCounts += bf.CountWord(abs,0);
                bf.CountPhrase(abs,m,0);
            }
            //д���ļ�
            fo.WriteFile(characters,wordCounts,fo.getTitles().size()*2,bf.sortList(),outFile,n);
        }else{
            System.out.println("�����������Ϊ�գ�");
        }

    }
}
class BaseFunction {

    public Map<String,Integer> wordCount =new HashMap<String,Integer>();//����ͳ��
    public Map<String,Integer> phraseCount =new HashMap<String,Integer>();//����ͳ��

    /**
     * ͳ����Ч����
     * @param text
     * @return
     */
    public int LineCount(String text){
    	
        int lines=0;
        boolean flag = false;
        for(int i=0;i<text.length();i++){
            if(text.charAt(i)>' '){
                flag=true;
            }
            else if(text.charAt(i)=='\n'){
                if(flag) {
                    lines++;
                    flag=false;
                }
            }
        }
        if(flag)
            lines++;
        return lines;
    }

    /**
     * ��Ƶͳ��
     * @param text
     * @return
     */
    public int WordCount(String text){
        int cpmount = 0;
        //ȫ����ĸתСд
        String textLow = text.toLowerCase(); 
        //������ʽ�����˷���ĸ�����ַ�
        String regex = "[^0-9a-zA-Z]"; 
        //�����ı�
        textLow = textLow.replaceAll(regex, " "); 
        //�ָ��ı��ɵ���
        StringTokenizer words = new StringTokenizer(textLow);
        try {
            while (words.hasMoreTokens()) {
                String word = words.nextToken();
                //�жϵ���ǰ4���Ƿ�Ϊ��ĸ
                if (word.length() >= 4 && Character.isLetter(word.charAt(0)) && Character.isLetter(word.charAt(1)) && Character.isLetter(word.charAt(2)) && Character.isLetter(word.charAt(3))) {  
                	cpmount++;
                    if (!wordCount.containsKey(word)) {
                        wordCount.put(word, new Integer(1));
                    } else {
                        int count = wordCount.get(word) + 1;
                        wordCount.put(word, count);
                    }
                }

            }
        }catch (Exception e){
        	//����
        }
        return cpmount;
    }

    
    /**
     * ͳ�Ƶ�����
     * @param text
     * @param wei ,��wei=1 Ȩ��Ϊ10  wei=0 Ȩ��Ϊ1
     * @return ��������
     */
    public int CountWord(String text,int wei){
        int ant=0;
        text = text.toLowerCase();
        //�ָ�������
        String regex = "[^0-9a-zA-Z]";
        text = text.replaceAll(regex, " ");
        //�ָ��ı��ɵ���
        StringTokenizer words = new StringTokenizer(text); 
        try {
            while (words.hasMoreTokens()) {
                String word = words.nextToken();
                if (word.length() >= 4 && Character.isLetter(word.charAt(0)) && Character.isLetter(word.charAt(1)) && Character.isLetter(word.charAt(2)) && Character.isLetter(word.charAt(3))) {  //�жϵ���ǰ4���Ƿ�Ϊ��ĸ
                    ant++;
                    if (!wordCount.containsKey(word)) {

                        wordCount.put(word, new Integer(wei==1 ? 10:1));
                    } else {
                        int count = wordCount.get(word) + (wei==1 ? 10:1);
                        wordCount.put(word, count);
                    }
                }
            }
        }catch (Exception e){
            //����
        }
        return ant;
    }

    /**
     * ͳ�ƴ���
     * @param text
     * @param cwords ����ĵ�����
     * @param wei ,��wei=1 Ȩ��Ϊ10  wei=0 Ȩ��Ϊ1
     */
    public void CountPhrase(String text,int cwords,int wei){
    	text = text.toLowerCase();
    	text = text.replaceAll("\n","");
        StringBuilder mid=new StringBuilder();//�ָ���
        StringBuilder wword=new StringBuilder();//����ƴ��
        Queue<String> que1=new LinkedList<String>();//���ڴ洢���鵥��
        Queue<String> que2=new LinkedList<String>();//���ڴ洢�ָ���
        String feng=",./;'[] \\<>?:\"{}|`~!@#$%^&*()_+-=";//�ָ�������
        StringTokenizer words = new StringTokenizer(text,feng,true); //�ָ��ı��ɵ��ʡ�
        try {
            while (words.hasMoreTokens()) {
                String word =words.nextToken();
                if (word.length() >= 4 && Character.isLetter(word.charAt(0)) && Character.isLetter(word.charAt(1)) && Character.isLetter(word.charAt(2)) && Character.isLetter(word.charAt(3))) {  //�жϵ���ǰ4���Ƿ�Ϊ��ĸ

                    que2.offer(mid.toString());
                    mid.delete(0,mid.length());
                    que1.offer(word);
                    if(que1.size()>=cwords){//�ﵽ���鵥������
                        int cnt=0;
                        wword.delete(0,wword.length());
                        for(String w:que1){
                            wword.append(w);
                            cnt++;
                            if(que2.size()>cnt)
                            {
                                String tmp=((LinkedList<String>) que2).get(cnt);//ȡ���м�ķָ���
                                wword.append(tmp);//ƴ��
                            }
                        }
                        //���������ȷ��wword ����
                        // ����ͳ�Ʋ���
                        if(!phraseCount.containsKey(wword.toString()))
                        {
                            phraseCount.put(wword.toString(),new Integer( wei==1 ? 10:1 ));
                        }
                        else{
                            int count=phraseCount.get(wword.toString()) + (wei==1 ? 10:1);
                            phraseCount.put(wword.toString(),count);
                        }
                        que1.remove();
                        que2.remove();
                    }
                }
                else if(word.length()!=1){//���������� ����ǰ��Ķ�ɾ��
                    que1.clear();
                    que2.clear();
                }else if(word.length()==1 && !(Character.isLetter(word.charAt(0)))){//�ж��Ƿ�Ϊ�ָ���
                    mid.append(word);
                }
            }
        }catch (Exception e){
            //����
        }

    }
    
    /**
     * ��Ƶ����
     * @return list
     */
    public List<HashMap.Entry<String, Integer>> sortList(){

        List<HashMap.Entry<String, Integer>> list = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : phraseCount.entrySet()){
        	list.add(entry);
        }
        Comparator<Map.Entry<String, Integer>> comp = new Comparator<Map.Entry<String, Integer>>(){
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(o1.getValue().equals(o2.getValue()))
                    return o1.getKey().compareTo(o2.getKey());     //ֵ��ͬ ���������ֵ���
                return o2.getValue()-o1.getValue();
            }
            //���򣨴Ӵ�С�����У�����Ϊ��return o1.getValue()-o2.getValue"
        };
        list.sort(comp);
        return list;
    }

}

class FileOperate {

    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> abstracts = new ArrayList<String>();
    ArrayList<String> PDFLink = new ArrayList<>();

    /**
     * д���ļ�
     * @param st
     * @param name
     */
    public void Write(String text, String file){
        try{
            FileOutputStream res = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(res);
            bos.write(text.getBytes(),0,text.getBytes().length);
            bos.flush();
            bos.close();
        }catch (Exception e){
            //����
        }
    }
    
    /**
     * �ļ�д��
     * @param length
     * @param wordAmount
     * @param lines
     * @param wList
     * @param out
     * @param n
     */
    public void WriteFile(int len, int wordMount, int lines, List<HashMap.Entry<String, Integer>> list,String out,int n){

        try{
            FileOutputStream res = new FileOutputStream(out);
            BufferedOutputStream bos = new BufferedOutputStream(res);
            String t = "characters: " + len +"\r\n"
                    +"words: " + wordMount +"\r\n"
                    +"lines: " + lines +"\r\n";
            int count = 0;
            for(HashMap.Entry<String,Integer> entry:list){
                count++;
                t += "<"+entry.getKey() + ">: " + entry.getValue();
                if(count<=n-1){
                    t += "\r\n";
                }else{
                    break;
                }
            }
            bos.write(t.getBytes(),0,t.getBytes().length);
            bos.flush();
            bos.close();
        }catch (Exception e){
            //����
        }
    }
    
    /**
     * �ļ�����
     * @param file
     */
    public String ReadFile(File file){
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder str = new StringBuilder();
            //�������
            titles.clear();
            abstracts.clear();
            PDFLink.clear();
            //��ʼ��ȡ
            int c_byte = 0;
            while((c_byte = br.read()) != -1){
                str.append((char) c_byte);
                if((char)c_byte == '\n'){
                    if(str.toString().charAt(0)=='T' && str.toString().charAt(1)=='i' && str.toString().charAt(2)=='t'){
                        str.delete(0,7);
                        titles.add(str.toString());
                    }else if(str.toString().charAt(0)=='A'){
                        str.delete(0,10);
                        abstracts.add(str.toString());
                    }else if(str.toString().charAt(0)=='P'){
                        str.delete(0,10);
                        PDFLink.add(str.toString());
                    }
                    str.delete(0,str.toString().length());
                }
            }
            //�ж�ĩβ
            //��ĩβ��abstract ��add����
            //��ĩβ��pdflink  ��add����
            if(str.toString().startsWith("A")){
                str.delete(0,10);
                abstracts.add(str.toString());
            }
            if(str.toString().endsWith("P")){
                str.delete(0,10);
                PDFLink.add(str.toString());
            }
            br.close();
            isr.close();
            return str.toString();
        }catch (Exception e){
             //����
        }
        return "";
    }
    
    //���������
    public ArrayList<String> getTitles(){
        return titles;
    }

    public ArrayList<String> getAbstracts(){
        return abstracts;
    }

    public ArrayList<String> getPDFLink() { return PDFLink;}
}



