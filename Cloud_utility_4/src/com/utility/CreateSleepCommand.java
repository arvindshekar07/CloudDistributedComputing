package com.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateSleepCommand {

	
	public static void main(String[] args) throws IOException {
		File f = new File("/home/aero/Desktop/1k.txt");
		//File f1 = new File("/home/aero/Desktop/100k.txt");
		
		PrintWriter pf =  new PrintWriter(new FileWriter(f),true);
		//PrintWriter pf1 =  new PrintWriter(new FileWriter(f1),true);
		
		for (long i = 0; i < 1000; i++) {
			pf.println("sleep0");
		}
		/*for (long i = 0; i < 100000; i++) {
			pf1.println("sleep0");
		}*/
		
		pf.close();
		//pf1.close();
		
	}
}
