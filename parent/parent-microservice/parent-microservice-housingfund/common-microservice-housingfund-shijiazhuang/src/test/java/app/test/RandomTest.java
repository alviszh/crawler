package app.test;
import java.net.URLEncoder;

public class RandomTest {  
  
    /** 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {  
//    		float Max = 1, Min = 0.0f;  
//            BigDecimal db = new BigDecimal(Math.random() * (Max - Min) + Min);  
//            System.out.println(db.setScale(16, BigDecimal.ROUND_HALF_UP).toString());   //保留16位小数并四舍五入
    		String str1="赵菲";
    		System.out.println(URLEncoder.encode(str1, "utf-8"));
    		
    }  
  
}  