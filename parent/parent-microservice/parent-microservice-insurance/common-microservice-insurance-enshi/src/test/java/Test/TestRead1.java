package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUserInfo;

import app.service.InsuranceEnShiCommonService;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\es.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document parse = Jsoup.parse(json);
		Element elementById = parse.getElementById("ctl00_ContentPlaceHolder1_BaseInfoTab");
//		System.out.println(elementById);
		InsuranceEnShiUserInfo i = new InsuranceEnShiUserInfo();
//		String nextLabelByKeyword = getNextLabelByKeyword(elementById, "姓名");
		i.setName(getNextLabelByKeyword(elementById, "姓名"));
		i.setSex(getNextLabelByKeyword(elementById, "性别"));
		i.setBirth(getNextLabelByKeyword(elementById, "出生日期"));
		i.setPersonal(getNextLabelByKeyword(elementById, "户口性质"));
		i.setNational(getNextLabelByKeyword(elementById, "民族"));
		i.setLevel(getNextLabelByKeyword(elementById, "文化程度"));
		i.setStatus(getNextLabelByKeyword(elementById, "人员状态"));
		i.setIDNum(getNextLabelByKeyword(elementById, "身份证号"));
		i.setJoinDate(getNextLabelByKeyword(elementById, "参加工作日期"));
		i.setPersonalNews(getNextLabelByKeyword(elementById, "个人身份"));
		i.setPhone(getNextLabelByKeyword(elementById, "联系电话"));
		i.setCompany(getNextLabelByKeyword(elementById, "工作单位"));
		i.setCompanyLevel(getNextLabelByKeyword(elementById, "行政职务"));
		i.setYongGong(getNextLabelByKeyword(elementById, "用工形式"));
		i.setEndDate(getNextLabelByKeyword(elementById, "离退休日期"));
		i.setProfessonal(getNextLabelByKeyword(elementById, "专业技术职务"));
		i.setWorker(getNextLabelByKeyword(elementById, "工人技术等级"));
		i.setSpecial(getNextLabelByKeyword(elementById, "特殊工种标识"));
		i.setHighMoney(getNextLabelByKeyword(elementById, "医保本年度基本医疗统筹支付最高限额"));
		i.setHouseLand(getNextLabelByKeyword(elementById, "居住地"));
		i.setHomeLand(getNextLabelByKeyword(elementById, "户口所在地"));
		i.setCb(getNextLabelByKeyword(elementById, "参保所在地"));
		i.setSbjg(getNextLabelByKeyword(elementById, "社保机构"));
		System.out.println(i);
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

	
	  /**
     * @param elementById
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public static String getNextLabelByKeyword(Element elementById, String keyword) {
        Elements es = elementById.select("td:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
    }
		
}