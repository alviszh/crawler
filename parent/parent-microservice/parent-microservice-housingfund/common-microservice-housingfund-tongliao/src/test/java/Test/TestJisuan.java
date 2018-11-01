package Test;

public class TestJisuan {

	public static void main(String[] args) {
		
		String a ="12345,23456";
		String[] split = a.split(",");
		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
		}
	}
}
