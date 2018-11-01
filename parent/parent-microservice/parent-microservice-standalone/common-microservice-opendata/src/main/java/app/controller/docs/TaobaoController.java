package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/taobao")
public class TaobaoController {
	
	/**
	 * 淘宝-快速接入
	 * @return
	 */
    @RequestMapping("/taobao-quickstart")
    public String quickstart(){
    	System.out.println("- taobao quickstart -");
        return "docs/dev-guide/taobao/taobao-quickstart";
    }

    /**
	 * 淘宝-淘宝信息
	 * @return
	 */
    @RequestMapping("/taobao-taobaoinfo")
    public String taobaoinfo(){
    	System.out.println("- taobao taobaoinfo -");
        return "docs/dev-guide/taobao/taobao-taobaoinfo";
    }

    /**
	 * 淘宝-淘宝信息（v6最新）
	 * @return
	 */
    @RequestMapping("/taobao-taobaoinfov6")
    public String taobaoinfov6(){
    	System.out.println("- taobao taobaoinfov6 -");
        return "docs/dev-guide/taobao/taobao-taobaoinfov6";
    }
    /**
	 * 淘宝-淘宝报告
	 * @return
	 */
    @RequestMapping("/taobao-reports")
    public String reports(){
    	System.out.println("- taobao reports -");
        return "docs/dev-guide/taobao/taobao-reports";
    }
    /**
  	 * 淘宝- 异常处理文档
  	 * @return
  	 */
      @RequestMapping("/taobao-reportsv4")
      public String reportsV4(){
      	System.out.println("- taobao reportsv4 -");
          return "docs/dev-guide/taobao/taobao-reportsv4";
      }

    /**
	 * 淘宝-查询报告状态接口
	 * @return
	 */
    @RequestMapping("/taobao-stateinterface")
    public String reportinterface(){
    	System.out.println("- taobao stateinterface -");
        return "docs/dev-guide/taobao/taobao-stateinterface";
    }

    /**
   	 * 淘宝- 服务器异步回调说明
   	 * @return
   	 */
    @RequestMapping("/taobao-asynccallback")
    public String asynccallback(){
    	System.out.println("- taobao asynccallback -");
        return "docs/dev-guide/taobao/taobao-asynccallback";
    }
  
    /**
	 * 淘宝-淘宝文档资源下载
	 * @return
	 */
    @RequestMapping("/taobao-download")
    public String download(){
    	System.out.println("- taobao download -");
        return "docs/dev-guide/taobao/taobao-download";
    }
    /**
	 * 淘宝-淘宝常见问题FAQ
	 * @return
	 */
    @RequestMapping("/taobao-questionfaq")
    public String questionfaq(){
    	System.out.println("- taobao questionfaq -");
        return "docs/dev-guide/taobao/taobao-questionfaq";
    }
}
