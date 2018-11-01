package app.service.aop;

import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import org.openqa.selenium.WebDriver;
import com.crawler.pbccrc.json.PbccrcJsonBean;

/**
 * agent中间层接口
 */
public interface IAgent {

    /**
     * 释放资源  （释放这台电脑 by IP，关闭WebDriver）
     */
    public void releaseInstance(String instanceIpAddr, WebDriver driver);

    /**
     * @Des POST代理请求的封装
     * @param pbccrcJsonBean
     * @param requestPath 登录的请求路径
     */
    public String postAgent(PbccrcJsonBean pbccrcJsonBean,String requestPath);

    /**
     * @Des POST代理请求的封装  ,可自定义intervalTime（自动关闭时间）长度，默认2分钟。例如：浦发信用卡需要3分钟才能完成爬取
     * @param pbccrcJsonBean
     * @param requestPath 登录的请求路径
     */
    public String postAgent(PbccrcJsonBean pbccrcJsonBean,String requestPath,Long intervalTime);
}
