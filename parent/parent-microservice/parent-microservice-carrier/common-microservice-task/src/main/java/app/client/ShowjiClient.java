package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "showji", url = "http://v.showji.com")
public interface ShowjiClient {
	
	 @GetMapping("/Locating/showji.com20180331.aspx?output=json")
	 String showji(@RequestParam("m") String m);

}
