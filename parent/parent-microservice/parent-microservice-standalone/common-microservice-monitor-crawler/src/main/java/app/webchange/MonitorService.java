package app.webchange;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.monitor.json.MonitorJsCompare;
import com.crawler.monitor.json.MonitorJsCountChange;
import com.crawler.monitor.json.MonitorTempBean;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebLoginUrl;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;
import com.microservice.dao.entity.crawler.monitor.MonitorJsAfterTreat;
import com.microservice.dao.entity.crawler.monitor.MonitorLoginPageHtml;
import com.microservice.dao.entity.crawler.monitor.MonitorLoginPageJs;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebLoginUrlRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebUsableRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorJsAfterTreatRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorLoginPageHtmlRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorLoginPageJsRepository;

import app.commontracerlog.TracerLog;
import app.utils.MD5Utils;
import app.utils.PatternUtils;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorService {
	@Autowired
	private MonitorLoginPageHtmlRepository loginPageHtmlRepository;
	@Autowired
	private MonitorLoginPageJsRepository loginPageJsRepository;
	@Autowired
	private MonitorHelperService helperService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MonitorJsAfterTreatRepository jsAfterTreatRepository;
	@Autowired
	private MonitorAllWebLoginUrlRepository allWebLoginUrlRepository;
	@Autowired
	private MonitorAllWebUsableRepository allWebUsableRepository;
	
	//用于存储js数量发生变化时回显数据使用(参数)
	List<MonitorJsCountChange> jsChangeList=new ArrayList<MonitorJsCountChange>();
	String lastTaskId="";
	/////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////
	//根据可用监控网站的信息内容首次获取html
	@Async
	public Future<String> monitorWeb(String eachDeveloper, String taskid) throws Exception{
		List<MonitorAllWebLoginUrl> list = allWebLoginUrlRepository.findUrlUsableByDeveloper(eachDeveloper);
		if(null!=list && list.size()>0){
			String webType="";
			String htmlexecutemethod="";
			String loginUrl="";
			String html="";   //解析返回的登录页面初始html
			Document doc;   
			int currentJsCount=0;  //本次执行任务获取的js数量
			MonitorTempBean tempBean=null;
			long newHtmlId=0;
			long loginUrlId=0;
			String developer="";

			String currentHtmlMd5 = "";
			String currentHtmlModified="";
			String htmlAfterTreat = "";
			
			long lastModified = 0;
			
			MonitorLoginPageHtml lastHtmlRecord = null;
			
			String lastHtmlMd5 = "";
			String lastHtmlModified="";
			String compareTaskid = "";  //参照taskid
			int lastJsCount = 0;
			String jsCountChangeDetail="无变化";
			boolean jsCountChangeFlag=false;
			int changeNum=0;
			int webStatusCode = 200;
			boolean usableflag=true;
			List<MonitorAllWebUsable> webStatusList = new ArrayList<MonitorAllWebUsable>();
			MonitorAllWebUsable monitorAllWebUsable=null;
			for (MonitorAllWebLoginUrl webLoginInfo : list) {
				try {
					//根据网站的执行方法，选择不同的方法执行
					htmlexecutemethod = webLoginInfo.getHtmlexecutemethod().trim();
					webType = webLoginInfo.getWebtype().trim();
					loginUrl=webLoginInfo.getUrl().trim();
					loginUrlId=webLoginInfo.getId();
					developer=webLoginInfo.getDeveloper().trim();   
					if(htmlexecutemethod.equals("ssl")){
						tempBean = helperService.getHtmlSourceCodeByIgnoreSSL(webType,loginUrl, null, "utf-8");
						webStatusCode = tempBean.getWebStatusCode();
						if(200==webStatusCode){ //网站可以正常连接(只存储不能正常链接的网站)
							html=tempBean.getHtml();
							doc = tempBean.getDoc();
							currentJsCount=tempBean.getCurrentJsCount();
						}else{  //直接跳出当前循环
							usableflag=false;
							monitorAllWebUsable=new MonitorAllWebUsable();
							monitorAllWebUsable.setIsusable(usableflag);
							monitorAllWebUsable.setStatuscode(webStatusCode);
							monitorAllWebUsable.setExceptioninfo(tempBean.getExceptioninfo().trim());
							monitorAllWebUsable.setUrl(loginUrl);
							monitorAllWebUsable.setWebtype(webType);
							monitorAllWebUsable.setTaskid(taskid);
							monitorAllWebUsable.setDeveloper(developer);
							webStatusList.add(monitorAllWebUsable);
							continue;
						}
					}else if(htmlexecutemethod.equals("jsoup")){
						tempBean = helperService.getHtmlByJsoup(webType,loginUrl);
						webStatusCode = tempBean.getWebStatusCode();
						if(200==webStatusCode){ //网站可以正常连接(只存储不能正常链接的网站)
							html=tempBean.getHtml();
							doc = tempBean.getDoc();
							currentJsCount=tempBean.getCurrentJsCount();
						}else{  //直接跳出当前循环
							usableflag=false;
							monitorAllWebUsable=new MonitorAllWebUsable();
							monitorAllWebUsable.setIsusable(usableflag);
							monitorAllWebUsable.setStatuscode(webStatusCode);
							monitorAllWebUsable.setExceptioninfo(tempBean.getExceptioninfo().trim());
							monitorAllWebUsable.setUrl(loginUrl);
							monitorAllWebUsable.setWebtype(webType);
							monitorAllWebUsable.setTaskid(taskid);
							monitorAllWebUsable.setDeveloper(developer);
							webStatusList.add(monitorAllWebUsable);
							continue;
						}
					}else if(htmlexecutemethod.equals("htmlunit")){   //htmlunit方法执行
						tempBean = helperService.getHtmlByHtmlunit(webType,loginUrl);
						webStatusCode = tempBean.getWebStatusCode();
						if(200==webStatusCode){ //网站可以正常连接(只存储不能正常链接的网站)
							html=tempBean.getHtml();
							doc = tempBean.getDoc();
							currentJsCount=tempBean.getCurrentJsCount();
						}else{  //直接跳出当前循环
							usableflag=false;
							monitorAllWebUsable=new MonitorAllWebUsable();
							monitorAllWebUsable.setIsusable(usableflag);
							monitorAllWebUsable.setStatuscode(webStatusCode);
							monitorAllWebUsable.setExceptioninfo(tempBean.getExceptioninfo().trim());
							monitorAllWebUsable.setUrl(loginUrl);
							monitorAllWebUsable.setWebtype(webType);
							monitorAllWebUsable.setTaskid(taskid);
							monitorAllWebUsable.setDeveloper(developer);
							webStatusList.add(monitorAllWebUsable);
							continue;
						}
					}else{  //httpclient
						tempBean = helperService.getHtmlByHttpClient(webType,loginUrl);
						webStatusCode = tempBean.getWebStatusCode();
						if(200==webStatusCode){ //网站可以正常连接(只存储不能正常链接的网站)
							html=tempBean.getHtml();
							doc = tempBean.getDoc();
							currentJsCount=tempBean.getCurrentJsCount();
						}else{  //直接跳出当前循环
							usableflag=false;
							monitorAllWebUsable=new MonitorAllWebUsable();
							monitorAllWebUsable.setIsusable(usableflag);
							monitorAllWebUsable.setStatuscode(webStatusCode);
							monitorAllWebUsable.setExceptioninfo(tempBean.getExceptioninfo().trim());
							monitorAllWebUsable.setUrl(loginUrl);
							monitorAllWebUsable.setWebtype(webType);
							monitorAllWebUsable.setTaskid(taskid);
							monitorAllWebUsable.setDeveloper(developer);
							webStatusList.add(monitorAllWebUsable);
							continue;
						}
					}
					//不管网页源码的变化与否用的是last-modified还是加密源码的的方式判断，都要存储处理后的源码——htmlAfterTreat，用于后期比对
					//对html的源码进行处理(如果不需要处理，返回htmlAfterTreat内容的还是最初的html)
					try {
						htmlAfterTreat = helperService.getHtmlAfterTreat(webLoginInfo, html, doc);
					} catch (Exception e) {
						tracer.addTag(webType+"处理源码的过程中出现异常:",e.getMessage());
						htmlAfterTreat = html;
					}
					currentHtmlMd5=MD5Utils.StringToMd5(htmlAfterTreat);
					//尝试获取上次修改时间，可以获取到时间戳的，就将时间戳作为加密后的md5码
					lastModified = new URL(loginUrl).openConnection().getLastModified();
			        if(lastModified==0){  //响应头没有提供最近一次修改时间,则用加密源码的方式，同时将currentHtmlModified置为"null"
			    		currentHtmlModified="nohtmllastmodified";
			        }else{  //响应头提供了最近一次修改时间，直接将时间戳作为MD5字段
			        	currentHtmlModified=lastModified+"";
			        }
					
					//根据登录网站名称检索数据库是否有满足条件的历史记录(参照素材)
					lastHtmlRecord = loginPageHtmlRepository.findTopByWebtypeOrderByCreatetimeDesc(webType);
					if(null!=lastHtmlRecord){ //参照信息实体不为null
						lastHtmlMd5 = lastHtmlRecord.getHtmlmd5();
						lastHtmlModified=lastHtmlRecord.getHtmlmodified();
						compareTaskid = lastHtmlRecord.getTaskid().trim();  //参照taskid
						lastJsCount = lastHtmlRecord.getJscount();
						//先检测js数量是否发生变化
//						monitorJsCount(taskid,webLoginInfo,lastHtmlRecord,currentJsCount);
						if(currentJsCount!=lastJsCount){
							if(currentJsCount>lastJsCount){
								jsCountChangeFlag=true;
								changeNum=currentJsCount-lastJsCount;
								jsCountChangeDetail="数量增加"+changeNum+"个";
							}else{
								jsCountChangeFlag=true;
								changeNum=lastJsCount-currentJsCount;
								jsCountChangeDetail="数量减少"+changeNum+"个";
							}
						}else{   //需要加else否则会影响下个网站的运行结果
							jsCountChangeFlag=false;
							changeNum=0;
							jsCountChangeDetail="无变化";
						}
						//再监测网页源码是否发生变化
						if(lastHtmlModified!=null && lastHtmlModified.equals("nohtmllastmodified")){  //说明应该对比md5码
							if(currentHtmlMd5.equals(lastHtmlMd5)){
								MonitorLoginPageHtml loginPageHtml=new MonitorLoginPageHtml(taskid, loginUrl,currentHtmlMd5, 
										false,loginUrlId,webType,currentJsCount, html,htmlAfterTreat,developer,currentHtmlModified,compareTaskid,jsCountChangeFlag,jsCountChangeDetail);
								MonitorLoginPageHtml htmlSaveResult = loginPageHtmlRepository.save(loginPageHtml);
								newHtmlId = htmlSaveResult.getId();
							}else{
								MonitorLoginPageHtml loginPageHtml=new MonitorLoginPageHtml(taskid, loginUrl,currentHtmlMd5, 
										true,loginUrlId,webType,currentJsCount, html,htmlAfterTreat,developer,currentHtmlModified,compareTaskid,jsCountChangeFlag,jsCountChangeDetail);
								MonitorLoginPageHtml htmlSaveResult = loginPageHtmlRepository.save(loginPageHtml);
								newHtmlId = htmlSaveResult.getId();
							}
						}else{   //比对时间戳，同时也比对md5码
							if((lastHtmlModified!=null && currentHtmlModified.equals(lastHtmlModified)) || currentHtmlMd5.equals(lastHtmlMd5)){
								MonitorLoginPageHtml loginPageHtml=new MonitorLoginPageHtml(taskid, loginUrl,currentHtmlMd5, 
										false,loginUrlId,webType,currentJsCount, html,htmlAfterTreat,developer,currentHtmlModified,compareTaskid,jsCountChangeFlag,jsCountChangeDetail);
								MonitorLoginPageHtml htmlSaveResult = loginPageHtmlRepository.save(loginPageHtml);
								newHtmlId = htmlSaveResult.getId();
							}else{
								MonitorLoginPageHtml loginPageHtml=new MonitorLoginPageHtml(taskid, loginUrl,currentHtmlMd5, 
										true,loginUrlId,webType,currentJsCount, html,htmlAfterTreat,developer,currentHtmlModified,compareTaskid,jsCountChangeFlag,jsCountChangeDetail);
								MonitorLoginPageHtml htmlSaveResult = loginPageHtmlRepository.save(loginPageHtml);
								newHtmlId = htmlSaveResult.getId();
							}
						}
					}else{ //说明是首次执行
						MonitorLoginPageHtml loginPageHtml=new MonitorLoginPageHtml(taskid, loginUrl,currentHtmlMd5, 
								false,loginUrlId,webType,currentJsCount, html,htmlAfterTreat,developer,currentHtmlModified,"",false,"");
						MonitorLoginPageHtml htmlSaveResult = loginPageHtmlRepository.save(loginPageHtml);
						newHtmlId = htmlSaveResult.getId();
					}
					//监测对应网站的js
					MonitorJs(webLoginInfo,tempBean,newHtmlId,taskid,lastHtmlRecord);
				} catch (Exception e) {
					tracer.addTag(webType+"  (异步调用层)解析网页源码时报异常，异常内容为：  ",e.toString());
				}
			}
			allWebUsableRepository.saveAll(webStatusList);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 获取js绝对路径之后，用指定的方法执行并且加密
	 * @param webLoginInfo
	 * @param tempBean2
	 * @param newHtmlId 
	 * @param taskid 
	 * @param lastHtmlRecord 
	 */
//	@Async   这个方法不能异步
	public void MonitorJs(MonitorAllWebLoginUrl webLoginInfo,MonitorTempBean tempBean, long newHtmlId, String taskid, MonitorLoginPageHtml lastHtmlRecord){
		int currentJsCount = tempBean.getCurrentJsCount();
		String jsexecutemethod = webLoginInfo.getJsexecutemethod().trim();
		String jspathtip = webLoginInfo.getJspathtip().trim();
		String developer=webLoginInfo.getDeveloper().trim();   
		Document doc = tempBean.getDoc();
		String loginUrl = webLoginInfo.getUrl().trim();
		String webType=webLoginInfo.getWebtype().trim();
		String jsContent="";
		String tempJsPath="";
		String loginUrlHeader="";
		String absJsPath="";
		String jsPathTreadContent="";
		MonitorLoginPageJs monitorLoginPageJs=null;
		List<MonitorLoginPageJs> jsList=new ArrayList<MonitorLoginPageJs>();
		/////////////////////////////////////
		boolean isJsaftertreatment = webLoginInfo.isJsaftertreatment(); //已经忽视的js没必要解析源码，此处直接忽视
		List<String> jsAfterTreatList = null;
		List<String> lastJsPathList = null;
		if(lastHtmlRecord!=null){   //如果对比素材不为null，将该任务下之前执行过的所有js全路径取出
			lastTaskId=lastHtmlRecord.getTaskid();
			//查询上次执行的所有js
			lastJsPathList = loginPageJsRepository.findJspathByTaskid(webType,lastTaskId);
		}
		if(isJsaftertreatment==true){  //有需要处理的js
			//查询出指定loginUrl下需要进行处理的jspath相关内容
			jsAfterTreatList = jsAfterTreatRepository.findJsNeedTreatByUrl(loginUrl);
		}
		/////////////////////////////////////
		if(currentJsCount>0){ 
			Elements eles = doc.select("script[src]");
			for(Element jsElement:eles){
				try{
					//获取js的绝对路径
					if(jspathtip.equals("part")){ //例如吉林公积金和石家庄社保
						tempJsPath=jsElement.attr("src");
						loginUrlHeader=PatternUtils.getLoginHttpAndHost(loginUrl);
						absJsPath=loginUrlHeader+tempJsPath;
					}else if(jspathtip.equals("add")){ //例如湖州公积金
						loginUrlHeader=PatternUtils.getLoginHttpAndHost(loginUrl);
						jsPathTreadContent=webLoginInfo.getJspathtreatcontent().trim();
						tempJsPath=jsElement.attr("src");   
						absJsPath=loginUrlHeader+jsPathTreadContent+tempJsPath;
					}else if(jspathtip.equals("cut")){  //例如丽江公积金
						jsPathTreadContent=webLoginInfo.getJspathtreatcontent().trim();
						absJsPath = jsElement.attr("abs:src");    //返回js的绝对路径
						absJsPath=absJsPath.replace(jsPathTreadContent, "");
					}else if(jspathtip.equals("partadd")){    //返回的路径是部分路径，且需要添加部分内容,还要替换掉部分内容，如吉林市公积金
						tempJsPath=jsElement.attr("src");
						loginUrlHeader=PatternUtils.getLoginHttpAndHost(loginUrl);
						jsPathTreadContent=webLoginInfo.getJspathtreatcontent().trim();
						absJsPath=loginUrlHeader+jsPathTreadContent+tempJsPath;
						if(absJsPath.contains("../")){
							absJsPath=absJsPath.replaceAll("\\.\\./", ""); //有时候部分js中会带有多余的../ 注意转义 
						}else if(absJsPath.contains("./")){
							absJsPath=absJsPath.replaceAll("\\./", ""); //有时候部分js中会带有多余的../ 注意转义 
						}
					}else if(jspathtip.equals("partreplace")){   //工商银行需要做这样的处理，因为返回的路径中，加了换行
						tempJsPath=jsElement.attr("src");
						loginUrlHeader=PatternUtils.getLoginHttpAndHost(loginUrl);
						absJsPath=loginUrlHeader+tempJsPath;
						absJsPath=replaceBlank(absJsPath);
					}else{
						absJsPath = jsElement.attr("abs:src");    //返回js的绝对路径
						absJsPath=absJsPath.replaceAll("\\.\\./", ""); //有时候部分js中会带有多余的../ 注意转义 
					}
					//青岛社保返回的js路径后边有拼接的变化内容，需去除
					if(absJsPath.contains(";")){
						absJsPath=absJsPath.split(";")[0];
					}
					//System.out.println(webType+"  jsPath-------------"+absJsPath);
					//将返回的js的绝对路径进行处理，用于判断是不是在待处理之列(有的不是js)
					String tempjspath="";
					if(absJsPath.contains(".js")){
						tempjspath=absJsPath.substring(0, absJsPath.indexOf(".js")+3);
					}else if(absJsPath.contains(".asp")){
						tempjspath=absJsPath.substring(0, absJsPath.indexOf(".asp")+4);
					}else if(absJsPath.contains(".php")){
						tempjspath=absJsPath.substring(0, absJsPath.indexOf(".php")+4);
					}else if(absJsPath.contains("&")){
						tempjspath=absJsPath.substring(0, absJsPath.lastIndexOf("&"));
					}else{
						tempjspath=absJsPath;
					}
					//===============================
					String currentJsMd5 = "";
					String afterTreatJs = "";
					int currentJsContentLength =0;
					String currentJsModified="";
					//===============================
					if(jsAfterTreatList!=null && jsAfterTreatList.size()>0){  //有需要处理的js
						if(jsAfterTreatList.toString().contains(tempjspath)){ //处理后的js在待处理之列
							//再查询该js的处理方法是什么
							MonitorJsAfterTreat monitorJsAfterTreat = jsAfterTreatRepository.findByJspath(tempjspath);
							String jsOrLength=monitorJsAfterTreat.getMd5orlength().trim();
							if(jsOrLength.contains("ignore")){   //带有时间戳的js，时间戳不一样，也要排除在监控范围之外
								jsContent="已排除在监控任务之外";    //将网页源码内容换成此
							}else{   //需要解析源码或者是获取时间戳
								if(jsexecutemethod.equals("ssl")){
									jsContent=helperService.getJsSourceCodeByIgnoreSSL(webType,absJsPath, null, "utf-8");
								}else if(jsexecutemethod.equals("jsoup")){
									jsContent=helperService.getJsByJsoup(absJsPath);
								}else{   //htmlunit方法执行
									jsContent=helperService.getJsByHtmlunit(absJsPath);
								}
							}
							MonitorJsCompare jsCompare = helperService.getJsAfterTreat(monitorJsAfterTreat, webType,absJsPath, jsContent);
							afterTreatJs = jsCompare.getAfterTreatJs();
							currentJsContentLength = jsCompare.getCurrentJsContentLength();
							currentJsMd5 = jsCompare.getCurrentJsMd5();
						}else{  //得到的js不需要经过处理，可直接运行
							if(jsexecutemethod.equals("ssl")){
								jsContent=helperService.getJsSourceCodeByIgnoreSSL(webType,absJsPath, null, "utf-8");
							}else if(jsexecutemethod.equals("jsoup")){
								jsContent=helperService.getJsByJsoup(absJsPath);
							}else{   //htmlunit方法执行
								jsContent=helperService.getJsByHtmlunit(absJsPath);
							}
							//================================================================
							MonitorJsCompare jsCompare = helperService.getJsAfterTreat(null, webType,absJsPath, jsContent);
							afterTreatJs = jsCompare.getAfterTreatJs();
							currentJsContentLength = jsCompare.getCurrentJsContentLength();
							currentJsMd5 = jsCompare.getCurrentJsMd5();
							//=============================================
							//尝试获取上次修改时间
							long lastModified = new URL(absJsPath).openConnection().getLastModified();
							if(lastModified==0){//响应头没有提供最近一次修改时间
								currentJsModified="nojslastmodified";
						    }else{  //响应头提供了最近一次修改时间
								currentJsModified=lastModified+"";
						    }
							//=============================================
							//================================================================
						}
					}else{ //本次要处理的网站没有相关js源码需要处理
						//根据路径，解析加密js内容
						if(jsexecutemethod.equals("ssl")){
							jsContent=helperService.getJsSourceCodeByIgnoreSSL(webType,absJsPath, null, "utf-8");
						}else if(jsexecutemethod.equals("jsoup")){
							jsContent=helperService.getJsByJsoup(absJsPath);
						}else{   //htmlunit方法执行
							jsContent=helperService.getJsByHtmlunit(absJsPath);
						}
						//=============================================
						MonitorJsCompare jsCompare = helperService.getJsAfterTreat(null, webType,absJsPath, jsContent);
						afterTreatJs = jsCompare.getAfterTreatJs();
						currentJsContentLength = jsCompare.getCurrentJsContentLength();
						currentJsMd5 = jsCompare.getCurrentJsMd5();
						//尝试获取上次修改时间
						long lastModified = new URL(absJsPath).openConnection().getLastModified();
						if(lastModified==0){//响应头没有提供最近一次修改时间
							currentJsModified="nojslastmodified";
					    }else{  //响应头提供了最近一次修改时间，直接将时间戳作为MD5字段,但是有时候响应回来的时间戳也不一样
							currentJsModified=lastModified+"";
					    }
						//=============================================
					}
					/////////////////////////////////////
					MonitorLoginPageJs loginPageJs=null;
					if(lastHtmlRecord!=null){
						if(null!=lastJsPathList && lastJsPathList.size()>0){
							if(lastJsPathList.contains(tempjspath)){ //获取的js处于上次执行任务中
								//用于对比时间戳等内容，同样的js路径有时候在多个网站中被引用，故此处还需要根据webtype进行唯一定位
								List<MonitorLoginPageJs> lastMonitorLoginPageJsList = loginPageJsRepository.findLastJsModifiedByTaskidAndJsPath(lastTaskId,tempjspath,webType);  
								if(lastMonitorLoginPageJsList!=null && lastMonitorLoginPageJsList.size()>0){  //有时候同样的js在网站登录页源码中出现两次
									MonitorLoginPageJs lastMonitorLoginPageJs = lastMonitorLoginPageJsList.get(0);
									String lastJsMd5 = lastMonitorLoginPageJs.getJsmd5().trim();
									String lastJsModified = "";
									try {
										lastJsModified =lastMonitorLoginPageJs.getJsmodified().trim(); //有时候出现异常的时候，没有执行该条记录
									} catch (Exception e) {
										System.out.println(webType+"---获取上次任务执行该js所获取的时间戳时出现异常:"+e.toString()+"---该异常对应的js是："+absJsPath);
										lastJsModified = "nojslastmodified";  //如果获取失败，默认将lastJsModified设置为nojslastmodified
									}
									
									int tempInt=0;
									//判断js内容是否更改	
									if(currentJsContentLength==tempInt){ //说明应该比对MD5或者时间戳
										if(lastJsModified!=null && lastJsModified.equals("nojslastmodified")){ //说明该js未提供时间戳，需要对比md5码
											if(currentJsMd5.equals(lastJsMd5)){  
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, false, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}else{  //未包含，有变动
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, true, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}
										}else{
											if(currentJsMd5.equals(lastJsMd5) || (lastJsModified!=null && currentJsModified.equals(lastJsModified))){  
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, false, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}else{  //未包含，有变动
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, true, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}
										}
									}else{ //比对长度(或者是忽略)
										monitorLoginPageJs = loginPageJsRepository.findByJspathAndTaskidOrderByCreatetimeDesc(absJsPath, lastTaskId);
										if(null!=monitorLoginPageJs){  //说明有比对素材
											int lastJsContentLength = monitorLoginPageJs.getJscontentlength();
											//比对前后两次js内容的长度
											if(currentJsContentLength==lastJsContentLength){  //长度相等，该js未改
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, false, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}else{  //长度不等，该js有改动
												loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
														currentJsMd5, currentJsCount, true, newHtmlId,
														webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,lastTaskId);
											}
										}else{//首次执行
											loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
													currentJsMd5, currentJsCount, false, newHtmlId,
													webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,"");
										}
									}
								}
							}else{ //（该js上次由于某些异常没有执行，本次执行了）								
							   loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
										currentJsMd5, currentJsCount, false, newHtmlId,
										webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,"");
							}
						}else{//相关js首次执行
							loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
									currentJsMd5, currentJsCount, false, newHtmlId,
									webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,"");
						}
					}else{  //无可比较对象,首次执行
							loginPageJs=new MonitorLoginPageJs(taskid, loginUrl, absJsPath, 
									currentJsMd5, currentJsCount, false, newHtmlId,
									webType, jsContent, afterTreatJs, currentJsContentLength,developer,currentJsModified,"");
						
					}
					//存储相关内容
					jsList.add(loginPageJs);
				}catch (Exception e) {
					if(!e.toString().contains("FileNotFoundException")){   //文件找不到异常可以忽视，因为有的js确实找不到
						System.out.println(webType+"---加密js的时候出现异常:"+e.toString()+"---该异常对应的js是："+absJsPath);
						tracer.addTag(webType+"  解析js源码时报异常,js路径为："+absJsPath,"异常内容为：   "+e.toString());
					}
				}
			}
			//存储本次监控任务下的js结果
			try {
				loginPageJsRepository.saveAll(jsList);
			} catch (Exception e) {
				System.out.println(webType+"   存储js的list集合时出现异常：     "+e.toString());
			}
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * js数量变化监控
	 * @param currentJsCount 
	 * @param html
	 */
	public void monitorJsCount(String taskid,MonitorAllWebLoginUrl webLoginInfo,MonitorLoginPageHtml lastHtmlRecord, int currentJsCount){
		lastTaskId = lastHtmlRecord.getTaskid();
		int lastJsCount = lastHtmlRecord.getJscount();
		String webType=webLoginInfo.getWebtype().trim();
		String loginUrl=webLoginInfo.getUrl().trim();
		String developer = webLoginInfo.getDeveloper().trim();
		try {
			MonitorJsCountChange monitorJsCountChange=null;
			String jsCountChangeTip;
			jsChangeList=new ArrayList<MonitorJsCountChange>();
			if(currentJsCount!=lastJsCount){
				if(currentJsCount>lastJsCount){
					jsCountChangeTip="数量增加";
					monitorJsCountChange=new MonitorJsCountChange(webType, loginUrl, lastJsCount, currentJsCount, jsCountChangeTip, lastTaskId, taskid,developer);
					jsChangeList.add(monitorJsCountChange);
				}else{
					jsCountChangeTip="数量减少";
					monitorJsCountChange=new MonitorJsCountChange(webType, loginUrl, lastJsCount, currentJsCount, jsCountChangeTip, lastTaskId, taskid,developer);
					jsChangeList.add(monitorJsCountChange);
				}
			}
		} catch (Exception e) {
			System.out.println("/////////////////监控js数量变化时报异常    start//////////////////////");
			System.out.println(webType+"监控js数量变化时报异常："+e.toString());
			tracer.addTag(webType+"  监控js数量变化时报异常，异常内容为：", e.toString());
			System.out.println("/////////////////监控js数量变化时报异常   end//////////////////////");
		}
	}
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
