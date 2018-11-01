package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/bank")
public class BankController {
	
	/**
	 * 网银-快速接入
	 * @return
	  */
    @RequestMapping("/bank-quickstart")
    public String quickstart(){
    	System.out.println("- bank quickstart -");
        return "docs/bank/bank-quickstart";
    }

    /**
	 * 网银-网银账单
	 * @return
	 */ 
    @RequestMapping("/bank-bills")
    public String bills(){
    	System.out.println("- bank bills -");
        return "docs/bank/bank-bills";
    }

    /**
	 * 网银-网银报告
	 * @return
	  */
    @RequestMapping("/bank-reports")
    public String reports(){
    	System.out.println("- bank reports -");
        return "docs/bank/bank-reports";
    }

    /**
	 * 网银- 报告状态接口
	 * @return
	 */
    @RequestMapping("/bank-stateinterface")
    public String reportinterface(){
    	System.out.println("- bank stateinterface -");
        return "docs/bank/bank-stateinterface";
    }

    /**
	 * 网银-服务异步回调说明
	 * @return
	 */
    @RequestMapping("/bank-asynccallback")
    public String asynccallback(){
    	System.out.println("- bank asynccallback -");
        return "docs/bank/bank-asynccallback";
    }

    /**
	 * 网银- 异常处理文档
	 * @return
	 */
    @RequestMapping("/bank-exceptdoc")
    public String exceptdoc(){
    	System.out.println("- bank exceptdoc -");
        return "docs/bank/bank-exceptdoc";
    }

    /**
	 * 网银-网银文档资源下载
	 * @return
	 */
    @RequestMapping("/bank-download")
    public String download(){
    	System.out.println("- bank download -");
        return "docs/bank/bank-download";
    }
    /**
	 * 网银-网银常见问题FAQ
	 * @return
	 */
    @RequestMapping("/bank-questionfaq")
    public String questionfaq(){
    	System.out.println("- bank questionfaq -");
        return "docs/bank/bank-questionfaq";
    }
}