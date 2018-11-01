/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;

import app.client.insur.InsuranceTaskClient;

/**
 * @author sln
 * @date 2018年9月12日上午10:39:06
 * @Description: 考虑到部分微服务可能所处的环境不一样，为了可以直接调用到，不受环境影响，故采用代理的方式，继承原有接口，
 * 改用url，url就是代理ip,默认端口是80，可以省略，社保暂时这样，用代理的方式，其他的先用微服务的方式调用
 * 放在公共client包下会影响其他项目，导致其他项目的配置文件中也需要写相关的注入参数，所以，暂时将社保单独放在monitor项目下
 * 
 * 为避免其他项目也会受到影响，到时将其他的网站的client也移动到本项目下
 */
@FeignClient(name = "taskid-proxy", url = "${task-insurance-proxy}")
public interface InsuranceProxyTaskClient extends InsuranceTaskClient {

}
