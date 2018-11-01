
public class test {

	public static void main(String[] args) {
//		String str ="单位电脑编号：2103040018307     单位名称：辽宁人才派遣有限公司鞍山分公司 个人电脑编号： 2103040070781253   姓名： 董金翰   ";
//		String str1 = str.substring(str.indexOf("：")+1).trim();
//		String str2 = str1.substring(0,str1.indexOf("单")).trim();
//		
//		System.out.println(str2);
//		
//		String str3 = str1.substring(str1.indexOf("：")+1).trim();
//		String str4 = str3.substring(0, str3.indexOf("个人电脑")-1).trim();
//		
//		System.out.println(str4);
//		
//		String str5 = str3.substring(str3.indexOf("：")+1).trim();
//		String str6 = str5.substring(0, str5.indexOf("姓名")-1).trim();
//		
//		System.out.println(str6);
//		
//		String str7 = str5.substring(str5.indexOf("：")+1).trim();
//		System.out.println(str7);
//		
		String str = "个人电脑编号：2103040070781253   姓名：董金翰   身份证号：210303198912212518";
		String idCard = str.substring(str.lastIndexOf("：")+1);
		System.out.println(idCard);
	}

}
