package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serve {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(9522);
		Socket echoSocket = serverSocket.accept();
		PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				echoSocket.getInputStream()));

		String userInput = null;
		// out.println("server says hi");

		// understanding the read and write concept
		out.println(" servermessaage message 1");

		System.out.println("echo: " + in.readLine());

		out.println(" servermessaage message 2");

		System.out.println("echo: " + in.readLine());

		out.println(" servermessaage message 3");

		System.out.println("echo: " + in.readLine());

		out.println(" servermessaage message 4");

		System.out.println("echo: " + in.readLine());

		/*
		 * while (true) {
		 * 
		 * if ((userInput = in.readLine()) != null) {
		 * System.out.println("echo: " + userInput); } }
		 */
	}

}
