package app.parser;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shangqiu.InsuranceShangQiuUserInfo;
import com.microservice.dao.entity.crawler.insurance.shangqiu.InsuranceShangQiuYearPayInfo;




@Component
public class InsuranceShangQiuParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceShangQiuParser.class);
	public InsuranceShangQiuUserInfo userInfoParser(TaskInsurance taskInsurance, String html1, String html2) {
		InsuranceShangQiuUserInfo insuranceShangQiuUserInfo=new InsuranceShangQiuUserInfo();
		insuranceShangQiuUserInfo.setTaskid(taskInsurance.getTaskid().trim());
		//解析个人权益维护单信息
		Document doc = Jsoup.parse(html1);
		String unitnum = doc.select("td:contains(单位编号)").first().text();
		unitnum=unitnum.split(":")[1].trim();
		insuranceShangQiuUserInfo.setUnitnum(unitnum);
		
		String pernum=doc.select("td:contains(用户名)+td").first().text();
		pernum=pernum.split("：")[1].trim();
		insuranceShangQiuUserInfo.setPernum(pernum);
		
		insuranceShangQiuUserInfo.setAccountmonths(doc.select("td:contains(账户月数)+td").first().text());
		insuranceShangQiuUserInfo.setArrearsmonths(doc.select("td:contains(欠费月数)+td").first().text());
		insuranceShangQiuUserInfo.setBirthday(doc.select("td:contains(出生日期)+td").first().text());
		insuranceShangQiuUserInfo.setBuildaccountdate(doc.select("td:contains(建账户时间)+td").first().text());
		insuranceShangQiuUserInfo.setGender(doc.select("td:contains(性别)+td").first().text());
		insuranceShangQiuUserInfo.setIdnum(doc.select("td:contains(身份证号码)+td").first().text());
		insuranceShangQiuUserInfo.setInsurstate(doc.select("td:contains(参保状态)+td").first().text());
		insuranceShangQiuUserInfo.setJoinworkdate(doc.select("td:contains(参加工作时间)+td").first().text());
		insuranceShangQiuUserInfo.setName(doc.select("td:contains(姓名)+td").first().text());
		insuranceShangQiuUserInfo.setNation(doc.select("td:contains(民族)+td").first().text());
		insuranceShangQiuUserInfo.setPerarrears(doc.select("td:contains(个人欠费本金)+td").first().text());
		insuranceShangQiuUserInfo.setPerchargedate(doc.select("td:contains(个人缴费时间)+td").first().text());
		insuranceShangQiuUserInfo.setPrincipalandinterest(doc.select("td:contains(账户本息合计)+td").first().text());
		insuranceShangQiuUserInfo.setTotalarrears(doc.select("td:contains(欠费本金合计)+td").first().text());
		insuranceShangQiuUserInfo.setUnitarrears(doc.select("td:contains(单位欠费本金)+td").first().text());
		//解析申报地址信息
		doc = Jsoup.parse(html2);
		insuranceShangQiuUserInfo.setPostaladdress(doc.select("td:contains(通讯地址)+td").first().text());
		insuranceShangQiuUserInfo.setPostalcode(doc.select("td:contains(邮政编码)+td").first().text());
		insuranceShangQiuUserInfo.setContactnum(doc.select("td:contains(联系电话)+td").first().text());
		insuranceShangQiuUserInfo.setUnitname(doc.select("td:contains(单位名称)+td").first().text());
		return insuranceShangQiuUserInfo;
	}
	//解析历年缴费基数信息
	public List<InsuranceShangQiuYearPayInfo> pensionParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceShangQiuYearPayInfo> list=new ArrayList<InsuranceShangQiuYearPayInfo>();
		InsuranceShangQiuYearPayInfo insuranceShangQiuYearPayInfo=null;
		
		StringBuilder sb = new StringBuilder();   //补全html
		sb.append("<table>");
		sb.append(html);
		sb.append("</table>");
		Document doc = Jsoup.parse(sb.toString());
		
		List<String> yearList=new ArrayList<String>();    //yearList大小是多少，就从baseNumList中取多少个数据
		List<String> baseNumList=new ArrayList<String>();
		
		Elements elementsByTag = doc.getElementsByTag("td");   //获取所有的td
		String data=null;
		if(elementsByTag.size()>0){   //有数据可供采集
			for (Element element : elementsByTag) {
				data=element.text();
				if(data.length()>6){  //说明得到的不是<\/td>替换之后的空值
					if(data.contains("年")){   //保存数据的时候不要"年"字
						yearList.add(data.substring(0, 4));
					}else{
						baseNumList.add(data.substring(0,data.indexOf("<")));
					}
				}
			}
			int size = yearList.size();
			for(int i=0;i<size;i++){
				insuranceShangQiuYearPayInfo=new InsuranceShangQiuYearPayInfo();
				insuranceShangQiuYearPayInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceShangQiuYearPayInfo.setYear(yearList.get(i));
				insuranceShangQiuYearPayInfo.setYearbasenum(baseNumList.get(i));
				list.add(insuranceShangQiuYearPayInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
}
