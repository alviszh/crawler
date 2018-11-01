package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crawler.search.bean.SearchTaskBean;


/**
 * 
 * 项目名称：common-microservice-client 类名称：SearchClient 类描述： 创建人：hyx 创建时间：2018年1月25日
 * 下午5:12:23
 * 
 * @version
 */
//@FeignClient(name = "search-task")
//@FeignClient(name = "search-task", url = "http://10.167.202.187:51318")
//@FeignClient(name = "search-task", url = "http://10.167.211.152:10010")
@FeignClient(name = "search-task", url = "${jobs.url}")

public interface SearchTaskClient {

	@RequestMapping(method = RequestMethod.POST, value = "/sanwang/search/gettask")
	public List<SearchTaskBean> getTask();

//	@PostMapping(value = "/sanwang/search/gettask")
//	public List<SearchTaskBean> getTask();

}
