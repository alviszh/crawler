package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MsgHistoryTest {

	public static void main(String[] args) {

	     String base7 = "<table cellpadding=\"8\" class=\"com_table com_table2\"><tr style=\"background:url(/selfservice/img/titbg.jpg) repeat-x; text-align:center\"><td height=\"32\" colspan=\"6\" ><div class=\"bill_list\"><span class=\"toptitle_kfc billfont13px\" style=\"width:100%\" align=\"center\">欢卡50元</span></div></td></tr><tr><td colspan='2'  align='left'>用户号码： 手机：18947109133</td></tr><tr align='left'><th colspan='2'>套餐及叠加包月基本费</th></tr><tr><td>套餐费</td><td>50.00元</td></tr><tr align='left'><th colspan='2'>套餐及叠加包超出费用</th></tr><tr><td>语音通话费</td><td>8.00元</td></tr><table cellpadding=\"8\" class=\"com_table com_table2\"><tr style=\"background:url(/selfservice/img/titbg.jpg) repeat-x; text-align:center\"><td height=\"32\" colspan=\"6\" ><div class=\"bill_list\"><span class=\"toptitle_kfc billfont13px\" style=\"width:100%\" align=\"center\">手机:18947109133</span></div></td></tr><tr><td colspan='2'  align='left'>用户号码： 手机：18947109133</td></tr><tr align='left'><th colspan='2'>短信彩信费</th></tr><tr><td>短信费</td><td>4.90元</td></tr>";
	        // 套餐费
			String mealfee = "";
			// 上网通讯费
			String netphonefee = "";
			// 代收费
			String collectionfee = "";
			// 短信费
			String msgfee = "";
			// 语音通话费
			String inlandfee = "";
			if (base7.contains("套餐费")) {
				String[] splitt = base7.split("<td>套餐费</td>");
				String[] split2t = splitt[1].split("元");
				String[] split3t = split2t[0].split("<td>");
				mealfee = split3t[1];
				System.out.println("套餐费" + mealfee);
			}
			if (base7.contains("上网通讯费")) {
				String[] splitt = base7.split("<td>上网通讯费</td>");
				String[] split2t = splitt[1].split("元");
				String[] split3t = split2t[0].split("<td>");
				netphonefee = split3t[1];
				System.out.println("上网通讯费" + netphonefee);
			}
			if (base7.contains("代收费")) {
				String[] splitt = base7.split("<td>代收费</td>");
				String[] split2t = splitt[1].split("元");
				String[] split3t = split2t[0].split("<td>");
				collectionfee = split3t[1];
				System.out.println("代收费" + collectionfee);
			}
			if (base7.contains("短信费")) {
				String[] split = base7.split("<td>短信费</td>");
				String[] split2 = split[1].split("元");
				String[] split3 = split2[0].split("<td>");
				msgfee = split3[1];
				System.out.println("短信费" + msgfee);
			}

			if (base7.contains("语音通话费")) {
				String[] splity = base7.split("<td>语音通话费</td>");
				String[] split2y = splity[1].split("元");
				String[] split3y = split2y[0].split("<td>");
				inlandfee = split3y[1];
				System.out.println("语音通话费" + inlandfee);
			}
	}

}
