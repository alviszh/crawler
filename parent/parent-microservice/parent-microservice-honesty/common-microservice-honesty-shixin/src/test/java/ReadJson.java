import java.io.BufferedReader;
import java.io.FileReader;

import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;

import app.crawler.htmlparse.HtmlParse;


/**
 * 
 * 项目名称：common-microservice-executor 类名称：ReadJson 类描述： 创建人：hyx 创建时间：2018年7月19日
 * 下午1:38:22
 * 
 * @version
 */
public class ReadJson {
//	ShiXinBeanRepository shiXinBeanRepository = new ShiXinBeanRepository();
	public static void main(String[] args) throws Exception {
		 BufferedReader br = new BufferedReader(new FileReader(  
                 "C:\\FayuanDishonestjson_0_.json"));// 读取原始json文件  
         String s = null;  
         while ((s = br.readLine()) != null) {  
        	 s = s.replaceAll("id", "shixinid");
              System.out.println(s);  
             ShiXinBean result =  HtmlParse.shixin_parse(s);
             
             System.out.println(result.toString());
              
         }

	}
}
