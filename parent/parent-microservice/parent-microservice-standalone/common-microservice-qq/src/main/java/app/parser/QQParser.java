package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.qq.QQFriend;
import com.microservice.dao.entity.crawler.qq.QQMessage;
import com.microservice.dao.entity.crawler.qq.QQqun;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class QQParser {

	public List<QQqun> getqqqun(String htmls, String taskid) {
		List<QQqun> list = new ArrayList<>();
		String[] split = htmls.split("_Callback");
		String[] split2 = split[1].split("</body></html>");
		split2[0] = split2[0].trim();
		String html = split[1].substring(1, split2[0].length()-2);
		JSONObject fromObject = JSONObject.fromObject(html.trim());
		JSONArray jsonArray = fromObject.getJSONObject("data").getJSONArray("group");
		for(int i=0;i<jsonArray.size();i++){
			QQqun qun = new QQqun();
			String groupcode = jsonArray.getJSONObject(i).getString("groupcode");//群号
			qun.setGroupcode(groupcode);
			String groupname = jsonArray.getJSONObject(i).getString("groupname");//群名
			qun.setGroupname(groupname);
			String total_member = jsonArray.getJSONObject(i).getString("total_member");//群人数
			qun.setTotal_member(total_member);
			String notfriends = jsonArray.getJSONObject(i).getString("notfriends");//不是好友的数量
			qun.setNotfriends(notfriends);
			qun.setTaskid(taskid);
			System.out.println("\b\r群号："+groupcode+"\b\r群名："+groupname+"\b\r群人数："+total_member);
			list.add(qun);
		}
		
		return list;
	}

	public List<QQFriend> getfriend(String taskid, String html2) {
		List<QQFriend> list = new ArrayList<>();
		String[] split3 = html2.split("_Callback");
		String[] split4 = split3[1].split("</body></html>");
		split4[0] = split4[0].trim();
		String html3 = split3[1].substring(1, split4[0].length()-2);
		JSONObject fromObject3 = JSONObject.fromObject(html3.trim());
		System.out.println(fromObject3);
		JSONObject fromObject2 = JSONObject.fromObject(html3.trim());
		JSONArray jsonArray2 = fromObject2.getJSONArray("items");
		JSONArray jsonArray3 = fromObject2.getJSONArray("gpnames");
		for(int k=0;k<jsonArray2.size();k++){
			QQFriend qQFriend = new QQFriend();
			String uin=jsonArray2.getJSONObject(k).getString("uin");//qq号
			qQFriend.setUin(uin);
			String name=jsonArray2.getJSONObject(k).getString("name");//网名
			qQFriend.setName(name);
			String yellow = jsonArray2.getJSONObject(k).getString("yellow");//黄钻等级
			qQFriend.setYellow(yellow);
//			String score=jsonArray2.getJSONObject(k).getString("score");//空间好感度
//			qQFriend.setScore(score);
			String img=jsonArray2.getJSONObject(k).getString("img");//qq头像
			qQFriend.setImg(img);
			String is_special = jsonArray2.getJSONObject(k).getString("is_special");//特别关心
			qQFriend.setIs_special(is_special);
			String is_xy = jsonArray2.getJSONObject(k).getString("is_xy");//是否是校友
			qQFriend.setIs_xy(is_xy);
			String xyname = jsonArray2.getJSONObject(k).getString("xyname");//校友名
			qQFriend.setXyname(xyname);
			String xyurl = jsonArray2.getJSONObject(k).getString("xyurl");//校友主页
			qQFriend.setXyurl(xyurl);
			String realname = jsonArray2.getJSONObject(k).getString("realname");//备注
			qQFriend.setRealname(realname);
			String groupid = jsonArray2.getJSONObject(k).getString("groupid");//分组
			qQFriend.setGroupid(groupid);
			for (int i = 0; i < jsonArray3.size(); i++) {
				String gpid = jsonArray3.getJSONObject(i).getString("gpid");
				if(gpid.equals(groupid)){
					String gpname = jsonArray3.getJSONObject(i).getString("gpname");//分组名
					qQFriend.setGpname(gpname);
				}
			}
			qQFriend.setTaskid(taskid);
			list.add(qQFriend);
		}
		return list;
	}

	public QQMessage getuser(String taskid, String html2) {
		String[] split = html2.split("_Callback");
		String[] split2 = split[1].split("</pre></body></html>");
		split2[0] = split2[0].trim();
		String substring = split2[0].substring(1, split2[0].length()-2);
		System.out.println(substring);
		JSONObject fromObject = JSONObject.fromObject(substring);
		System.out.println(fromObject);
		String uin = fromObject.getJSONObject("data").getString("uin");//QQ号
		String age = fromObject.getJSONObject("data").getString("age");//年龄
		String spacename = fromObject.getJSONObject("data").getString("spacename");//空间名
		String signature = fromObject.getJSONObject("data").getString("signature");//签名
		String nickname = fromObject.getJSONObject("data").getString("nickname");//网名
		String sex = fromObject.getJSONObject("data").getString("sex");//性别 1男
		String constellation = fromObject.getJSONObject("data").getString("constellation");//星座 4狮子
		String avatar = fromObject.getJSONObject("data").getString("avatar");//头像
		String birthyear = fromObject.getJSONObject("data").getString("birthyear");//出生年
		String birthday = fromObject.getJSONObject("data").getString("birthday");//出生日
		String country = fromObject.getJSONObject("data").getString("country");//国家
		String province = fromObject.getJSONObject("data").getString("province");//省
		String city = fromObject.getJSONObject("data").getString("city");//城市
		String bloodtype = fromObject.getJSONObject("data").getString("bloodtype");//血型  3：O
		String marriage = fromObject.getJSONObject("data").getString("marriage");//结婚  4：恋爱中
		String hco = fromObject.getJSONObject("data").getString("hco");//故乡
		String cco = fromObject.getJSONObject("data").getString("cco");//
		String cp = fromObject.getJSONObject("data").getString("cp");//        公司所在地
		String cc = fromObject.getJSONObject("data").getString("cc");//
		String cb = fromObject.getJSONObject("data").getString("cb");//
		String lover = fromObject.getJSONObject("data").getString("lover");//爱好
		QQMessage user = new QQMessage(taskid, uin, age, spacename, signature, 
				nickname, sex, constellation, avatar, birthyear, birthday, 
				country, province, city, bloodtype, marriage, hco, cco, cp, cc, cb, lover);
		return user;
		
	}

	
}
