package app.controller;

import java.text.ParseException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.bean.NewsBean;
import app.bean.SourceBean;
import app.util.NewsHandle;


/**
 * 新闻处理器-处理新闻来源，时间等字段。
 * @author zz
 *
 */
@RestController
@Configuration
@RequestMapping("/nlp")
public class NewsHandleController {
	
	/**
	 * 时间处理
	 * @param createTime
	 * @param publishTime
	 * @return
	 */
	@GetMapping(value = "/newshandle/time")
	public String dealWithTime(@RequestParam("createTime") String createTime, 
			@RequestParam("publishTime") String publishTime){		
		NewsHandle newsHandle = new NewsHandle();
		try {
			return newsHandle.dealWithTime(createTime,publishTime);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	
	/**
	 * 来源处理  (要用post。因为来源字段里有很多不确定的未知字符)
	 * @param source
	 * @return
	 */
	@PostMapping(value = "/newshandle/source")
	public NewsBean dealWithSource(@RequestBody SourceBean sourceBean){		
		NewsHandle newsHandle = new NewsHandle();
		return newsHandle.dealWithSource(sourceBean.getSource());		
	}
	

}
