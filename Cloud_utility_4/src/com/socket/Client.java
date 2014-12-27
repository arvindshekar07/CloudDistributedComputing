package com.socket;

// going to write a program where you are to listen to listen 
//import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		Socket echoSocket = new Socket("localhost", 9522);

		PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				echoSocket.getInputStream()));

		out.println("hi from client");
		
		System.out.println("echo: " + in.readLine());
		

		// understanding the read and write concept
		out.println(" client message 1");

		System.out.println("echo: " + in.readLine());

		
		out.println(" client message 2");

		System.out.println("echo: " + in.readLine());

		
		out.println(" client message 3");
		
		System.out.println("echo: " + in.readLine());

		

		
		/*
		 * Scanner sc = new Scanner(System.in);
		 * 
		 * while (true) {
		 * 
		 * out.println(sc.nextLine());
		 * 
		 * }
		 */
	}
}
