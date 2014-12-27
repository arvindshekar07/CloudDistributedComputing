package com.schedular;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author sahithkumar
 *
 */
public class LocalQueue {

	private static Queue<Object> readQueue = new ArrayDeque<Object>(100);

	// add object
	public static void enqueue(Object object) {
		readQueue.add(object);
	}

	// delete object
	public static Object dequeue() {
		return readQueue.remove();
	}

	// display queue
	public static void printQueue() {
		/*System.out.println("Printing Queue Head");*/
		System.out.println(readQueue.peek().toString());
	}

	// display queue length
	public static int queueLength() {
		return readQueue.size();
	}

}
