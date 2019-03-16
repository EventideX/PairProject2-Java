
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Lib {

    private static class MyComparator implements Comparator<Map.Entry<String, Integer>> {
        public int compare(Map.Entry<String,Integer>m1,Map.Entry<String,Integer>m2){
            int result = m2.getValue()-m1.getValue();
            if(result!=0)return result;
            else return m1.getKey().compareTo(m2.getKey());
        }
    }

    public static void fileOutPut(String outPutPath, ResultString rString,File outFile){
        FileWriter writer = null;
        try{
            outFile.createNewFile();
        }catch (IOException e){
            System.out.println("fail to create new file ");
        }

        try{
            writer = new FileWriter(outPutPath,true);
            for(int i=0;i<rString.outString.length();i++){
                writer.write(rString.outString.charAt(i));
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(writer != null){
                try{
                    writer.close();
                }catch (IOException e){
                    throw new RuntimeException("output float closed failured");
                }
            }
        }
    }

    public static void characterCount(String readPath,ResultString rString,int line){
        FileReader  reader = null;

        int character = 0;
        int articleNum = 0;

        try{
            reader = new FileReader(readPath);
            int ch=-1;
            while((ch=reader.read())!=-1) {
                if (ch==9||ch==10 || (ch >= 14 && ch <= 127)) character++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    throw new RuntimeException("FileReader close failure");
                }
            }
        }

        articleNum = line/2;
        character -= 3/2*(line-2)+1;
        character -=articleNum*17;
        character -= articleNum -1;

        if(line>=0&&line<=9)character -= line/2;
        else if(line>9&&line<=99){
            character -= 10;
            character -= (line - 20);
        }else if(line>99&&line<=999){
            character -= 190;
            character -= (line - 200)/2*3;
        }

        System.out.println("characters:"+character);
        rString.outString +="characters:"+character+"\r\n";
    }

    public  static void wordCount(Map<String,Integer> map,ResultString rString){
        int num = 0;
        for (Iterator<Map.Entry<String,Integer>> it = map.entrySet().iterator();it.hasNext();){
            it.next();
            num++;
        }

        System.out.println("word:"+num);
        rString.outString += "word:"+num+"\r\n";
    }

    public static int lineCount(File firstFile,ResultString rString) throws Exception{
        int line = 0;


        BufferedReader in =null;


        in = new BufferedReader(new InputStreamReader(new FileInputStream(firstFile),"utf-8"));

        String content = "";
        while((content = in.readLine())!=null){
            for(int i=0;i<content.length();i++){
                char ch=content.charAt(i);
                if((ch>=0&&ch<=32)||ch==127);
                else {
                    line++;
                    break;
                }
            }
        }
        if(null!=in){
            in.close();
        }

        line = (line- line/3);

        rString.outString += "line:"+line+"\r\n";

        return line;
    }

    public static  void putIntoMap(String result[],Pattern p,Map<String,Integer> map,int weight){
        for(int i=1;i<result.length;i++){
            Matcher m = p.matcher(result[i]);
            if(m.find()){
                String strMatch = m.group().toLowerCase();
                boolean foundOrNew = false;
                int num = 0;
                if(map.isEmpty()){
                    map.put(strMatch,weight);
                }else{
                    for(Iterator<Map.Entry<String,Integer>> it = map.entrySet().iterator();it.hasNext();){
                        Map.Entry<String,Integer> item = it.next();
                        if(strMatch.compareTo(item.getKey())==0){
                            num = item.getValue();
                            num += weight;
                            it.remove();
                            foundOrNew = true;
                            break;
                        }
                    }
                    if(foundOrNew)map.put(strMatch,num);
                    else map.put(strMatch,weight);
                }
            }
        }
    }

    public static void wordWeightCount(boolean isWeight,File firstFile,Map<String,Integer> map)throws  Exception{
        if(isWeight)    wordMatch(firstFile,map,10,1);
        else    wordMatch(firstFile,map,1,1);
    }

    public static void wordMatch(File firstFile,Map<String,Integer>map,int tWeight,int aWeight)throws Exception {
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(new FileInputStream(firstFile), "gbk"));

        String reg = "^[a-zA-Z]{4}.*";
        String result[] = null;
        String content = "";

        Pattern p = Pattern.compile(reg);

        boolean flag = true;

        while ((content = in.readLine()) != null) {
            result = content.split("[^0-9a-zA-Z]");
            if (result[0].compareTo("Title") == 0) {
                putIntoMap(result,p,map,tWeight);
            }else{
                if(result[0].compareTo("Abstract")==0){
                    putIntoMap(result,p,map,aWeight);
                }
            }
        }
    }

    public static void wordSort(Map<String,Integer>map,ResultString rString,int outPutNum){
        int num = 1,finishNum = 0;

        if(outPutNum == 0){
            finishNum = 10 ;
        }else finishNum = outPutNum;

        List<Map.Entry<String,Integer>> list = new ArrayList<>();
        list.addAll(map.entrySet());
        Lib.MyComparator myc=new MyComparator();
        Collections.sort(list,myc);
        for(Iterator<Map.Entry<String,Integer>>it =list.iterator();it.hasNext();){
            Map.Entry<String,Integer> item = it.next();
            System.out.println("<"+item.getKey()+">:"+item.getValue());
            rString.outString += "<"+item.getKey()+">:"+item.getValue()+"\r\n";
            if(num==finishNum)break;
            else num++;
        }
    }
}
