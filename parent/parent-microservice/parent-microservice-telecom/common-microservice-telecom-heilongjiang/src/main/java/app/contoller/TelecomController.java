package app.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.TelecomControllerService;

@RestController
@RequestMapping("/carrier")
public class TelecomController {

	public static final Logger log = LoggerFactory.getLogger(TelecomController.class);

	@Autowired
	private TelecomControllerService telecomControllerService;

	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public TaskMobile telecomgetcode(@RequestBody MessageLogin messageLogin) {
		
		return telecomControllerService.sendSms(messageLogin);
	}

	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public TaskMobile telecomsetcode(@RequestBody MessageLogin messageLogin) {
		return telecomControllerService.verifySms(messageLogin);
	}

}
