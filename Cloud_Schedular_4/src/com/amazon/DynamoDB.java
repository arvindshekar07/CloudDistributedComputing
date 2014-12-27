/**
 * 
 */
package com.amazon;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.sqs.AmazonSQS;

public class DynamoDB {

	private AWSCredentials credentials = null;
	private AmazonSQS sqs = null;

	private AmazonDynamoDBClient dynamoDB = null;
	private String tableName = null;

	/**
	 * @throws Exception
	 */
	public DynamoDB() throws Exception {
		init();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void createDDBTable(String tableName) {

		this.tableName = tableName;

		System.out.println("table name is " + this.tableName);
		// Create table if it does not exist yet
		if (Tables.doesTableExist(dynamoDB, tableName)) {
			System.out.println("Table " + tableName + " is already ACTIVE");
		} else {
			// Create a table with a primary hash key named 'name', which
			// holds a string
			CreateTableRequest createTableRequest = new CreateTableRequest()
					.withTableName(tableName)
					.withKeySchema(
							new KeySchemaElement().withAttributeName("MsgId")
									.withKeyType(KeyType.HASH))
					.withAttributeDefinitions(
							new AttributeDefinition()
									.withAttributeName("MsgId")
									.withAttributeType(ScalarAttributeType.S))
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(
									1000L).withWriteCapacityUnits(1000L));

			TableDescription createdTableDescription = dynamoDB.createTable(
					createTableRequest).getTableDescription();

			System.out.println("Created Table: " + createdTableDescription);

			// Wait for it to become active
			System.out.println("Waiting for " + tableName
					+ " to become ACTIVE...");
			Tables.waitForTableToBecomeActive(dynamoDB, tableName);
		}
		// Describe our new table
		DescribeTableRequest describeTableRequest = new DescribeTableRequest()
				.withTableName(tableName);
		TableDescription tableDescription = dynamoDB.describeTable(
				describeTableRequest).getTable();
		System.out.println("Table Description: " + tableDescription);

	}

	private void init() throws Exception {
		AWSCredentials credentials = null;
		try {

			credentials = new PropertiesCredentials(
					DynamoDB.class
							.getResourceAsStream("Credentials.properties"));

			AmazonEC2Client ec2 = new AmazonEC2Client(credentials);

		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location  and is in valid format.", e);
		}

		dynamoDB = new AmazonDynamoDBClient(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);
		
	}

	public void putObj(String para1) {
		// Add an item
		Map<String, AttributeValue> item = newItem(para1);

		PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		System.out.println("Result: " + putItemResult);

	}

	public boolean search(String tableName, String in_para) {

		// Searches parameter in dynamoDB
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(in_para));
		scanFilter.put("MsgId", condition);

		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		System.out.println("Result: " + scanResult);
		if (scanResult.getCount() == 0)
			return false;
		else
			return true;

	}

	private Map<String, AttributeValue> newItem(String para1) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("MsgId", new AttributeValue(para1));// name
		return item;
	}

	public static void main(String[] args) throws Exception {

		DynamoDB dynamoDBObj = new DynamoDB();
		dynamoDBObj.createDDBTable(Constants.DynamoDBName);

		dynamoDBObj.putObj("Hiiiiiii");
		if (dynamoDBObj.search(Constants.DynamoDBName, "Hiiiiiii") == true)
			System.out.println("Present!! ");
		else
			System.out.println("Not present!!");

		if (dynamoDBObj.search(Constants.DynamoDBName, "Dummy_data") == true)
			System.out.println("Present!! ");
		else
			System.out.println("Not present!!");
		
		

	}
}
