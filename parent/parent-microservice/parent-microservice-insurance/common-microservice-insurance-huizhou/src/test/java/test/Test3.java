package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouUserInfo;

import net.sf.json.JSONObject;

public class Test3 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\444.html"),"UTF-8");		
		if(html.contains("message")){
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String flag = list1ArrayObjs.getString("flag");
			if("1".equals(flag)){
				String dataStr = list1ArrayObjs.getString("datas");	
				JSONObject dateObjs = JSONObject.fromObject(dataStr);
			
				String  user_ncm_gt= dateObjs.getString("ncm_gt_个人基本信息");
				if(null != user_ncm_gt){
					JSONObject userDatas = JSONObject.fromObject(user_ncm_gt);
					if(null != userDatas){						
						String userParams = userDatas.getString("params");
						JSONObject userObject = JSONObject.fromObject(userParams);
						String institutionName = userObject.getString("所属机构");					
						String institutionNum = userObject.getString("社会保险登记证编码");
						String unitname = userObject.getString("单位名称");					
						String idnum = userObject.getString("证件号码");
						String username = userObject.getString("姓名");					
						String gender = userObject.getString("性别");
						
						String birthdate = userObject.getString("出生日期");					
						String personalState = userObject.getString("个人身份");

						String householdState = userObject.getString("户口性质");					
						String employmenState = userObject.getString("用工性质");
						String firstworkDate = userObject.getString("参加工作日期");					
						String workerType = userObject.getString("职工类别");
					
						String civilserviceType = userObject.getString("公务员类别");					
						String paymentType = userObject.getString("缴费人员类别");
						String persionFirst = userObject.getString("养老保险参保时间");					
						String persionmonthNum = userObject.getString("养老保险实际月数");
						
						String lostFirst = userObject.getString("失业保险参保时间");					
						String lostMonthNum = userObject.getString("失业保险实际月数");
						String contactPersion = userObject.getString("联系人");					
						String contactTel = userObject.getString("联系电话");
						
						String zipcode = userObject.getString("邮政编码");
						String householdSeat = userObject.getString("户口所在地");					
						String liveAddress = userObject.getString("居住地地址");
						InsuranceHuiZhouUserInfo  userInfo=new InsuranceHuiZhouUserInfo( institutionName,  institutionNum,  unitname,  idnum,
								 username,  gender,  birthdate,  personalState,  householdState,
								 employmenState,  firstworkDate,  workerType,  civilserviceType,  paymentType,
								 persionFirst,  persionmonthNum,  lostFirst,  lostMonthNum,  contactPersion,
								 contactTel,  zipcode,  householdSeat,  liveAddress,  "111");
						System.out.println(userInfo.toString());
					}
				}
			}
		}

	}

}
