package test;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class DescribeInstances {
	
	public static String endpoint ="ec2.cn-north-1.amazonaws.com.cn";
    
    //private static String accessKey = "AKIAOLE75G26VWQOUB2Q";//AKIAOLCIKIGWN4XZ52WA
    private static String accessKey = "AKIAOLCIKIGWN4XZ52WA";//
    
    //private static String secretKey = "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";//wd3ka2uLeSzvB3ePP0F9hKSKG2MTV0EbSvp7jpQT
    private static String secretKey = "wd3ka2uLeSzvB3ePP0F9hKSKG2MTV0EbSvp7jpQT";//
      
    private BasicAWSCredentials awsCreds;
    
    private AmazonEC2Client amazonEC2Client;
    
	public DescribeInstances(){
		System.out.println("accessKey:"+accessKey);
		System.out.println("secretKey:"+secretKey);
		this.awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		this.amazonEC2Client = new AmazonEC2Client(awsCreds);
		this.amazonEC2Client.setEndpoint(endpoint); 
	} 
	
	public void describe(){
		boolean done = false;

		DescribeInstancesRequest request = new DescribeInstancesRequest();
		//Set<String> instanceIds = new HashSet<String>(); //{"i-0afe00ceb68cc43e9","i-ff1425c7"};
		//instanceIds.add("i-0afe00ceb68cc43e9");
		//instanceIds.add("i-ff1425c7");
		//request.setInstanceIds(instanceIds);
		
		//while(!done) {
		    DescribeInstancesResult response = amazonEC2Client.describeInstances(request);

		    for(Reservation reservation : response.getReservations()) {
		        for(Instance instance : reservation.getInstances()) { 
		        	System.out.println("InstanceId:"+instance.getInstanceId());
		        	System.out.println("InstanceType:"+instance.getInstanceType());
		        	System.out.println("PublicIpAddress:"+instance.getPublicIpAddress()); 
		        	System.out.println("State:"+instance.getState().getCode()+"   "+instance.getState().getName());//需要是running状态的，因为还可能存在terminated
		        	System.out.println("Tags:"+instance.getTags()); 
		        	System.out.println("----------------------------------------------------"); 
		        }
		    }

		    //request.setNextToken(response.getNextToken());

		    //if(response.getNextToken() == null) {
		        //done = true;
		    //}
		//}
	}

	public static void main(String[] args) { 
		
		DescribeInstances di= new DescribeInstances();
		di.describe();
	}

}
