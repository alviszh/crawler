package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3ChargeInfo;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CurrentSituation;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Html;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3UserInfo;

public class WebParam<T> {
	
	private HtmlPage page;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	
	private Integer monthtotalrow;   //用于通话或者短信记录中，记录每个月的总记录数据（因为山西的没有具体的通话时间）
	private List<T> list;
	private TelecomShanxi3Html telecomShanxi3Html;
	private TelecomShanxi3UserInfo telecomShanxi3UserInfo;
	private TelecomShanxi3ChargeInfo telecomShanxi3ChargeInfo;
	private TelecomShanxi3CurrentSituation telecomShanxi3CurrentSituation;
	
	//为了方便类型转换添加的TextPage
	private TextPage tpage;
	
	private Page ppage;
	public HtmlPage getPage() {
		return page;
	}
	public void setPage(HtmlPage page) {
		this.page = page;
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
	public TelecomShanxi3Html getTelecomShanxi3Html() {
		return telecomShanxi3Html;
	}
	public void setTelecomShanxi3Html(TelecomShanxi3Html telecomShanxi3Html) {
		this.telecomShanxi3Html = telecomShanxi3Html;
	}
	public TelecomShanxi3UserInfo getTelecomShanxi3UserInfo() {
		return telecomShanxi3UserInfo;
	}
	public void setTelecomShanxi3UserInfo(TelecomShanxi3UserInfo telecomShanxi3UserInfo) {
		this.telecomShanxi3UserInfo = telecomShanxi3UserInfo;
	}
	public TextPage getTpage() {
		return tpage;
	}
	public void setTpage(TextPage tpage) {
		this.tpage = tpage;
	}
	public Integer getMonthtotalrow() {
		return monthtotalrow;
	}
	public void setMonthtotalrow(Integer monthtotalrow) {
		this.monthtotalrow = monthtotalrow;
	}
	public TelecomShanxi3ChargeInfo getTelecomShanxi3ChargeInfo() {
		return telecomShanxi3ChargeInfo;
	}
	public void setTelecomShanxi3ChargeInfo(TelecomShanxi3ChargeInfo telecomShanxi3ChargeInfo) {
		this.telecomShanxi3ChargeInfo = telecomShanxi3ChargeInfo;
	}
	public Page getPpage() {
		return ppage;
	}
	public void setPpage(Page ppage) {
		this.ppage = ppage;
	}
	public TelecomShanxi3CurrentSituation getTelecomShanxi3CurrentSituation() {
		return telecomShanxi3CurrentSituation;
	}
	public void setTelecomShanxi3CurrentSituation(TelecomShanxi3CurrentSituation telecomShanxi3CurrentSituation) {
		this.telecomShanxi3CurrentSituation = telecomShanxi3CurrentSituation;
	}
	
}
