package app.service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;

/**
 * 
 * 项目名称：common-microservice-search 类名称：SearchFutureService 类描述： 创建人：hyx
 * 创建时间：2018年1月18日 上午10:53:02
 * 
 * @version
 */

@Component
public class SearchUnitService {

	@Autowired
	private TracerLog tracerLog;

	@Value("${jobs.sensitive}")
	private String sensitive;

	private String[] sensitiveStrings;

	@Autowired
	public SearchUnitService() {
		
		System.out.println(sensitive);


	}

	
	/**   
	  *    
	  * 项目名称：common-microservice-honesty-search  
	  * 所属包名：app.service
	  * 类描述：   匹配敏感词
	  * 创建人：hyx 
	  * 创建时间：2018年10月18日 
	  * @version 1  
	  * 返回值    String
	  */
	public Set<String> matchSensitive(String txt) {
		
		if(sensitiveStrings == null || sensitiveStrings.length<=0){
			try {
				sensitive = new String(sensitive.getBytes("ISO-8859-1"),"utf-8");
				//* 直接从git上获取的敏感词中文会乱码，需要转码
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sensitiveStrings = sensitive.split(",");

//			for (String sensitiveString : sensitiveStrings) {
//				tracerLog.output("sensitiveString", sensitiveString);
//			}
		}
	

		Set<String> matchedList = new  HashSet<String>();
		for (String sensitiveString : sensitiveStrings) {
			if (txt.indexOf(sensitiveString) != -1) {

				tracerLog.output("sensitiveString matched ", sensitiveString);
				matchedList.add(sensitiveString);
			}
		}

		return matchedList;
	}

	public static void main(String[] args) {

		String sensitive = " 骗贷,诈骗,骗子,催收,赌,毒,瘾君子,校园贷,失联,中介,分期,小贷,放贷,首付,按揭,下款,抵押,质押,拿钱,全款,利息,利率,高利贷,黑社会,借贷,急用钱,现金,额度,理财,涉嫌,小额,P2P,互联网金融,信贷,逾期,网贷,失信,金融,传销,聚众,伤害,杀人,斗殴,违约,低息,集资,众筹,秒批,出让,出售,纠纷,资产,贷款,联系,担保,投资,放款,信用,借款,套现,保险,借贷,违法,犯罪,被告,原告,老赖,黑名单,纳税,拖欠,恶意,违,偷,执行人,结案,非法,爪子,涉黄,头息,海洛因,K粉,冰毒,摇头丸,口子,黑户,借钱,小袋,融资,宜人贷,宜信,平安易贷,小牛,恒昌,你我贷,人人贷,信而富";

		String[] sensitiveStrings = sensitive.trim().split(",");

		for (String sensitiveString : sensitiveStrings) {

			System.out.println("==============" + sensitiveString.trim());
		}
		
		
		Set<String> matchedList = new  HashSet<String>();
		
		String txt = "信和财富 骗贷 黄赌毒 骗贷";

		for (String sensitiveString : sensitiveStrings) {
			if (txt.indexOf(sensitiveString) != -1) {

				System.out.println("sensitiveString matched :"+sensitiveString);
				matchedList.add(sensitiveString);
			}
		}
		
		System.out.println("============="+matchedList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

	}

}
