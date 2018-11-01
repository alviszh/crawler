public class TestSub {
	public static void main(String[] args) {
		String str="window.open('../gjjcx/cx_fromsfzhm_sz.aspx?cx=20180426&sfzhm=F38wrqoLFj/znMCuz29AJsWsRFD%2B8Tx5');";
		String splitData = splitData(str,"window.open('..","')");
		System.out.println(splitData);
	}
	public static String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
}
