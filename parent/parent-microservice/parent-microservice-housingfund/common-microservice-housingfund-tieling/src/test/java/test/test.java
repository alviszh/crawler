package test;

import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {

	public static void main(String[] args) {
		String html ="<title> transdate~summary~busiamt~balance</title><row><transdate>2017-12-14</transdate><summary>1001</summary><busiamt>862.00</busiamt><balance>73794.64</balance></row><row><transdate>2015-12-15</transdate><summary>1001</summary><busiamt>786.00</busiamt><balance>52154.97</balance></row><row><transdate>2016-02-05</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>52950.97</balance></row><row><transdate>2016-02-14</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>53746.97</balance></row><row><transdate>2016-03-16</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>54542.97</balance></row><row><transdate>2016-04-15</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>55338.97</balance></row><row><transdate>2016-05-16</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>56134.97</balance></row><row><transdate>2016-06-15</transdate><summary>1001</summary><busiamt>796.00</busiamt><balance>56930.97</balance></row><page><xml><TranCode>111141</TranCode><TranDate>2017-12-14</TranDate><STimeStamp>2013-07-11 09:07:42:45000</STimeStamp><MTimeStamp>2013-07-11 09:07:42:45000</MTimeStamp><BrcCode>00041026</BrcCode><TellCode>1234</TellCode><ChkCode>1234</ChkCode><AuthCode1>1234</AuthCode1><AuthCode2>1234</AuthCode2><AuthCode3>1234</AuthCode3><TranChannel>01</TranChannel><TranIP>10.22.21.125</TranIP><ChannelSeq>7544692</ChannelSeq><TranSeq>4989</TranSeq><BusiSeq>4989</BusiSeq><RspCode>000000</RspCode><RspMsg>交易处理成功...</RspMsg><NoteMsg/><AuthFlag/><CO_PageCount>8</CO_PageCount><CO_PageNum>1</CO_PageNum><CO_TotalNum>29</CO_TotalNum><CO_TotalPage>4</CO_TotalPage><CO_FileName>/home/bspstd/fil/1234</CO_FileName></xml></page>";

		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("row");
		System.out.println(ele.size());
		for(Element trele : ele){
			String date = trele.select("transdate").text().trim();          //交易日期
			String stract = trele.select("summary").text().trim();        //摘要
			if(stract.equals("1001")){
				stract = "汇缴";
			}else if(stract.equals("1004")){
				stract = "结息";
			}else if(stract.equals("1900")){
				stract = "年度结转";
			}
			String forehead = trele.select("busiamt").text().trim();      //发生额
			String balance = trele.select("balance").text().trim();       //余额
			System.out.println("date"+date);
			System.out.println("stract"+stract);
			System.out.println("forehead"+forehead);
			System.out.println("balance"+balance);
		}
		//Calendar now = Calendar.getInstance();
//		now.get(Calendar.YEAR);
//		now.get(Calendar.MONTH);
//		now.get(Calendar.DAY_OF_MONTH);
//		 System.out.println("年: " + now.get(Calendar.YEAR));  
//	        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");  
//	        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));  
	}

}
