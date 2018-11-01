package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingInjury;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingPension;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingUserinfo;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceSanmingParser {
	
	@Autowired
	private TracerLog tracer; 

	/**
	 * 解析用户信息
	 * @param contentAsString
	 * @return
	 */
	public InsuranceSanmingUserinfo parserUserinfo(String html) {
		try{			
			Document doc = Jsoup.parse(html);
			Element table = doc.select("[bgcolor=#71a7d9]").first();
			Elements trs = table.select("tr");
			
			Element tr = trs.get(3);
			String idnum = tr.child(1).child(0).text();
			String name = tr.child(3).child(0).text();
			
			System.out.println("身份证号："+idnum);
			System.out.println("姓名："+name);
			
			tr = trs.get(4);	
			String address = tr.child(1).child(0).text();		
			System.out.println("通讯地址："+address);
			
			tr = trs.get(5);
			String phone = tr.child(1).child(0).text();
			String postcode = tr.child(3).child(0).text();
			
			System.out.println("联系电话："+phone);
			System.out.println("邮政编码："+postcode);
			
			tr = trs.get(6);
			String companyCode = tr.child(1).child(0).text();
			String companyName = tr.child(3).child(0).text();
			
			System.out.println("单位编码："+companyCode);
			System.out.println("单位名称："+companyName);
			
			tr = trs.get(7);
			String birthday = tr.child(1).child(0).text();
			String wordType = tr.child(3).child(0).text();
			
			System.out.println("出生年月："+birthday);
			System.out.println("用工形式："+wordType);
			
			tr = trs.get(8);
			String workTime = tr.child(1).child(0).text();
			String pensionMonth = tr.child(3).child(0).text();
			
			System.out.println("参加工作年月："+workTime);
			System.out.println("参加养老保险年月："+pensionMonth);
			
			tr = trs.get(9);
			String payMonth = tr.child(1).child(0).text();
			String beginMonth = tr.child(3).child(0).text();
			
			System.out.println("建账前累计缴费月数："+payMonth);
			System.out.println("建立个人账户年月："+beginMonth);
			
			tr = trs.get(11);
			String payMonthEnd = tr.child(1).child(0).text();
			String payBaseTotal = tr.child(3).child(0).text();
			
			System.out.println("建帐后累计缴费月数："+payMonthEnd);
			System.out.println("缴费总基数："+payBaseTotal);
			
			tr = trs.get(12);
			String accountTotalMoney = tr.child(1).child(0).text();
			String myNum = tr.child(3).child(0).text();
			
			System.out.println("个人账户总金额："+accountTotalMoney);
			System.out.println("个人编号："+myNum);
			
			InsuranceSanmingUserinfo insuranceSanmingUserinfo = new InsuranceSanmingUserinfo();
			insuranceSanmingUserinfo.setCompanyCode(companyCode);
			insuranceSanmingUserinfo.setCompanyName(companyName);
			insuranceSanmingUserinfo.setIdnum(idnum);
			insuranceSanmingUserinfo.setName(name);
			insuranceSanmingUserinfo.setPhone(phone);
			insuranceSanmingUserinfo.setAccountTotalMoney(accountTotalMoney);
			insuranceSanmingUserinfo.setAddress(address);
			insuranceSanmingUserinfo.setBeginMonth(beginMonth);
			insuranceSanmingUserinfo.setBirthday(birthday);
			insuranceSanmingUserinfo.setMyNum(myNum);
			insuranceSanmingUserinfo.setPayBaseTotal(payBaseTotal);
			insuranceSanmingUserinfo.setPayMonth(payMonthEnd);
			insuranceSanmingUserinfo.setPayMonthEnd(payMonthEnd);
			insuranceSanmingUserinfo.setPensionMonth(pensionMonth);
			insuranceSanmingUserinfo.setPostcode(postcode);
			insuranceSanmingUserinfo.setWordType(wordType);
			insuranceSanmingUserinfo.setWorkTime(workTime);
			
			return insuranceSanmingUserinfo;
		}catch(Exception e){
			tracer.addTag("解析用户信息出错", e.getMessage());		
			return null;
		}
	}

	/**
	 * 解析获取养老保险详细缴纳情况的所需参数
	 * @param html
	 * @return
	 */
	public List<String> parserPensionParam(HtmlPage html) {
		List<String> params = new ArrayList<String>();
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
		try{
			Elements inputs = doc.select("[value=详细]");
			for(int i = 0;i<inputs.size();i++){
				String onClick = inputs.get(i).attr("onclick");
				String code = onClick.substring(onClick.indexOf("\'")+1, onClick.length()-2).trim();
				params.add(code);
			}
			return params;
			
		}catch(Exception e){
			tracer.addTag("解析获取养老保险详细缴纳情况的所需参数出错了", e.getMessage());
			return null;
		}
	}

	/**
	 * 解析养老保险详情
	 * @param detailHtml
	 * @param taskid
	 * @return
	 */
	public List<InsuranceSanmingPension> parserPensionDetail(HtmlPage detailHtml, String taskid) {
		try{
			List<InsuranceSanmingPension> list = new ArrayList<InsuranceSanmingPension>();
			Document doc = Jsoup.parse(detailHtml.getWebResponse().getContentAsString());
			String accountMonthly = doc.select("span:contains(到帐年月)").first().parent().nextElementSibling().text();
			String declarationDate = doc.select("span:contains(申报日期)").first().parent().nextElementSibling().text();
			String companyCode = doc.select("span:contains(单位编号)").first().parent().nextElementSibling().text();
			String payMonth = doc.select("span:contains(缴费月数)").first().parent().nextElementSibling().text();
			String beginDate = doc.select("span:contains(费款所属起始日期)").first().parent().nextElementSibling().text();
			String endDate = doc.select("span:contains(费款所属截止日期)").first().parent().nextElementSibling().text();
			String companyPayCardinal = doc.select("span:contains(单位缴费基数)").first().parent().nextElementSibling().text();
			String companyPayScale = doc.select("span:contains(单位缴费比例)").first().parent().nextElementSibling().text();
			String ordinatingMoney = doc.select("span:contains(统筹金额)").first().parent().nextElementSibling().text();
			String personPayCardinal = doc.select("span:contains(个人缴费基数)").first().parent().nextElementSibling().text();
			String personPayScale = doc.select("span:contains(个人缴费比例)").first().parent().nextElementSibling().text();
			String personAccountMoney = doc.select("span:contains(个人账户金额)").first().parent().nextElementSibling().text();
			String Ashare = doc.select("span:contains(个帐比例)").first().parent().nextElementSibling().text();
			String companyPartMoney = doc.select("span:contains(单位划拨部分金额)").first().parent().nextElementSibling().text();
			String totalMoney = doc.select("span:contains(实缴总金额)").first().parent().nextElementSibling().text();
			
			InsuranceSanmingPension insuranceSanmingPension = new InsuranceSanmingPension();
			insuranceSanmingPension.setAccountMonthly(accountMonthly);
			insuranceSanmingPension.setAshare(Ashare);
			insuranceSanmingPension.setBeginDate(beginDate);
			insuranceSanmingPension.setCompanyCode(companyCode);
			insuranceSanmingPension.setCompanyPartMoney(companyPartMoney);
			insuranceSanmingPension.setCompanyPayCardinal(companyPayCardinal);
			insuranceSanmingPension.setCompanyPayScale(companyPayScale);
			insuranceSanmingPension.setDeclarationDate(declarationDate);
			insuranceSanmingPension.setEndDate(endDate);
			insuranceSanmingPension.setOrdinatingMoney(ordinatingMoney);
			insuranceSanmingPension.setPayMonth(payMonth);
			insuranceSanmingPension.setPersonAccountMoney(personAccountMoney);
			insuranceSanmingPension.setPersonPayCardinal(personPayCardinal);
			insuranceSanmingPension.setPersonPayScale(personPayScale);
			insuranceSanmingPension.setTaskid(taskid);
			insuranceSanmingPension.setTotalMoney(totalMoney);
			
			list.add(insuranceSanmingPension);
			return list;
		}catch(Exception e){
			tracer.addTag("解析养老保险缴纳详情出错", e.getMessage());
			return null;
		}
	}

	/**
	 * 解析工伤和生育保险信息
	 * @param taskid
	 * @param detailHtml
	 * @return
	 */
	public InsuranceSanmingInjury parserInjury(String taskid, String detailHtml) {
		
		Document doc = Jsoup.parse(detailHtml);
		try{
			String businessNum = doc.select("nobr:contains(业务流水号)").first().parent().parent().nextElementSibling().text();
			String personCode = doc.select("span:contains(个人编号)").first().parent().nextElementSibling().text();
			String insuranceType = doc.select("span:contains(险种类型)").first().parent().nextElementSibling().text();
			String payType = doc.select("span:contains(缴费类型)").first().parent().nextElementSibling().text();
			String companyName = doc.select("nobr:contains(单位名称)").first().parent().parent().nextElementSibling().text();
			String declarationDate = doc.select("span:contains(申报年月)").first().parent().nextElementSibling().text();
			String payWay = doc.select("nobr:contains(缴费方式)").first().parent().parent().nextElementSibling().text();
			String companyPayCardinal = doc.select("span:contains(单位缴费基数)").first().parent().nextElementSibling().text();
			String personPayCardinal = doc.select("span:contains(个人缴费基数)").first().parent().nextElementSibling().text();
			String companyPayScale = doc.select("nobr:contains(单位缴费比例)").first().parent().parent().nextElementSibling().text();
			String personPayScale = doc.select("span:contains(个人缴费比例)").first().parent().nextElementSibling().text();
			String companyPayMoney = doc.select("span:contains(应缴金额单位)").first().parent().nextElementSibling().text();
			String personPayMoney = doc.select("span:contains(应缴金额个人)").first().parent().nextElementSibling().text();
			String payMoney = doc.select("nobr:contains(应缴金额)").first().parent().parent().nextElementSibling().text();
			String noticeOrder = doc.select("span:contains(通知单流水号)").first().parent().nextElementSibling().text();
			
			InsuranceSanmingInjury insuranceSanmingInjury = new InsuranceSanmingInjury();
			insuranceSanmingInjury.setBusinessNum(businessNum);
			insuranceSanmingInjury.setCompanyName(companyName);
			insuranceSanmingInjury.setCompanyPayCardinal(companyPayCardinal);
			insuranceSanmingInjury.setCompanyPayMoney(companyPayMoney);
			insuranceSanmingInjury.setCompanyPayScale(companyPayScale);
			insuranceSanmingInjury.setDeclarationDate(declarationDate);
			insuranceSanmingInjury.setInsuranceType(insuranceType);
			insuranceSanmingInjury.setNoticeOrder(noticeOrder);
			insuranceSanmingInjury.setPayMoney(payMoney);
			insuranceSanmingInjury.setPayType(payType);
			insuranceSanmingInjury.setPayWay(payWay);
			insuranceSanmingInjury.setPersonCode(personCode);
			insuranceSanmingInjury.setPersonPayCardinal(personPayCardinal);
			insuranceSanmingInjury.setPersonPayMoney(personPayMoney);
			insuranceSanmingInjury.setPersonPayScale(personPayScale);
			insuranceSanmingInjury.setTaskid(taskid);
			
			return insuranceSanmingInjury;
			
		}catch(Exception e){
			tracer.addTag("解析失败！", e.getMessage());
			return null;
		}
	}

}
