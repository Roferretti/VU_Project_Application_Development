package pad.ijvm;

import java.util.Stack;

public class Word {
	byte [] wordByte;
	int wordInt;
	
	Word(byte [] word){
		this.wordByte = word;
	}
	Word(int wordInt){
		this.wordInt = wordInt;
	}
	
	public int BytesToInt(byte bytes){
		return (int) bytes;
	}
	
	public int toInt(int wordInt){
		int result = wordInt;
		return result;
	}
	
	public static int[] EnumToIntArray(Stack<Integer> Stack){
		int[] stackInt = new int[Stack.size()];
		for(int index=0; index<Stack.size(); index++){
			stackInt[index] = Stack.elementAt(index);
		}
		return stackInt;
	}
	
	public int wordConverter(){
		int result;
		result = (wordByte[0] & 0xff) << 24 | (wordByte[1] & 0xff) << 16 | (wordByte[2] & 0xff) << 8 | wordByte[3] & 0xff;
		return result;
	}

}
