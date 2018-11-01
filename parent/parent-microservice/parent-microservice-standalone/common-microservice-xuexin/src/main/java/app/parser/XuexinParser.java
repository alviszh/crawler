package app.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.xuexin.XuexinEducationInfo;
import com.microservice.dao.entity.crawler.xuexin.XuexinSchoolInfo;
import com.microservice.dao.repository.crawler.xuexin.XuexinEducationInfoRepository;
import com.microservice.dao.repository.crawler.xuexin.XuexinSchoolInfoRepository;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class XuexinParser {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	protected XuexinEducationInfoRepository xuexinEducationInfoRepository;
	
	@Autowired
	protected XuexinSchoolInfoRepository xuexinSchoolInfoRepository;
 
	public void getXuexinSchoolInfo(String html, String taskid) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(html);
			String payLog = jsonObj.getString("words_result");
			JSONArray jsonArray = JSONArray.fromObject(payLog);
			String name = getValue(jsonArray, 0,"姓名");
			String gender = getValue(jsonArray, 1,"性别");
			String birthday = getValue(jsonArray, 2,"出生日期");
			String nation = getValue(jsonArray, 3,"民族");
			String idnumber = getValue(jsonArray, 4,"证件号码");
			String school_name = getValue(jsonArray, 5,"学校名称");
			String level = getValue(jsonArray, 6,"层次");
			String major = getValue(jsonArray, 7,"专业");
			String length_schooling = getValue(jsonArray, 8,"学制");
			String education_category = getValue(jsonArray, 9,"学历类别");
			String study_form = getValue(jsonArray, 10,"学习形式");
			String branch = getValue(jsonArray, 11,"分院");
			String system_place = getValue(jsonArray, 12,"系(所、函授站)");
			String class_place = getValue(jsonArray, 13,"班级");
			String study_num = getValue(jsonArray, 14,"学号");
			String entrance_date = getValue(jsonArray, 15,"入学日期");
			String leave_school_date = getValue(jsonArray, 16,"离校日期");
			String status = getValue(jsonArray, 17,"学籍状态");

			XuexinSchoolInfo xuexinSchoolInfo = new XuexinSchoolInfo(taskid, name, gender, birthday, nation, idnumber,
					school_name, level, major, length_schooling, education_category, study_form, branch, system_place,
					class_place, study_num, entrance_date, leave_school_date, status);
			xuexinSchoolInfoRepository.save(xuexinSchoolInfo);
			
		} catch (Exception e) {
			tracer.addTag("getXuexinSchoolInfo.error", e.getMessage());
		}

	}
	
	public void getXuexinEducationInfo(String html, String taskid) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(html);
			String payLog = jsonObj.getString("words_result");
			JSONArray jsonArray = JSONArray.fromObject(payLog);
			String name = getValue(jsonArray, 0,"姓名");
			String gender = getValue(jsonArray, 1,"性别");
			String birthday = getValue(jsonArray, 2,"出生日期");
			String entrance_date = getValue(jsonArray, 3,"入学日期");
			String graduation_date = getValue(jsonArray, 4,"毕(结)业日期");
			String school_name = getValue(jsonArray, 5,"学校名称");
			String major = getValue(jsonArray, 6,"专业");
			String education_category = getValue(jsonArray, 7,"学历类别");
			String length_schooling = getValue(jsonArray, 8,"学制");
			String study_form = getValue(jsonArray, 9,"学习形式");
			String level = getValue(jsonArray, 10,"层次");
			String graduation = getValue(jsonArray, 11,"毕(结)业");
			String dean = getValue(jsonArray, 12,"校(院)长姓名");
			String certificate_num = getValue(jsonArray, 13,"证书编号");

			XuexinEducationInfo xuexinEducationInfo = new XuexinEducationInfo(taskid, name, gender, birthday, entrance_date,
					graduation_date, school_name, major, education_category, length_schooling, study_form, level,
					graduation, dean, certificate_num);
			xuexinEducationInfoRepository.save(xuexinEducationInfo);
		} catch (Exception e) {
			tracer.addTag("getXuexinEducationInfo.error", e.getMessage());
		}

	}
	public String getValue(JSONArray jsonArray, int i,String key) {
		String string = JSONObject.fromObject(jsonArray.get(i)).getString("words");
		if(string.contains(key)){
			string = string.replace(key, "");
		}
		if (string.contains(":")) {
			string = string.substring(string.lastIndexOf(":") + 1);
		}
		return string;
	}

}
