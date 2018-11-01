import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class test {

	public static void main(String[] args) {
//		Calendar now = Calendar.getInstance();
//		int endNian = now.get(Calendar.YEAR);
//		int beginNian = endNian-20;
//		int yue = now.get(Calendar.MONTH) +1;
//		int ri = now.get(Calendar.DAY_OF_MONTH);
//		String yu = null;
//		String r = null;
//		if (yue>9){
//			yu = String.valueOf(yue);
//		}else{
//			yu = "0"+yue;
//		}
//		if (ri>9){
//			r = String.valueOf(ri);
//		}else{
//			r = "0"+ri;
//		}
//		String endTime = String.valueOf(endNian)+ yu + r;
//		String beginTime = beginNian +"0101";
//		System.out.println(endTime);
//		System.out.println(beginTime);
		String html = "{'TranCode':'119903','TellCode':'9999','recode':'000000','BrcCode':'08760001','MTimeStamp':'095437','TranDate':'2018-02-27','TranIP':'','BusiSeq':'4904','totalnum':'88','totalpage':'9','msg':'交易处理成功...','AuthCode2':'','AuthCode1':'','ChannelSeq':'0','STimeStamp':'095437','AuthCode3':'','results':[{'seqno':' 1','xingming':'姜寿菊','bz':'汇缴[2018-02]','ywlxmc':'汇缴','fse':'1414.00','zhye':'26772.41','grzh':'532000144080','jbgy':'艾静','gjhtqywlx':'01 ','jzrq':'20180213'},{'seqno':' 2','xingming':'姜寿菊','bz':'汇缴[2018-01]','ywlxmc':'汇缴','fse':'1414.00','zhye':'25358.41','grzh':'532000144080','jbgy':'文庆','gjhtqywlx':'01 ','jzrq':'20180125'},{'seqno':' 3','xingming':'姜寿菊','bz':'汇缴[2017-12]','ywlxmc':'汇缴','fse':'1414.00','zhye':'23944.41','grzh':'532000144080','jbgy':'文庆','gjhtqywlx':'01 ','jzrq':'20171205'},{'seqno':' 4','xingming':'姜寿菊','bz':'汇缴[2017-11]','ywlxmc':'汇缴','fse':'1414.00','zhye':'22530.41','grzh':'532000144080','jbgy':'文庆','gjhtqywlx':'01 ','jzrq':'20171121'},{'seqno':' 5','xingming':'姜寿菊','bz':'汇缴[2017-10]','ywlxmc':'汇缴','fse':'1414.00','zhye':'21116.41','grzh':'532000144080','jbgy':'艾静','gjhtqywlx':'01 ','jzrq':'20171026'},{'seqno':' 6','xingming':'姜寿菊','bz':'汇缴[2017-09]','ywlxmc':'汇缴','fse':'1414.00','zhye':'19702.41','grzh':'532000144080','jbgy':'艾静','gjhtqywlx':'01 ','jzrq':'20170922'},{'seqno':' 7','xingming':'姜寿菊','bz':'汇缴[2017-08]','ywlxmc':'汇缴','fse':'1414.00','zhye':'18288.41','grzh':'532000144080','jbgy':'艾静','gjhtqywlx':'01 ','jzrq':'20170818'},{'seqno':' 8','xingming':'姜寿菊','bz':'汇缴[2017-07]','ywlxmc':'汇缴','fse':'1414.00','zhye':'16874.41','grzh':'532000144080','jbgy':'文庆','gjhtqywlx':'01 ','jzrq':'20170726'},{'seqno':' 9','xingming':'姜寿菊','bz':'变更前基数[4533.33], 变更后基数[5894.00]','ywlxmc':'基数调整','fse':'0.00','zhye':'15460.41','grzh':'532000144080','jbgy':'文庆','gjhtqywlx':'10 ','jzrq':'20170710'},{'seqno':' 10','xingming':'姜寿菊','bz':'年度结息: 775.67','ywlxmc':'年终结息','fse':'775.67','zhye':'15460.41','grzh':'532000144080','jbgy':'张丽琴','gjhtqywlx':'09 ','jzrq':'20170630'}],'TranChannel':'4','filename':'2018-02-27/downfile/dpprt102.4904','NoteMsg':'','AuthFlag':'0','TranSeq':'4904','ChkCode':''}";
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("results").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			System.out.println(account);
		}
	}

}
