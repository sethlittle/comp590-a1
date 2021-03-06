package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.OutputStreamBitSink;

public class HuffEncode {

	public static void main(String[] args) throws IOException {
		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/recompressed.txt";

		FileInputStream fis = new FileInputStream(input_file_name);

		int[] symbol_counts = new int[256];
		int num_symbols = 0;
		
		System.out.println("Encoding the file(uncompressed): " + input_file_name);
		System.out.println("Into the following file(compressed): " + output_file_name);
		System.out.println();

		// Read in each symbol (i.e. byte) of input file and 
		// update appropriate count value in symbol_counts
		// Should end up with total number of symbols 
		// (i.e., length of file) as num_symbols
		int next_byte = fis.read();
		while (next_byte != -1) {
			symbol_counts[next_byte]++;
			num_symbols++;
			next_byte = fis.read();
		}
	
		//FOR PART 3
		double[] probabilities = new double[256];
		for (int i = 0; i < 256; i++) {
			probabilities[i] = ((double) symbol_counts[i]/num_symbols);
		}
		
		//Using equation 2.4 on page 15
		double entropy = 0.0;
		double sum = 0.0;
		for (int i = 0; i < probabilities.length; i++) {
			if ((probabilities[i] * (Math.log(probabilities[i]) / Math.log(2))) < 0) { // to exclude the NaN values
				sum = sum + (probabilities[i] * (Math.log(probabilities[i]) / Math.log(2)));
			}
		}
		entropy = -sum;
		System.out.println("Theoretical entropy of the source message in bits per symbol: " + entropy + " bits/symbol");
		
		// Close input file
		fis.close();

		// Create array of symbol values
		int[] symbols = new int[256];
		for (int i=0; i<256; i++) {
			symbols[i] = i;
		}
		
		// Create encoder using symbols and their associated counts from file.
		HuffmanEncoder encoder = new HuffmanEncoder(symbols, symbol_counts);
		
		// Open output stream.
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);

		// Write out code lengths for each symbol as 8 bit value to output file.
		for (int i = 0; i < 256; i++) {
			encoder.encode(symbols[i], bit_sink);
		}
		
		// Write out total number of symbols as 32 bit value.
		bit_sink.write(num_symbols,32);
		System.out.println("Total number of symbols to encode: " + num_symbols);

		// Reopen input file.
		fis = new FileInputStream(input_file_name);

		// Go through input file, read each symbol (i.e. byte),
		// look up code using encoder.getCode() and write code
        // out to output file.
		while (fis.available() > 0) { // gives the estimated bytes remaining to encode
			bit_sink.write(encoder.getCode(fis.read()));
		}

		// Pad output to next word.
		bit_sink.padToWord();

		// Close files.
		fis.close();
		fos.close();
	}
}

