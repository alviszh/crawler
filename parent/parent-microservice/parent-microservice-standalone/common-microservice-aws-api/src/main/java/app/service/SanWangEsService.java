package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.ModifyInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;

import app.commontracerlog.TracerLog;

@Component
public class SanWangEsService {
	
	@Autowired
    private TracerLog tracer;

    public static String endpoint ="ec2.cn-north-1.amazonaws.com.cn";
 
    private static String accessKey = "AKIAOLCIKIGWN4XZ52WA";//
 
	private static String secretKey = "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";// 

    private BasicAWSCredentials awsCreds;

    private AmazonEC2Client amazonEC2Client;
    
    @Value("${esinstanceId}")
    String instance_id;
    
    //三网联查的elasticsearch 服务器
    //private static String instance_id = "i-076b78c72729c698d";//测试用的 blockchain-md
   // private static String instance_id = "i-a0b99298";//生产环境的es服务器
    
    
    public SanWangEsService(){
        System.out.println("accessKey:"+accessKey);
        System.out.println("secretKey:"+secretKey);
        this.awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonEC2Client = new AmazonEC2Client(awsCreds);
        this.amazonEC2Client.setEndpoint(endpoint);
    }
    
    
    public void stop(){ 
    	tracer.addTag("stop",instance_id);
    	System.out.println("--------stop--------"+instance_id);
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instance_id); 
		amazonEC2Client.stopInstances(request);
	}
    
    public void start(){
    	tracer.addTag("start",instance_id);
    	System.out.println("--------start--------"+instance_id);
		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instance_id); 
		amazonEC2Client.startInstances(request);
	}
    
    public void update2xlarge(){
    	tracer.addTag("update2xlarge",instance_id);
    	System.out.println("--------update2xlarge--------"+instance_id);
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest().withInstanceId(instance_id); 
		request.setInstanceType("t2.2xlarge"); 
		amazonEC2Client.modifyInstanceAttribute(request); 
	}
	
	public void updatet2large(){
		tracer.addTag("updatet2medium",instance_id);
		System.out.println("--------updatet2large--------"+instance_id);
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest().withInstanceId(instance_id); 
		request.setInstanceType("t2.large"); 
		amazonEC2Client.modifyInstanceAttribute(request); 
	}

    
    
    
    
    
    
    
    
    
    

}
