package app.controller.developer;


import app.entity.developer.AppProductList;
import app.service.AppProductListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/developer/app")
public class AppProductListRestController {

    @Autowired
    private AppProductListService appProductListService;

    @RequestMapping(value = "/findAppProductList", method = RequestMethod.GET)
    public AppProductList findAppProductList(String prodClientId, String flag, String appmode) {
        AppProductList appProductList = appProductListService.findAppProductList(prodClientId, flag, appmode);
        System.out.println(""+appProductList);
        return appProductList;
    }
}
