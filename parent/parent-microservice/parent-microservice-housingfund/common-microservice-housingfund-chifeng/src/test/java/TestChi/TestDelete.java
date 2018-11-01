package TestChi;

public class TestDelete {

	public static void main(String[] args) {
		
		String a = "sdsd:sdsd";
		if(a.contains(":"))
		{
			String replace = a.replace(":", "");
			System.out.println(replace);
		}
	}
}
