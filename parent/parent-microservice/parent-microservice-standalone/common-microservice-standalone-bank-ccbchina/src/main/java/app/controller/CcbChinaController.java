package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.service.CcbChinaService;

@RestController
@Configuration
@RequestMapping("/bank/ccbchina/debitcard")
public class CcbChinaController {
	
	public static final Logger log = LoggerFactory.getLogger(CcbChinaController.class);

//	@Autowired
//	private CcbChinaService ccbChinaService;

//	@PostMapping(path = "/login")
//	public void login(String loginName,String password) {
//		log.info("接口调用爬取开始");
//		try {
//			ccbChinaService.loginByAccountNum(loginName,password);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
