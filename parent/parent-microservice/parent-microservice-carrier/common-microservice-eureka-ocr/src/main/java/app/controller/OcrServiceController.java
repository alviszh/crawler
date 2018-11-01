package app.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import app.service.OcrService;

@RestController
public class OcrServiceController {
	
	public static final Logger log = LoggerFactory.getLogger(OcrServiceController.class);
	
	@Autowired
	private OcrService ocrService;
	
	@RequestMapping(value="/getImageView/{md5}", method = RequestMethod.GET)
	public String getImageView(@PathVariable("md5") String md5){
		
		log.info("---------------------OcrServiceController.getImageView-------------------");
		
		if (StringUtils.isEmpty(md5)) {
			return "error:md5 is null!";
		}
		
		return ocrService.getImageView(md5);
		
	}

}
