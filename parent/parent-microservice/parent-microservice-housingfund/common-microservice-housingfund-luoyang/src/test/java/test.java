import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String url = "http://www.lyzfgjj.com/login.do?r=0.7171368220006062&username=410302198411050021&password=841105&loginType=4&vertype=1";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		webClient.setJavaScriptTimeout(50000); 
//		webClient.getOptions().setTimeout(50000); // 15->60 
//		webClient.getOptions().setJavaScriptEnabled(false);
//		HtmlPage htmlpage = webClient.getPage(webRequest);
//		String html = htmlpage.getWebResponse().getContentAsString();
//		System.out.println(html);
		String html = "{'success':true,'fieldData':{'totaldepmny':'7168'},'lists':{'datalist':{'gridId':'datalist','start':null,'limit':null,'total':null,'list':[{'acctime':'2017-12-26','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201712','depmny':'256','payemnh':'201712','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-11-27','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201711','depmny':'256','payemnh':'201711','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-10-26','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201710','depmny':'256','payemnh':'201710','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-09-27','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201709','depmny':'256','payemnh':'201709','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-08-24','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201708','depmny':'256','payemnh':'201708','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-07-26','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201707','depmny':'256','payemnh':'201707','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-06-28','bustype':'1','corpdepmny':'128','deptype':'01','remark':'系统生成','paybmnh':'201706','depmny':'256','payemnh':'201706','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-05-27','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201705','depmny':'256','payemnh':'201705','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-05-03','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201704','depmny':'256','payemnh':'201704','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-04-06','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201703','depmny':'256','payemnh':'201703','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-03-06','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201702','depmny':'256','payemnh':'201702','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2017-01-25','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201701','depmny':'256','payemnh':'201701','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-12-23','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201612','depmny':'256','payemnh':'201612','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-12-01','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201611','depmny':'256','payemnh':'201611','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-11-07','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201610','depmny':'256','payemnh':'201610','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-09-28','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201609','depmny':'256','payemnh':'201609','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-08-30','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201608','depmny':'256','payemnh':'201608','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-07-28','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201607','depmny':'256','payemnh':'201607','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-07-05','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201606','depmny':'256','payemnh':'201606','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-06-02','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201605','depmny':'256','payemnh':'201605','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'1','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201604','depmny':'256','payemnh':'201604','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201603','depmny':'256','payemnh':'201603','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201602','depmny':'256','payemnh':'201602','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201601','depmny':'256','payemnh':'201601','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201512','depmny':'256','payemnh':'201512','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201511','depmny':'256','payemnh':'201511','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201510','depmny':'256','payemnh':'201510','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'},"
+"{'acctime':'2016-05-03','bustype':'2','corpdepmny':'128','deptype':'01','remark':'','paybmnh':'201509','depmny':'256','payemnh':'201509','corpname':'河南易才人力资源咨询有限公司洛阳分公司-正式工','corpcode':'022010663218','perdepmny':'128'}]}}}";
		
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		//System.out.println(object);
		JsonObject accountCard = object.get("lists").getAsJsonObject();
		JsonObject accountCard1 = accountCard.get("datalist").getAsJsonObject();
		JsonArray accountCardList = accountCard1.get("list").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			//System.out.println(account);
			String years = account.get("accmnh").toString().replaceAll("\"", "");            //缴存年月
//			String time = account.get("acctime").toString().replaceAll("\"", "");             //入账时间
//		    String companyName = account.get("corpname").toString().replaceAll("\"", "");      //单位名称
//		    String income = account.get("income").toString().replaceAll("\"", "");           //收入(元)
//			String expenditure = account.get("outcome").toString().replaceAll("\"", "");      //支出(元)
//			String balance = account.get("accbal").toString().replaceAll("\"", "");          //当前余额(元)
//		    String type = account.get("remark").toString().replaceAll("\"", "");             //业务类型
			System.out.println(years);
		}
	}

}
