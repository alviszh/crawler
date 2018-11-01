package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/housingfund")
public class HousingfundController {
	
	/**
	 * 公积金-快速接入
	 * @return
	 */
    @RequestMapping("/housingfund-quickstart")
    public String quickstart(){
    	System.out.println("- housingfund quickstart -");
        return "docs/dev-guide/housingfund/housingfund-quickstart";
    }

    /**
	 * 公积金-公积金信息信息
	 * @return
	 */
    @RequestMapping("/housingfund-housingfundinfo")
    public String housingfundinfo(){
    	System.out.println("- housingfund housingfundinfo -");
        return "docs/dev-guide/housingfund/housingfund-housingfundinfo";
    }


    /**
	 * 京东-京东报告api文档
	 * @return
	 */
    @RequestMapping("/housingfund-reportsapi")
    public String reports(){
    	System.out.println("- housingfund reportsapi -");
        return "docs/dev-guide/housingfund/housingfund-reportsapi";
    }
   
    /**
	 * 京东-查询报告状态接口
	 * @return
	 */
    @RequestMapping("/housingfund-stateinterface")
    public String reportinterface(){
    	System.out.println("- housingfund stateinterface -");
        return "docs/dev-guide/housingfund/housingfund-stateinterface";
    }

    /**
   	 * 京东- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/housingfund-asynccallback")
    public String asynccallback(){
    	System.out.println("- housingfund asynccallback -");
        return "docs/dev-guide/housingfund/housingfund-asynccallback";
    }
    
    /**
   	 * 京东- 异常处理文档
   	 * @return
   	 */
    @RequestMapping("/housingfund-exceptdoc")
    public String exceptdoc(){
    	System.out.println("- housingfund exceptdoc -");
        return "docs/dev-guide/housingfund/housingfund-exceptdoc";
    }
    
    /**
	 * 京东-京东文档资源下载
	 * @return
	 */
    @RequestMapping("/housingfund-download")
    public String download(){
    	System.out.println("- housingfund download -");
        return "docs/dev-guide/housingfund/housingfund-download";
    }
    /**
	 * 京东-京东常见问题FAQ
	 * @return
	 */
    @RequestMapping("/housingfund-questionfaq")
    public String questionfaq(){
    	System.out.println("- housingfund questionfaq -");
        return "docs/dev-guide/housingfund/housingfund-questionfaq";
    }
}
