package test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年11月15日 下午4:45:29 
 */
public class JsonTest {
	public static void main(String[] args) {
		String str="{\"jsonError\":[{\"_exceptionMessageCode\":\"EFE0004\",\"_exceptionMessage\":\"EFE0004:未找到符合条件的记录。\"}]}";
		if(str.contains("未找到符合条件的记录")){
			System.out.println("可以判断");
		}else{
			System.out.println("不能判断");
		}
		
		
		str=str.substring(1, str.length()-1);
		System.out.println(str);
		if(str.contains("jsonError")){
			System.out.println("jsonError");
		}
	}
}
