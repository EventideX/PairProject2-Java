
public class Analysis {
	private String[] args;
	private String inputFilePath;
	private String outputFilePath;
	private int weight;
	private int phraseSize=1;//Ĭ�Ͻ��е��ʴ�Ƶͳ��
	private int resultCnt=10;//Ĭ�����10��

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
			else if (args[i].equals("-m"))
				phraseSize = Integer.parseInt(args[i + 1]);
			else if (args[i].equals("-n"))
				resultCnt = Integer.parseInt(args[i + 1]);
		}
	}
}
