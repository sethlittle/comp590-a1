package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.OutputStreamBitSink;

public class HuffmanEncoder {
		
	private Map<Integer, String> _code_map;
	
	public HuffmanEncoder(int[] symbols, int[] symbol_counts) {
		assert symbols.length == symbol_counts.length;
		
		// Given symbols and their associated counts, create initial
		// Huffman tree. Use that tree to get code lengths associated
		// with each symbol. Create canonical tree using code lengths.
		// Use canonical tree to form codes as strings of 0 and 1
		// characters that are inserted into _code_map.

		// Start with an empty list of nodes
		List<HuffmanNode> node_list = new ArrayList<HuffmanNode>();
		
		// Create a leaf node for each symbol, encapsulating the
		// frequency count information into each leaf.
		for (int i = 0; i < symbols.length ;i++) {
			node_list.add(new LeafHuffmanNode(symbol_counts[i], symbols[i]));
		}

		// Sort the leaf nodes
		node_list.sort(null);

		// While you still have more than one node in your list...
		while(node_list.size() > 1) {
			// Remove the two nodes associated with the smallest counts
			HuffmanNode left = node_list.remove(0);
			HuffmanNode right = node_list.remove(0); // because the position at 1 would have shifted to 0
			
			// Create a new internal node with those two nodes as children.
			InternalHuffmanNode internalNode = new InternalHuffmanNode(left, right);
			
			// Add the new internal node back into the list
			node_list.add(internalNode);
			
			// Resort
			node_list.sort(null);
		}
		
		//now the size of this list should be equal to 1
		assert node_list.size() == 1;
		
		// Create a temporary empty mapping between symbol values and their code strings
		Map<Integer, String> cmap = new HashMap<Integer, String>();

		// Start at root and walk down to each leaf, forming code string along the
		// way (0 means left, 1 means right). Insert mapping between symbol value and
		// code string into cmap when each leaf is reached.
		for (int i = 0; i < symbols.length; i++) {
			traverseFromRoot(cmap, node_list.get(0), "", i);	//helper method because do this same code twice		
		}
		
		// Create empty list of SymbolWithCodeLength objects
		List<SymbolWithCodeLength> sym_with_length = new ArrayList<SymbolWithCodeLength>();

		// For each symbol value, find code string in cmap and create new SymbolWithCodeLength
		// object as appropriate (i.e., using the length of the code string you found in cmap).
		for (int i = 0; i < symbols.length; i++) {
			sym_with_length.add(new SymbolWithCodeLength(i, cmap.get(i).length()));
		}
		
		// Sort sym_with_lenght
		sym_with_length.sort(null);

		// Now construct the canonical tree as you did in HuffmanDecodeTree constructor
		InternalHuffmanNode canonical_root = new InternalHuffmanNode();		
		for (int i = 0; i < sym_with_length.size(); i++) {
			canonical_root.insertSymbol(sym_with_length.get(i).codeLength(), sym_with_length.get(i).value());
		}

		// If all went well, tree should be full.
		assert canonical_root.isFull();
		
		// Create code map that encoder will use for encoding
		_code_map = new HashMap<Integer, String>();
		
		// Walk down canonical tree forming code strings as you did before and
		// insert into map.		
		for (int i = 0; i < symbols.length; i++) {
			traverseFromRoot(_code_map, canonical_root, "", i);
		}
				
	}
	
	//helper method
	public void traverseFromRoot(Map<Integer, String> map, HuffmanNode curr, String codeLength, int i) {	
			// this method is what is used to traverse from the root node to each of the inidividual children
			//this method is looking for node i
		
			if (curr.isLeaf() && curr.symbol() == i) {
					map.put(i, codeLength);
					return;
			}
			
			// for each node - take the codeLength and pass it back in recursively, 1 for right and 0 for left
			if (!curr.isLeaf()) {
				traverseFromRoot(map, curr.right(), codeLength + "1", i);
				traverseFromRoot(map, curr.left(), codeLength + "0", i);
			}
						
	}

	public String getCode(int symbol) {
		return _code_map.get(symbol);
	}

	public void encode(int symbol, OutputStreamBitSink bit_sink) throws IOException {
		//writes each 8 bit encoded value
		bit_sink.write(_code_map.get(symbol).length(), 8);
	}
	

}
