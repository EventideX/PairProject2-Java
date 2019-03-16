
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
			else if (args[i].equals("-m")) {
				if(Integer.parseInt(args[i+1])>=0)
					phraseSize = Integer.parseInt(args[i + 1]);
				else System.out.println("-m����ӦΪ��Ȼ����Ĭ�Ͻ��е���ͳ��");
			}
			else if (args[i].equals("-n"))
				if(Integer.parseInt(args[i+1])>=0) 
					resultCnt = Integer.parseInt(args[i + 1]);
				else System.out.println("-n����ӦΪ��Ȼ����Ĭ�����ǰʮλ����");
		}
	}
}
