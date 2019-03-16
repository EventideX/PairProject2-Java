import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args)throws Exception{
        String inFilePath = "D:/java/cspv/input2.txt";
        String outFilePath="D:/java/cspv/rs4.txt";

        int line;

        boolean isWeight=false;

        int outPutNum = 0;
        for(int i=0;i<args.length;i++){
            if((args[i].compareTo("-i"))==0){
                i++;
                inFilePath=args[i];
            }else if((args[i].compareTo("-o"))==0){
                i++;
                outFilePath = args[i];
            }else if((args[i].compareTo("-w"))==0){
                i++;
                if(args[i].compareTo("1")==0)isWeight =true;
                else if(args[i].compareTo("0")==0);
            }else if(args[i].compareTo("-n")==0){
                i++;
                outPutNum = Integer.parseInt(args[i]);
            }else if(args[i].compareTo("-m")==0){
                i++;
                System.out.println("-m function is disabled");
            }
        }

        File firstFile = new File(inFilePath);
        File outFile =new File(outFilePath);

        ResultString result=new ResultString();
        line = Lib.lineCount(firstFile,result);
        Lib.characterCount(inFilePath,result,line);
        System.out.println("line:"+line);
        Lib.wordWeightCount(isWeight,firstFile,result.map);
        Lib.wordCount(result.map,result);
        Lib.wordSort(result.map,result,outPutNum);
        Lib.fileOutPut(outFilePath,result,outFile);

    }
}

class ResultString{

    Map<String, Integer> map = new HashMap<String, Integer>();

    String outString="";
}