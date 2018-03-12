package pad.ijvm;

public class Text {
	byte[] address;
	byte[] size;
	byte[] textData;
	int blockSize;
	
	 int readOp(int index){	
		 if(this.textData[index] < 0 ){
			 return this.textData[index];
		 }else{
		 int result = this.textData[index] & 0xff; //convert to unsigned
		 	return result;
		 }
	 }
	 
	 boolean hasNextOp(int index){
		 boolean result = true;
		 if (index >= this.textData.length){
			 result = false;
		 }
		 
		 return result;
	 }	 
	 
	 public int join2Bytes(int byte1, int byte2){
		 int result=0;
		 result = (byte1 & 0xff) << 8 | (byte2 & 0xff);
		 
		 return result;
		 
	 }
	 
	 public int join2BytesSigned(int byte1, int byte2){
		 int result=0;
		 result = (byte1) << 8 | (byte2 & 0xff);
		 
		 return result;
		 
	 }
	 
	 public int branch(int PC){
			int numberToSkipGOTO;

			int byte1 = readOp(PC);
			PC++;
			int byte2 = readOp(PC);
			PC++;
			
			numberToSkipGOTO = join2BytesSigned(byte1, byte2);
			PC+= numberToSkipGOTO -3;

			return PC;
	 }
}
