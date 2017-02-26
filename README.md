# CloudDistributedComputing

Here I am leveraging the  the AWS services (ie ec2 instance, dynamo db  and sqs queue) to create a framework that will take in a task
from a user. The task is recorded in sqs work queue  and also registered in dynamo db. When a task is picked up 
the recoded is commited in the DDB such that no other instance can take the task.

