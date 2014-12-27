package com.schedular;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	/**
	 * @param inputArgs
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("************staring server******************");

		int i = 0;
		Boolean remoteW = false;
		int port = 0;
		int numOfLocalW = 0;
		Scanner scan = new Scanner(System.in);
		ExecutorService pool = Executors.newFixedThreadPool(5);

		String[] inputArgs = null;
		System.out.println("server$");
		inputArgs = scan.nextLine().split(" ");
		while (i < inputArgs.length) {
			if (inputArgs[i].equals("-s")) {
				i = i + 1;
				port = Integer.parseInt(inputArgs[i]);
			}

			if (inputArgs[i].equals("-rw")) {
				remoteW = true;
			}

			if (inputArgs[i].equals("-lw")) {
				i = i + 1;
				numOfLocalW = Integer.parseInt(inputArgs[i]);
			}

			i++;
		}

		System.out.println(" " + port + " " + numOfLocalW + " " + remoteW);
		Server server = new Server(port, numOfLocalW, remoteW);
		server.startServer();
		/*
		 * pool.execute(new Server(port, numOfLocalW, remoteW));
		 * pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		 */

		/*
		 * Server server = new Server(port, numOfLocalW, remoteW);
		 * server.startServer();
		 */
	}
}
