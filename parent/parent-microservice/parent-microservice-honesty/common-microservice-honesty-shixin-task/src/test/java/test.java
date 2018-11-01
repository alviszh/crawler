
import com.google.gson.Gson;


/**   
*    
* 项目名称：common-microservice-honesty-shixin-task   
* 类名称：test   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年8月6日 下午6:38:37   
* @version        
*/
public class test {

	static Gson gs = new Gson();
	public static void main(String[] args) {
		
		
		
		StringBuilder sb = new StringBuilder("150403197933334545");
		sb.replace(10, 14, "****");
		
		System.out.println(sb.toString());
		
		
		
		
		
		
//		HonestyJsonBean honestyJsonBean = new HonestyJsonBean();
//		
//		 List<Honestybean> keys = new ArrayList<Honestybean>();
//		 keys.add(null);
//
//		 Honestybean honestybean = new Honestybean();
//		 honestybean.setpName("王凯");
//		 keys.add(honestybean);
//		 
//		 honestybean = new Honestybean();
//		 honestybean.setpName("李刚");
//		 keys.add(honestybean);
//		 honestybean = new Honestybean();
//		 honestybean.setpName("");
//		 keys.add(honestybean);
//		 
//		 honestyJsonBean.setKeys(keys);
//		 honestyJsonBean.setTaskid("0001");
//		 
//		 String objectStr = gs.toJson(honestyJsonBean);//把对象转为JSON格式的字符串  
//		 System.out.println("把对象转为JSON格式的字符串///  :"+objectStr); 
	}
}
