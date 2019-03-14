import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lib {
    private static final int HAN_ZI=0x80;
    /* 要输入的文件的路径*/
    public static final String I ="I";
    /*要输出的文件的路径*/
    public static final String O ="O";
    /*是否采用不同权重来统计词频*/
    public static final String W ="W";
    /* 开启词频统计*/
    public static final String M ="M";
    /* 设定要输出的频率最高的单词或词组的个数*/
    public static final String N ="N";
    /**
     * 统计字符数
     * @param stringList
     * @return
     */
    public static int countChars(List<StringBuilder> stringList,String str) {
        int sum=0;
        for(StringBuilder line :stringList){
            if(line.indexOf(str)!=-1){
                sum+=(line.length()-str.length());
                System.out.print(line);
            }else{
                sum+=line.length();
            }
        }
        return sum;
    }

    /**
     * 读取文件到ListStringBuilder中
     * @param arg
     * @return
     * @throws IOException
     */
    public static List<StringBuilder> readFile(String arg) throws IOException {
        BufferedInputStream file=new BufferedInputStream(new FileInputStream(arg));
        List<StringBuilder> stringBuilders=new ArrayList<>();
        int pre=0,b;
        StringBuilder builder=new StringBuilder();
        while((b=file.read())!=-1){
            if(pre=='\r'&&b=='\n'){
                //将\r\n改为\n
                builder.append((char)b);
                if(builder.lastIndexOf("\r\n")!=-1){
                    builder.deleteCharAt(builder.length()-2);
                }
                stringBuilders.add(builder);
                builder=new StringBuilder();
            }else if(b<HAN_ZI){
                if(b<='Z'&&b>='A')b+=32;
                builder.append((char)b);
            }
            pre=b;
        }
        if(builder.lastIndexOf("\r\n")!=-1){
            builder.deleteCharAt(builder.length()-2);
        }
        stringBuilders.add(builder);
        return stringBuilders;
    }
    /**
     * 去除List<StringBuilder>中的空白
     * @param stringBuilders
     * @return
     */
    public static List<StringBuilder> removeSpace(List<StringBuilder> stringBuilders){
        List<StringBuilder> res=new ArrayList<>();
        int i;
        for (StringBuilder stringBuilder : stringBuilders) {
            i = 0;
            while (i < stringBuilder.length()) {
                if (stringBuilder.charAt(i) > 32 && stringBuilder.charAt(i) < 127) {
                    break;
                }
                i++;
            }
            if (i != stringBuilder.length()) {
                res.add(stringBuilder);
            }
        }
        return res;
    }

    /**
     * 统计词组词频
     * @param stringList
     * @return
     */
    static Map<String,Integer> countWords(int delete, int m, List<StringBuilder> stringList){
        Map<String,Integer> hashTable=new LinkedHashMap<>();

        // 创建 Pattern 对象
        String patternString = "^[a-z]{4}[a-z0-9]*";
        Pattern pattern = Pattern.compile(patternString);

        boolean flag;
        for(StringBuilder line:stringList){
            String volatileString= "";
            if(line.length()>delete) {
                volatileString = line.substring(delete);
            }
            String notAlphaandNumber="[^a-z0-9]+";
            String [] strings=volatileString.split(notAlphaandNumber);
            for(int i=0;i<strings.length-m+1;i++){

                //是否全为单词
                flag=true;
                for(int j=i;j<i+m&&j<strings.length;++j){
                    Matcher matcher = pattern.matcher(strings[j]);
                    if(!matcher.find()){
                        flag=false;
                        break;
                    }
                }
                //如果全为单词
                if(flag){
                    StringBuilder regEx= new StringBuilder();
                    for(int k=0;k<m;++k){
                       regEx.append(strings[i + k]);
                       if(k!=m-1)regEx.append(notAlphaandNumber);
                    }
                    Pattern compile = Pattern.compile(regEx.toString());
                    Matcher matcher=compile.matcher(volatileString);
                    if(matcher.find()){
                        hashTable.merge(matcher.group(), 1, (a, b) -> a + b);
                    }
                }
                int beginIndex=volatileString.indexOf(strings[i]);
//                int endIndex=volatileString.indexOf(strings[i+m-1])+strings[i+m-1].length();
                int endIndex=volatileString.indexOf(strings[i])+strings[i].length();
//                volatileString=volatileString.substring(0,beginIndex)+volatileString.substring(endIndex);
                volatileString=volatileString.substring(0,beginIndex)+volatileString.substring(endIndex);
            }
        }
        return hashTable;
    }

    /**
     * 对Map进行排序
     * @param map
     * @return
     */
    private static Map<String,Integer> sort(Map<String, Integer> map){
        List<Map.Entry<String, Integer>> keyList= new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        keyList.sort( new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if(o2.getValue().equals(o1.getValue())){
                    String key1=o1.getKey();
                    String key2=o2.getKey();
                    for(int i=0;i<key1.length()&&i<key2.length();++i){
                        if(key1.charAt(i)>key2.charAt(i)){
                            return 1;
                        }else if(key1.charAt(i)<key2.charAt(i)){
                            return -1;
                        }
                    }
                    return key1.length()>key2.length()?1:-1;
                }
                return Integer.compare(o2.getValue().compareTo(o1.getValue()), 0);
            }

        });
        map.clear();
        for(Map.Entry<String, Integer> entry:keyList){
            map.put(entry.getKey(),entry.getValue());
        }
        return map;
    }

    /**
     * 获得参数
     * @param args
     * @return
     */
    static Map<String,String> getParameter(String[] args) {
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<args.length;++i){
            switch (args[i]) {
                case "-i":
                    map.put(I, args[i + 1]);
                    break;
                case "-o":
                    map.put(O, args[i + 1]);
                    break;
                case "-w":
                    map.put(W, args[i + 1]);
                    break;
                case "-m":
                    map.put(M, args[i + 1]);
                    break;
                case "-n":
                    map.put(N, args[i + 1]);
                    break;
                default:
                    break;
            }
        }
        return map;
    }

    /**
     * 将文件分割成Title和Abstrat两个部分
     * @param lineNumber
     * @param stringBuilders
     * @return
     */
    static List<StringBuilder> getContent(int lineNumber, List<StringBuilder> stringBuilders) {
        int i=lineNumber;
        List<StringBuilder> stringBuilderList=new ArrayList<>();
        while(i<stringBuilders.size()){
            stringBuilderList.add(stringBuilders.get(i));
            i+=5;
        }
        return stringBuilderList;
    }

    /**
     * 统计单词数
     * @param stringList
     * @return
     */
    static int countWords(String delete, List<StringBuilder> stringList){
        Map<String,Integer> hashTable=new LinkedHashMap<>();
        String spilt="[^a-z0-9]";
        String patternString = "^[a-z]{4}[a-z0-9]*";
        Pattern pattern=Pattern.compile(patternString);
        int sum=0;
        for(StringBuilder line:stringList){
            String temp= "";
            if(line.length()>delete.length()&&line.indexOf(delete)!=-1){
                temp=line.substring(delete.length());
            }
            String []array=temp.split(spilt);
            for(String str:array){
                Matcher matcher=pattern.matcher(str);
                if(matcher.find()){
                    sum+=1;
                }
            }
        }
        return sum;
    }

    public static Map<String, Integer> combine(Map<String, Integer> map1, Map<String, Integer> map2,int w) {
        if(w==1){
            for(String key:map1.keySet()){
                map1.put(key,map1.get(key)*10);
            }
        }
        for(String key:map1.keySet()){
            if(map2.get(key)!=null){
                map2.put(key,map2.get(key)+map1.get(key));
            }else{
                map2.put(key,map1.get(key));
            }
        }
        sort(map2);
        return map2;
    }
}
