package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/jd")
public class JdController {
	
	/**
	 * 京东-快速接入
	 * @return
	 */
    @RequestMapping("/jd-quickstart")
    public String quickstart(){
    	System.out.println("- jd quickstart -");
        return "docs/dev-guide/jd/jd-quickstart";
    }

    /**
	 * 京东-京东信息
	 * @return
	 */
    @RequestMapping("/jd-jdinfo")
    public String jdinfo(){
    	System.out.println("- jd jdinfo -");
        return "docs/dev-guide/jd/jd-jdinfo";
    }

    /**
	 * 京东-京东信息（v6最新）
	 * @return
	 */
    @RequestMapping("/jd-jdinfov6")
    public String jdinfov6(){
    	System.out.println("- jd jdinfov6 -");
        return "docs/dev-guide/jd/jd-jdinfov6";
    }
    /**
	 * 京东-京东报告
	 * @return
	 */
    @RequestMapping("/jd-reports")
    public String reports(){
    	System.out.println("- jd reports -");
        return "docs/dev-guide/jd/jd-reports";
    }
   
    /**
	 * 京东-查询报告状态接口
	 * @return
	 */
    @RequestMapping("/jd-stateinterface")
    public String reportinterface(){
    	System.out.println("- jd stateinterface -");
        return "docs/dev-guide/jd/jd-stateinterface";
    }

    /**
   	 * 京东- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/jd-asynccallback")
    public String asynccallback(){
    	System.out.println("- jd asynccallback -");
        return "docs/dev-guide/jd/jd-asynccallback";
    }
  
    /**
	 * 京东-京东文档资源下载
	 * @return
	 */
    @RequestMapping("/jd-download")
    public String download(){
    	System.out.println("- jd download -");
        return "docs/dev-guide/jd/jd-download";
    }
    /**
	 * 京东-京东常见问题FAQ
	 * @return
	 */
    @RequestMapping("/jd-questionfaq")
    public String questionfaq(){
    	System.out.println("- jd questionfaq -");
        return "docs/dev-guide/jd/jd-questionfaq";
    }
}
