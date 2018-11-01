package app.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiAccount;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiCallRec;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiMsg;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiPayfee;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiUserInfo;
import app.bean.DataBean;
import app.bean.MsgBean;
import app.bean.ResultBean;
import app.commontracerlog.TracerLog;

@Component
public class TelecomShanghaiParser {
	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 解析用户信息
	 * @param page
	 * @param task_id
	 * @return
	 */
	public TelecomShanghaiUserInfo parserUserinfo(Page page, String task_id) {
		
		tracer.addTag("crawler.telecom.shanghai.parserUserinfo.parser", task_id);
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<TelecomShanghaiUserInfo>>(){}.getType();
		DataBean<TelecomShanghaiUserInfo> data = gson.fromJson(page.getWebResponse().getContentAsString(), userListType);
		TelecomShanghaiUserInfo telecomShanghaiUserInfo = data.getRESULT();
		if(null == telecomShanghaiUserInfo){
			return null;
		}
		telecomShanghaiUserInfo.setTaskid(task_id);
		return telecomShanghaiUserInfo;
	}

	/**
	 * @Des 解析缴费信息
	 * @param page
	 * @param task_id
	 * @return
	 */
	public List<TelecomShanghaiPayfee> parserPayfee(Page page, String task_id) {
		
		tracer.addTag("crawler.telecom.shanghai.parserPayfee.parser", task_id);
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<TelecomShanghaiPayfee>>(){}.getType();
		DataBean<TelecomShanghaiPayfee> data = gson.fromJson(page.getWebResponse().getContentAsString(),userListType);
		if(null == data){
			return null;
		}
		List<TelecomShanghaiPayfee> list = data.getList();
		for(TelecomShanghaiPayfee payfee:list){
			payfee.setTaskid(task_id);
		}
		return list;
	}

	/**
	 * @Des 解析账户信息
	 * @param page
	 * @param task_id
	 * @return
	 */
	public TelecomShanghaiAccount parserAccount(Page page, String task_id) {
		
		tracer.addTag("crawler.telecom.shanghai.parserAccount.parser", task_id);
		Gson gson = new Gson();
		TelecomShanghaiAccount account = gson.fromJson(page.getWebResponse().getContentAsString(), TelecomShanghaiAccount.class);
		if(null == account){
			return null;
		}
		account.setTaskid(task_id);
		return account;
	}

	/**
	 * @Des 解析通话详单
	 * @param page
	 * @param task_id
	 * @return
	 */
	public List<TelecomShanghaiCallRec> parserCallRec(Page page, String task_id) throws Exception{
		
		tracer.addTag("crawler.telecom.shanghai.parserCallRec.parser", task_id);
		List<TelecomShanghaiCallRec> telecomShanghaiCallRecs = new ArrayList<TelecomShanghaiCallRec>();
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<ResultBean>>(){}.getType();
		DataBean<ResultBean> data = gson.fromJson(page.getWebResponse().getContentAsString(), userListType);
		List<TelecomShanghaiCallRec> list = data.getRESULT().getPagedResult();
		if(null != list && list.size()>0){
			for(int i=1;i<list.size();i++){
				list.get(i).setTaskid(task_id);
				telecomShanghaiCallRecs.add(list.get(i));
			}
		}
		return telecomShanghaiCallRecs;
	}

	/**
	 * @Des 解析短信信息
	 * @param page
	 * @param task_id
	 * @return
	 */
	public List<TelecomShanghaiMsg> parserMsgRec(Page page, String task_id) {
		tracer.addTag("crawler.telecom.shanghai.parserMsgRec.parser", task_id);
		List<TelecomShanghaiMsg> telecomShanghaiMsgs = new ArrayList<TelecomShanghaiMsg>();
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<MsgBean>>(){}.getType();
		String html = page.getWebResponse().getContentAsString();
		DataBean<MsgBean> data = gson.fromJson(html, userListType);
		List<TelecomShanghaiMsg> list = data.getRESULT().getPagedResult();
		if(null != list && list.size()>0){
			for(int i=1;i<list.size();i++){
				list.get(i).setTaskid(task_id);
				telecomShanghaiMsgs.add(list.get(i));
			}
		}
		return telecomShanghaiMsgs;
	}

}
