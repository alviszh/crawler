package app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.bean.PageBean;
import app.service.EmotionService;


@RestController
@Configuration
@RequestMapping("/nlp")
public class EmotionController {
	
	@Autowired
	private EmotionService emotionService;
	//文章标签
    @PostMapping(path = "/articleTag")
    public List<PageBean> articleTag(@RequestBody PageBean page) {
    	List<PageBean> articleTag = emotionService.articleTag(page);
        return articleTag;
    }
    //情感倾向分析
    @PostMapping(path = "/emotion")
    public PageBean emotion(@RequestBody PageBean page) {
    	PageBean emotion = emotionService.emotion(page);
    	return emotion;
    }
    //自动摘要
    @PostMapping(path = "/summary")
    public PageBean summary(@RequestBody PageBean page) {
    	PageBean wordSimilarity = emotionService.summary(page);
    	return wordSimilarity;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //文章分类
    @PostMapping(path = "/articleType")
    public PageBean articleType(@RequestBody PageBean page) {
    	PageBean articleType = emotionService.articleType(page);
    	return articleType;
    }
    
    //评论观点抽取
    @PostMapping(path = "/commentPointExtract")
    public PageBean commentPointExtract(@RequestBody PageBean page) {
    	PageBean commentPointExtract = emotionService.commentPointExtract(page);
    	return commentPointExtract;
    }
    
    //短文本相似度
    @PostMapping(path = "/textSimilarity")
    public PageBean textSimilarity(@RequestBody PageBean page) {
    	PageBean textSimilarity = emotionService.textSimilarity(page);
    	return textSimilarity;
    }
    
    //词义相似度
    @PostMapping(path = "/wordSimilarity")
    public PageBean wordSimilarity(@RequestBody PageBean page) {
    	PageBean wordSimilarity = emotionService.wordSimilarity(page);
    	return wordSimilarity;
    }
    
}
