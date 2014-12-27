package com.schedular;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.amazon.Constants;
import com.amazon.DynamoDB;
import com.amazon.SQSS;
import com.amazonaws.services.sqs.model.Message;

/**
 * @author sahithkumar
 *
 */
public class Server {

	private int port;
	ServerSocket welcomeSocket;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;
	private ExecutorService pool;
	private Boolean rw;
	private int numOfThreads = 0;

	private SQSS GlobalReqQueue = null;
	private SQSS GlobalResQueue = null;
	private DynamoDB dynamoDBObj = null;
	private String _REQ_URL = null;
	private String _RES_URL = null;

	private ConcurrentHashMap<String, Boolean> chm = null;

	public Server(int serverPort, int numOfThreads, Boolean rwOption)
			throws Exception {
		port = serverPort;
		System.out.println(numOfThreads);
		this.numOfThreads = numOfThreads;
		rw = rwOption;

		// / here you are creating the queues sqs and dynamo db
		GlobalReqQueue = new SQSS();
		GlobalResQueue = new SQSS();
		// creation of the queue
		_REQ_URL = GlobalReqQueue.createSQS("RequestQueue");
		_RES_URL = GlobalResQueue.createSQS("ResponseQueue");
		dynamoDBObj = new DynamoDB();
		dynamoDBObj.createDDBTable(Constants.DynamoDBName);

	}

	public void startServer() throws Exception {

		// activating this socket as server port
		ServerSocket clientSocket = new ServerSocket(port);

		System.out.println(clientSocket);

		Socket connectionSocket = null;
		// accepting the client socket

		while (true) {
			try {

				System.out.println("server ready");

				connectionSocket = clientSocket.accept();

				System.out.println("Server started");

				// initialization--------------------------------
				// initialing the cuncurrrent hashmaps
				chm = new ConcurrentHashMap<String, Boolean>();

				String clientSentence = null;

				inFromClient = new BufferedReader(new InputStreamReader(
						connectionSocket.getInputStream()));
				outToClient = new PrintWriter(
						connectionSocket.getOutputStream());

				// / you are reading all the data form the client
				while (!(clientSentence = inFromClient.readLine())
						.matches("Completed")) {

					System.out.println("cli:" + clientSentence);
					if (clientSentence != null) {
						LocalQueue.enqueue(clientSentence);
						LocalQueue.printQueue();

					}
				}
				long len = LocalQueue.queueLength();

				System.out.println(clientSentence);
				
				// -----------------------------Part for local workers
				if (!rw) {
					pool = Executors.newFixedThreadPool(numOfThreads);

					/* if (clientSentence.equals("Completed")) { */
					System.out.println("running in the local worker");
					System.out.println("Server: Execcuting all the task");

					System.out.println("queue length"
							+ LocalQueue.queueLength());
					// ---------------------------------running all the
					
					//long len = LocalQueue.queueLength();
					long i = 0;
					
					while (i < len) {
						System.out.println(i + 1);
						pool.execute(new LocalWorker(i));
						i++;
						/*
						 * Thread t = new Thread(new LocalWorker(i)); t.start();
						 * t.join();
						 */
					}
					// shutting down the executor
					pool.shutdown();
					pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

					System.out
							.println("Server: putting in the resposnse queue ");

					// && (LocalResponseQueue.queueLength() != len)
					while (LocalResponseQueue.isEmpty()
							&& (LocalResponseQueue.queueLength() != len)) {
					/*	System.out.println("Waiting for results to arrive");*/
						/* Thread.sleep(5000); */
					}

					long taskCounter = 0;
					// remove all the
					while (!LocalResponseQueue.isEmpty()) {
						LocalResponseQueue.dequeue();
						taskCounter++;
					}

					String result = Long.toString(taskCounter);

				/*	System.out.println("Sending result: " + result);*/
					outToClient.println(result);
				/*	System.out.println("Sending result 2: " + result);*/
					outToClient.close();
				} else {
					System.out.println("Remote worker is working");
					/*System.out.println("Server: Execcuting all the task");*/

					/*System.out.println("queue length"+ LocalQueue.queueLength());*/

					//creating the thread
					Thread t = new Thread(new SendToQueue(len, GlobalReqQueue));
					Thread t1 = new Thread(new ServerWorker(outToClient, GlobalResQueue, len));
					//, chm));
					t.start();
					t1.start();
					
					
					
					
				//	String req_sting_id = null;

					
					
								
					//t.join();
					// logic to get the messages

/*					long tempSizeCheck = 0;
					while (!(tempSizeCheck == len)) {
						List<Message> messages = GlobalResQueue
								.getMessage(Constants.responseQueue);

						Message retMessage = null;

						for (Message m : messages) {
							retMessage = m;
						}
						if (messages.size() != 0) {
							if (chm.containsKey(retMessage.getBody())) {
								System.out
										.println("you are in  the hash table check");
								chm.put(retMessage.getBody(), true);
								GlobalResQueue.deleteMessage(retMessage);
								tempSizeCheck++;
							}
						}else
						{
							Thread.sleep(500);
						}
					}

					System.out.println("tempCheck Size" + tempSizeCheck);

					long taskCounter = tempSizeCheck;
					// remove all the
					
					 * while (!LocalResponseQueue.isEmpty()) {
					 * LocalResponseQueue.dequeue(); taskCounter++; }
					 

					String result = Long.toString(taskCounter);

					System.out.println("Sending result: " + result);
					outToClient.println(result);

					// this is for the socket checking purpose
					System.out.println("Sending result 2: " + result);

					outToClient.close();
					chm = null;
*/				}

			} catch (IOException e) {
				System.out.println("Error" + ": " + e);
			}
		}
	}

}
