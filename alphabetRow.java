/**
 * 
 * @author Jude Murphy
 *
 */

public class alphabetRow 
{
	private int leaf;
	private char charFromFile;
	private String code;
	private double totalNum;
	private double weightedFrequency;
	
	public alphabetRow()
	{
		leaf = 0;
		charFromFile = 'a';
		totalNum = 1.0;
		weightedFrequency = 0.0;
		code = "00000000";
	}

	public alphabetRow(char charFromFile, double totalNum, double weightedFrequency, int leaf, String code)
	{
		this.leaf = leaf;
		this.charFromFile = charFromFile;
		this.totalNum = totalNum;
		this.weightedFrequency = weightedFrequency;
		this.code = code;
	}

	public char getcharFromFile() 
	{
		return charFromFile;
	}
	public void setcharFromFile(char charFromFile) 
	{
		this.charFromFile = charFromFile;
	}
	
	public double gettotalNum() 
	{
		return totalNum;
	}
	public void settotalNum(double totalNum) 
	{
		this.totalNum = totalNum;
	}
	
	public double getweightedFrequency() 
	{
		return weightedFrequency;
	}
	public void setweightedFrequency(double weightedFrequency) 
	{
		this.weightedFrequency = weightedFrequency;
	}

	public int getLeaf() {
		return leaf;
	}

	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() 
	{
		return charFromFile + " " + code + " " + leaf;
	}
}
