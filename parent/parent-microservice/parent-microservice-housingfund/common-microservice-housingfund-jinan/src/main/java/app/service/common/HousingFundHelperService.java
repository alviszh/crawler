package app.service.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

@Component
public class HousingFundHelperService {
	public static void main(String[] args) {
		System.out.println(getYear(0));   //济南市公积金个人明细可直接输入要查询的起始日期，不用分年份
	}
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public static String getYear(int i){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -1);
	    String year = f.format(c.getTime());
		return year;
	}
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	//借贷标志
	public static String getBorrowflag(String borrowflagCode){
		String borrowflag="贷(+)";
		if(borrowflagCode.equals("2")){
			borrowflag="贷(+)";
		}else{
			borrowflag="借(-)";   //code为1
		}
		return borrowflag;
	}
	
	//摘要
	public static String getSummary(String summaryCode){
		String summary="汇缴";
		if(summaryCode.equals("1001")){
			summary="开户";
		}else if(summaryCode.equals("1015")){
			summary="汇缴";
		}else if(summaryCode.equals("1009")){
			summary="单位账户同行转移（转出）";   
		}else if(summaryCode.equals("1024")){
			summary="单位账户同行转移（转入）";
		}else if(summaryCode.equals("1010")){
			summary="单位账户跨行转出";
		}else if(summaryCode.equals("1011")){
			summary="单位账户跨行接收";
		}else if(summaryCode.equals("1025")){
			summary="整体补缴";
		}else if(summaryCode.equals("1022")){
			summary="单位销户";
		}else if(summaryCode.equals("2001")){
			summary="个人开户";
		}else if(summaryCode.equals("2004")){
			summary="个人账户转移（转出）";
		}else if(summaryCode.equals("2038")){
			summary="个人账户转移（转入）";
		}else if(summaryCode.equals("2005")){
			summary="个人账户跨行转出";
		}else if(summaryCode.equals("2006")){
			summary="个人账户跨行接收";
		}else if(summaryCode.equals("2007")){
			summary="个人并户（并出）";
		}else if(summaryCode.equals("2008")){
			summary="个人并户（并入）";
		}else if(summaryCode.equals("2009")){
			summary="个人账户转入托管（转出）";
		}else if(summaryCode.equals("2039")){
			summary="个人账户转入托管（转入）";
		}else if(summaryCode.equals("1016")){
			summary="正常补缴";
		}else if(summaryCode.equals("1017")){
			summary="差额补缴";
		}else if(summaryCode.equals("1018")){
			summary="不定额补缴";
		}else if(summaryCode.equals("1019")){
			summary="系统外转入";
		}else if(summaryCode.equals("2024")){
			summary="住房提取";
		}else if(summaryCode.equals("2026")){
			summary="销户提取";
		}else if(summaryCode.equals("2036")){
			summary="销户结息";
		}else if(summaryCode.equals("2037")){
			summary="年度结息";
		}else if(summaryCode.equals("2011")){
			summary="个人账户封存";
		}else if(summaryCode.equals("2013")){
			summary="个人账户启封";
		}else if(summaryCode.equals("2016")){
			summary="个人基数变更";
		}else if(summaryCode.equals("9001")){
			summary="个人账户信息修改";
		}else if(summaryCode.equals("9002")){
			summary="批量个人账户信息修改";
		}else if(summaryCode.equals("9003")){
			summary="余额限制";
		}else if(summaryCode.equals("9004")){
			summary="全部冻结";
		}else if(summaryCode.equals("9005")){
			summary="部分解冻";
		}else if(summaryCode.equals("9006")){
			summary="全部解冻";
		}else if(summaryCode.equals("9007")){
			summary="核定汇缴";
		}else if(summaryCode.equals("5001")){
			summary="行财汇缴";
		}else if(summaryCode.equals("9008")){
			summary="核定补缴";
		}else if(summaryCode.equals("9009")){
			summary="年度调整基数";
		}else if(summaryCode.equals("9010")){
			summary="修改预调基数";
		}else if(summaryCode.equals("9011")){
			summary="变更新增开户";
		}else if(summaryCode.equals("9012")){
			summary="变更待转出停缴";
		}else if(summaryCode.equals("9013")){
			summary="变更待销户停缴";
		}else if(summaryCode.equals("9014")){
			summary="变更待停缴转正常";
		}else if(summaryCode.equals("9015")){
			summary="变更停缴转正常";
		}else if(summaryCode.equals("9016")){
			summary="客户信息修改";
		}else if(summaryCode.equals("9017")){
			summary="单位账户信息修改";
		}else if(summaryCode.equals("9018")){
			summary="改末次汇缴月";
		}else if(summaryCode.equals("9019")){
			summary="改挂账汇缴月";
		}else if(summaryCode.equals("9020")){
			summary="部分解冻";
		}else if(summaryCode.equals("9007")){
			summary="汇缴（建行摘要）";
		}else if(summaryCode.equals("9008")){
			summary="核定补缴-正常补缴";
		}else if(summaryCode.equals("9022")){
			summary="挂账户存入";
		}else if(summaryCode.equals("9021")){
			summary="取消核定";
		}else if(summaryCode.equals("9023")){
			summary="254挂账补缴";
		}else if(summaryCode.equals("9022")){
			summary="205挂账户补缴";
		}else if(summaryCode.equals("9009")){
			summary="年度调整基数";
		}else if(summaryCode.equals("9024")){
			summary="错帐调整";
		}else if(summaryCode.equals("1031")){
			summary="个人退缴";
		}else if(summaryCode.equals("1021")){
			summary="挂账户退款";
		}
		return summary;
	}
}
