# aws-ec2-dashboard - beta
Dashboard to attach and manage AWS IAM Role to an Existing Amazon EC2 instances.

A couple of weeks ago <a href='https://aws.amazon.com/about-aws/whats-new/2017/02/new-attach-an-iam-role-to-your-existing-amazon-ec2-instance/' target='_blank'>AWS announced</a> a new feature related to EC2 and IAM roles. Finally, you can attach or replace an IAM role to an existing Amazon EC2.

Right now it is possible to attach or remove roles on existing EC2 instances, via CLI or SDK.

So, I decided to created a dashboard (as a MVP) to help my infrastructure team to udpate and attach roles to existing AWS EC2 instances.

![EC2 Dashboard screen](https://s3.amazonaws.com/cloudjourney/dashboard_screen.png)

Feel free to use, fork and update it.

### Requirements
- **Java 8**: if you are using current Amazon Linux AMI 2016.09.1, you must update it.
```
sudo yum install java-1.8.0 -y
sudo yum remove java-1.7.0-openjdk -y
```
- **Security Group**: using Custom TCP Rule and port range: 4567 (if you keep the current port).
- **IAM Role**: this application must IAMFullAccess and AmazonEC2FullAccess policies attached.
- **Instance Type and storage**: at least t2.micro and 8Gb.

### Running it
1. First of all you should build it as any maven project. The final artifact is a jar file.
2. Run it as any jar file: ``` java -jar cc.cloudjourney.ec2dashboard-0.0.1-SNAPSHOT-jar-with-dependencies.jar ```
3. It is going to be available on http://public_dns`:4567

### Limitations
- There is no cache yet, so any refresh is calling the AWS API again.
- There is no user/password for this dashboard, make sure you setup a security group which allows only your IP.
- Only see EC2 instances from installed Region.

### Much easier - Sorry, since June this options is not available anymore.
If you want to use it right now, but you dont want to build it and configure anything on the EC2 instance, you can use an AMI image.
There is a Community AMI named: "EC2 - Dashboard - cloudjourney.cc - Beta", just search for it and setup Security Group and IAM Role as describled on requirements.

![EC2 Community AMI](https://s3.amazonaws.com/cloudjourney/AMI_screen.png)

**Make sure you test it first on a different region, just to make sure it really helps you.**
