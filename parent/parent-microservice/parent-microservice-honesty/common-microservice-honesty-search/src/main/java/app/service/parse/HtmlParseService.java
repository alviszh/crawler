package app.service.parse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.crawler.htmlparser.AbstractParser;
import com.microservice.dao.entity.crawler.search.NewsListJson;
import com.microservice.dao.entity.crawler.search.SearchTask;

import app.service.SearchUnitService;
import app.service.unit.SanWangUnitService;


/**
 * 
 * 项目名称：common-microservice-search 类名称：HtmlParse 类描述： 创建人：hyx 创建时间：2018年1月17日
 * 下午6:23:55
 * 
 * @version
 */
@Component
public class HtmlParseService extends AbstractParser {


	
	private List<NewsListJson> searchNewsList ;
	
	private Set<String> sensitiveKeyList;
	
	@Autowired
	private SanWangUnitService sanWangUnitService;
	
	@Autowired
	private SearchUnitService searchUnitService;
	
	
	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.parse 类描述： 百度解析 创建人：hyx
	 * 创建时间：2018年1月18日
	 * 
	 * @version 1 返回值 List<News>
	 */
	public List<NewsListJson> baiduParse(String html, SearchTask searchTask) {
		if (StringUtils.isEmpty(html)) {
			return searchNewsList;
		}
		// Elements

		Elements ele = Jsoup.parse(html).select("div#content_left");

		if (ele == null) {
			return searchNewsList;
		}

		Elements ele_results = ele.select("div.c-container");
		if (ele_results == null || ele_results.isEmpty()) {
			return searchNewsList;
		}
		searchNewsList = new ArrayList<NewsListJson>();

		for (Element ele_result : ele_results) {

			NewsListJson newsListJson = new NewsListJson();
			sensitiveKeyList = new  HashSet<String>();

			try {
				String title = ele_result.select("h3.t>a").text();
				newsListJson.setTitle(title);
				
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(title));
//				String  sensitivekey = searchUnitService.matchSensitive(title);
//				
//				newsListJson.setSensitivekey(sensitiveKeyList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			} catch (Exception e) {

			}

			try {
				String linkUrl =sanWangUnitService.toUtf8String( ele_result.select("h3.t>a").attr("abs:href"));
				newsListJson.setLinkUrl(new String(linkUrl.getBytes("gbk"), "UTF8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				String abstractTxt = ele_result.select("div.c-abstract").text();
				newsListJson.setAbstractTxt(abstractTxt);
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(abstractTxt));

			} catch (Exception e) {

			}

			newsListJson.setType("baidu");
			
			if (searchTask.getKeyword() != null) {

				newsListJson.setKeyword(searchTask.getKeyword());
			}
			if (searchTask.getTaskid() != null) {

				newsListJson.setTaskid(searchTask.getTaskid());
			}
			
			if(newsListJson.getLinkUrl()==null|| newsListJson.getLinkUrl().isEmpty()){
				continue;
			}
			newsListJson.setSensitivekey(sensitiveKeyList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
			
			searchNewsList.add(newsListJson);
		}

		return searchNewsList;
	}

	public List<NewsListJson> haoSaoParser(String html, SearchTask searchTask) {
		if (StringUtils.isEmpty(html)) {
			return searchNewsList;
		}

		Elements ele = Jsoup.parse(html).select("ul.result");

		if (ele == null) {
			return searchNewsList;
		}

		Elements ele_results = ele.select("li.res-list");
		if (ele_results == null || ele_results.isEmpty()) {
			return searchNewsList;
		}
		searchNewsList = new ArrayList<NewsListJson>();

		for (Element ele_result : ele_results) {

			NewsListJson newsListJson = new NewsListJson();
			sensitiveKeyList = new  HashSet<String>();

			try {
				String title = ele_result.select("h3.res-title>a").text();
				newsListJson.setTitle(title);
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(title));

			} catch (Exception e) {

			}

			try {

				String linkurl = sanWangUnitService.toUtf8String(ele_result.select("h3.res-title>a").attr("abs:href"));
				newsListJson.setLinkUrl(new String(linkurl.getBytes("gbk"), "UTF8"));
			} catch (Exception e) {
			}

//			try {
//				String title = ele_result.select("h3.res-title>a").text();
//				newsListJson.setTitle(title);
//			} catch (Exception e) {
//
//			}

			try {
				String abstractTxt = ele_result.select("p.res-desc").text();

				newsListJson.setAbstractTxt(abstractTxt.toString());
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(abstractTxt));


			} catch (Exception e) {

			}

			if (searchTask.getKeyword() != null) {

				newsListJson.setKeyword(searchTask.getKeyword());
			}
			if (searchTask.getTaskid() != null) {

				newsListJson.setTaskid(searchTask.getTaskid());
			}

			newsListJson.setType("haosou");
			
			if(newsListJson.getLinkUrl()==null|| newsListJson.getLinkUrl().isEmpty()){
				continue;
			}
			newsListJson.setSensitivekey(sensitiveKeyList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			searchNewsList.add(newsListJson);
		}

		return searchNewsList;
	}

	public List<NewsListJson> souGouParser(String html, SearchTask searchTask) {
		if (StringUtils.isEmpty(html)) {
			return searchNewsList;
		}

		Elements ele = Jsoup.parse(html).select("div.results");

		if (ele == null) {
			return searchNewsList;
		}

		Elements ele_results = ele.select("div.vrwrap");
		if (ele_results == null || ele_results.isEmpty()) {
			return searchNewsList;
		}
		searchNewsList = new ArrayList<NewsListJson>();

		for (Element ele_result : ele_results) {

			NewsListJson newsListJson = new NewsListJson();
			sensitiveKeyList = new  HashSet<String>();

			
			try {
				String title = ele_result.select("h3.vrTitle").text();

				if (title == null || title.isEmpty()) {
					continue;
				}
				newsListJson.setTitle(title);
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(title));


			} catch (Exception e) {

			}
		
			try {
				String abstractTxt = ele_result.select("p.str-text-info").text();

				newsListJson.setAbstractTxt(abstractTxt.toString());
				
				sensitiveKeyList.addAll(searchUnitService.matchSensitive(abstractTxt));

			} catch (Exception e) {

			}
			try {

			} catch (Exception e) {

			}
			try {
				String linkurl = sanWangUnitService.toUtf8String(ele_result.select("h3.vrTitle>a").attr("abs:href"));
				newsListJson.setLinkUrl(new String(linkurl.getBytes("gbk"), "UTF8"));
			} catch (Exception e) {

			}

			if (searchTask.getKeyword() != null) {

				newsListJson.setKeyword(searchTask.getKeyword());
			}
			if (searchTask.getTaskid() != null) {

				newsListJson.setTaskid(searchTask.getTaskid());
			}

			newsListJson.setType("sougou");
			
			if(newsListJson.getLinkUrl()==null|| newsListJson.getLinkUrl().isEmpty()){
				continue;
			}
			newsListJson.setSensitivekey(sensitiveKeyList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			searchNewsList.add(newsListJson);
		}

		return searchNewsList;
	}

	
	public String getElementText(Element e) {
		String text = null;
		if (e != null) {
			text = e.text();
		}
		return text;
	}

	public String getElementHtml(Element e) {
		String text = null;
		if (e != null) {
			text = e.outerHtml();
		}
		return text;
	}

	public String getElementAttr(Element e, String attr) {
		String text = null;
		if (e != null) {
			text = e.hasAttr(attr) ? e.attr(attr) : null;
		}
		return text;
	}

	public String getHtml(Element e) {
		String html = null;
		if (e != null) {
			html = e.outerHtml();
		}
		return html;
	}

	public List<String> getSubStringByRegex(String str, String regex) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
}
