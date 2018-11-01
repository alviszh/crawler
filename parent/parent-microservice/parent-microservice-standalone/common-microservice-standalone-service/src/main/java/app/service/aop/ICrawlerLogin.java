package app.service.aop;

import com.crawler.pbccrc.json.PbccrcJsonBean;

/**
 * 爬取接口，适用于登录就可以直接爬取的情况，可直接实现此接口
 * 
 * 
 * */
public interface ICrawlerLogin extends ICrawler{
	
	/**
	 * 登录接口
	 * 
	 * 
	 * */
	public String login(PbccrcJsonBean pbccrcJsonBean);

}
