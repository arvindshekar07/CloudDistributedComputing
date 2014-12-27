package com.schedular;

public class LocalWorker implements Runnable{
	int duration;
	long taskID;

	public LocalWorker(long i) {
		Object obj = LocalQueue.dequeue();
		String objStr = obj.toString();
		duration = Integer.parseInt(objStr);
		taskID = i;
	}

	public void run() {
		try {
			/*System.out.println("Executing  ID: " + Integer.toString(taskID)
					+ " and sleep of: " + Integer.toString(duration));*/
			Thread.sleep(duration);
			LocalResponseQueue.enqueue(taskID);

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
