package pad.ijvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BinaryLoader {
	byte [] bytes;

public void readFile(File binary) throws IOException{
		
		bytes = new byte[(int) binary.length()];
		
		FileInputStream fileInputStream = new FileInputStream(binary);
		fileInputStream.read(bytes);
		fileInputStream.close();

//		System.out.println("print 2 inside readFile method inside binaryLoader");
	}

public Constants getConstants() {
	//Create constant object (containing 4 sections, address (4 bytes), size (4 bytes) and data (no specific size)
	Constants constants = new Constants(); 
	
	
	//Create byte array for address, copy from file just address bytes (4) to byte array & put the value into constants obj
	byte [] addressByteArray = new byte [4];
	System.arraycopy(this.bytes, 4, addressByteArray, 0, 4);
	constants.address=addressByteArray;
	
	
	//Create byte array for size, copy from file just size bytes (4) to byte array
	//Create a word object and send the size. Then call word and stitch together the bytes of size into a number, return as int.
	//Finally put the size array into the constant obj, and the whole block size too (+ 8) --> 4 for magic number and 4 for address of block.
	byte [] sizeByteArray = new byte [4];
	System.arraycopy(this.bytes, 8, sizeByteArray, 0, 4); //copy size Bytes
	Word sizeAsWord = new Word(sizeByteArray); 
	int sizeInt = sizeAsWord.wordConverter();
	constants.size = sizeByteArray;
	constants.blockSize = sizeInt + 8;
	
	
	//Create a byte array for the data, copy from the file to these bytes (with the size obtained before) and put the data into the constant obj
	byte [] constantsDataByteArray = new byte [sizeInt]; //Copy from this.bytes to the bytes of the constant
	System.arraycopy(this.bytes, 12, constantsDataByteArray, 0, sizeInt); //Copy constants Data Bytes
	constants.constantsData = constantsDataByteArray;
	
	constants.getConstantArrayInt(); 
	
//	System.arraycopy(src, srcPos, dest, destPos, length);
	return constants;
}

public Text getText(int index) {
	Text text = new Text();
	
	byte [] addressByteArray = new byte [4];
	System.arraycopy(this.bytes, index, addressByteArray, 0, 4); //Copy address Bytes.
	text.address = addressByteArray;
	
	byte [] sizeByteArray = new byte [4];
	System.arraycopy(this.bytes, index+4, sizeByteArray, 0, 4); //Copy address Bytes.
	Word word = new Word(sizeByteArray); 
	int sizeInt = word.wordConverter();
	text.size= addressByteArray;
	text.blockSize = sizeInt;
	
	byte [] textDataByteArray = new byte [sizeInt];
	System.arraycopy(this.bytes, index+8, textDataByteArray, 0, sizeInt); //Copy text Data Bytes
	text.textData = textDataByteArray;

	return text;
	}
}