package text;

import java.util.regex.Pattern;

public class dandonglogin {

	public static void main(String[] args) {
		String regEx = "[\\u4e00-\\u9fa5]";      
		String a = "11,568.69补缴201001至201005公积金";
		String[] split = a.split("");
		String ri = "";
		for(int i=0;i<split.length;i++){
			ri +=split[i];
			if(split[i].equals(regEx)){
				String[] split2 = a.split(ri);
				String string = split2[1];
				System.out.println(string);
				break;
			};
		}
		
		
		
		
	}
}
