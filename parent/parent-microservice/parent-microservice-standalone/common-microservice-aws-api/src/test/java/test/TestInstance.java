package test;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.ModifyInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;

public class TestInstance {
	
	private static String instance_id = "i-076b78c72729c698d";
	
	private static String endpoint ="ec2.cn-north-1.amazonaws.com.cn";
 
	private static String accessKey = "AKIAOLCIKIGWN4XZ52WA";//
 
	private static String secretKey = "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";//
	
	private BasicAWSCredentials awsCreds;

	private AmazonEC2Client amazonEC2Client;
	
	public TestInstance(){
        System.out.println("accessKey:"+accessKey);
        System.out.println("secretKey:"+secretKey);
        this.awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonEC2Client = new AmazonEC2Client(awsCreds);
        this.amazonEC2Client.setEndpoint(endpoint);
    }
	
	public void stop(){ 
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instance_id);

		amazonEC2Client.stopInstances(request);
	}
	
	public void start(){
		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instance_id);

		amazonEC2Client.startInstances(request);
	}
	
	public void update2xlarge(){
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest().withInstanceId(instance_id);
		
		request.setInstanceType("t2.2xlarge");
		
		amazonEC2Client.modifyInstanceAttribute(request);
		
	}
	
	public void updatet2large(){
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest().withInstanceId(instance_id);
		
		request.setInstanceType("t2.large");
		
		amazonEC2Client.modifyInstanceAttribute(request);
		
	}


	public static void main(String[] args) throws InterruptedException {
		
		
		TestInstance ti = new TestInstance();
		
		ti.stop();
		System.out.println("----------------stop--------------------");
		Thread.sleep(30000L);
		ti.updatet2large();
		System.out.println("----------------updatet--------------------");
		Thread.sleep(30000L);
		ti.start();
		System.out.println("----------------start--------------------");

	}

}
