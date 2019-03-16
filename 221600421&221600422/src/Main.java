
public class Main {

    public static void main(String []args){
        try {
            String ii = "", o = "", w = "", m = "1", n = "10";
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-i":
                        ii = args[++i];
                        break;
                    case "-o":
                        o = args[++i];
                        break;
                    case "-w":
                        w = args[++i];
                        break;
                    case "-m":
                        m = args[++i];
                        break;
                    case "-n":
                        n = args[++i];
                        break;
                    default:
                        break;
                }
            }
            boolean judge = false;
            if (m.equals("1"))
                judge = true;
            CountAchieve count = new CountAchieve(ii, o, Integer.parseInt(m), Integer.parseInt(n), judge);
            count.OutPutWords();
        }catch(Exception t){
            System.out.println(t.getMessage());
        }
    }
}
