package app.utils;

import java.security.MessageDigest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboHtml;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboHtmlRepository;

import app.crawler.domain.WebParam;
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zibo"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zibo"})
public class CommonUtils {

	@Autowired
	public InsuranceZiboHtmlRepository insuranceZiboHtmlRepository;
	
	//将字符串md5加密，返回加密后的字符串
	public String md5(String s) {
		 
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
	
	//通过taskid获取登陆成功后的url和html源码，从而获得需要的参数
	public WebParam getPostParameters(String taskid) {
		
		InsuranceZiboHtml insuranceZiboHtml = insuranceZiboHtmlRepository.findByTaskid(taskid).get(0);
		String url = insuranceZiboHtml.getUrl();
		String usersession = url.substring(url.indexOf("=")+1, url.indexOf("&",url.indexOf("=")+1));
		String html = insuranceZiboHtml.getHtml();
		Element element = Jsoup.parse(html).select(".dw-laneContainer-mainLane").first();
		String laneid = element.attr("id");
		
		WebParam webParam = new WebParam();
		webParam.setUsersessionuuid(usersession);
		webParam.setLaneid(laneid);
		
		return webParam;
	}
}
