/**
 * 
 * @author Jude Murphy
 *
 */

public class Forest implements Comparable<Forest>
{
	private int root;
	private double frequency;

	public Forest ()
	{
		root = 0;
		frequency = 0.0;
	}

	public Forest (double frequency, int root)
	{
		this.frequency = frequency;
		this.root = root;
	}

	public int getRoot() 
	{
		return root;
	}
	public void setRoot(int root) 
	{
		this.root = root;
	}

	public double getfrequency() 
	{
		return frequency;
	}
	public void setfrequency(double frequency) 
	{
		this.frequency = frequency;
	}

	@Override
	public int compareTo(Forest otherForest) 
	{
		return new Double(this.frequency).compareTo(new Double(otherForest.frequency));
	}

	// *** USE THIS SPECIFIC TO STRING METHOD FOR DECODING LATER ON
	public String toString() 
	{
		return "ForestObject [frequency=" + frequency + ", root=" + root + "]";
	}
}
