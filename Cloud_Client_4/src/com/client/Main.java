package com.client;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Scanner;

/**
 * @author sahithkumar
 *
 */
public class Main {

	/**
	 * @param inputArgs
	 *            the command line arguments
	 */
	public static void main(String[] Args) throws IOException {

		System.out.println("----------starting client interface---------");
		int i = 0;
		String hostIP = null, filePath = null;
		int port = 0;
		Scanner scan = new Scanner(System.in);

		String inputArgs[] = null;
		while (true) {
			System.out.println("client$");
			inputArgs = scan.nextLine().split(" ");

			while (i < inputArgs.length) {
				if (inputArgs[i].equals("-s")) {
					i = i + 1;
					String address = inputArgs[i];
					String[] array = address.split(":");
					hostIP = array[0];
					port = Integer.parseInt(array[1]);
				}

				if (inputArgs[i].equals("-w")) {
					i = i + 1;
					filePath = inputArgs[i];
				}

				i++;
			}

			System.out.println(" " + hostIP + " " + port + " " + filePath);
			Client client = new Client(hostIP, port, filePath);
			Thread t = new Thread(client);
			t.start();

		}
	}

}
