/**
 * 
 */
package com.amazon;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSS {

	private AWSCredentials credentials = null;
	private AmazonSQS sqs = null;
	private String queueName = null;
	private String myQueueUrl = null;

	private List<Message> messages = null;
	private ReceiveMessageRequest receiveMessageRequest = null;

	public SQSS() {

		init();// init would get initial credentials and connect to cloud

	}

	public String createSQS(String queueName) {

		// Set the queue name
		this.queueName = queueName;

		// Create a queue
		System.out.println("Creating a new queue by the name " + queueName
				+ ".\n");
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(
				queueName);
		myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		
		return myQueueUrl;
	}

	private void init() {
		try {

			credentials = new PropertiesCredentials(
					SQSS.class.getResourceAsStream("Credentials.properties"));

			AmazonEC2Client ec2 = new AmazonEC2Client(credentials);

			/*
			 * credentials = new ProfileCredentialsProvider("default")
			 * .getCredentials();
			 */
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location  and is in valid format.", e);
		}

		sqs = new AmazonSQSClient(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);

	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void diplaylistQueue() {
		// List queues
		System.out.println("Listing all queues in your account.\n");
		for (String queueUrl : sqs.listQueues().getQueueUrls()) {
			System.out.println("  QueueUrl: " + queueUrl);
		}
		System.out.println();
	}

	public List<String> listQueue() {
		return sqs.listQueues().getQueueUrls();
	}

	public int listSize() {
		// List queues

		List<String> alist = sqs.listQueues().getQueueUrls();
		return alist.size();
	}

	public String putMessage(String message) {
		myQueueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

		System.out.println("Sending a message to Queue. \n");

		SendMessageRequest smr = new SendMessageRequest(myQueueUrl, message);

		// MessageAttributeValue mav = new MessageAttributeValue();
		// mav.setStringValue("");
		// smr.addMessageAttributesEntry("asdas", mav);
		// smr.addMessageAttributesEntry(queue_name);

		SendMessageResult srv = sqs.sendMessage(smr);
		return srv.getMessageId();
	}

	public void putMessage(Message message, String queueName) {

		myQueueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

		System.out.println("Sending a message to Queue\n");

		SendMessageRequest smr = new SendMessageRequest(myQueueUrl,
				message.getBody());

		smr.setMessageAttributes(message.getMessageAttributes());

		// smr.addMessageAttributesEntry(queue_name);

		sqs.sendMessage(smr);
	}

	public void showAllMessage(String queueName) {

		// you are creating the queue
		myQueueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

		// Receive messages
		System.out.println("Receiving messages from Queue.\n");

		receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

		for (Message message : messages) {
			System.out.println("  Message");
			System.out.println("    MessageId:     " + message.getMessageId());
			System.out.println("    ReceiptHandle: "
					+ message.getReceiptHandle());
			System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
			System.out.println("    Body:          " + message.getBody());
			for (Entry<String, String> entry : message.getAttributes()
					.entrySet()) {
				System.out.println("  Attribute");
				System.out.println("    Name:  " + entry.getKey());
				System.out.println("    Value: " + entry.getValue());
			}
		}
		System.out.println();

	}

	public List<Message> getMessage(String queueName) {

		// Creation of the queue
		myQueueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

		// System.out.println("From getMessage" + messages.size());

		return messages;

	}

	public void removeQueue() {
		// Delete a queue
		System.out.println("Deleting the " + queueName + " queue.\n");
		sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
	}

	public void deleteAllMessage() {
		// should not delete with out listing
		messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		for (Message message : messages) {
			// Delete a message
			System.out.println("Deleting all message.\n");
			String messageRecieptHandle = message.getReceiptHandle();
			sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl,
					messageRecieptHandle));
		}
	}

	public void deleteMessage(Message message) {

		// Should not delete with out listing
		System.out.println("Deleting a message.\n");
		String messageRecieptHandle = message.getReceiptHandle();
		sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl,
				messageRecieptHandle));
	}

}
