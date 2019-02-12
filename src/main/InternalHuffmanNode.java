package main;

public class InternalHuffmanNode implements HuffmanNode {

	private HuffmanNode _left;
	private HuffmanNode _right;
	
	public InternalHuffmanNode(HuffmanNode left, HuffmanNode right) {
		_left = left;
		_right = right;
	}
	public InternalHuffmanNode() { // to create empty nodes
		_left = null;
		_right = null;
	}
	
	@Override
	//add counts of children
	public int count() {
		return _left.count() + _right.count();
	}

	@Override
	//internal node
	public boolean isLeaf() {
		return false;
	}

	@Override
	//internal nodes don't have a symbol
	public int symbol() {
		if (!isLeaf()) {
			throw new RuntimeException("Internal Nodes dont have symbols");
		}
		return 0;
	}

	@Override
	//returns the height of each left and right child and updates Lh and Rh
	public int height() {
		int Lh = 0;
		int Rh = 0;
		if (this.left() != null) {
			Lh = this.left().height();
		}
		if (this.right() != null) {
			Rh = this.right().height();
		}
		
		//add one for the root
		if (Lh >= Rh) {
			return Lh + 1;
		} else {
			return Rh + 1;
		}
	}

	@Override
	//must check first if the children are not null
	//then checks to see if both children are full
	public boolean isFull() {
		if (this.left() != null && this.right() != null) {
			if (this.left().isFull() && this.right().isFull()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean insertSymbol(int length, int symbol) {
		// insertSymbol() attempts to insert leaf node for given
		// symbol under this node that is length distance
		// away and follows rules for canonical tree construction:
		//  * First try to go left
		//  * If not possible or fails, then try to go right
		//  * If not possible or fails, return false to indicate failure.
		//
		// Returns true if new leaf node was successfully inserted, 
		// false otherwise. Should be able to implement this recursively,
		// reducing length by 1 for each recursion until length is 1 at which
		// point new leaf node with symbol should be created and attached
		// if possible. 
		//
		if (length < 0 || symbol < 0) {
			return false;
		}
		
		if (length == 1) {
			if (this.left() == null) {
				this._left = new LeafHuffmanNode(symbol);
				return true;
			} else if (this.right() == null) {
				this._right = new LeafHuffmanNode(symbol);
				return true;
			} else {
				return false;
			}
				
		}
		
		if (this.left() == null || !this.left().isFull()) {
			if (this.left() == null) {
				this._left = new InternalHuffmanNode();
				this._left.insertSymbol(length - 1, symbol);
			} else {
				this.left().insertSymbol(length - 1, symbol);
			}
		} else if (this.right() == null || !this.right().isFull()) {
			if (this.right() == null) {
				this._right = new InternalHuffmanNode();
				this._right.insertSymbol(length - 1, symbol);
			} else {
				this.right().insertSymbol(length - 1, symbol);
			}
		} else {
			return false;
		}
		
		
		return false;
	}

	@Override
	public HuffmanNode left() {
		return _left;
	}

	@Override
	public HuffmanNode right() {
		return _right;
	}

}
