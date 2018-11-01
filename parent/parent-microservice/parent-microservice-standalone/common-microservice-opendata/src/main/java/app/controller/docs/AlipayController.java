package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/alipay")
public class AlipayController {
	
	/**
	 * 支付宝-快速接入
	 * @return
	 */
    @RequestMapping("/alipay-quickstart")
    public String quickstart(){
    	System.out.println("- alipay quickstart -");
        return "docs/dev-guide/alipay/alipay-quickstart";
    }

    /**
	 * 支付宝-支付宝信息
	 * @return
	 */
    @RequestMapping("/alipay-alipayinfo")
    public String bills(){
    	System.out.println("- alipay alipayinfo -");
        return "docs/dev-guide/alipay/alipay-alipayinfo";
    }

    /**
	 * 支付宝-支付宝信息（v6最新）
	 * @return
	 */
    @RequestMapping("/alipay-alipayinfov6")
    public String alipayinfov6(){
    	System.out.println("- alipay alipayinfov6 -");
        return "docs/dev-guide/alipay/alipay-alipayinfov6";
    }
    /**
	 * 支付宝-支付宝报告
	 * @return
	 */
    @RequestMapping("/alipay-reports")
    public String reports(){
    	System.out.println("- alipay reports -");
        return "docs/dev-guide/alipay/alipay-reports";
    }

    /**
	 * 支付宝-查询报告状态接口
	 * @return
	 */
    @RequestMapping("/alipay-stateinterface")
    public String reportinterface(){
    	System.out.println("- alipay stateinterface -");
        return "docs/dev-guide/alipay/alipay-stateinterface";
    }

 
    /**
   	 * 支付宝- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/alipay-asynccallback")
    public String asynccallback(){
    	System.out.println("- alipay asynccallback -");
        return "docs/dev-guide/alipay/alipay-asynccallback";
    }
    /**
	 * 支付宝- 异常处理文档
	 * @return
	 */
    @RequestMapping("/alipay-exceptdoc")
    public String exceptdoc(){
    	System.out.println("- alipay exceptdoc -");
        return "docs/dev-guide/alipay/alipay-exceptdoc";
    }

    /**
	 * 支付宝-支付宝文档资源下载
	 * @return
	 */
    @RequestMapping("/alipay-download")
    public String download(){
    	System.out.println("- alipay download -");
        return "docs/dev-guide/alipay/alipay-download";
    }
    /**
	 * 支付宝-支付宝常见问题FAQ
	 * @return
	 */
    @RequestMapping("/alipay-questionfaq")
    public String questionfaq(){
    	System.out.println("- alipay questionfaq -");
        return "docs/dev-guide/alipay/alipay-questionfaq";
    }
}
