
public class testa {

	public static void main(String[] args) {
		String s = ".4";
		int i = s.indexOf(".");
		if(i==0){
			s = "0"+s;
		}
		System.out.println(s);
	}

}
