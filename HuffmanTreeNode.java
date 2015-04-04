public class HuffmanTreeNode 
{
	private int left;
	private int right;
	private int parent;

	public HuffmanTreeNode (int left, int right, int parent)
	{
		this.left = left;
		this.right = right;
		this.parent = parent;
	}
	
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	
	@Override
	public String toString() {
		return "[left=" + left + ", right=" + right
				+ ", parent=" + parent + "]";
	}
}
