public class Main {

    public static void main(String[] args){
        try{
            if(args.length == 0){
                throw new IllegalArgumentException();
            }
            lib lib = new lib();
            for (int i = 0; i < args.length; i++) {
                if(args[i].equals("-i")){
                    lib.orders.put("-i",args[i+1]);
                    i++;
                }else if(args[i].equals("-o")){
                    lib.orders.put("-o",args[i+1]);
                    i++;
                }else if(args[i].equals("-w")){
                    lib.orders.put("-w",args[i+1]);
                    i++;
                }else if(args[i].equals("-m")){
                    lib.orders.put("-m",args[i+1]);
                    i++;
                }else if(args[i].equals("-n")){
                    lib.orders.put("-n",args[i+1]);
                    i++;
                }
            }
            lib.terminal();
            lib.readFile();
            lib.dealFile();
            lib.writeFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
