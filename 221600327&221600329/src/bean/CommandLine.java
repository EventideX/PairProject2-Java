package bean;

public class CommandLine {
	
	//输入文件
	private String inputFileName;
	//输出文件
	private String outputFileName;
    //是否有权重
	private int weight;
	//几个词为一组
	private int wordGroup;
	//top词频，默认为top10
	private int wordFrequency;
	
	public CommandLine() {
		inputFileName="input.txt";
		outputFileName="output.txt";
		weight=0;	
		wordGroup=-1;
		wordFrequency=10;
	}
	//输入文件存取
	public String getInputFileName() {
		return this.inputFileName;
	}
	
	public void setInputFIleName(String fileName) {
		this.inputFileName=fileName;
	}
	//输出文件存取
	public String getoutputFileName() {
		return this.outputFileName;
	}
	
	public void setOutputFIleName(String fileName) {
		this.outputFileName=fileName;
	}
	//权重操作函数
	public int getWeight() {
		return this.weight;
	}
	
	public void setWeight(int weight) {
		this.weight=weight;
	}
	//词组操作函数
	public int getWordGroup() {
		return this.wordGroup;
	}
	
	public void setWordGroup(int wordGroup) {
		this.wordGroup=wordGroup;
	}
	//词频操作函数
	public int getWordFrequency() {
		return this.wordFrequency;
	}
	
	public void setWordFrequency(int wordFrequency) {
		this.wordFrequency=wordFrequency;
	}
	
	@Override
	public String toString() {
		return "inputFileName: "+inputFileName+"\n"+
				"outputFileName: "+outputFileName+"\n"+
				"weight: "+weight+"\n"+
				"wordGroup: "+wordGroup+"\n"+
				"wordFrequency: "+wordFrequency;
	}
}
