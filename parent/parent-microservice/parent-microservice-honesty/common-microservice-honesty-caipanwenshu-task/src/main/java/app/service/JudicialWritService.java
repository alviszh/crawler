package app.service;


import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritKeyWord;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritKeyWordRepository;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritTaskRepository;

import app.bean.HonestyJsonBean;
import app.bean.Honestybean;
import app.bean.IsDoneBean;
import app.bean.WebParam;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritService {

	@Autowired
	private JudicialWritParameterService judicialWritParameterService;
	@Autowired
	private JudicialWritGetAllService judicialWritGetAllService;
	@Autowired
	private JudicialWritKeyWordRepository judicialWritKeyWordRepository;
	public IsDoneBean crawler(HonestyJsonBean honestyJsonBean, HttpProxyBean httpProxyBean) throws Exception{
		//关键字
		List<Honestybean> keys = honestyJsonBean.getKeys();
		//参数
		WebParam webParam = judicialWritParameterService.getParameter(httpProxyBean,honestyJsonBean,1);
		if(webParam!=null){
			for (Honestybean honestybean : keys) {
				String getpName = honestybean.getpName();
				String keyword = "";
				if(getpName==null){
					keyword = "";
				}
				keyword = "全文检索:"+getpName;
				keyword=URLEncoder.encode(keyword, "utf-8");
				judicialWritGetAllService.getAlldata(webParam,httpProxyBean, honestyJsonBean, keyword, 1);
			}
		}
		return null;

	}
}
