import java.io.*;
import java.util.*;

public class Main {
	public Main(){
		Scanner in = new Scanner(System.in);
		String instruct = in.nextLine();
		lib b = new lib();
		String [] ch = instruct.split("-");
		for(int i = 1; i < ch.length; ++i)
		{
			//System.out.println(ch[i]);
			switch(ch[i].charAt(0)){
				case 'i'|'I' : {
					String fin = ch[i].substring(1, ch[i].length()).trim();
					b.setFileInput(fin);
					//System.out.println(fin);
					b.setWord();
					break;
				}
				case 'o'|'O' : {
					String fout = ch[i].substring(1, ch[i].length()).trim();
					//System.out.println(fout);
					b.setFileOutput(fout);
					break;
				}
				case 'w'|'W' : {
					int w = Integer.parseInt(ch[i].substring(1, ch[i].length()).trim());
					//System.out.println(w+1);
					b.setWValue(w);
					break;
				}
				case 'm'|'M' : {
					break;
				}
				case 'n'|'N' : {
					int n = Integer.parseInt(ch[i].substring(1, ch[i].length()).trim());
					//System.out.println(n+1);
					b.setMaxWordNum(n);
					break;
				}
				default : {
					System.out.println("找不到该指令");
				}
			}
		}
		b.getWord();
	}
	public static void main(String []args)
	{
		new Main();
	}
}
