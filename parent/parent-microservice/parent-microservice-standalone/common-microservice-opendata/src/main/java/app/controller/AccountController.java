package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 账户中心
 * @return
 */
@Controller
@RequestMapping(value="/account")
public class AccountController {

	/**
	 * 账号
	 * @return
	 */
    @RequestMapping("/accountNum")
    public String feeContact(){
        return "account/accountNum";
    }
    
    
}
