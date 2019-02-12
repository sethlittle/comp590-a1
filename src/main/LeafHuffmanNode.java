package main;

public class LeafHuffmanNode implements HuffmanNode {
	
	private int _sym;
	private int _count;
	
	public LeafHuffmanNode(int count, int symbol) {
		_sym = symbol;
		_count = count;
	}
	
	public LeafHuffmanNode(int symbol) {
		_sym = symbol;
		_count = 0;
	}

	@Override
	public int count() {
		return _count;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public int symbol() {
		return _sym;
	}

	@Override
	public int height() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return true;
	}

	@Override
	public boolean insertSymbol(int length, int symbol) {
		if (this.isLeaf()) {
			throw new RuntimeException("Cannot insert onto a leaf node");
		}
		return false;
	}

	@Override
	public HuffmanNode left() {
		if (this.isLeaf()) {
			throw new RuntimeException("Leaf Nodes have no children");
		}
		return null;
	}

	@Override
	public HuffmanNode right() {
		if (this.isLeaf()) {
			throw new RuntimeException("Leaf Nodes have no children");
		}
		return null;
	}

}
