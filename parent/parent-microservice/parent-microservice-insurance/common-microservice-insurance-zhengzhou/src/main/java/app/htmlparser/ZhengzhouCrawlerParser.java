package app.htmlparser;

import app.domain.WebParam;
import app.enums.InsuranceZhengzhouCrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfo;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenCompany;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenPayDetail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳社保爬取 HTML解析
 * @author rongshengxu
 *
 */
@Component
public class ZhengzhouCrawlerParser {
	
	public static final Logger log = LoggerFactory.getLogger(ZhengzhouCrawlerParser.class);
	
	/**
	 * 解析爬取的登录信息
	 * @param
	 * @return
	 */
	public String parserLogin(WebParam<HtmlPage> webParam){
		//无法获取网页内容,超时
		if(webParam == null){
			return InsuranceZhengzhouCrawlerResult.TIMEOUT.getCode();
		}
		String contextString = webParam.getCode();
		if(contextString.equals(InsuranceZhengzhouCrawlerResult.SUCCESS.getCode())){
			return InsuranceZhengzhouCrawlerResult.SUCCESS.getCode();
		}else{
			return InsuranceZhengzhouCrawlerResult.EXCEPTION.getCode();
		}
	}

}
