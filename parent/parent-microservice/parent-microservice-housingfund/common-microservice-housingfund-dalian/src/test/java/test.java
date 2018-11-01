import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class test {
	public static String html = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		s();
		System.out.println(html);
//		String ss = "{'data':[{'jylxms':'正常','qsrq':'2016-06-01','jyms':'汇缴','qdlyms':'网站','dffse':'260.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16295.79','jffse':'0.00','jyrq':'2016-06-27','zzrq':'2016-06-01'},{'jylxms':'正常','qsrq':'','jyms':'结息','qdlyms':'','dffse':'261.25','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16557.04','jffse':'0.00','jyrq':'2016-07-01','zzrq':''},{'jylxms':'正常','qsrq':'','jyms':'年度结转','qdlyms':'','dffse':'0.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16557.04','jffse':'0.00','jyrq':'2016-07-01','zzrq':''},{'jylxms':'正常','qsrq':'','jyms':'提取','qdlyms':'','dffse':'0.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'15897.20','jffse':'659.84','jyrq':'2016-07-10','zzrq':''},{'jylxms':'正常','qsrq':'2016-07-01','jyms':'汇缴','qdlyms':'网站','dffse':'306.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16203.20','jffse':'0.00','jyrq':'2016-07-27','zzrq':'2016-07-01'},{'jylxms':'正常','qsrq':'','jyms':'提取','qdlyms':'','dffse':'0.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'15835.43','jffse':'367.77','jyrq':'2016-08-10','zzrq':''},{'jylxms':'正常','qsrq':'2016-08-01','jyms':'汇缴','qdlyms':'网站','dffse':'306.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16141.43','jffse':'0.00','jyrq':'2016-08-24','zzrq':'2016-08-01'},{'jylxms':'正常','qsrq':'','jyms':'提取','qdlyms':'','dffse':'0.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'15773.66','jffse':'367.77','jyrq':'2016-09-10','zzrq':''},{'jylxms':'正常','qsrq':'2016-09-01','jyms':'汇缴','qdlyms':'网站','dffse':'306.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'16079.66','jffse':'0.00','jyrq':'2016-09-23','zzrq':'2016-09-01'},{'jylxms':'正常','qsrq':'','jyms':'提取','qdlyms':'','dffse':'0.00','dwmc1':'大连易才人力资源顾问有限公司-(10%,10%)','yue':'15711.89','jffse':'367.77','jyrq':'2016-10-10','zzrq':''}],'pageInfo':{'pageSize':10,'pageNum':1,'totalRowNum':40,'totalPageNum':4,'startRowNum':1,'endRowNum':1},'exception':null}";
////		String rowNum = ss.substring(ss.indexOf("totalRowNum")+13);
////		String totalRowNum = rowNum.substring(0,rowNum.indexOf(","));
////		String pageNum = ss.substring(ss.indexOf("totalPageNum")+14);
////		String totalPageNum = pageNum.substring(0,pageNum.indexOf(","));
////		System.out.println(totalRowNum);
////		System.out.println(totalPageNum);
//		
//		JsonParser parser = new JsonParser();
//		JsonObject object = (JsonObject) parser.parse(ss); // 创建JsonObject对象
//		JsonArray accountCardList = object.get("data").getAsJsonArray();
//		for (JsonElement acc : accountCardList) {
//			JsonObject account = acc.getAsJsonObject();
//			String jyrq = account.get("jyrq").toString().replaceAll("\"", "");
//			String jyms = account.get("jyms").toString().replaceAll("\"", "");
//			String jffse = account.get("jffse").toString().replaceAll("\"", "");
//			String dffse = account.get("dffse").toString().replaceAll("\"", "");
//			String yue = account.get("yue").toString().replaceAll("\"", "");
//			String dwmc1 = account.get("dwmc1").toString().replaceAll("\"", "");
//			String qsrq = account.get("qsrq").toString().replaceAll("\"", "");
//			String zzrq = account.get("zzrq").toString().replaceAll("\"", "");
//			String jylxms = account.get("jylxms").toString().replaceAll("\"", "");
//			String qdlyms = account.get("qdlyms").toString().replaceAll("\"", "");
//			System.out.println(jyrq);
//			System.out.println(jyms);
//			System.out.println(jffse);
//			System.out.println(dffse);
//			System.out.println(yue);
//			System.out.println(dwmc1);
//			System.out.println(qsrq);
//			System.out.println(zzrq);
//			System.out.println(jylxms);
//			System.out.println(qdlyms);
//		}
	}
	
	public static void s(){
		html="qqqq";
	}

}
