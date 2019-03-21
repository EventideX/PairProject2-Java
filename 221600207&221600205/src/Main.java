import java.util.*;

public class Main {
    public Main(String instruct){
        try {
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
        b.setWord();
        b.getWord();
        } catch (NumberFormatException e) {    
            e.printStackTrace();    
        }
    }
    public static void main(String []args)
    {
        String instruct = " ";
        for(int i = 0; i < args.length; i++)
            instruct += args[i]+" ";
        //System.out.println(instruct);
        new Main(instruct);
    }
}
