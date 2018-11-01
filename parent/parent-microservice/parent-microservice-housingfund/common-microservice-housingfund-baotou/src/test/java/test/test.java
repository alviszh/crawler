package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String html = "{'status':1, 'msg':'公积金查询成功','strHtml':'<tr><td class='tdleft'>姓名：</td><td class='tdright' id='persion_name'>郭瑞芬</td><td class='tdleft'>账号：</td><td class='tdright' id='persion_id'>02476429</td></tr><tr><td class='tdleft'>单位名称：</td><td class='tdright' id='unit_name'>北京易才人力资源顾问有限公司包头分公司</td><td class='tdleft'>单位账号：</td><td class='tdright' id='unit_account'>2010705433062   </td></tr><tr><td class='tdleft'>月缴存额：</td><td class='tdright' id='all_turnnum'>362.00</td><td class='tdleft'>缴存至：</td><td class='tdright' id='last_turndate'>2018-04   </td></tr><tr><td class='tdleft'>缴存基数：</td><td class='tdright' id='persion_wagebase'>1640.00</td><td class='tdleft'>公积金余额：</td><td class='tdright' id='Balance'>14938.22</td></tr><tr><td class='tdleft'>缴存比例：</td><td class='tdright' id='jcbl'>11.00%</td><td class='tdleft'>帐号状态：</td><td class='tdright' id='accstatus_name'>正常</td></tr><tr><td class='tdleft'>利息：</td><td class='tdright' id='interest' colspan='3'>136.76</td></tr><tr><td colspan='4' style='height:10px; overflow:hidden; background-color:#FFFFFF'></td></tr>'}";
		html = html.substring(html.indexOf("<"), html.lastIndexOf(">")+1);
		html = "<html><head></head><body><table>"+html+"</table></body></html>";
		System.out.println(html);
		Document doc = Jsoup.parse(html);
		System.out.println(doc);
		Elements ele = doc.select("td.tdright");
		System.out.println(ele.size());
		if (ele.size()>0){
			for(int i= 0;i<ele.size();i++){
				String s = ele.get(i).text().trim();
				System.out.println(s);
			}
		}
	}

}
