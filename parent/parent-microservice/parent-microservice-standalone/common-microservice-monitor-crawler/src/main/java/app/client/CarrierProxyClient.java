/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;

import app.client.carrier.TaskClient;

/**
 * @author sln
 * @Description: 
 */
@FeignClient(name = "h5-proxy",configuration = ClientConfig.class, url = "${h5-proxy}")
public interface CarrierProxyClient extends TaskClient {
   
}
