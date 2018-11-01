package app.client.monitor;

import com.crawler.pbccrc.json.PbccrcJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crawler.monitor.json.MailBean;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @description:邮件发送
 * @author: sln 
 */
@FeignClient("MAIL")
//@FeignClient(name = "mail", url = "http://10.167.202.231:8323")
public interface MailClient {
	@PostMapping(path = "/mail/sendmail")
	public void sendMail(@RequestBody MailBean mailBean);


	@PostMapping(path = "/mail/pbccrcmail")
	public void pbccrcmail(@RequestBody PbccrcJsonBean pbccrcJsonBean,
						   @RequestParam("exception") String exception);
}
