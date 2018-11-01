package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.crawler.cmcc.domain.json.CallRecordBean;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import org.springframework.retry.annotation.Backoff; 
import com.google.gson.Gson;
import app.bean.PageBean;
import app.commontracerlog.TracerLog;
import app.exception.DonotRetryException;
import app.unit.Def;

@Component
public class CmccRetryService {
	
	@Autowired
	private TracerLog tracer;

	/**
	 * 获取详情数据，增加重试机制
	 * @param url
	 * @param webClient
	 * @param qryMonth
	 * @param y
	 * @param m
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws Exception520001 
	 * @throws Exception2039 
	 */ 
	@Retryable(value={RuntimeException.class,},maxAttempts=5,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public PageBean retry(String url, WebClient webClient, String qryMonth, int y, int m) throws FailingHttpStatusCodeException, IOException, DonotRetryException{
		
		long welcomeTimeMillis = System.currentTimeMillis();
		Page page = null;
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+welcomeTimeMillis);
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		long startTime = System.currentTimeMillis();
		page = webClient.getPage(requestSettings);
		long endTime = System.currentTimeMillis();
		String orihtml = page.getWebResponse().getContentAsString(); 
		int statusCode = page.getWebResponse().getStatusCode();
		if(555 == statusCode || 404 == statusCode){
			tracer.addTag(qryMonth+" y:"+y+" m:"+m+" statusCode:"+statusCode+" "+System.currentTimeMillis(), statusCode+" 重试机制触发 URL"+url);
			tracer.addTag(qryMonth+" y:"+y+" m:"+m+" statusCode:"+statusCode+" "+System.currentTimeMillis(), statusCode+" 重试机制触发 HTML"+orihtml);
			throw new RuntimeException("状态码="+statusCode+" ，重试机制触发！");
		}
		String html = "";	
		if(orihtml!=null&&orihtml.contains("jQuery")){
			int subStart = orihtml.indexOf("({"); 
			html = orihtml.substring(subStart+1, orihtml.length()-1);
		}
		tracer.addTag(qryMonth+" y:"+y+" m:"+m +" 耗时 "+(endTime-startTime)+":ms HTML", html);
		tracer.addTag(qryMonth+" y:"+y+" m:"+m +" 耗时 "+(endTime-startTime)+":ms URL","[StatusCode] "+statusCode+" [URL] "+url);
			
		//第一页或最后一页有 totalNum by meidi 20180510
		@SuppressWarnings("unchecked")
		CallRecordBean<List> callRecordBean = new Gson().fromJson(html,CallRecordBean.class);
			
		if(callRecordBean!=null){ 
			String retCode = callRecordBean.getRetCode();
			if(callRecordBean.getTotalNum()!=null){ // 第一页或最后一页有 totalNum 的情况 by meidi 20170510
				int totalNum = callRecordBean.getTotalNum();
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 总条数", totalNum+"");
				if(totalNum>=Def.pageSize){
					m = totalNum;
				}
				tracer.addTag(qryMonth+"-Total-m-max"+" y:"+y+" m:"+m, "[StatusCode] "+statusCode+" [URL] "+url);
				PageBean pageBean = new PageBean();
				pageBean.setPages(page);
				pageBean.setStartYear(qryMonth);
				pageBean.setM(m);
				pageBean.setTotal(totalNum);
				return pageBean;  
			}else if(callRecordBean.getData()!=null && callRecordBean.getData().size()>0){ // 没有 totalNum 但条数是200 by meidi 20170510
				int size = callRecordBean.getData().size();
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" -noTotal-RealSize", size+"");
				if(size>=Def.pageSize){
					m+=Def.pageSize;
				}
				tracer.addTag(qryMonth+"-noTotal-m-max"+" y:"+y+" m:"+m, "[StatusCode] "+statusCode+" [URL] "+url);
				PageBean pageBean = new PageBean();
				pageBean.setPages(page);
				pageBean.setStartYear(qryMonth);
				pageBean.setM(m);
				return pageBean; 
			}else if("999999".equals(retCode)){		//详单查询异常
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 重试机制触发 retcode 999999 URL "+System.currentTimeMillis(), "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" retcode 999999 HTML "+System.currentTimeMillis(), orihtml);
				throw new RuntimeException("retcode=999999，详单查询异常，重试机制触发！");
			}else if("9700".equals(retCode)){		//系统正在进行流量控制
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 重试机制触发 retcode 9700 URL "+System.currentTimeMillis(), "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" retcode 9700 HTML "+System.currentTimeMillis(), orihtml);
				throw new RuntimeException("retcode=9700，详单查询异常，重试机制触发！");
			}else if("3019".equals(retCode)){		//特殊时期不受理详单查询业务，感谢您使用中国移动网上商城
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 重试机制触发 retcode 3019 URL "+System.currentTimeMillis(), "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" retcode 3019 HTML "+System.currentTimeMillis(), orihtml);
				throw new DonotRetryException(3019,"retcode=3019，特殊时期不受理详单查询业务，感谢您使用中国移动网上商城！");
			}else if("520001".equals(retCode)){
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 临时身份凭证不存在  retcode 520001 URL "+System.currentTimeMillis(), "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" retcode HTML "+System.currentTimeMillis(), orihtml);
				throw new DonotRetryException(520001,"临时身份凭证不存在");
			}else if("2039".equals(retCode)){
				tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 您选择时间段没有详单记录  retcode 2039 URL "+System.currentTimeMillis(), "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m +" retcode 2039 HTML", orihtml);
				throw new DonotRetryException(2039,"您选择时间段没有详单记录");
			}else{
				tracer.addTag(qryMonth+" y:"+y+" m:"+m +" TODO URL", "[StatusCode] "+statusCode+" [URL] "+url);
				tracer.addTag(qryMonth+" y:"+y+" m:"+m +" TODO HTML", orihtml);
			} 
		}else{
			tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanIsNull", "callRecordBean is null");
			tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanIsNull HTML", orihtml);
			tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanIsNull URL", "[StatusCode] "+statusCode+" [URL] "+url);
		}	
		
		tracer.addTag(qryMonth+" y:"+y+" m:"+m+" return null", "[StatusCode] "+statusCode+" [URL] "+url);
		return null;
		
	}
	
	

}
