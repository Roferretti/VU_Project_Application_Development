package pad.ijvm;
import java.io.InputStream;
import java.io.PrintStream;
import pad.ijvm.interfaces.IJVMInterface;

public class IJVM implements IJVMInterface{
	PrintStream out;
	InputStream in;
	BinaryLoader machineLoader;
	Constants constants;
	Text text;
	boolean running = true;
	Word word1; Word word2;
	
	Frame myFrame;  //Stack <Frame> frameStack;
	Frame [] frameStack;
	int framesIndex;
	
	IJVM(BinaryLoader machineLoader){
		out = System.out;
		in = System.in;
		this.machineLoader = machineLoader; 
		this.constants = machineLoader.getConstants();
		this.text = machineLoader.getText(this.constants.blockSize + 4); //constant block size + the magic number.
		this.myFrame = new Frame(0);
		this.frameStack = new Frame [1000];
		framesIndex = 0;
		frameStack[framesIndex] = myFrame;
	}

	@Override
	public int topOfStack() {
		return myFrame.stack.peek();
	}

	@Override
	public int[] getStackContents() {
		int[] stackInt = Word.EnumToIntArray(myFrame.stack);
		return stackInt;
	}

	@Override
	public byte[] getText() {
		return this.text.textData;
	}

	@Override
	public int getProgramCounter() {
		return myFrame.actualPC;
	}

	@Override
	public int getLocalVariable(int i) {
		return myFrame.iload(i); 
	}

	@Override
	public int getConstant(int i) {
		return constants.constantArrayInt[i];
	}

	@Override
	public void step() {

		if (!running) return;
		switch(this.text.readOp(myFrame.actualPC)){
		case (byte) 0xFF: //HALT
			System.out.println("HALT");
			running = false;
			break;
		case (byte) 0xC4: //WIDE
			System.out.println("WIDE");
			myFrame.actualPC++;
			break;
		case (byte) 0x10: //BIPUSH
			System.out.println("BIPUSH");
			myFrame.actualPC++;
			myFrame.stack.push(text.readOp(myFrame.actualPC));
			myFrame.actualPC++;
			break;
		case (byte) 0x59: //DUP
			System.out.println("DUP");
			myFrame.stack.push(myFrame.stack.peek());
			myFrame.actualPC++;
			break;
		case (byte) 0x84:
			System.out.println("IINC");
			myFrame.actualPC++;
			int variableIndex = text.readOp(myFrame.actualPC);
			
			myFrame.stack.push(myFrame.iload(variableIndex));
			myFrame.actualPC++;
			
			myFrame.stack.push(text.readOp(myFrame.actualPC));		
			myFrame.istore((myFrame.stack.pop() + myFrame.stack.pop()), variableIndex);		
			myFrame.actualPC++;
			break;
		case (byte) 0x15:
			System.out.println("ILOAD");
			myFrame.actualPC++;
			myFrame.stack.push(myFrame.iload(text.readOp(myFrame.actualPC)));
			myFrame.actualPC++;
			break;
		case (byte) 0xFC:
			System.out.println("IN");
			myFrame.actualPC++;
			break;
		case (byte) 0xAC:
			System.out.println("IRETURN");
			framesIndex--;
			frameStack[framesIndex].stack.push(myFrame.stack.pop());
			myFrame = frameStack[framesIndex];
			break;
		case (byte) 0xB6:
			System.out.println("INVOKEVIRTUAL");
			pushConstant();
			
			framesIndex++;
			frameStack[framesIndex] = new Frame(myFrame.stack.pop());
			
			myFrame = frameStack[framesIndex];
			int parameters = 0; //read parameters
			parameters = readShort(); 
			myFrame.actualPC++;
			
			int localVariables = readShort(); //read local variables
			 myFrame.actualPC++;
			
			 for(int i=0; i<parameters; i++){ //parameters
				 myFrame.istore(frameStack[framesIndex-1].stack.pop(),  parameters - 1 - i); //reversa por los parameters 
			 }
			break;
		case (byte) 0x36:
			System.out.println("ISTORE");
			myFrame.actualPC++;
			myFrame.istore(myFrame.stack.pop(), text.readOp(myFrame.actualPC));
			myFrame.actualPC++;
			break;
		case (byte) 0x64:
			System.out.println("ISUB"); 
			word1 = new Word(myFrame.stack.pop());
			word2 = new Word(myFrame.stack.pop());
			myFrame.stack.push(word2.wordInt - word1.wordInt);
			myFrame.actualPC++;
			break;
		case (byte) 0x13:
			System.out.println("LDC_W"); 
			pushConstant();
			break;
		case (byte) 0xB0:
			System.out.println("IOR");
			word1 = new Word(myFrame.stack.pop());
			word2 = new Word(myFrame.stack.pop());
			myFrame.stack.push(word1.wordInt | word2.wordInt);
			myFrame.actualPC++;
			break;
		case (byte) 0xFE:
			System.out.println("ERR");
			myFrame.actualPC++;
			break;
		case (byte) 0x60:
			System.out.println("IADD");
			word1 = new Word(myFrame.stack.pop());
			word2 = new Word(myFrame.stack.pop());
			myFrame.stack.push(word1.wordInt + word2.wordInt);
			myFrame.actualPC++;
			break;
		case (byte) 0x00:
			System.out.println("NOP");
			myFrame.actualPC++;
			break;
		case (byte) 0x7E:
			System.out.println("IAND");
			word1 = new Word(myFrame.stack.pop());
			word2 = new Word(myFrame.stack.pop());
			myFrame.stack.push(word1.wordInt & word2.wordInt);
			myFrame.actualPC++;
			break;
		case (byte) 0xA7:
			System.out.println("GOTO");
			myFrame.actualPC++;
			myFrame.actualPC = text.branch(myFrame.actualPC);
			break;
		case (byte) 0xFD:
			System.out.println("OUT");
//			System.out.println("The TOS is: " + Stack.peek());
			int poppedElement = myFrame.stack.pop();
			out.printf("%c", (char) poppedElement);
			myFrame.actualPC++;
			break;
		case (byte) 0x57:
			System.out.println("POP");
			myFrame.stack.pop();
			myFrame.actualPC++;
			break;
		case (byte) 0x5F:
			System.out.println("SWAP"); 
			word1 = new Word(myFrame.stack.pop());
			word2 = new Word(myFrame.stack.pop());
			myFrame.stack.push(word1.wordInt);
			myFrame.stack.push(word2.wordInt);
			myFrame.actualPC++;
			break;
		case (byte) 0x99:
			System.out.println("IFEQ");
			myFrame.actualPC++;
			if(myFrame.stack.pop() == 0) myFrame.actualPC = text.branch(myFrame.actualPC);
			else myFrame.actualPC += 2;
			break;
		case (byte) 0x9B:
			System.out.println("IFLT");
			myFrame.actualPC++;
			if(myFrame.stack.pop() < 0) myFrame.actualPC = text.branch(myFrame.actualPC);
			else myFrame.actualPC+= 2; 
			break;
		case (byte) 0x9F:
			System.out.println("IF_ICMPEQ");
			myFrame.actualPC++;
			if(myFrame.stack.pop() == myFrame.stack.pop()) myFrame.actualPC = text.branch(myFrame.actualPC);
			else myFrame.actualPC+= 2;
			break;
		default:
			break;
		}
	}

	private void pushConstant() {
		myFrame.actualPC++;
		int constantPush = readShort();		
		myFrame.stack.push(constants.constantArrayInt[constantPush]); 	
		myFrame.actualPC++;
	}

	private int readShort() {
		int byte1 = text.readOp(myFrame.actualPC);
		myFrame.actualPC++;
		int byte2 = text.readOp(myFrame.actualPC);	

		int result = text.join2Bytes(byte1, byte2);
		return result;
	}

	@Override
	public void run() {
		while(text.hasNextOp(myFrame.actualPC) && running){
			System.out.println(String.format("0x%02X", this.text.readOp(myFrame.actualPC)));
			step();
		}
	}

	@Override
	public byte getInstruction(){
		return 0;
	}

	@Override
	public void setOutput(PrintStream out){
		this.out = out;
		return; 
	}

	@Override
	public void setInput(InputStream in){
		this.in = in;
		return;
	}

}





















//COMMENTS FOR PROGRAMMER


// IN CONSTRUCTOR
//this.frameStack = new Stack <Frame>(); //Several stacks piled up  1st
//frameStack.push(myFrame);  //Pushes 
//myFrame = frameStack.peek(); 2nd

//
//private int pushConstant1() {
//	myFrame.actualPC++;
//	myFrame.actualPC++;
//	int readByte = readByte();
//	int methodLocation1 = constants.constantArrayInt[readByte];
//	int numberOfArgs = text.join2Bytes(text.textData[methodLocation1], text.textData[methodLocation1 +1]);
//	myFrame.actualPC++;
//	return numberOfArgs;
//}

//
//private int readByte() {
//	int byte1 = text.readOp(myFrame.actualPC);
//	myFrame.actualPC++;
//	
//	return 0;
//}