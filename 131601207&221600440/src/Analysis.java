
public class Analysis {
	private String[] args;
	private String inputFilePath;
	private String outputFilePath;
	private int weight;
	private int phraseSize=1;//默认进行单词词频统计
	private int resultCnt=10;//默认输出10个

	public Analysis(String args[]) {
		this.args = args;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public int getWeight() {
		return weight;
	}

	public int getPhraseSize() {
		return phraseSize;
	}

	public int getResultCnt() {
		return resultCnt;
	}

	public void analyse() {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i"))
				inputFilePath = args[i + 1];
			else if (args[i].equals("-o"))
				outputFilePath = args[i + 1];
			else if (args[i].equals("-w"))
				weight = Integer.parseInt(args[i + 1]);
			else if (args[i].equals("-m")) {
				if(Integer.parseInt(args[i+1])>=0)
					phraseSize = Integer.parseInt(args[i + 1]);
				else System.out.println("-m参数应为自然数，默认进行单词统计");
			}
			else if (args[i].equals("-n"))
				if(Integer.parseInt(args[i+1])>=0) 
					resultCnt = Integer.parseInt(args[i + 1]);
				else System.out.println("-n参数应为自然数，默认输出前十位数据");
		}
	}
}
