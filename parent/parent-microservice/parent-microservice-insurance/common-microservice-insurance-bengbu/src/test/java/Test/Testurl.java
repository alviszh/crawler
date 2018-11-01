package Test;

public class Testurl {

	public static void main(String[] args) {
		String s = "你好";
	    System.out.println(toUnicode(s));
	    
	    String decodeUnicode = decodeUnicode("\\u000d\\u000a");
	    System.out.println(decodeUnicode);
	}
	
	public static String decodeUnicode(final String dataStr) {     
        int start = 0;     
        int end = 0;     
        final StringBuffer buffer = new StringBuffer();     
        while (start > -1) {     
            end = dataStr.indexOf("\\u", start + 2);     
            String charStr = "";     
            if (end == -1) {     
                charStr = dataStr.substring(start + 2, dataStr.length());     
            } else {     
                charStr = dataStr.substring(start + 2, end);     
            }     
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
            buffer.append(new Character(letter).toString());     
            start = end;     
        }     
        return buffer.toString();     
     }  
	public static String toUnicode(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) <= 256) {
				sb.append("\\u00");
			} else {
				sb.append("\\u");
			}
			sb.append(Integer.toHexString(s.charAt(i)));
		}
		return sb.toString();
	}
}
