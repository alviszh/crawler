package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.monitor.json.MailBean;
import com.crawler.pbccrc.json.PbccrcJsonBean;

import app.mailservice.MailContentBuilder;
import app.service.MailService;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月20日 上午10:29:35 
 */
@RestController
@Configuration
@RequestMapping("/mail") 
public class MailController {
	public static final Logger log = LoggerFactory.getLogger(MailController.class);
	@Autowired
	private MailService mailService;
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Value("${huichengPbccrcReceivers}") 
	public String huichengPbccrcReceivers;
	@Value("${mailsender}") 
	public String mailsender;
	
	@PostMapping("/sendmail")
	public void sendMail(@RequestBody MailBean mailBean) { 
		//判断用户是否在基本表中已经存在
		mailService.sendMail(mailBean);
	}
	
	@RequestMapping(path = "/pbccrcmail",method = {RequestMethod.POST})
//	public void sendmail(@RequestBody TaskStandalone taskStandalone,@RequestParam(name = "exception") String exception){
	public void sendmail(@RequestBody PbccrcJsonBean pbccrcJsonBean,@RequestParam(name = "exception") String exception){
		MailBean mailBean=new MailBean();
		//获取构建之后的模板文件
		String content = mailContentBuilder.buildPbccrcException(pbccrcJsonBean,exception);
		String owner = pbccrcJsonBean.getOwner();
		if(owner.equals("huicheng")){  //汇城 
			mailBean.setReceiver(huichengPbccrcReceivers);
		}else if(owner.equals("yanjiuyuan")){   //研究院（薪动钱包）
			
		}else if(owner.equals("jinxinwang")){   //金信网（贷乎） 
			
		}else if(owner.equals("dajinrong")){  //大金融（借么）
			
		}
		String subject="人行征信执行回调接口异常详情通知邮件";
		mailBean.setMailcontent(content);
		mailBean.setSender(mailsender);
		mailBean.setSubject(subject);
		mailService.sendMail(mailBean);
	}
	@GetMapping("/testmail")
	public void testMail(){
		mailService.testSendMail();
	}
}
