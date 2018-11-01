package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/insurance")
public class InsuranceController {
	
	/**
	 * 社保-快速接入
	 * @return
	 */
    @RequestMapping("/insurance-quickstart")
    public String quickstart(){
    	System.out.println("- insurance quickstart -");
        return "docs/dev-guide/insurance/insurance-quickstart";
    }

    /**
	 * 社保-社保信息信息
	 * @return
	 */
    @RequestMapping("/insurance-insuranceinfo")
    public String insuranceinfo(){
    	System.out.println("- insurance insuranceinfo -");
        return "docs/dev-guide/insurance/insurance-insuranceinfo";
    }


    /**
	 * 京东-京东报告api文档
	 * @return
	 */
    @RequestMapping("/insurance-reportsapi")
    public String reports(){
    	System.out.println("- insurance reportsapi -");
        return "docs/dev-guide/insurance/insurance-reportsapi";
    }
   
    /**
	 * 京东-查询报告状态接口
	 * @return
	 */
    @RequestMapping("/insurance-stateinterface")
    public String reportinterface(){
    	System.out.println("- insurance stateinterface -");
        return "docs/dev-guide/insurance/insurance-stateinterface";
    }

    /**
   	 * 京东- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/insurance-asynccallback")
    public String asynccallback(){
    	System.out.println("- insurance asynccallback -");
        return "docs/dev-guide/insurance/insurance-asynccallback";
    }
    
    /**
   	 * 京东- 异常处理文档
   	 * @return
   	 */
    @RequestMapping("/insurance-exceptdoc")
    public String exceptdoc(){
    	System.out.println("- insurance exceptdoc -");
        return "docs/dev-guide/insurance/insurance-exceptdoc";
    }
    
    /**
	 * 京东-京东文档资源下载
	 * @return
	 */
    @RequestMapping("/insurance-download")
    public String download(){
    	System.out.println("- insurance download -");
        return "docs/dev-guide/insurance/insurance-download";
    }
    /**
	 * 京东-京东常见问题FAQ
	 * @return
	 */
    @RequestMapping("/insurance-questionfaq")
    public String questionfaq(){
    	System.out.println("- insurance questionfaq -");
        return "docs/dev-guide/insurance/insurance-questionfaq";
    }
}
