
public class Main {
	public static void main(String[] args)
	{
		//long startTime = System.currentTimeMillis();
		
		
		//爬虫部分 去掉注释可用
		/*
		lib count = new lib();
		count.GetInfo();
		*/
		WordCount words = new WordCount();
		//CountPhrase cp = new CountPhrase();
		String inpath = null; //-i
		String outpath = null;	//-o
		int xx = 0;  //-w
		int yy = 1;  //-m
		int zz = 10;  //-n
		for(int i=0; i<args.length;i+=2)
		{
			//if(args[i].equals("-i")) ;
			//else if(args[i].equals("-o")) ;
			if(args[i].equals("-i"))
			{	
				inpath = args[i+1];
			}
			if(args[i].equals("-o"))
			{
				outpath = args[i+1];
			}
			if(args[i].equals("-w")){
				xx = Integer.parseInt(args[i+1]);
			}
			if(args[i].equals("-m"))
			{
				yy = Integer.parseInt(args[i+1]);
			}
			if(args[i].equals("-n"))
			{
				zz = Integer.parseInt(args[i+1]);
			}
		}
		
		words.CountChar(inpath);
		words.CountLine(inpath);
		words.CountWord(xx, yy, zz, inpath, outpath);
		
		
		//long endTime = System.currentTimeMillis();	
		//System.out.println("程序运行时间："+(endTime-startTime)+"ms");
		
	
	}
}
