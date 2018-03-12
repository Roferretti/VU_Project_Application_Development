package pad.ijvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    static void printUsage() {
        System.out.println("Usage: java Main file.bin");
    }

    public static void main(String args[]) {
    	
//    	String[] aaa = new String[1];
//    	aaa[0] = "files/task2/TestIadd1.ijvm"; //"files/task2/TestIadd1.ijvm"
//    	args = aaa;
    	
        if (args.length < 1) {
            printUsage();
            System.exit(0);
        }
        try {
            File binary = new File(args[0]);

            // Create the ijvm instance
            MachineFactory.createIJVMInstance(binary).run();
        } catch (FileNotFoundException e) {
            System.err.printf("%s\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("%s\n", e.getMessage());
        }

    }
}

//First use as argument files/task1/program2.ijvm
//Second use files/task2/testBipush1.ijvm
