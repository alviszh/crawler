package app.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParser {
	
	public static void main(String[] args) throws Exception {
//		String path = "D:\\app\\message.txt";
//		String callHtml = readText(path);
//		parser(callHtml);
		
		String str = "张振";
		System.out.println(URLEncoder.encode(str, "gb2312"));
		
//		String aaa = "<script>funcFmtTime(\"70\")</script>";
//		System.out.println(aaa.substring(aaa.indexOf("(")+2, aaa.indexOf(")")-1));
		
		
	}

	private static void parser(String callHtml) {
		Document doc = Jsoup.parse(callHtml);
		Element div = doc.getElementById("Pzone_details_content_2");
		Elements trs = div.select("tr");
		if(null != trs && trs.size()>0){
			for(int i=1;i<trs.size();i++){
				if(trs.get(i).select("td").size()>8){
					if(i==1){
						continue;
					}
					String otherNum = trs.get(i).child(1).text();
					String businessType = trs.get(i).child(2).text();
					String beginTime = trs.get(i).child(3).text();
					String fee = getParam(trs.get(i).child(4));
					String remission = getParam(trs.get(i).child(5));
					String totalFee = getParam(trs.get(i).child(6));

					
//					System.out.println("******************************************"+i);
//					System.out.println(otherNum);
//					System.out.println(callType);
//					System.out.println(beginTime);
//					System.out.println(callDuriation);
//					System.out.println(calledPartyVisitedCity);
//					System.out.println(callType1);
										
				}
			}			
		}
	}
	
	private static String getParam(Element e){
		Element script = e.getElementsByTag("script").first();
//		System.out.println(script.toString());
		String str = script.toString().substring(script.toString().indexOf("(")+2, script.toString().indexOf(")")-1);
		System.out.println(str);
		return str;
		
	}

	private static String readText(String path) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        byte[] bys = new byte[1024];
        int len = 0;
        String html = "";
        while ((len = bis.read(bys)) != -1) {
            String str = new String(bys, 0, len);
            html = html+str;
        }
        bis.close();
        return html;
	}

}
