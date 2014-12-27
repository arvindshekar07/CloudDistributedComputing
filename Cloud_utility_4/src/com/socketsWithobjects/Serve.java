package com.socketsWithobjects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serve {

	public static void main(String[] args) {
		

			ServerSocket serverSocket =null;
			
			Socket echoSocket =null;
			
			try {
			serverSocket=new ServerSocket(9522);
			
			echoSocket=serverSocket.accept();
			// PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
			// true);

			ObjectInputStream in = new ObjectInputStream(
					echoSocket.getInputStream());
			
			String userInput = null;
			// out.println("server says hi");

			while (true) {
				SockVO vo = (SockVO) in.readObject();

				if ((userInput = vo.getFirstName()) != null) {
					System.out.println("echo: " + userInput);
					// out.println("server says hi");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			try {
				serverSocket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
