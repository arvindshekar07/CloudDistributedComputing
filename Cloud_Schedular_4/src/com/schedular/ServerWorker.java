package com.schedular;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.amazon.Constants;
import com.amazon.SQSS;
import com.amazonaws.services.sqs.model.Message;

public class ServerWorker implements Runnable {

	private PrintWriter outToClient;
	private SQSS GlobalResQueue;
	private long len;
	ConcurrentHashMap<String, Boolean> chm = null;

	public ServerWorker(PrintWriter outToClient,SQSS GlobalResQueue,long len){//, ConcurrentHashMap<String, Boolean> chm ) {
		this.outToClient=outToClient;
		this.GlobalResQueue=GlobalResQueue;
		this.len=len;
		//this.chm= chm;
				
	}

	@Override
	public void run() {
		

		long tempSizeCheck = 0;
		while (!(tempSizeCheck == len)) {
			System.out.println("lenght of input  queue"+len);
			List<Message> messages = GlobalResQueue
					.getMessage(Constants.responseQueue);

			System.out.println("pinging the resposnse queue");
			Message retMessage = null;

			for (Message m : messages) {
				retMessage = m;
			}
			if (messages.size() != 0) {
				//if (chm.containsKey(retMessage.getBody())) {
					System.out.println("you are in  the hash table check");
					//chm.put(retMessage.getBody(), true);
					GlobalResQueue.deleteMessage(retMessage);
					tempSizeCheck++;
				//}
			}
		}

		/* System.out.println("tempCheck Size" + tempSizeCheck); */

		long taskCounter = tempSizeCheck;
		// remove all the
		/*
		 * while (!LocalResponseQueue.isEmpty()) { LocalResponseQueue.dequeue();
		 * taskCounter++; }
		 */

		String result = Long.toString(taskCounter);

		System.out.println("Sending result: " + result);
		outToClient.println(result);

		// this is for the socket checking purpose
		System.out.println("Sending result 2: " + result);

		outToClient.close();
		chm = null;

	}

}
