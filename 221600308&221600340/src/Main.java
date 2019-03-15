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
    	//参数默认值
        String inFile = null;
        String outFile = null;
        int weight = 0;
        int m = 1;
        int n = 10;
        //获取参数
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
        //判断输入输出文件是否指定
        if(inFile!=null && outFile!=null){
            BaseFunction bf = new BaseFunction();
            FileOperate fo = new FileOperate();
            fo.ReadFile(new File(inFile));
            int characters = 0;
            int wordCounts = 0;
            //权重为true title：1 abstract：0 权重为false title：0 abstract：0
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
            //写入文件
            fo.WriteFile(characters,wordCounts,fo.getTitles().size()*2,bf.sortList(),outFile,n);
        }else{
            System.out.println("必需参数不能为空！");
        }

    }
}
class BaseFunction {

    public Map<String,Integer> wordCount =new HashMap<String,Integer>();//单词统计
    public Map<String,Integer> phraseCount =new HashMap<String,Integer>();//词组统计

    /**
     * 统计有效行数
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
     * 词频统计
     * @param text
     * @return
     */
    public int WordCount(String text){
        int cpmount = 0;
        //全部字母转小写
        String textLow = text.toLowerCase(); 
        //正则表达式，过滤非字母数字字符
        String regex = "[^0-9a-zA-Z]"; 
        //过滤文本
        textLow = textLow.replaceAll(regex, " "); 
        //分割文本成单词
        StringTokenizer words = new StringTokenizer(textLow);
        try {
            while (words.hasMoreTokens()) {
                String word = words.nextToken();
                //判断单词前4个是否为字母
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
        	//错误
        }
        return cpmount;
    }

    
    /**
     * 统计单词数
     * @param text
     * @param wei ,当wei=1 权重为10  wei=0 权重为1
     * @return 单词数量
     */
    public int CountWord(String text,int wei){
        int ant=0;
        text = text.toLowerCase();
        //分隔符集合
        String regex = "[^0-9a-zA-Z]";
        text = text.replaceAll(regex, " ");
        //分割文本成单词
        StringTokenizer words = new StringTokenizer(text); 
        try {
            while (words.hasMoreTokens()) {
                String word = words.nextToken();
                if (word.length() >= 4 && Character.isLetter(word.charAt(0)) && Character.isLetter(word.charAt(1)) && Character.isLetter(word.charAt(2)) && Character.isLetter(word.charAt(3))) {  //判断单词前4个是否为字母
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
            //错误
        }
        return ant;
    }

    /**
     * 统计词组
     * @param text
     * @param cwords 词组的单词数
     * @param wei ,当wei=1 权重为10  wei=0 权重为1
     */
    public void CountPhrase(String text,int cwords,int wei){
    	text = text.toLowerCase();
    	text = text.replaceAll("\n","");
        StringBuilder mid=new StringBuilder();//分隔符
        StringBuilder wword=new StringBuilder();//单词拼接
        Queue<String> que1=new LinkedList<String>();//用于存储词组单词
        Queue<String> que2=new LinkedList<String>();//用于存储分隔符
        String feng=",./;'[] \\<>?:\"{}|`~!@#$%^&*()_+-=";//分隔符集合
        StringTokenizer words = new StringTokenizer(text,feng,true); //分割文本成单词。
        try {
            while (words.hasMoreTokens()) {
                String word =words.nextToken();
                if (word.length() >= 4 && Character.isLetter(word.charAt(0)) && Character.isLetter(word.charAt(1)) && Character.isLetter(word.charAt(2)) && Character.isLetter(word.charAt(3))) {  //判断单词前4个是否为字母

                    que2.offer(mid.toString());
                    mid.delete(0,mid.length());
                    que1.offer(word);
                    if(que1.size()>=cwords){//达到词组单词数量
                        int cnt=0;
                        wword.delete(0,wword.length());
                        for(String w:que1){
                            wword.append(w);
                            cnt++;
                            if(que2.size()>cnt)
                            {
                                String tmp=((LinkedList<String>) que2).get(cnt);//取出中间的分隔符
                                wword.append(tmp);//拼接
                            }
                        }
                        //最后生成正确的wword 词组
                        // 进行统计操作
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
                else if(word.length()!=1){//不符合条件 将其前面的都删除
                    que1.clear();
                    que2.clear();
                }else if(word.length()==1 && !(Character.isLetter(word.charAt(0)))){//判断是否为分隔符
                    mid.append(word);
                }
            }
        }catch (Exception e){
            //出错
        }

    }
    
    /**
     * 词频排序
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
                    return o1.getKey().compareTo(o2.getKey());     //值相同 按键返回字典序
                return o2.getValue()-o1.getValue();
            }
            //逆序（从大到小）排列，正序为“return o1.getValue()-o2.getValue"
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
     * 写入文件
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
            //出错
        }
    }
    
    /**
     * 文件写入
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
            //出错
        }
    }
    
    /**
     * 文件读入
     * @param file
     */
    public String ReadFile(File file){
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder str = new StringBuilder();
            //清空数组
            titles.clear();
            abstracts.clear();
            PDFLink.clear();
            //开始读取
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
            //判断末尾
            //若末尾是abstract 则add数组
            //若末尾是pdflink  则add数组
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
             //出错
        }
        return "";
    }
    
    //定义访问器
    public ArrayList<String> getTitles(){
        return titles;
    }

    public ArrayList<String> getAbstracts(){
        return abstracts;
    }

    public ArrayList<String> getPDFLink() { return PDFLink;}
}



