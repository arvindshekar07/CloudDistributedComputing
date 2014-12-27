package com.schedular;

import com.amazon.SQSS;

public class SendToQueue implements Runnable {

	long len;
	SQSS GlobalReqQueue = null;

	public SendToQueue(long len, SQSS GlobalReqQueue) {
		this.GlobalReqQueue = GlobalReqQueue;
		this.len = len;
	}

	@Override
	public void run() {
		long i = 0;
		while (i < len) {

			// logic to send to the sqs
			GlobalReqQueue.putMessage(LocalQueue.dequeue().toString());
			// chm.put(req_sting_id, false);
			i++;

		}

	}

}
