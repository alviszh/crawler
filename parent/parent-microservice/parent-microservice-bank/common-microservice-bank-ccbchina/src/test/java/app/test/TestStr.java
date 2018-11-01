package app.test;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestStr {
	
	public static void main(String[] args) {
		
		String str = "6217000010077013236|110000000|lct|人民币||N|北京市|12|01|";
		
		String[] params = str.split("\\|");
		
		System.out.println(params[0]);
		System.out.println(params[1]);
		System.out.println(params[2]);
		System.out.println(params[3]);
//		System.out.println(params[4]);
		System.out.println(params[5]);
		System.out.println(params[6]);
		System.out.println(params[7]);
		System.out.println(params[8]);
		
		String aaa = URLEncoder.encode("北京市");
		System.out.println(aaa);
		
		
		
		String onclick = "mingxi('6217000010077013236|0101010|0101');";
		
		String last = onclick.substring(onclick.indexOf("|")+1, onclick.indexOf(")")-1);
		
		System.out.println(last);
		
		String[] str1 = last.split("\\|");
		
		System.out.println(str1[0]);
		System.out.println(str1[1]);
		
		
		String td = "<td width=\"7%\" >20170801<br/><script>formatTime('102136')</script></td>";
		
//		System.out.println("标签名   ===》》"+);
		
		Document doc = Jsoup.parse(td);
		System.out.println(doc.text());
		String script = doc.getElementsByTag("script").first().toString();
		System.out.println(script);
		String time = script.substring(script.indexOf("(")+2, script.indexOf(")")-1);
		System.out.println(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		try {
			Date date = sdf.parse(doc.text()+" "+time);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("jaja");
		
		
		System.out.println("11111111111 ; "+doc.attr("title"));
		
		
		String bbb = "<tr class=\"td_span\"  zcsr='1|400.00'>\r\n\t\t\t<td width=\"7%\" >20170801</td>\r\n\t\t\t<td width=\"7%\" >20170801<br/><script>formatTime('102136')</script></td>\t\t\t\r\n\t\t\t<td width=\"9%\" class=\"font_money\" style=\"color:#d62f2f;\">\r\n           \t\r\n\t\t\t-\r\n            </td>\r\n\t\t\t<td width=\"9%\" class=\"font_money\" style=\"color:#d62f2f;\">\r\n           \t\r\n\t        \t<script language=\"javascript\">formatAmt('400.00')</script>\r\n\t\t\t\r\n\t\t\t\r\n            </td> \r\n            \r\n              <td width=\"11%\" class=\"font_money\" style=\"color:#d62f2f;\">\r\n\t        \t<script language=\"javascript\">formatAmt('653.39')</script>\r\n\t        </td>\r\n\t\t\t\r\n\t\t\t \r\n\t\t\t<script>document.write(\"<td width='13%'  title='\"+accountProtect2('125906626210501')+\"'>\")</script>\r\n\t\t\t<!-- <td width=\"11%\" class=\"table_content text_center\" title=\"125906626210501\"> -->\r\n\t\t\t\t\r\n\t\t\t    <div id=\"L\"><script>accountProtect(\"125906626210501\")</script></div>\r\n\t\t\t</td>\r\n\t\t\t\r\n\t\t\t\r\n\t\t\t<td width=\"12%\"  title=\"江苏鑫时光老年产业开发有限公司\"><div id=\"W\" style=\"overflow:hidden;text-overflow:ellipsis;\"><script language=\"javascript\">chLengthWrit('江苏鑫时光老年产业开发有限公司','8')</script></div></td>\r\n\t\t\t\r\n\t\t\t<td width=\"7%\" style=\"white-space:nowrap\">人民币</td>\r\n\t\t\t<td width=\"11%\"  title=\"还款\">\r\n              <div id=\"S\" ><script language=\"javascript\">chLengthWrit('还款','18')</script></div>\r\n            </td>\r\n\t\t\t<td width=\"14%\" title=\"还款\">\r\n\t\t\t     \r\n\t\t\t     <div id=\"L\"><script language=\"javascript\">chLengthWrit('还款','18')</script></div>  \r\n            </td>\r\n\t  \t  </tr>";
		
		Document doc1 = Jsoup.parseBodyFragment(bbb.replaceAll("<!-- ", "").replaceAll("-->", ""));
		
		System.out.println(doc1.toString());
		
	
	}

}
