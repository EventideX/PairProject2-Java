package unitl;

import bean.CommandLine;

public class Command {
	public static boolean isInt(String value) {  
        try {  
            Integer.parseInt(value);  
        } catch (NumberFormatException e) {  
            return false;  
        }  
        return true;  
    }  
	
		/**
		 * 解析命令行参数
		 * @param args 参数
		 * @return CommandLine对象
		 */
		public CommandLine ParseCommand(String[] args) {
			//使用CommandLine来存储相对应的命令
			CommandLine CommandLine=new CommandLine();
			int length=args.length;
			//命令行拆分
			for(int i=0;i<length;i++) {
				switch(args[i]) {
				//读入输入文件名
				case "-i":
					try {
					    CommandLine.setInputFIleName(args[i+1]);
					}catch(Exception e) {
						e.printStackTrace();
					}
//					System.out.println("input "+CommandLine.getInputFileName());
					i++;//参数存在则移到下一位
					break;
			        //读入输出文件名
				case "-o":
					try {
					    CommandLine.setOutputFIleName(args[i+1]);
					}catch(Exception e) {
						e.printStackTrace();
					}
//					System.out.println("output "+CommandLine.getoutputFileName());
					i++;
					break;
					//读入权重
				case "-w":
					try {
						CommandLine.setWeight(Integer.valueOf(args[i+1]));
					}catch(Exception e) {
						e.printStackTrace();
					}
					i++;
					break;
					//词组
				case "-m":
					if(i<(length-1)) {
						if(isInt(args[i+1])) {
							try {
								CommandLine.setWordGroup(Integer.valueOf(args[i+1]));
							}catch(Exception e) {
								e.printStackTrace();
							}
							i++;
						}
					}
//					System.out.println(CommandLine.getWordGroup());
					break;
					//词频
				case "-n":
					if(i<(length-1)) {
						if(isInt(args[i+1])) {
							try {
								CommandLine.setWordFrequency(Integer.valueOf(args[i+1]));
							}catch(Exception e) {
								e.printStackTrace();
							}
							i++;
						}
					}
//					System.out.println(CommandLine.getWordFrequency());
					break;
				default:
//					System.out.println("No such command!");
					break;
				}
			
		    }
			return CommandLine;
		}
}
