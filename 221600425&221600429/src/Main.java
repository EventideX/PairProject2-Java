import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pers.lhj.wordCount.Lib;

public class Main {
	public static void main(String[] args) throws IOException {
		
		String inputFile=null;
		String outputFile=null;
		Integer weightFlag=null;
		Integer	phraseFlag=null;
		Integer	numberFlag=null;
		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-i")) {
				if(i+1<args.length) {
					inputFile=args[i+1];
					i++;
				}
			}
			else if(args[i].equals("-o")) {
				if(i+1<args.length) {
					outputFile=args[i+1];
					i++;
				}
			}
			else if(args[i].equals("-w")) {
				if(i+1<args.length) {
					weightFlag=Integer.parseInt(args[i+1]);
					i++;
				}
			}
			else if(args[i].equals("-m")) {
				if(i+1<args.length) {
					phraseFlag=Integer.parseInt(args[i+1]);
					i++;
				}
			}
			else if(args[i].equals("-n")) {
				if(i+1<args.length) {
					numberFlag=Integer.parseInt(args[i+1]);
					i++;
				}
			}
			else {
				System.out.println("input error");
				System.exit(0);
			}
		}
		
		if(inputFile==null||outputFile==null||weightFlag==null) {
			System.exit(0);
		}
		Lib.solveInput(inputFile);
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(outputFile)));
		bw.write("characters: "+Lib.charNum("inputTemp.txt")+"\r\n");
		bw.write("words: "+Lib.letterNum("inputTemp.txt")+"\r\n");
		bw.write("lines: "+Lib.lineNum("inputTemp.txt")+"\r\n");
		
		if(numberFlag==null) {
			numberFlag=10;
		}
		if(phraseFlag==null) {
			phraseFlag=1;
		}
		List<String> list=new ArrayList<String>();
		List<Integer>list1=new ArrayList<Integer>();
		Lib.getWordFrequency(inputFile, list, list1, weightFlag,numberFlag);
		for(int i=0;i<list.size();i++) {
			bw.write("<"+list.get(i)+">: "+list1.get(i)+"\r\n");
		}
		bw.close();
	}
	
}
