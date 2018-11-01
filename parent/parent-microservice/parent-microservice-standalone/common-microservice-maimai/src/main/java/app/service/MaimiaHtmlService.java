package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.maimai.json.MaimaiJsonBean;
import com.crawler.maimai.json.MaimaiStatusCode;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendEducations;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendUserInfo;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendWorkExps;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserEducations;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserInfo;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserWorkExps;
import com.microservice.dao.entity.crawler.maimai.TaskMaimai;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.maimai.MaimaiFriendEducationsRepository;
import com.microservice.dao.repository.crawler.maimai.MaimaiFriendUserInfoRepository;
import com.microservice.dao.repository.crawler.maimai.MaimaiFriendWorkExpsRepository;
import com.microservice.dao.repository.crawler.maimai.MaimaiUserEducationsRepository;
import com.microservice.dao.repository.crawler.maimai.MaimaiUserInfoRepository;
import com.microservice.dao.repository.crawler.maimai.MaimaiUserWorkExpsRepository;
import com.microservice.dao.repository.crawler.maimai.TaskMaimaiRepository;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;

import app.commontracerlog.TracerLog;
import app.htmlparser.MiamaiParser;
@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.maimai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.maimai" })
public class MaimiaHtmlService {
	@Autowired
	private MiamaiParser miamaiParser;
	@Autowired
	private MaimaiUserInfoRepository maimaiUserInfoRepository;
	@Autowired
	private MaimaiUserEducationsRepository maimaiUserEducationsRepository;
	@Autowired
	private MaimaiUserWorkExpsRepository maimaiUserWorkExpsRepository;
	@Autowired
	private MaimaiFriendUserInfoRepository maimaiFriendUserInfoRepository;
	@Autowired
	private MaimaiFriendEducationsRepository maimaiFriendEducationsRepository;
	@Autowired
	private MaimaiFriendWorkExpsRepository maimaiFriendWorkExpsRepository;
	@Autowired
	private TaskMaimaiRepository taskMaimaiRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
    private TaskStandaloneRepository taskStandaloneRepository;
	
	public Future<String> getResult(PbccrcJsonBean pbccrcJsonBean, TaskMaimai taskMaimai, TaskStandalone taskStandalone, WebClient webClient, String html) throws Exception{
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCardList = object.get("auth_info").getAsJsonObject();
		String u = accountCardList.get("u").toString().replaceAll("\"", "");
		String channel = accountCardList.get("channel").toString().replaceAll("\"", "");
		String version = accountCardList.get("version").toString().replaceAll("\"", "");
		String _csrf = accountCardList.get("_csrf").toString().replaceAll("\"", "");
		String access_token = accountCardList.get("access_token").toString().replaceAll("\"", "");
		String uid = accountCardList.get("uid").toString().replaceAll("\"", "");
		String token = accountCardList.get("token").toString().replaceAll("\"", "");
		
		String u1=URLEncoder.encode( u, "UTF-8" );
		String channel1=URLEncoder.encode( channel, "UTF-8" );
		String version1=URLEncoder.encode( version, "UTF-8" );
		String _csrf1=URLEncoder.encode( _csrf, "UTF-8" );
		String access_token1=URLEncoder.encode( access_token, "UTF-8" );
		String uid1=URLEncoder.encode( uid, "UTF-8" );
		String token1=URLEncoder.encode( token, "UTF-8" );
		
		String userUrl = "https://maimai.cn/contact/detail/"+u1+"?from=HeaderCard&jsononly=1";
		WebRequest webRequest1 = new WebRequest(new URL(userUrl), HttpMethod.GET);
		Page searchPage1= webClient.getPage(webRequest1);
		String html2 = searchPage1.getWebResponse().getContentAsString();
		tracerLog.addTag("脉脉用户html", html2);
		//脉脉用户的基本信息
		MaimaiUserInfo userInfo = miamaiParser.userInfo(html2,taskMaimai);
		if(userInfo!=null||!userInfo.equals("")){
			maimaiUserInfoRepository.save(userInfo);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_SUCCESS.getDescription());
			taskMaimai.setUserinfoStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户的基本信息入库失败");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_FAILUE.getDescription());
			taskMaimai.setUserinfoStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_INFO_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
		
				
		//脉脉用户的教育经历
		List<MaimaiUserEducations> userEducations = miamaiParser.userEducations(html2,taskMaimai);
		if(userEducations!=null||!userEducations.equals("")){
			maimaiUserEducationsRepository.saveAll(userEducations);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_SUCCESS.getDescription());
			taskMaimai.setUserEducationsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户的教育经历");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_FAILUE.getDescription());
			taskMaimai.setUserEducationsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_EDUCATIONS_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
				
		//脉脉用户的工作经历
		List<MaimaiUserWorkExps> userWorkExps = miamaiParser.userWorkExps(html2,taskMaimai);
		if(userWorkExps!=null||!userWorkExps.equals("")){
			maimaiUserWorkExpsRepository.saveAll(userWorkExps);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_SUCCESS.getDescription());
			taskMaimai.setUserWorkExpsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户的工作经历");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_FAILUE.getDescription());
			taskMaimai.setUserWorkExpsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_USER_WORK_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
		
		
		String url = "https://open.taou.com/maimai/contact/v4/pbd1?version="+version1+"&ver_code=web_1"
				+ "&channel="+channel1+"&push_permit=1&u="+u1+"&_csrf="+_csrf1+""
				+ "&access_token="+access_token1+"&uid=%22"+uid1+"%22&token=%22"+token1+"%22&json=1&paginate=0";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		String html1 = searchPage.getWebResponse().getContentAsString();
		tracerLog.addTag("脉脉用户朋友html", html1);
		System.out.println("html1:"+html1);
		JsonObject object1 = (JsonObject) parser.parse(html1); // 创建JsonObject对象
		//System.out.println(object);
		JsonArray accountCardList1 = object1.get("data").getAsJsonArray();
		System.out.println("accountCardList1:"+accountCardList1.size());
		//List<String> list = new ArrayList<String>();
		for (JsonElement acc : accountCardList1) {
			JsonObject account = acc.getAsJsonObject();
			String id = account.get("id").toString().replaceAll("\"", "");
			//list.add(id);
			System.out.println("id:"+id);
			String url1 = "https://maimai.cn/contact/detail/"+id+"?from=selectUser&jsononly=1";
			try {
				html(url1, webClient, taskMaimai);
			} catch (Exception e) {
				// TODO: handle exception
				taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getPhase());
				taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getPhasestatus());
				taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getDescription());
//				taskMaimai.setError_code(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getError_code());
				taskStandaloneRepository.save(taskStandalone);
			}
		}
		
		taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_SUCCESS.getPhase());
		taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_SUCCESS.getPhasestatus());
		taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_SUCCESS.getDescription());
//		taskMaimai.setError_code(MaimaiStatusCode.MAIMAI_CRAWLER_SUCCESS.getError_code());
		taskStandalone.setFinished(true);
		taskStandaloneRepository.save(taskStandalone);
		return null;
		
	}
	
	@Async
	public void html(String url, WebClient webClient, TaskMaimai taskMaimai) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		String html1 = searchPage.getWebResponse().getContentAsString();
		//脉脉用户朋友的基本信息
		MaimaiFriendUserInfo friendUserInfo = miamaiParser.friendUserInfo(html1,taskMaimai);
		if(friendUserInfo!=null||!friendUserInfo.equals("")){
			maimaiFriendUserInfoRepository.save(friendUserInfo);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_SUCCESS.getDescription());
			taskMaimai.setFriendUserInfoStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户朋友的基本信息");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_FAILUE.getDescription());
			taskMaimai.setFriendUserInfoStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_USER_INFO_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
		
		//脉脉用户朋友的教育经历
		List<MaimaiFriendEducations> friendEducations = miamaiParser.friendEducations(html1,taskMaimai);
		if(friendEducations!=null||!friendEducations.equals("")){
			maimaiFriendEducationsRepository.saveAll(friendEducations);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_SUCCESS.getDescription());
			taskMaimai.setFriendEducationsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户朋友的教育经历");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_FAILUE.getDescription());
			taskMaimai.setFriendEducationsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_EDUCATIONS_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
		
		//脉脉用户朋友的工作经历
		List<MaimaiFriendWorkExps> friendWorkExps = miamaiParser.friendWorkExps(html1,taskMaimai);
		if(friendWorkExps!=null||!friendWorkExps.equals("")){
			maimaiFriendWorkExpsRepository.saveAll(friendWorkExps);
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_SUCCESS.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_SUCCESS.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_SUCCESS.getDescription());
			taskMaimai.setFriendWorkExpsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_SUCCESS.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}else{
			System.out.println("脉脉用户朋友的工作经历");
//			taskMaimai.setPhase(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_FAILUE.getPhase());
//			taskMaimai.setPhase_status(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_FAILUE.getPhasestatus());
//			taskMaimai.setDescription(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_FAILUE.getDescription());
			taskMaimai.setFriendWorkExpsStatus(MaimaiStatusCode.MAIMAI_CRAWLER_FRIEND_WORK_FAILUE.getError_code());
			taskMaimaiRepository.save(taskMaimai);
		}
	}

}
