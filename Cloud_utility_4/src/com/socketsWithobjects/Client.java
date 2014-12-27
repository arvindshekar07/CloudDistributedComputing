package com.socketsWithobjects;

// in this program you are sending  an object to  the server   and server is reading it

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {

		Socket echoSocket = null;

		try {
			echoSocket = new Socket("localhost", 9522);

			/*
			 * PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
			 * true);
			 * 
			 * out.print(new SockVO("arvind", "shekar"));
			 */
																																																																																																																																																																																																																																																																																																																															ObjectOutputStream oos = new ObjectOutputStream(
																																																																																																																																																																																																																																																																																																																																	echoSocket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));

			while (true) {
				oos.writeObject(new SockVO("arvind", "shekar"));
				// System.out.println("echo: " + in.readLine());
				oos.writeObject(new SockVO("anujav", "v"));
				Thread.sleep(10000);
				// System.out.println("echo: " + in.readLine());
				oos.writeObject(new SockVO("varun ", "b"));
				// System.out.println("echo: " + in.readLine());

			}

			/*
			 * Scanner sc = new Scanner(System.in); while (true) {
			 * 
			 * out.println(sc.nextLine());
			 * 
			 * }
			 */

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				echoSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
