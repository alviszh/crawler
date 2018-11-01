package app.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaDebitCardDeadline;

import app.commontracerlog.TracerLog;


@Component
public class CebChainParser {
	@Autowired 
	private TracerLog tracerLog;

	public List<CebChinaDebitCardDeadline> getDeadline(String html5, String taskid) {
		List<CebChinaDebitCardDeadline> list = new ArrayList<CebChinaDebitCardDeadline>();
		try {

			Document doc = Jsoup.parse(html5);
			System.out.println(doc.toString());
			Elements elementsByClass = doc.getElementsByClass("td2");
			for (int i=0;i<elementsByClass.size();i++) {
				CebChinaDebitCardDeadline cebChinaDebitCardDeadline = null;
				Element element = elementsByClass.get(i);
				Elements tag = element.getElementsByTag("td");
				String leixing = tag.get(0).text().trim();//类型
				String bizhong = tag.get(1).text().trim();//币种
				String qianleixing = tag.get(2).text();//钞汇标志
				String qixiang = tag.get(3).text().trim();//期限
				String kaihu = tag.get(4).text().trim();//开户日期
				String quhu = tag.get(5).text().trim();//到期日期
				String text = tag.get(6).text().trim();
				String text2 = tag.get(7).text().trim();
				String ztai = "";
				if(elementsByClass.size()>11){
					ztai = tag.get(12).text().trim();//状态
				}
				cebChinaDebitCardDeadline=new CebChinaDebitCardDeadline(
						taskid,leixing,bizhong,qianleixing,qixiang,kaihu,quhu,text,text2,ztai);
				list.add(cebChinaDebitCardDeadline);
			}
			return list;
		} catch (Exception e) {
			tracerLog.output("parser-getDeadline", e.toString());
			return null;
		}

	}

	
	

}
