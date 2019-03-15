import java.io.*;
import java.util.*;
import java.util.regex.*;

public class CountAchieve implements Count {

    private BufferedReader bufferedReader;
    private String outputurl;
    private int wordlength;
    private int outputnumber;
    private boolean weightjudge;
    TreeMap<String,Integer> treeMap=new TreeMap<>();

    public CountAchieve(String inputurl,String outputurl,int wordlength,int outputnumber,boolean weightjudge)
            throws Exception
    {
        File file=new File(inputurl);
        bufferedReader=new BufferedReader(new FileReader(file));
        bufferedReader.mark((int)file.length()+1);
        this.outputurl=outputurl;
        this.wordlength=wordlength;
        this.outputnumber=outputnumber;
        this.weightjudge=weightjudge;
    }

    @Override
    public int CharCount() throws IOException {
        int count=0;
        bufferedReader.reset();
        int temp;
        while ((temp=bufferedReader.read())!=-1){
            count++;
            if(temp==13)
                bufferedReader.read();
        }
        return count;
    }

    @Override
    public int WordCount() throws IOException {
        int count=0;
        String line;
        bufferedReader.reset();
        StringBuffer stringBuffer=new StringBuffer();
        while ((line=bufferedReader.readLine())!=null)
            stringBuffer.append(line+"\n");
        String content=stringBuffer.toString();
        String [] words=content.split("([^a-zA-Z0-9]|\n)+");//1
        String [] division=content.split("[a-zA-Z0-9]+");//2

        int whofirst=1;
        if(words.length>0&&division.length>0){
            if(content.indexOf(words[0])<content.indexOf(division[0]))
                whofirst=1;
            else
                whofirst=2;
        }
        else if(words.length>0)
            whofirst=1;
        else if(division.length>0)
            whofirst=2;


        //用于处理m
        List<String> wordgroup=new ArrayList<>();

        Pattern pattern=Pattern.compile("^[a-zA-Z]{4,}[0-9]*[a-zA-Z]*");
        Integer value=0;
        String temp="";
        int weight=1;

//        for(int i=0;i<words.length;i++)
//            System.out.println(words[i].toLowerCase());

        for(int i=0,record=0;i<words.length;i++){
            //System.out.println(words[i].toLowerCase());
            if(weightjudge){
                if(words[i].equals("Title")){
                    weight=10;
                    temp="";
                    record=0;
                    wordgroup.clear();
                    continue;
                }
                else if(words[i].equals("Abstract")){
                    weight=1;
                    temp="";
                    record=0;
                    wordgroup.clear();
                    continue;
                }
            }
            else{
                if(words[i].equals("Title")||words[i].equals("Abstract")){
                    temp="";
                    record=0;
                    wordgroup.clear();
                    continue;
                }
            }

            if(pattern.matcher(words[i]).matches()){
                count++;
                words[i]=words[i].toLowerCase();
                temp+=words[i];
                record++;
                if(this.wordlength>1&&record<this.wordlength){ 

                    if((whofirst==1)&&(i<division.length)){
                        temp+=division[i];
                        wordgroup.add((words[i]+division[i]));
                    }
                    else if((whofirst==2)&&((i+1)<division.length)){
                        temp+=division[i+1];
                        wordgroup.add((words[i]+division[i+1]));
                    }
                    else
                        wordgroup.add((words[i]));
                }
                else if (this.wordlength>1&&record==this.wordlength){
                    if((whofirst==1)&&(i<division.length))
                        wordgroup.add((words[i]+division[i]));
                    else if((whofirst==2)&&((i+1)<division.length))
                        wordgroup.add((words[i]+division[i+1]));
                    else
                        wordgroup.add((words[i]));
                }
                if(record==this.wordlength){
                    if(treeMap.containsKey(temp)) {
                        value = treeMap.get(temp) + weight;
                        treeMap.put(temp, value);
                    }
                    else{
                        treeMap.put(temp,weight);
                    }
                    temp="";
                    if(this.wordlength>1){
                        for(int x=1;x<wordgroup.size();x++)
                            temp+=wordgroup.get(x);
                        wordgroup.remove(0);
                    }
                    record--;
                }
                else;
            }
            else{
                temp="";
                record=0;
                wordgroup.clear();
            }
        }
        return count;
    }

    @Override
    public int LineCount() throws IOException {
        int count=0;
        bufferedReader.reset();
        String line;
        while ((line=bufferedReader.readLine())!=null){
            if(!line.isEmpty())
                count++;
        }
        return count;
    }

    @Override
    public void OutPutWords() throws IOException{
        File file=new File(this.outputurl);
        PrintWriter writer=new PrintWriter(new FileWriter(file));
        writer.write("characters: "+this.CharCount()+"\n");
        writer.write("words: "+this.WordCount()+"\n");
        writer.write("lines: "+this.LineCount()+"\n");

        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(treeMap.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int i=0;
        for(Map.Entry<String,Integer> mapping:list) {
            writer.write("<"+mapping.getKey() + ">:" + mapping.getValue()+"\n");
            i++;
            if(i==this.outputnumber)
                break;
        }
        writer.close();
        CloseFile();
    }

    @Override
    public void CloseFile() throws IOException {
        bufferedReader.close();
    }
}
