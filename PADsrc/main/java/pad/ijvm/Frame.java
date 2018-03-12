package pad.ijvm;

import java.util.Stack;

public class Frame {
	Stack <Integer> stack;
	int [] frameStack;
	int actualPC;
	
	Frame(int actualPC){
		stack = new Stack<Integer>();
		this.frameStack = new int[100000];
		this.actualPC = actualPC;
	}

	public void istore(int pop, int index) {
		frameStack[index] = pop; 
	}
	
	public int iload(int index){
		return frameStack[index];
	}
	
	
}