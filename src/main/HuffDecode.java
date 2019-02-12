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
		String input_file_name = "data/compressed.dat";
		String output_file_name = "data/reuncompressed.txt";
		
		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);

		// this will hold our symbols with both the value and length property encapsulated
		List<SymbolWithCodeLength> symbols_with_length = new ArrayList<SymbolWithCodeLength>();
		
		System.out.println("Decoding the file(compressed): " + input_file_name);
		System.out.println("Into the following file(uncompressed): " + output_file_name);
		System.out.println();

		// Read in huffman code lengths from input file and associate them with each symbol as a 
		// SymbolWithCodeLength object and add to the list symbols_with_length.
		for (int i = 0; i < 256; i++) {
			int b = bit_source.next(8); // this is the value
			symbols_with_length.add(new SymbolWithCodeLength(i, b));
		}
		
		// Then sort the symbols.
		symbols_with_length.sort(null);

		// Now construct the canonical huffman tree
		HuffmanDecodeTree huff_tree = new HuffmanDecodeTree(symbols_with_length);
		
		int num_symbols = bit_source.next(32);
		System.out.println("Total number of Symbols to Decode: " + num_symbols);
		// Read in the next 32 bits from the input file  the number of symbols

		try {

			// Open up output file.
			
			FileOutputStream fos = new FileOutputStream(output_file_name);

			for (int i = 0; i < num_symbols; i++) {
				fos.write(huff_tree.decode(bit_source));
				//writes each symbol as a single byte
			}

			// Flush output and close files.
			
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println("Done");
		
	}
}


