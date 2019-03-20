import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    private static final String TITLE="title: ";
    private static final String ABSTRACT="abstract: ";
    public static void main(String args[]) throws IOException {

        Map<String,String> map= Lib.getParameter(args);

        /* 获取参数*/
        String intputPath=map.get(Lib.I);
        String outputPath=map.get(Lib.O);
        String M=map.get(Lib.M);
        int w=0;
        String W=map.get(Lib.W);
        int n=10;
        String N=map.get(Lib.N);

        /* 读入文件 */
        List<StringBuilder> stringBuilders= Lib.readFile(intputPath);

        /* 获得文件当中Title和Abstract的内容 */
        List<StringBuilder> title= Lib.getContent(1,stringBuilders);
        List<StringBuilder> anAbstract= Lib.getContent(2,stringBuilders);

        /* 打开输出流*/
        BufferedOutputStream bf=new BufferedOutputStream(new FileOutputStream(new File(outputPath)));

        /* 输出字符数 */
        bf.write(("characters: "+ (Lib.countChars(title,TITLE)+ Lib.countChars(anAbstract,ABSTRACT))+"\r\n").getBytes());

        /* 用于输出的哈希表*/
        Map<String,Integer> hashTable=null;

        if(W!=null){
            w=Integer.valueOf(W);
        }

        /* map1存储Title统计结果，map2统计Abstract统计结果 */
        int map1Number=Lib.countWords(TITLE,title);
        int map2Number =Lib.countWords(ABSTRACT,anAbstract);
        /* 输出单词统计结果*/
        bf.write(("words: "+(map2Number+map1Number)+"\r\n").getBytes());

        Map<String,Integer> map1=null;
        Map<String,Integer> map2=null;
        if(M!=null){
            int m=Integer.valueOf(M);
            map1= Lib.countWords(TITLE.length(),m,title);
            map2= Lib.countWords(ABSTRACT.length(),m,anAbstract);
        }else{
            map1= Lib.countWords(TITLE.length(),1,title);
            map2= Lib.countWords(ABSTRACT.length(),1,anAbstract);
        }
        /* 根据权重合并两个map */
        hashTable=Lib.combine(map1,map2,w);

        /* 输出行数统计结果 */
        bf.write(("lines: "+(title.size()*2)+"\r\n").getBytes());

        /* 输出词频统计结果*/
        int count=0;
        if(N!=null){
            n=Integer.valueOf(N);
        }
        for(String key:hashTable.keySet()){
            bf.write(("<"+key+">: "+hashTable.get(key)+"\r\n").getBytes());
            if(++count==n)break;
        }
        bf.flush();
        bf.close();
    }


}
