package Test;

public class TestProd {

	public static void main(String[] args) {
		String a="data:{MOBILE_NAME:'18172055939',RAND_TYPE:'025',OPER_TYPE:'CR1',PRODTYPE:'2020966'},dataType:\"xml\",";
		int indexOf = a.indexOf("PRODTYPE");
		String substring = a.substring(indexOf);
		System.out.println(substring.substring(10,17));
	}
}
