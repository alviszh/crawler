package app.unit;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.bean.HonestyJsonBean;


/**   
*    
* 项目名称：common-microservice-search-task   
* 类名称：SanWangUnit   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月31日 下午4:00:29   
* @version        
*/
public class SanWangUnit {

	private  static Gson gs = new Gson();
	
	public static HonestyJsonBean JsonParse(String json){
		Type type = new TypeToken<HonestyJsonBean>() {
		}.getType();
		HonestyJsonBean jsonObject = gs.fromJson(json, type);
		System.out.println(jsonObject.toString());
		return jsonObject;
	}
	
	public static void main(String[] args) {
//		SanWangJsonBean sanWangJsonBean = new SanWangJsonBean();
//		
//		sanWangJsonBean.setTaskid("posttest01");
//		
//		 List<String> keys = new ArrayList<>();
//		 keys.add("腾讯");
//		 keys.add("360");
//		 keys.add("雅虎");
//		 sanWangJsonBean.setKeys(keys);
//		 sanWangJsonBean.setPagenum(2);
//		 
//		 System.out.println(gs.toJson(sanWangJsonBean));
		
//		Date day=new Date();    
//
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//
//		System.out.println(df.format(day));   
		
Timestamp time = Timestamp.valueOf(LocalDateTime.now().plusMinutes(-5));  
		
		System.out.println("kaishi:  "+time);
		
		System.out.println(LocalDateTime.now());
		 
	}
}
