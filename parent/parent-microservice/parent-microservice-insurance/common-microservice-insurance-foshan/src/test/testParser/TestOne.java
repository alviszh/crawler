package testParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import app.service.InsuranceService;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class TestOne {

	
	public static void main(String[] args) {
		InsuranceService insuranceService = new InsuranceService();
		String txt = null;
		try {
            String encoding="Unicode";
            File file = new File("E:\\crawler\\foshan\\userInfo.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(txt);
		Elements elements = doc.select(".tdread");
		List<String> str = new ArrayList<String>();
		for (Element element : elements) {
			str.add(element.text());
		}
		String manageOrganization = insuranceService.getNextLabelByKeyword(doc, "管理的社保机构");
		String insuranceNum = insuranceService.getNextLabelByKeyword(doc, "社会保障号");
		String personalInsuranceNum = insuranceService.getNextLabelByKeyword(doc, "个人社保号");
		String name = insuranceService.getNextLabelByKeyword(doc, "姓名");
		String gender = insuranceService.getNextLabelByKeyword(doc, "性别");
		String birthday = insuranceService.getNextLabelByKeyword(doc, "出生日期");
		String organizationName = insuranceService.getNextLabelByKeyword(doc, "现单位名称");
		String pension = insuranceService.getNextLabelByKeyword(doc, "养老");
		String medical = insuranceService.getNextLabelByKeyword(doc, "医疗");
		String bear = insuranceService.getNextLabelByKeyword(doc, "生育");
		String injury = insuranceService.getNextLabelByKeyword(doc, "工伤");
		String unemployment = insuranceService.getNextLabelByKeyword(doc, "失业");
		String arrearageMonth = insuranceService.getNextLabelByKeyword(doc, "欠费月数");
		String pensionRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "养老 实际缴费月数");
		String medicalRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "医疗 实际缴费月数");
		String unemploymentRealPayMonth = insuranceService.getNextLabelByKeyword(doc, "失业 实际缴费月数");
		
		System.out.println("***"+manageOrganization+"***"+insuranceNum+"***"
				+personalInsuranceNum+"***"+name+"***"+gender+"***"
						+birthday+"***"+organizationName+"***"+pension+"***"+medical+"***"
				+bear+"***"+injury+"***"+unemployment+"***"+arrearageMonth+"***"+pensionRealPayMonth+"***"
						+medicalRealPayMonth+"***"+unemploymentRealPayMonth+"***");
	}
	
}
