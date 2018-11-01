package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/qq")
public class QqController {
	
	/**
	 * qq-快速接入
	 * @return
	 */
    @RequestMapping("/qq-quickstart")
    public String quickstart(){
    	System.out.println("- qq quickstart -");
        return "docs/dev-guide/qq/qq-quickstart";
    }

    /**
	 * qq-qq信息
	 * @return
	 */
    @RequestMapping("/qq-qqinfo")
    public String qqinfo(){
    	System.out.println("- qq qqinfo -");
        return "docs/dev-guide/qq/qq-qqinfo";
    }

    /**
   	 * qq- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/qq-asynccallback")
    public String asynccallback(){
    	System.out.println("- qqasynccallback -");
        return "docs/dev-guide/qq/qq-asynccallback";
    }
    
    /**
	 * qq-异常处理文档
	 * @return
	 */
    @RequestMapping("/qq-exceptdoc")
    public String questionfaq(){
    	System.out.println("- qq exceptdoc -");
        return "docs/dev-guide/qq/qq-exceptdoc";
    }
}
