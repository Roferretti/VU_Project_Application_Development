package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class MachineFactory  /* implements IJVMInterface */ {

    public static IJVMInterface createIJVMInstance(File binary) throws IOException {
        // Create new machine instance here and return it.
    	
    	BinaryLoader machineLoader = new BinaryLoader();
        
//    	System.out.println("print  1 before readFile method in MachineFactory");
        // 1) Load the binary
    	machineLoader.readFile(binary);
    	
    	//once binary is loaded in BinaryLoader object send it to IJVM object machine.
    	IJVM machine = new IJVM(machineLoader);
    	
    	//printing test
//    	System.out.println("print 3 after readFile method in MachineFactory");
 	
        // 2) Return the new IJVM instance without starting it.
    	return machine;
    }
}
