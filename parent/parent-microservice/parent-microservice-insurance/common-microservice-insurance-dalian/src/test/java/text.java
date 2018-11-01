
public class text {

	public static void main(String[] args) {
		 String currentUrl3 = "http://124.93.240.20:7017/grqyw/framework/main/outmain.jsp?subsys=grqyweb";
		 //System.out.println("url"+currentUrl3);
		 String s = currentUrl3.substring(0,currentUrl3.indexOf("grqyw/"));   
		 System.out.println(s);
	}

}
