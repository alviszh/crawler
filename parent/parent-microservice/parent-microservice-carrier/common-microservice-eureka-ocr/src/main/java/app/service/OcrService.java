package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import com.microservice.dao.entity.crawler.ocr.OcrVerifycode;
import com.microservice.dao.repository.crawler.ocr.OcrVerifycodeRepository;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
@Component
@EntityScan(basePackages="com.microservice.dao.entity.crawler.ocr")
@EnableJpaRepositories(
		basePackages="com.microservice.dao.repository.crawler.ocr")
public class OcrService extends AbstractChaoJiYingHandler{
	
	public static final Logger log = LoggerFactory.getLogger(OcrService.class);
	private static BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
	@Autowired
	private OcrVerifycodeRepository ocrVerifycodeRepository;

	/**
	 * @Description: 展示验证码
	 * @param  md5
	 * @return String 
	 * @throws
	 */
	public String getImageView(String md5) {
		
		OcrVerifycode ocrVerifycode = ocrVerifycodeRepository.findByImgageName(md5);
		byte[] bytes = ocrVerifycode.getImgage();
		String base64Code =  encoder.encodeBuffer(bytes).trim();    	
		return "<html>  <head>  <title>验证码展示</title>  </head>  <body>  <img src=\"data:image/png;base64,"+base64Code+"\""+"/>  </body>  </html>";
	}


}
