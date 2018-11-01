package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouBase;
import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouHtml;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuDetail;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuHtml;
import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanDetail;
import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunBase;
import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunHtml;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinBasic;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinDetail;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinHtml;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongBase;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongHtml;


public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingChaoZhouBase  chaozhouBase;
	public HousingChaoZhouHtml chaozhouHtml;
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public HousingChaoZhouBase getChaozhouBase() {
		return chaozhouBase;
	}
	public void setChaozhouBase(HousingChaoZhouBase chaozhouBase) {
		this.chaozhouBase = chaozhouBase;
	}
	public HousingChaoZhouHtml getChaozhouHtml() {
		return chaozhouHtml;
	}
	public void setChaozhouHtml(HousingChaoZhouHtml chaozhouHtml) {
		this.chaozhouHtml = chaozhouHtml;
	}
	
	
}
