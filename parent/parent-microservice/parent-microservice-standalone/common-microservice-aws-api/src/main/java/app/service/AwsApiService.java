package app.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;

import app.commontracerlog.TracerLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zmy on 2018/4/9.
 */
@Component
public class AwsApiService {
    @Autowired
    private TracerLog tracer;

    public static String endpoint ="ec2.cn-north-1.amazonaws.com.cn";

    //private static String accessKey = "AKIAOLE75G26VWQOUB2Q";//AKIAOLCIKIGWN4XZ52WA
    private static String accessKey = "AKIAOLCIKIGWN4XZ52WA";//

    //private static String secretKey = "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";//wd3ka2uLeSzvB3ePP0F9hKSKG2MTV0EbSvp7jpQT
    private static String secretKey = "wd3ka2uLeSzvB3ePP0F9hKSKG2MTV0EbSvp7jpQT";//

//    private static String allocationId = "eipalloc-c8bddaf2";//弹性IP的Id

    private BasicAWSCredentials awsCreds;

    private AmazonEC2Client amazonEC2Client;

    @Value("${instanceId}")
    String instanceIds;

    @Value("${allocation_id}")
    String allocationId;//弹性IP的Id
    
    @Value("${proxy_port}")
	String proxy_port;

    public AwsApiService(){
        System.out.println("accessKey:"+accessKey);
        System.out.println("secretKey:"+secretKey);
        this.awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonEC2Client = new AmazonEC2Client(awsCreds);
        this.amazonEC2Client.setEndpoint(endpoint);
    }

    /**
     * 获取实例的公网ip
     */
    @Cacheable(value="mycache" ,key="'proxyIpSet'")
    public HttpProxyRes getProxyIps(){
    	HttpProxyRes httpProxyRes = new HttpProxyRes();
        System.out.println("开始获取实例的公网IP地址");
        tracer.addTag("getProxyIps","开始获取实例的公网IP地址");
        List<HttpProxyBean> proxyIpSet = new ArrayList<HttpProxyBean>();
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        System.out.println("instanceId:"+instanceIds);
        tracer.addTag("instanceId",instanceIds);
        int errornum = 0;
        
        String[] instanceIdArry = instanceIds.split(","); //实例Id
        for (String id : instanceIdArry) {
        	Set<String>  einstanceIds = new HashSet<>();
        	einstanceIds.add(id);
            request.setInstanceIds(einstanceIds);
            System.out.println("id----"+id);
            HttpProxyBean hpb = new HttpProxyBean();
            hpb.setInstanceId(id);
            try {
                DescribeInstancesResult response = amazonEC2Client.describeInstances(request);
                System.out.println("response.getReservations().size()---"+response.getReservations().size());
                for(Reservation reservation : response.getReservations()) {
                	System.out.println("reservation.getInstances().size()---"+reservation.getInstances().size());
                	System.out.println("reservation.getInstances().size()---"+reservation.getInstances());
                    for(Instance instance : reservation.getInstances()) {
                    	//instance.getState().getName()  //running
                        String publicIpAddress = instance.getPublicIpAddress();
                        System.out.println("publicIpAddress:"+publicIpAddress);
                        String name = instance.getTags().get(0).getValue();
                        System.out.println("name:"+name);
                        String instanceId = instance.getInstanceId();
                        System.out.println("instanceId:"+instanceId);

                        String publicDnsName = instance.getPublicDnsName();//共有DNS

                        hpb.setIp(publicIpAddress); 
                        hpb.setName(publicDnsName);
                        hpb.setPort(proxy_port); 
                        
                       
                    }
                }
            } catch (Exception e) { 
            	errornum++;
                tracer.addTag("getProxyIps.Exception","获取实例公网IP失败");
                System.out.println("获取实例异常：>>"+id+"<< 的公网IP失败，"+e.toString());
                tracer.addTag("getProxyIps.Exception.e","获取实例异常：>>"+id+"<< 的公网IP失败，"+e.toString());
            }
            
            proxyIpSet.add(hpb);
            
            
        } 
        
        Date currentTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateStr = df.format(currentTime); 
        
        httpProxyRes.setUpdateTime(dateStr);
        httpProxyRes.setHttpProxyBeanSet(proxyIpSet);
        httpProxyRes.setTotalnum(proxyIpSet.size());
        httpProxyRes.setErrornum(errornum);
        
        return httpProxyRes;
    }

    public AssociateAddressResult associateAddress(String instanceId){
        AssociateAddressRequest associateAddressRequest  = new AssociateAddressRequest ();
        associateAddressRequest.withAllocationId(allocationId).withInstanceId(instanceId);
        AssociateAddressResult associateAddressResult = amazonEC2Client.associateAddress(associateAddressRequest);
        return associateAddressResult;
    }

    public void disassociateAddress(String associationId){
        DisassociateAddressRequest disassociateAddressRequest = new DisassociateAddressRequest();
        disassociateAddressRequest.withAssociationId(associationId);
        amazonEC2Client.disassociateAddress(disassociateAddressRequest);

    }

    /**
     * 切换IP
     * @CacheEvict 删除mycache的缓存数据
     */
    @CacheEvict(value="mycache" ,key="'proxyIpSet'")
    public void changeIP(){
        tracer.addTag("changeIP", "开始切换IP");
        tracer.addTag("changeIP.time","" + new Date());
        //String[] strs = {"i-53cf9c6b","i-54cf9c6c","i-55cf9c6d","i-dc5df5e4","i-db4fefe3","i-b54eee8d","i-c890c8f0","i-c990c8f1","i-ca90c8f2","i-cb90c8f3","i-ce90c8f6","i-cf90c8f7"};

        String[] instanceIdArry = instanceIds.split(","); //实例Id

        for (String id: instanceIdArry){
            System.out.println(id);
            try {
                AssociateAddressResult associateAddressResult = associateAddress(id);
                String associationId = associateAddressResult.getAssociationId();
                System.out.println(associationId);
                disassociateAddress(associationId);
            } catch (Exception e) {
                tracer.addTag("changeIP.Exception","切换实例失败");
                System.out.println("获取实例："+id+"失败，"+e.toString());
                tracer.addTag("changeIP.Exception.e","切换实例："+id+"失败，"+e.toString());
            }
        }
    }

    public static void main(String[] args) {

        AwsApiService di= new AwsApiService();
        di.getProxyIps();
    }
}
