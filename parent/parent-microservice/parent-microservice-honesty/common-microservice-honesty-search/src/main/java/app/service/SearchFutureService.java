package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.microservice.dao.entity.crawler.search.NewsContent;
import com.microservice.dao.entity.crawler.search.NewsListJson;
import com.microservice.dao.entity.crawler.search.SearchTask;

import app.commontracerlog.TracerLog;
import app.domain.WebParm;
import app.service.parse.HtmlParseService;
import app.service.unit.SanWangUnitService;

/**
 * 
 * 项目名称：common-microservice-search 类名称：SearchFutureService 类描述： 创建人：hyx
 * 创建时间：2018年1月18日 上午10:53:02
 * 
 * @version
 */

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.search",
		"com.microservice.dao.repository.crawler.es.search" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.search",
		"com.microservice.dao.repository.crawler.es.search" })
public class SearchFutureService {

	@Autowired
	private HtmlParseService htmlParseService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private SanWangGetHtmlService sanWangGetHtmlService;

	@Autowired
	private SanWangUnitService sanWangUnitService;

	public static final Logger log = LoggerFactory.getLogger(SearchFutureService.class);

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 百度爬虫 创建人：hyx
	 * 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 Future<WebParm>
	 */
	public WebParm baiduCrawler(SearchTask searchTask) {
		tracerLog.System("baiduCrawler start", searchTask.getLinkurl());
		List<NewsListJson> list = new ArrayList<>();
		WebParm webParm = new WebParm();
		webParm.setKeyword(searchTask.getKeyword());
		webParm.setLinkurl(searchTask.getLinkurl());
		try {
			long startTime = System.currentTimeMillis();

			String html = sanWangGetHtmlService.getDocByHtmlunitFalse(searchTask, searchTask.getLinkurl());
			long endTime = System.currentTimeMillis();
			tracerLog.System("baidu获取列表页 耗时111", (endTime - startTime) + "ms");
			tracerLog.System("baidu获取列表页 耗时222       当前共计程序耗时s：", (endTime - startTime) / 1000 + "s");
			long startTime_parse = System.currentTimeMillis();

			list.addAll(htmlParseService.baiduParse(html, searchTask));
			long endTime_parse = System.currentTimeMillis();
			tracerLog.System("baidu解析列表页 耗时333", (endTime_parse - startTime_parse) + "ms");
			tracerLog.System("baidu解析列表页 耗时444        当前共计程序耗时s：", (endTime_parse - startTime_parse) / 1000 + "s");
			List<NewsListJson> listbasic = new ArrayList<>();
			long startTime_sql = System.currentTimeMillis();

			List<Future<NewsContent>> future_content_list = new ArrayList<>(); 
			
			
			for (NewsListJson newsListJson : list) {
				Future<NewsContent> fnc = sanWangUnitService.getContent(newsListJson, searchTask); 
				future_content_list.add(fnc);
			}
			boolean isdone = true;
			tracerLog.System("baidu["+searchTask.getLinkurl()+"] 获取到了列表数量",""+future_content_list.size());
			while (isdone) {
				for (Future<NewsContent> future : future_content_list) {

					if (future.isDone()) { // 判断是否执行完毕
						future_content_list.remove(future);
						tracerLog.System("baidu抓取future_content_list", future_content_list.size() + "");

						try {
							List<NewsContent> listNewsContent = new ArrayList<>();
							if (!(future.get() == null || future.get().getContent() == null
									|| future.get().getContent().isEmpty())) {
								if(future.get().getContent()!=null  ||future.get().getUrl()!=null){
									listNewsContent.add(future.get());
								}
								tracerLog.System("baidu抓取url:size", listNewsContent.size() + "");
								NewsListJson newsListJson = future.get().getNewsListJson();
								newsListJson.setList(listNewsContent);
								listbasic.add(newsListJson);
							} else {
								tracerLog.System("baidu抓取 详情页返回值:", "返回值为空");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					}

				}
				if (future_content_list.size() <= 0) {
					isdone = false;
				}
			}
			long endTime_sql = System.currentTimeMillis();
			tracerLog.System("baidu详情页共计 耗时", (endTime_sql - startTime_sql) + "ms");
			tracerLog.System("baidu详情页共计 耗时        当前共计程序耗时s：", (endTime_sql - startTime_sql) / 1000 + "s");
			if (listbasic == null || listbasic.size() <= 0) {
				webParm.setCode("404");

			} else {
				if (listbasic.get(0).getList() == null || listbasic.get(0).getList().size() < 0) {
					webParm.setList(listbasic);
					webParm.setCode("404");
				} else {
					webParm.setList(listbasic);
					webParm.setCode("200");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParm.setCode("404");
			tracerLog.System("baidu错误信息：" + searchTask.getLinkurl(), "修改状态改为 爬取中 1");
			tracerLog.System("baidu错误信息： 原始链接linkurl" + searchTask.getLinkurl(), e.getMessage());
			tracerLog.System("baidu错误信息：  linkurl webParm" + searchTask.getLinkurl(), webParm.toString());
		}

		return webParm;
	}

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 好搜爬取 创建人：hyx
	 * 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 Future<WebParm>
	 */
	public WebParm haosouCrawler(SearchTask searchTask) {
		tracerLog.System("haosouCrawler start", searchTask.getLinkurl());
		List<NewsListJson> list = new ArrayList<>();

		WebParm webParm = new WebParm();
		webParm.setKeyword(searchTask.getKeyword());
		webParm.setLinkurl(searchTask.getLinkurl());
		try {

			long startTime = System.currentTimeMillis();

			String html = sanWangGetHtmlService.getDocByHtmlunitFalse(searchTask, searchTask.getLinkurl());
			long endTime = System.currentTimeMillis();
			tracerLog.System("haosou获取列表页 耗时111", (endTime - startTime) + "ms");
			tracerLog.System("haosou获取列表页 耗时222        当前共计程序耗时s：", (endTime - startTime) / 1000 + "s");

			// newsListJsonRepository.saveAll(list);
			long startTime_parse = System.currentTimeMillis();
			list.addAll(htmlParseService.haoSaoParser(html, searchTask));

			long endTime_parse = System.currentTimeMillis();
			tracerLog.System("haosou解析列表页 耗时333", (endTime_parse - startTime_parse) + "ms");
			tracerLog.System("haosou解析列表页 耗时444        当前共计程序耗时s：", (endTime_parse - startTime_parse) / 1000 + "s");
			List<NewsListJson> listbasic = new ArrayList<>();
			long startTime_sql = System.currentTimeMillis();

			List<Future<NewsContent>> future_content_list = new ArrayList<>();

			for (NewsListJson newsListJson : list) {
				tracerLog.System("haosou抓取url:", newsListJson.getLinkUrl());
//				String url = sanWangUnit.getSouGouurl(newsListJson.getLinkUrl(), searchTask);
//				if (url == null) {
//					newsListJson.setLinkUrl(url);
//					future_content_list.add(sanWangUnit.getContent(newsListJson, searchTask));
//				} else {
//					newsListJson.setLinkUrl(url);
					future_content_list.add(sanWangUnitService.getContent(newsListJson, searchTask));
//				}
			}
			boolean isdone = true;
			tracerLog.System("haosou["+searchTask.getLinkurl()+"] 获取到了列表数量",""+future_content_list.size());
			while (isdone) {
				for (Future<NewsContent> future : future_content_list) {

					if (future.isDone()) { // 判断是否执行完毕
						future_content_list.remove(future);
						tracerLog.System("haosou抓取future_content_list", future_content_list.size() + "");

						try {
							List<NewsContent> listNewsContent = new ArrayList<>();
							if (!(future.get() == null || future.get().getContent() == null
									|| future.get().getContent().isEmpty())) {
								if(future.get().getContent()!=null  ||future.get().getUrl()!=null){
									listNewsContent.add(future.get());
								}
								tracerLog.System("haosou抓取url:size", listNewsContent.size() + "");
								NewsListJson newsListJson = future.get().getNewsListJson();
								newsListJson.setList(listNewsContent);
								listbasic.add(newsListJson);
							} else {
								tracerLog.System("haosou抓取 详情页返回值:", "返回值为空");
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					}

				}
				if (future_content_list.size() <= 0) {
					isdone = false;
				}
			}
			long endTime_sql = System.currentTimeMillis();
			tracerLog.System("haosou详情页共计  耗时", (endTime_sql - startTime_sql) + "ms");
			tracerLog.System("haosou详情页共计  耗时        当前共计程序耗时s：", (endTime_sql - startTime_sql) / 1000 + "s");
			if (listbasic == null || listbasic.size() <= 0) {
				webParm.setCode("404");
			} else {
				if (listbasic.get(0).getList() == null || listbasic.get(0).getList().size() < 0) {
					webParm.setList(listbasic);
					webParm.setCode("404");
				} else {
					webParm.setList(listbasic);
					webParm.setCode("200");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParm.setCode("404");
			tracerLog.System("haosou错误信息：" + searchTask.getLinkurl(), "修改状态改为 爬取中 1");
			tracerLog.System("haosou错误信息： 原始链接linkurl" + searchTask.getLinkurl(), e.getMessage());
			tracerLog.System("haosou错误信息：   原始链接linkurl webParm" + searchTask.getLinkurl(), webParm.toString());
		}

		return webParm;
	}

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 搜狗爬取 创建人：hyx
	 * 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 Future<WebParm>
	 */
	public WebParm sougouCrawler(SearchTask searchTask) {
		tracerLog.System("sougouCrawler start", searchTask.getLinkurl());
		List<NewsListJson> list = new ArrayList<>();
		WebParm webParm = new WebParm();
		webParm.setKeyword(searchTask.getKeyword());
		webParm.setLinkurl(searchTask.getLinkurl());
		try {
			long startTime = System.currentTimeMillis();
			String html = sanWangGetHtmlService.getDocByHtmlunitFalse(searchTask, searchTask.getLinkurl());
			long endTime = System.currentTimeMillis();
			tracerLog.System("sogou获取列表页 耗时111"+searchTask.getLinkurl(), (endTime - startTime) + "ms");
			tracerLog.System("sogou获取列表页 耗时111        当前共计程序耗时s："+searchTask.getLinkurl(), (endTime - startTime) / 1000 + "s");

			long startTime_parse = System.currentTimeMillis();
			list.addAll(htmlParseService.souGouParser(html, searchTask));

			long endTime_parse = System.currentTimeMillis();
			tracerLog.System("sogou解析列表页 耗时222"+searchTask.getLinkurl(), (endTime_parse - startTime_parse) + "ms");
			tracerLog.System("sogou解析列表页 耗时222        当前共计程序耗时s："+searchTask.getLinkurl(), (endTime_parse - startTime_parse) / 1000 + "s");
			List<NewsListJson> listbasic = new ArrayList<>();
			long startTime_sql = System.currentTimeMillis();

			List<Future<NewsContent>> future_content_list = new ArrayList<>();

			for (NewsListJson newsListJson : list) {
				tracerLog.System("sogou抓取url:"+System.currentTimeMillis(), newsListJson.getLinkUrl());

					future_content_list.add(sanWangUnitService.getContent(newsListJson, searchTask));
			}
			boolean isdone = true;
			tracerLog.System("sougou["+searchTask.getLinkurl()+"] 获取到了列表数量",""+future_content_list.size());
			while (isdone) {
				for (Future<NewsContent> future : future_content_list) {

					if (future.isDone()) { // 判断是否执行完毕
						future_content_list.remove(future);
						tracerLog.System("sogou抓取future_content_list", future_content_list.size() + "");

						try {
							List<NewsContent> listNewsContent = new ArrayList<>();
							if (!(future.get() == null || future.get().getContent() == null
									|| future.get().getContent().isEmpty())) {
								
								if(future.get().getContent()!=null  ||future.get().getUrl()!=null){
									listNewsContent.add(future.get());
								}
								tracerLog.System("sogouurl:size", listNewsContent.size() + "");
								NewsListJson newsListJson = future.get().getNewsListJson();
								newsListJson.setList(listNewsContent);
								listbasic.add(newsListJson);
							} else {
								tracerLog.System("sogou详情页返回值:", "返回值为空");
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					}

				}
				if (future_content_list.size() <= 0) {
					isdone = false;
				}

			}
			long endTime_sql = System.currentTimeMillis();
			tracerLog.System("sogou详情页["+searchTask.getLinkurl()+"]共计  耗时", (endTime_sql - startTime_sql) + "ms");
			tracerLog.System("sogou详情页["+searchTask.getLinkurl()+"]共计  耗时        当前共计程序耗时s：", (endTime_sql - startTime_sql) / 1000 + "s");
			if (listbasic == null || listbasic.size() <= 0) {
				webParm.setCode("404");
			} else {
				if (listbasic.get(0).getList() == null || listbasic.get(0).getList().size() < 0) {
					webParm.setList(listbasic);
					webParm.setCode("404");
				} else {
					webParm.setList(listbasic);
					webParm.setCode("200");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParm.setCode("404");
			tracerLog.System("sougou错误信息： 原始链接url" + searchTask.getLinkurl(), e.getMessage());
			tracerLog.System("sougou错误信息： 原始链接 url webParm" + searchTask.getLinkurl(), webParm.toString());
		}

		return webParm;
	}
}
