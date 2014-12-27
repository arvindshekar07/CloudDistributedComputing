package com.schedular;

import java.util.ArrayDeque;
import java.util.Queue;

public class LocalResponseQueue {

	public static Queue<Object> readQueue = new ArrayDeque<Object>(100);

	public static void enqueue(Object object) {
		// do some stuff
		readQueue.add(object);
	}

	public static Object dequeue() {
		// do some stuff
		return readQueue.remove();
	}

	public static boolean isEmpty() {
		// do some stuff
		return readQueue.isEmpty();
	}

	// display queue length
	public static int queueLength() {
		return readQueue.size();
	}

}
