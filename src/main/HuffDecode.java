package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class HuffDecode {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		String input_file_name = "data/recompressed.txt";
		String output_file_name = "data/reuncompressed.txt";
		
		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);

		List<SymbolWithCodeLength> symbols_with_length = new ArrayList<SymbolWithCodeLength>();

		// Read in huffman code lengths from input file and associate them with each symbol as a 
		// SymbolWithCodeLength object and add to the list symbols_with_length.
		
		for (int i = 0; i < 256; i++) {
			int b = bit_source.next(8);
			symbols_with_length.add(new SymbolWithCodeLength(i, b));
		}
		
		// Then sort they symbols.
		symbols_with_length.sort(null);

		// Now construct the canonical huffman tree
		HuffmanDecodeTree huff_tree = new HuffmanDecodeTree(symbols_with_length);
		
		int num_symbols = bit_source.next(32);

		// Read in the next 32 bits from the input file  the number of symbols

		try {

			// Open up output file.
			
			FileOutputStream fos = new FileOutputStream(output_file_name);

			for (int i = 0; i < num_symbols; i++) {
				fos.write(huff_tree.decode(bit_source));
			}

			// Flush output and close files.
			
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}


