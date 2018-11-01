package app.controller.contact;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**  
 * 联系人管理
 * @return
 */
@Controller
@RequestMapping(value="/contact")
public class ContactController {
	
    @RequestMapping("/contact-list")
    public String businessContact(Model model){
        return "contact/contact_list";
    }
    
    @RequestMapping("/contact-list-fee")
    public String feeContact(Model model){
        return "contact/contact_list_fee";
    }
    
    
    
}
