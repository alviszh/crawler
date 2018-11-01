package TestTianjin;

public class TEst {
	public static void main(String[] args) {
		String a = "3null";
		String string = getOutNull(a);
		System.out.println(string+"==");
	}
	
	public static String getOutNull(String string){
		if(string=="null")
		{
			string="";
		}
		return string;
	}
}
