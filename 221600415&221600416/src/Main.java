import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args)throws Exception{
		lib lib=new lib();

		Map<String,String> mapArgs=new HashMap<>();
		if(args.length>0){//命令行输入
			for(int i=0;i<args.length;i+=2){
				mapArgs.put(args[i],args[i+1]);
			}
		}
		String inputPath=mapArgs.get("-i");
		String outputPath=mapArgs.get("-o");
		String isWeightStr=mapArgs.get("-w");
		String mStr=mapArgs.get("-m");
		String nStr=mapArgs.get("-n");

		boolean isM=false;//自定义词频统计输出
		boolean isN=false;//多参数的混合使用
		Integer m=0;
		Integer n=0;

		if(mStr!=null){
			isM=true;
			m=Integer.parseInt(mStr);
		}
		if(nStr!=null){
			isN=true;
			n=Integer.parseInt(nStr);
		}

		boolean isWeight=false;
		if(isWeightStr.equals("0")){
			isWeight=false;
		}
		else if(isWeightStr.equals("1")) {
			isWeight=true;
		}

		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath))));

		Map<String,Integer> map=lib.countLettersInPapers(inputPath);
		Integer characters=map.get("charCount");
		Integer lines=map.get("lines");
		Integer words=map.get("wordCount");

		bw.write("characters: "+characters+"\r\n");
		bw.write("words: "+words+"\r\n");
		bw.write("lines: "+lines+"\r\n");
		bw.flush();

		if(isM){
			Map<String,Integer> arrayMap;
			if(isN){
				arrayMap=lib.getTopTenWordarray(inputPath, m,n,isWeight);
			}
			else {
				arrayMap=lib.getTopTenWordarray(inputPath, m,10,isWeight);
			}
			for(String key:arrayMap.keySet()){
				bw.write("<"+key+">: "+arrayMap.get(key)+"\r\n");
			}
			bw.flush();
		}
		else {
			Map<String,Integer> wordMap;
			if(isN){
				wordMap=lib.getTopTenWeightWords(inputPath,n,isWeight);
			}
			else {
				wordMap=lib.getTopTenWeightWords(inputPath,10,isWeight);
			}
			for(String key:wordMap.keySet()){
				bw.write("<"+key+">: "+wordMap.get(key)+"\r\n");
			}
			bw.flush();
		}
		bw.close();
	}
}