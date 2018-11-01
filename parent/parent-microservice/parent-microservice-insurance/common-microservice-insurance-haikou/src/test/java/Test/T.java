package Test;

public class T {

	public static void main(String[] args) {
		String a="11112222";
		String b="223333";
		
		for (int i = 0; i < 2; i++) {
			if(a.contains("1"))
			{
				for (int j = 0; j < 2; j++) {
					System.out.println(a);
				}
			}
			else if(!b.contains("1"))
			{
				for (int j = 0; j < 2; j++) {
					System.out.println(b);
				}
			}
		}
	}
}
