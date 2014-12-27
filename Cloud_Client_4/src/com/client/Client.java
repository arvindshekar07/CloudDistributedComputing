package com.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {

	private long threadID;
	private Socket clientSocket;
	public List<String> anArray = new ArrayList();

	public Client(String IP, int port, String fileLocation) throws IOException {
		clientSocket = new Socket(IP, port);
		System.out.println(clientSocket);

		try {

			Scanner scan = new Scanner(new File(fileLocation));

			String strLine;
			String fileLines = null;

			while (scan.hasNextLine()) {
				fileLines = scan.nextLine();
				System.out.println(fileLines);

				// removeing all the extra text in the file and sending it the
				// clients
				String sleepTime = fileLines.replaceAll("[a-zA-Z\\W]", "");

				System.out.println(sleepTime);

				// putting all the data an array list
				anArray.add(sleepTime);

			}
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}

	}

	public void runClient() throws Exception {

		long startTime = System.currentTimeMillis();// staring the timer

		// this is for the socket connection

		// this is to write the data
		try {
			PrintWriter pr = new PrintWriter(
					this.clientSocket.getOutputStream(), true);
			// this is to read the data
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(this.clientSocket.getInputStream()));

			long taskCounter = 0;

			System.out.println("stating the client oprations");

			System.out.println(this.clientSocket);

			// --------------------------------------------------------------------------------------

			// sending each task at a time
			for (String z : anArray) {

				taskCounter++;

				System.out.println("sending task sleep :" + z);

				pr.println(z);

			}

			System.out.println("Total tasks sent: " + taskCounter);

			// sending to the server that you have sent the task to the server
			pr.println("Completed");

			String taskResultCounter = null;

			System.out.println(taskCounter);

			taskResultCounter = inFromServer.readLine();

			System.out.println(taskResultCounter);

			System.out.println("All Tasks Completed Successfully");

			// Validation to check if all tasks have been completed or not

			if (Long.toString(taskCounter).equals(taskResultCounter)) {
				System.out.println("All Tasks Completed Successfully");
			}

			long endTime = System.currentTimeMillis() - startTime;

			System.out.println("Time taken for execution \t" + endTime + " ms");

		} catch (Exception ex) {
			System.err.println("you are having an error in the client side");
			System.err.println(ex.toString());
		} finally {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException ignored) {
				}
			}
		}

		System.out.println("closing the socket");

	}

	public void run() {

		try {
			this.runClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
