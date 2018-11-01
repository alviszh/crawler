/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;

import app.client.standalone.StandaloneTaskClient;

@FeignClient("standalone-task")
//@FeignClient(name = "taskid-proxy", url = "${task-pbccrc-proxy}")
public interface PbccrcProxyTaskClient extends StandaloneTaskClient {

}
