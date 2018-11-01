package test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Unit test for simple App.
 */
public class AppTest {
	@Autowired 
	private static DiscoveryClient discoveryClient; 
    public static void main(String[] args) {
    	List<ServiceInstance> showServiceInfo = showServiceInfo();
    	System.out.println(showServiceInfo.size());
    	System.out.println("1111111111");
	}
    public static List<ServiceInstance> showServiceInfo() { 
    	return discoveryClient.getInstances("crawler-insurance-foshan"); 
    } 
}
