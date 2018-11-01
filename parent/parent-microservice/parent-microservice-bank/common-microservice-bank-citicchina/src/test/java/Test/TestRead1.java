package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zx1.txt"); 
		String json = txt2String(file);
//		if(json.contains("查询签约卡列表成功"))
//		{
////			System.out.println(json);
//			int indexOf1 = json.indexOf("stmt_dte_1");
//			int indexOf2 = json.indexOf("stmt_dte_2");
//			String substring = json.substring(indexOf1, indexOf2);
//			String substring2 = substring.substring(12);
//			String replace = substring2.replace("\"", "").replace("-", "");
//			System.out.println(replace);
//		}
		System.out.println(json);
		int indexOf3 = json.indexOf("acct");
		int indexOf4 = json.indexOf("acct_pdt");
		String substring = json.substring(indexOf3, indexOf4);
		String substring2 = substring.substring(6).replace("\"", "");
		System.out.println(substring2);
		
	}
	public static String txt2String(File file) { 
		StringBuilder result = new StringBuilder(); 
		try { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
		String s = null; 
		while ((s = br.readLine()) != null) { 
		result.append(System.lineSeparator() + s); 
		} 
		br.close(); 
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		return result.toString(); 
		}

		
}