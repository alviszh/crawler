package test;

public class Test {

	public static void main(String[] args) {
		String base = "GlobalNames.__usersession_uuid = 'USERSESSION_151440d5_5984_4c35_924c_6eaa44b4bd59';";
		String[] split = base.split("GlobalNames.__usersession_uuid =");
		String[] split2 = split[1].split("'");
		System.out.println(split2[1]);
		
		
	}

}
