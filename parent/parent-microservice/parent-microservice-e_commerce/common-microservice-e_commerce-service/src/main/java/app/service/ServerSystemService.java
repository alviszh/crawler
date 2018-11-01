package app.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;  

/**
 * @Des 获取程序运行的服务器IP 和 Hostname
 * @author meidi
 *
 */
@Component
public class ServerSystemService {

	public InetAddress getInetAddress() throws UnknownHostException{   
		return InetAddress.getLocalHost();   
    }  
  
    public String getHostIp() throws UnknownHostException{  
    	InetAddress netAddress = getInetAddress();  
        if(null == netAddress){  
            return null;  
        }  
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;  
    }  
  
    public static String getHostName(InetAddress netAddress){  
        if(null == netAddress){  
            return null;  
        }  
        String name = netAddress.getHostName(); //get the host address  
        return name;  
    }  
    
}
