package app.controller.doc;

import app.enums.CbConfModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 自然语言接口说明
 */
@Controller
@RequestMapping("/doc/naturallanguage")
public class NaturalLanguageController {

    public static final Logger log = LoggerFactory.getLogger(ETLController.class);

    @RequestMapping("/articleTag")
    public String articleTag(Model model){
        model.addAttribute("module", CbConfModule.NATURALLANGUAGE.getCode());
        model.addAttribute("isApi", true);
        return "doc/naturallanguage/articleTag";
    }
    
    @RequestMapping("/emotion")
    public String emotion(Model model){
    	model.addAttribute("module", CbConfModule.NATURALLANGUAGE.getCode());
    	model.addAttribute("isApi", true);
    	return "doc/naturallanguage/emotion";
    }
    
    @RequestMapping("/summary")
    public String summary(Model model){
    	model.addAttribute("module", CbConfModule.NATURALLANGUAGE.getCode());
    	model.addAttribute("isApi", true);
    	return "doc/naturallanguage/summary";
    }
}
