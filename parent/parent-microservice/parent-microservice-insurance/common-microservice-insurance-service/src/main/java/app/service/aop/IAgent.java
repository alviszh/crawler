package app.service.aop;

import org.openqa.selenium.WebDriver;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public interface IAgent {
	/**
	 * 释放资源 （释放这台电脑 by IP，关闭WebDriver）
	 */
	public void releaseInstance(String instanceIpAddr, WebDriver driver);

	/**
	 * @Des POST代理请求的封装 用于登录
	 * @param bankJsonBean
	 * @param requestPath
	 *            登录的请求路径
	 */
	public TaskInsurance postAgent(InsuranceRequestParameters insuranceRequestParameters, String requestPath);

	/**
	 * @Des POST代理请求的封装
	 *      用于登录,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
	 * @param bankJsonBean
	 * @param requestPath
	 *            登录的请求路径
	 */
	public TaskInsurance postAgent(InsuranceRequestParameters insuranceRequestParameters, String requestPath,
			Long intervalTime);

	/**
	 * @Des POST代理请求的封装 用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作
	 * @param bankJsonBean
	 * @param requestPath
	 *            登录的请求路径
	 */
	public TaskInsurance postAgentCombo(InsuranceRequestParameters insuranceRequestParameters, String requestPath);
}
