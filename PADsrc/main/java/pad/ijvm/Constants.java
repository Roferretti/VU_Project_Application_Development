package pad.ijvm;

public class Constants {
	byte[] address;
	byte[] size;
	byte[] constantsData;
	int [] constantArrayInt;
	int blockSize;

	public void getConstantArrayInt(){
		constantArrayInt = new int [constantsData.length/4]; //from constant block remove 4 bytes of address, 4 of size and divide by 4 to get the number of constants
		
		for (int i=0; i<constantsData.length; i+=4){	
			int result;
			result = ((constantsData[i] & 0xff) << 24) | ((constantsData[i+1] & 0xff)  << 16) | ((constantsData[i+2]  & 0xff) << 8) | (constantsData[i+3] & 0xff); // & 0xff
			constantArrayInt[i/4] = result;
//			System.err.println(result);
		}
		
		
	}
	
}
