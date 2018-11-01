package app.htmlparser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendEducations;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendUserInfo;
import com.microservice.dao.entity.crawler.maimai.MaimaiFriendWorkExps;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserEducations;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserInfo;
import com.microservice.dao.entity.crawler.maimai.MaimaiUserWorkExps;
import com.microservice.dao.entity.crawler.maimai.TaskMaimai;

@Component
public class MiamaiParser {

	//脉脉用户的信息
	public MaimaiUserInfo userInfo(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc1 = acc.get("card").getAsJsonObject();
		JsonObject acc2 = acc.get("uinfo").getAsJsonObject();
		String taskid = taskMaimai.getTaskid();
		String mm_id = acc1.get("mmid").toString().replaceAll("\"", "");     //脉脉账号在脉脉系统中的ID
		String name = acc1.get("name").toString().replaceAll("\"", "");      //用户姓名
		String gender = acc1.get("gender").toString().replaceAll("\"", "");     //用户性别(1: 男; 2: 女;)
//		String account = acc1.get("mmid").toString().replaceAll("\"", "");    //用户的脉脉号(这个号不是脉脉用户的登录账户)
		String company = acc1.get("company").toString().replaceAll("\"", "");    //用户的工作单位
		String position = acc1.get("position").toString().replaceAll("\"", "");   //工作职务
		String province = acc1.get("province").toString().replaceAll("\"", "");   //用户当前所在的省份
		String city = acc1.get("city").toString().replaceAll("\"", "");       //用户当前所在的城市	
		String rank = acc1.get("rank").toString().replaceAll("\"", "");        //影响力(脉脉系统给用户的评分)
		
		String birthday = acc2.get("birthday").toString().replaceAll("\"", "");   //用户生日
		String mobile = acc2.get("mobile").toString().replaceAll("\"", "");     //用户手机号
		String email = acc2.get("email").toString().replaceAll("\"", "");      //用户邮箱
		String home_province = acc2.get("ht_province").toString().replaceAll("\"", ""); //用户家乡所在的省份	
		String home_city = acc2.get("ht_city").toString().replaceAll("\"", "");   //用户家乡所在的城市
		String headline = acc2.get("headline").toString().replaceAll("\"", "");    //用户的自我介绍	
		String tags = acc2.get("weibo_tags").toString().replaceAll("\"", "");        //标签(多个标签之间用逗号分隔)
		String business = acc1.get("line4").toString().replaceAll("\"", "");    //行业, 方向, 影响力
		MaimaiUserInfo userInfo = new MaimaiUserInfo();
		userInfo.setTaskid(taskid);
		userInfo.setMm_id(mm_id);
		userInfo.setName(name);
		userInfo.setGender(gender);
		userInfo.setCompany(company);
		userInfo.setPosition(position);
		userInfo.setProvince(province);
		userInfo.setCity(city);
		userInfo.setRank(rank);
		userInfo.setBirthday(birthday);
		userInfo.setMobile(mobile);
		userInfo.setEmail(email);
		userInfo.setHome_province(home_province);
		userInfo.setHome_city(home_city);
		userInfo.setHeadline(headline);
		userInfo.setTags(tags);
		userInfo.setBusiness(business);
		return userInfo;
		
	}
	
	//脉脉用户的教育经历
	public List<MaimaiUserEducations> userEducations(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc2 = acc.get("card").getAsJsonObject();
		String mm_id = acc2.get("mmid").toString().replaceAll("\"", "");       //脉脉账号在脉脉系统中的ID
		JsonObject acc1 = acc.get("uinfo").getAsJsonObject();
		JsonArray accountCardList1 = acc1.get("education").getAsJsonArray();
		List<MaimaiUserEducations> list = new ArrayList<MaimaiUserEducations>();
		for (JsonElement accd : accountCardList1) {
			JsonObject account = accd.getAsJsonObject();
			String taskid = taskMaimai.getTaskid();

			String school = account.get("school").toString().replaceAll("\"", "");      //毕业学校
			String department = account.get("department").toString().replaceAll("\"", "");  //专业
			String degree = account.get("degree").toString().replaceAll("\"", "");      //学历(0:专科; 1: 本科; 2: 硕士; 3: 博士; 4: 博士后; 5: 其他; 255:其他)
			String start_date = account.get("start_date").toString().replaceAll("\"", "");  //入学时间
			String end_date = account.get("end_date").toString().replaceAll("\"", "");    //毕业时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
			String description = account.get("description").toString().replaceAll("\"", ""); //教育经历描述
			MaimaiUserEducations educations = new MaimaiUserEducations();
			educations.setMm_id(mm_id);
			educations.setTaskid(taskid);
			educations.setSchool(school);
			educations.setDepartment(department);
			educations.setDegree(degree);
			educations.setStart_date(start_date);
			educations.setEnd_date(end_date);
			educations.setDescription(description);
			list.add(educations);
		}
		return list;
		
	}	
		
	//脉脉用户的工作经历
	public List<MaimaiUserWorkExps> userWorkExps(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc2 = acc.get("card").getAsJsonObject();
		String mm_id = acc2.get("mmid").toString().replaceAll("\"", "");       //脉脉账号在脉脉系统中的ID
		JsonObject acc1 = acc.get("uinfo").getAsJsonObject();
		JsonArray accountCardList1 = acc1.get("work_exp").getAsJsonArray();
		List<MaimaiUserWorkExps> list = new ArrayList<MaimaiUserWorkExps>();
		for (JsonElement accd : accountCardList1) {
			JsonObject account = accd.getAsJsonObject();
			String taskid = taskMaimai.getTaskid();
	
			String company = account.get("company").toString().replaceAll("\"", "");     //工作单位
			String position = account.get("position").toString().replaceAll("\"", "");    //工作职务
			String start_date = account.get("start_date").toString().replaceAll("\"", "");  //工作开始时间
			String end_date = account.get("end_date").toString().replaceAll("\"", "");    //工作结束时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
			String description = account.get("description").toString().replaceAll("\"", "");   //工作经历描述
			MaimaiUserWorkExps workExps =new MaimaiUserWorkExps();
			workExps.setMm_id(mm_id);
			workExps.setTaskid(taskid);
			workExps.setCompany(company);
			workExps.setPosition(position);
			workExps.setStart_date(start_date);
			workExps.setEnd_date(end_date);
			workExps.setDescription(description);
			list.add(workExps);
		}
		return list;
		
	}			
		
	//脉脉用户朋友的基本信息
	public MaimaiFriendUserInfo friendUserInfo(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc1 = acc.get("card").getAsJsonObject();
		JsonObject acc2 = acc.get("uinfo").getAsJsonObject();
		JsonObject acc3 = acc.get("mycard").getAsJsonObject();
		String taskid = taskMaimai.getTaskid();
		String mm_id = acc3.get("mmid").toString().replaceAll("\"", "");     //脉脉账号在脉脉系统中的ID
		String friend_id = acc1.get("mmid").toString().replaceAll("\"", "");     //朋友在脉脉系统中的ID
		String name = acc1.get("name").toString().replaceAll("\"", "");      //朋友姓名
		String gender = acc1.get("gender").toString().replaceAll("\"", "");     //朋友性别(1: 男; 2: 女;)
//		String account = acc1.get("mmid").toString().replaceAll("\"", "");    //朋友的脉脉号(这个号不是脉脉用户的登录账户)
		String company = acc1.get("company").toString().replaceAll("\"", "");    //朋友的工作单位
		String position = acc1.get("position").toString().replaceAll("\"", "");   //工作职务
		String province = acc1.get("province").toString().replaceAll("\"", "");   //朋友当前所在的省份
		String city = acc1.get("city").toString().replaceAll("\"", "");       //朋友当前所在的城市	
		String rank = acc1.get("rank").toString().replaceAll("\"", "");        //影响力(脉脉系统给用户的评分)
		
		String birthday = acc2.get("birthday").toString().replaceAll("\"", "");   //朋友生日
		String mobile = acc2.get("mobile").toString().replaceAll("\"", "");     //朋友手机号
		String email = acc2.get("email").toString().replaceAll("\"", "");      //朋友邮箱
		String home_province = acc2.get("ht_province").toString().replaceAll("\"", ""); //朋友家乡所在的省份	
		String home_city = acc2.get("ht_city").toString().replaceAll("\"", "");   //朋友家乡所在的城市
		String headline = acc2.get("headline").toString().replaceAll("\"", "");    //朋友的自我介绍	
		String tags = acc2.get("weibo_tags").toString().replaceAll("\"", "");        //标签(多个标签之间用逗号分隔)
		String business = acc1.get("line4").toString().replaceAll("\"", "");    //行业, 方向, 影响力
		MaimaiFriendUserInfo userInfo = new MaimaiFriendUserInfo();
		userInfo.setTaskid(taskid);
		userInfo.setMm_id(mm_id);
		userInfo.setFriend_id(friend_id);
		userInfo.setName(name);
		userInfo.setGender(gender);
		userInfo.setCompany(company);
		userInfo.setPosition(position);
		userInfo.setProvince(province);
		userInfo.setCity(city);
		userInfo.setRank(rank);
		userInfo.setBirthday(birthday);
		userInfo.setMobile(mobile);
		userInfo.setEmail(email);
		userInfo.setHome_province(home_province);
		userInfo.setHome_city(home_city);
		userInfo.setHeadline(headline);
		userInfo.setTags(tags);
		userInfo.setBusiness(business);
		return userInfo;
		
	}	
	
	//脉脉用户朋友的教育经历
	public List<MaimaiFriendEducations> friendEducations(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc2 = acc.get("card").getAsJsonObject();
		String mm_id = acc2.get("mmid").toString().replaceAll("\"", "");       //脉脉账号在脉脉系统中的ID
		JsonObject acc3 = acc.get("mycard").getAsJsonObject();
		String friend_id = acc3.get("mmid").toString().replaceAll("\"", "");   //朋友在脉脉系统中的ID	
		JsonObject acc1 = acc.get("uinfo").getAsJsonObject();
		JsonArray accountCardList1 = acc1.get("education").getAsJsonArray();
//		System.out.println("accountCardList1:"+accountCardList1.size());
		List<MaimaiFriendEducations> list = new ArrayList<MaimaiFriendEducations>();
		for (JsonElement accd : accountCardList1) {
			JsonObject account = accd.getAsJsonObject();
			String taskid = taskMaimai.getTaskid();
			String start_date = null;
			String end_date = null;
			if(account.toString().contains("start_date")){
				start_date = account.get("start_date").toString().replaceAll("\"", "");  //入学时间
				
			}
			if(account.toString().contains("end_date")){
				end_date = account.get("end_date").toString().replaceAll("\"", "");    //毕业时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
			}
			String school = account.get("school").toString().replaceAll("\"", "");      //毕业学校
			String department = account.get("department").toString().replaceAll("\"", "");  //专业
			String degree = account.get("degree").toString().replaceAll("\"", "");      //学历(0:专科; 1: 本科; 2: 硕士; 3: 博士; 4: 博士后; 5: 其他; 255:其他)
			
			String description = account.get("description").toString().replaceAll("\"", ""); //教育经历描述
			MaimaiFriendEducations educations = new MaimaiFriendEducations();
			educations.setMm_id(mm_id);
			educations.setTaskid(taskid);
			educations.setFriend_id(friend_id);
			educations.setSchool(school);
			educations.setDepartment(department);
			educations.setDegree(degree);
			educations.setStart_date(start_date);
			educations.setEnd_date(end_date);
			educations.setDescription(description);
			list.add(educations);
		}
		return list;
		
	}		
		
	//脉脉用户朋友的工作经历
	public List<MaimaiFriendWorkExps> friendWorkExps(String html,TaskMaimai taskMaimai){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject acc = object.get("data").getAsJsonObject();
		JsonObject acc2 = acc.get("card").getAsJsonObject();
		String mm_id = acc2.get("mmid").toString().replaceAll("\"", "");       //脉脉账号在脉脉系统中的ID
		JsonObject acc3 = acc.get("mycard").getAsJsonObject();
		String friend_id = acc3.get("mmid").toString().replaceAll("\"", "");   //朋友在脉脉系统中的ID	
		JsonObject acc1 = acc.get("uinfo").getAsJsonObject();
		JsonArray accountCardList1 = acc1.get("work_exp").getAsJsonArray();
		List<MaimaiFriendWorkExps> list = new ArrayList<MaimaiFriendWorkExps>();
		for (JsonElement accd : accountCardList1) {
			JsonObject account = accd.getAsJsonObject();
			String taskid = taskMaimai.getTaskid();
	
			String company = account.get("company").toString().replaceAll("\"", "");     //工作单位
			String position = account.get("position").toString().replaceAll("\"", "");    //工作职务
			String start_date = account.get("start_date").toString().replaceAll("\"", "");  //工作开始时间
			String end_date = account.get("end_date").toString().replaceAll("\"", "");    //工作结束时间(包括年月, 如:2009-7; 如果为空表示至今或用户没有填写;)
			String description = account.get("description").toString().replaceAll("\"", "");   //工作经历描述
			MaimaiFriendWorkExps workExps =new MaimaiFriendWorkExps();
			workExps.setMm_id(mm_id);
			workExps.setTaskid(taskid);
			workExps.setFriend_id(friend_id);
			workExps.setCompany(company);
			workExps.setPosition(position);
			workExps.setStart_date(start_date);
			workExps.setEnd_date(end_date);
			workExps.setDescription(description);
			list.add(workExps);
		}
		return list;
		
	}				
}
