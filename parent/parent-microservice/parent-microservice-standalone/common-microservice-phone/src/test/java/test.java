import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.entity.crawler.telecom.phone.quhao.AreaPhone;
import com.module.htmlunit.WebCrawler;

//import net.sf.json.JSONArray;

public class test {
	
	public static void main(String[] args) throws Exception {
		String a = "075588321613";
		String b = "0755883216138039";
		if(a.contains(b)){
			System.out.println("aaaa");
		}else{
			System.out.println("bbbb");
		}
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
//		String url1 = "https://haoma.baidu.com/user_code_info?cid=1574cb49389978745jje4581cb9f9546eb1f";
//		WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.GET);
//		Page searchPage2= webClient.getPage(webRequest2);
//		String html = searchPage2.getWebResponse().getContentAsString();
//		System.err.println(html);
//		String s = "17910、17911;';:";
//		Pattern p = Pattern.compile("[^0-9]");        //得到字符串中的数字
//        Matcher m = p.matcher(s);
//        String repickStr = m.replaceAll("");
//        System.out.println(repickStr);
//		Date currentTime = new Date();
//	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    String dateString = formatter.format(currentTime);
//	    System.out.println(dateString);
		// TODO Auto-generated method stub  
//		String n = "广告,骚扰,诈骗,金融,中介,宅急送";
//		String markType = "骚扰";
//		if(n.contains(markType)){
//			System.out.println("sssssssssssss");
//		}else{
//			System.out.println("meiyou");
//		}
//		String name = "å¹¿å,éªæ°,è¯éª,éè,ä¸­ä»,å®æ¥é"; 
//		  
//		        try { 
//		             byte[] iso8859 = name.getBytes("ISO-8859-1"); 
//		             String n = new String(iso8859,"ISO-8859-1"); 
//		             n = new String(iso8859,"UTF-8"); 
//		             System.out.println(n);
//	                 
//		        } catch (Exception e) { 
//		            e.printStackTrace(); 
//		        } 
//		
//        String regEx = "[a-zA-Z\u4e00-\u9fa5]";      
//        String str = "中文fdas313afasfs42342 ";      
//        Pattern p = Pattern.compile(regEx);      
//        Matcher m = p.matcher(str);      //p.matcher()只适合做
//        String repickStr = m.replaceAll("");
//        System.out.println(repickStr);  		
//        if(m.find()){
//        	System.out.println("有汉字 ");  		   
//        }
//		WebDriver webDriver = openloginCmbChina(); 
//		webDriver.get("https://haoma.baidu.com/query");
//		Thread.sleep(10000);
//		System.err.println(webDriver.getPageSource());
		
//        String ss = "18682465782,03912105425,01057694422,17086644023,18695681030,05923303596,059522089604,05923598396,01059265588,02038573291,02759526205,05922979497,051482043155,059522669344,05923603811,15330231280,051262973783,18556518621,02866281012,055164924123,055164924149,17091997771,051566660068,051587014402,18929354390,05468250859,15130072347,13352020631,05923291492,02568905258,05923291493,037122066567,05923291520,05923734256,02582292348,02986692365,07452522513,051668116808,059528394072,02988896188,037960187508,051681009909,18136290580,15673899876,18042018194,15149059191,057156383106,13357795023,18042033081,08733152206,02083251510,01058712013,17187263438,02131029854,02083670390,4001122016,05923781518,037156503950,045157782084,037156503988,17032657023,037156503989,051268321158,02180374884,05923294232,13059138562,08733152277,037156809800,18142029781,17714416729,18143438173,05923294356,13223050947,079187797249,13316476072,053155519055,05722795406,03525330581,05923574313,09168191001,01057954096,13585312735,05726237555,09173014624,13025922857,02061005651,13237972344,02151324391,13027699007,02061014091,15710402932,059188614921,13270388172,03562210944,05966019112,01056349080,18115341782,02968922858,02968951082,05966019138,055166039922,03737126596,18118772453,051280806658,01056988357,18972290325,01058166208,01088631097,07579506596,05926586047,05532202594,05922064673,07582358282,02062341461,02151858216,02361170579,051068118616,02381697124,15313298043,85237246442,02062733566,01053958084,05972553624,05923597035,02180356150,05925535543,02961856076,05998230163,07716411745,057983502600,01057352473,037963001635,18124023216,031180973781,057984896070,02062934547,057456624044,15381077535,02151127887,02868654034,057126208413,18932137688,18072759225,01058534072,01058534100,051267122925,02161834557,057186990909,18105918905,037156851972,18957906908,07525715014,01059720427,18059815682,02151352096,076938943892,18887181564,051289885897,02759707295,03706388883,18912369779,18001589059,18626888067,055168128441,09911163239,01058364301,057427654724,15550059595,02126122930,057428554473,057456152127,18123673282,079187815252,05362256659,079187815724,059522651914,02759598916,13503813274,037158501599,079182117284,02968811087,15929351300,07198458683,15979172360,079183637502,079187820346,05395320670,079188244854,073188199004,02120703478,02160562780,15137266898,15595310573,18070390558,037122701140,01056761108,07392296008,01064477117,01050801804,051267121048,051780101808,057156570005,18604033843,05923521037,02133190351,05923291864,15516929515,057189265116,13266780175,18109248551,037156677208,02751257161,15620125452,089866737129,02131296268,079187803094,02066305462,13608439668,18626857330,02166194248,05966019110,02151366377,05966019137,037122050505,037189920535,079187803420,051380217367,05972530806,037768082848,18437386123,073183084009,05923584616,053258689406,02363465129,15771937183,085188171185,02961855573,05996101081,13391416595,051211831212,05782658545,037963001560,05573905019,18124500541,051282195035,18017145097,17085737007,03912105427,13321837606,13078864791,05332329950,13247387625,18124530750,18158564120,17085737227,13980343186,13321970837,05923303572,02062995566,14792307884,15673060452,02066209611,051282261367,073183094158,18160010125,02133370406,059162039420,02037024834,17195441398,02087511940,05923303600,15375924409,15573477390,02037510872,15321213431,13248312056,18697328430,13148119776,02038209882,17195442689,18163753937,15377921034,15779827927,02868525651,15029289969,15573565231,02120503569,13398096913,18698133432,13148137573,073188040171,17195443166,15577246191,02160154923,05922575403,02759568457,13249052283,02868654010,13152507293,17195443167,18182160720,15388102363,15879851449,02151218142,15577246813,15321521262,13250516815,087163911000,02868654035,18712788473,13152693187,17195443263,18184970030,02868654036,15577339694,01056844676,087164983169,17195443264,02965620076,15910861082,02868654231,15577602712,07176720112,02160266308,05923274073,02965620199,037156852176,087164989162,02866208532,17195443265,02968793407,01056871481,15924575493,02751905237,15578640521,01056871482,17088607981,05923274075,13036934443,051482043261,18914251131,02752102639,087164989187,13161824614,02160426789,17195445306,18124553968,037160337134,18017857203,15578997296,07177256982,13288256773,05923274099,18914265756,13189172934,18556526067,13161829485,17195445307,18124596108,055164924136,17091801687,15335702805,13189651907,02866454164,17195445308,037165375441,04000331315,059183057439,08733059015,13105436426,18124695535,125909888711,079187820106,17092900134,01058143854,04000518648,13301519849,059183076501,13044219275,13192144271,08733059016,13106541240,18126048153,051286852321,075533248577,079187820107,05923307673,15392033475,17092900606,04000806003,13301954467,15095507838,13044225503,051587014401,13193168317,02066636439,08733059060,13108967838,13250530184,04000921218,13002019171,075533549367,18184970473,073158399159,055164924230,17092900996,04006573351,13302464696,13044258372,01085922042,13193168583,08733059138,051262975390,13110268872,02131653019,13256492290,18127066692,13002085326,01085922043,05923308051,18184970474,15394237301,073158630320,17093343136,13305824142,13193168683,13110269152,4000939538,18127071136,051287650558,01085924130,01060219199,17093343180,13306171495,01085924131,18932376274,13205120181,18127077621,05923308053,18190855801,059522870295,02061840266,02131776137,17093343509,059186214073,02160429475,02080737083,02061849047,076022641044,13005138527,051288178109,073188842541,02080737084,18193710195,15397201727,05923291323,13005160086,17195452301,18193710569,02180312725,15397255870,059523149790,059186294185,02151792971,15338826360,02180314047,13113323997,02122854282,17195456748,01064101199,05923308079,18194244093,02062100518,13352979306,15338860537,02062119129,01053235258,055163725394,13262754690,17195456749,17004821502,05923734058,01053708275,13355483527,01053708276,13262754769,18753047008,17195457446,17004822446,01056599855,13356927332,13044284281,13263163530,07386508862,17195457447,02180342810,17031123102,05923750435,073158630623,01056817596,02759209260,01056824128,02867730662,037122734622,076028141436,08733059184,051262975440,01056824129,17195458143,079187650058,02867730663,01056840450,17031123326,15602765935,073158630730,055164924483,13306171743,05923291546,13048037596,02867756985,037153606165,18105607002,037960107731,17195463227,02867816873,17031123394,15607224521,073158631065,17095073538,037960151107,13306172442,02759261011,05923291590,15346497915,13048707005,13206071128,076028149352,08733059211,037960151391,17195464518,079187650348,17032170168,18025425059,17096057481,037960151560,13306181946,15347083420,02867940750,13206085798,08733059235,037960160631,17196317948,02867940751,17032170265,031168041400,055164924545,17096057732,01057132705,13306188241,01057233182,037960186387,18130035052,000001901212,02867942295,000190852213,17032170417,01010105757,055164924590,17097748166,13306195481,15350125490,13207724394,08733059282,13115181814,01050829099,05925753748,075561828157,18034627181,05923308548,073181893481,17097935261,01057320449,059186345431,18952751037,08733059359,18598247621,13117121842,079187768757,05925756284,075561918561,15506741767,17097935338,18953540155,13211390045,08733059457,18603461834,13117121843,13263717884,075561956471,15506774886,055164925579,17098073078,13311956367,059187277609,15151931745,13059129552,18953555947,13360050397,13265019357,18138473849,18042400946,15506868327,0196852213,17183151197,18954301050,02131838311,13121624049,13265061294,18140759506,18802978568,15676393057,01058534818,18042592510,15513014013,17187112835,02083256381,18954555974,13214205552,01058645229,043180887393,13265386885,076028149365,02385371468,15676712191,15513099418,059528584316,15188158263,13362597436,076028149414,13018545484,18821791028,02759278546,15676724853,07502384343,059568366356,15191685073,055163726817,13265915421,15678943465,17032170463,15191855787,02180361825,13366727898,02131223892,13122745897,13266062817,02388796111,07507841018,05923531656,15521047159,17032331792,02155368774,02389165202,059187894042,02180370461,13122745970,18106045769,02180370462,02759420824,07507841063,17370073186,15524732429,01056572586,17032331891,02961875633,02155690600,076028184525,18829597381,15683957587,15525420225,057456634230,02961895196,05923844831,13059133982,17394897271,075561984546,057582710431,18109370685,051280660644,05962108074,17057692152,13316421968,05923294257,13062319192,076923168114,02965620074,17712110241,05962135154,17057693293,075566845624,03912106540,18051594875,13522515737,02965620075,4007078199,13316422986,05923294258,18112325104,08733152693,17058506832,18052712494,13316425921,03128414020,15359704282,17753808911,18143800297,03128414044,17058506857,01057719364,057587099497,01057719365,03938639085,18114518126,09133366810,18143800298,17058523414,01057813501,055166017432,13316511152,059187894105,13068301036,18961705049,01057814003,079187803082,18143830411,03955658947,13573613509,059187894980,13068339570,18961705442,13229481573,051280753236,01083031097,18143830412,13022079660,075584379411,01083031098,055166017458,4007440016,13072309105,18961779475,02131575405,13229928460,13372277394,051280753237,13267484465,18144081760,13317318579,059188232446,15302554292,13072313631,02061005037,05726518333,13372281183,051280786115,13267492332,18059164775,02120563421,13319421360,15302598269,02061014020,02120563643,13268697024,18146783965,02061014021,13319421659,085187160752,02120563864,13127549148,02151349503,076926988473,18146785315,02120563961,07525975122,15534075945,02759614581,02868715019,15307075618,13162234320,13373648724,18262546797,13128205437,18115341365,17768370642,15536548604,17058525057,02151376899,059188707464,15308448635,05925147862,02180209355,13373649349,13128238531,18115341712,02868956709,02180216402,17768370764,15536594868,01056452501,17058820149,05922042827,15308449110,02568234140,13373649394,02869060138,02866563473,075521854158,17768370864,02196833956,17068891922,02869514042,15308449112,13373652461,02968923281,18409182757,075521874796,17774083581,17068893519,057682149783,15308449136,15626149810,02866849844,13162679131,037122066566,13373662343,085187160827,04765870082,13072325870,076986972430,18118251944,051280786581,075521944203,05972508154,17068895554,03732454789,05925219472,02866909316,04765870083,13072326052,053158563593,18118760659,02866911481,17069610640,4007724055,13319421680,18965152981,13242719187,076986972456,18146785317,075766657730,03757255467,057689814202,13636566032,051380796531,13319422011,085187160885,13242823920,037760220304,18118792460,18146785360,05972531208,17074045638,18060505984,15628999778,13319422037,13242870463,18146785361,17074047725,07579506501,15628999807,13166439687,13319422039,18979976566,13242904369,18123229808,18654330231,18146785537,05926537514,17074047726,18061166940,13319422135,13073050212,18986222901,13242970098,13128607392,073183058315,057777754325,13319422652,13073591985,18987682531,13244825326,051280897421,18674137720,13129816467,02131830893,17189102715,18150468184,02266334105,13319422706,15308449138,13373667714,18675969445,13129835014,02062284730,01058364325,17192384061,05926586341,02062341460,15311557656,02161732834,13246865868,02083250243,13129848918,17193849440,02126017625,02161732958,15771785652,18062571895,02083250244,13319426311,15311795643,02161737510,02151865926,02180344010,13136140475,02062720133,073183093520,17193849465,02161737512,05927111349,02151867196,02161737537,13247330245,085187160897,02180345372,13138252380,17195440550,076989071538,02586916661,18063412544,02131014211,051380796544,13379648302,085187160898,02886190777,13140354456,073183093534,17195440701,02151919122,02180347140,01056009448,13166439739,13380780712,01056117192,037156174379,17195440703,076989244442,18014624454,17074047993,02589623853,051380929402,02155001033,874088111111,13380798670,02180354905,13140356347,17195441273,05973110086,17079675726,05922278445,13167865204,886277055335,13145754224,17195441274,18014924070,02868073043,15317231980,085188233543,02961860853,02868073044,17195441275,18015433876,05923294357,02961860879,15572313827,17081318496,15318624469,13076752786,02868073833,05998230165";
//      //字符串转list<String>
////       String str = "asdfghjkl";
//        Set<String> staffsSet = new HashSet<>(Arrays.asList(ss.split(",")));
//        System.err.println(staffsSet.size());
//        for (String str : staffsSet) {  
//			System.err.println("phone :"+str);
//			String url = "https://haoma.baidu.com/query";
//			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//			webClient.getOptions().setJavaScriptEnabled(false);
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			HtmlPage searchPage1= webClient.getPage(webRequest);
//			String html = searchPage1.getWebResponse().getContentAsString();
////			System.err.println(html);
//			Document doc = Jsoup.parse(html);
//			Elements ele = doc.select("#queryForm > input");
//			Elements ele1 = doc.select("#id_captcha_0");
//			Elements ele2 = doc.select("#id_cid");
//			String csrfmiddlewaretoken = null;
//			String captcha_0 = null;
//			String cid = null;
//			if(ele.size()>0&&ele1.size()>0&&ele2.size()>0){
//				csrfmiddlewaretoken = ele.get(0).attr("value").trim();
//				captcha_0 = ele1.attr("value").trim();
//				cid = ele2.attr("value").trim();
//			}
////			System.err.println(csrfmiddlewaretoken);
////			System.err.println(captcha_0);
////			System.err.println(cid);
//			String url1 = "https://haoma.baidu.com/user_code_info?cid=0385696406fd3363c3d3e81a3fcf89a5adc5f437";
//			WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.GET);
//			HtmlPage searchPage2= webClient.getPage(webRequest2);
//			WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.POST);
//			webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
//			webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
//			webRequest1.setAdditionalHeader("Connection", "keep-alive");
//			webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//			webRequest1.setAdditionalHeader("Host", "haoma.baidu.com");
//			webRequest1.setAdditionalHeader("Origin", "https://haoma.baidu.com");
//			webRequest1.setAdditionalHeader("Referer", "https://haoma.baidu.com/query");
//			webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests:", "1");
//			webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//			String requestBody = "csrfmiddlewaretoken="+csrfmiddlewaretoken+"&phone="+str+"&captcha_0="+captcha_0+"&captcha_1=1&cid=0385696406fd3363c3d3e81a3fcf89a5adc5f437";
//			webRequest1.setRequestBody(requestBody);
//			Page searchPage = webClient.getPage(webRequest1);
//			String html4 = searchPage.getWebResponse().getContentAsString();
////			System.err.println(html4);
//			Document doc1 = Jsoup.parse(html4);
//			Elements ele3 = doc1.select("div.category h2");
//			Elements ele4 = doc1.select("div.category > span");
//			String type = null;
//			String phone = null;
//			String mark = null;
//			String markType = null;
//			if(ele3.size()>0){
//				
//				if(ele4.size()==0){
//					type = ele3.text().trim();
//				}else if(ele4.size()==1){
//					type = ele3.text().trim();
//					phone = ele4.text().trim();
//				}else if(ele4.size()==2){
//					markType = ele3.text().trim();
//					mark = ele4.get(0).text().trim();
//					phone = ele4.get(1).text().trim();
//				}else if(ele4.size()==3){
//					markType = ele3.text().trim();
//					mark = ele4.get(0).text().trim();
//					phone = ele4.get(1).text().trim();
//					type = ele4.get(2).text().trim();
//				}else{
//					ele3 = doc1.select("div.category h2");
//					ele4 = doc1.select("div.category  span");
//					markType = ele3.text().trim();
//					type = ele4.text().trim();
//				}
//				System.err.println("type :"+type);
//				System.err.println("markType :"+markType);
//				System.err.println("mark :"+mark);
//				System.err.println("phone :"+phone);
////				System.out.println(ssp);
////				System.out.println(sspp);
//			}
//		}
////        for (String string : staffsSet) {
//            System.out.println(string);
//        }
//		JSONArray json = JSONArray.fromObject(ss);
//		System.out.println(json);
		

		
//		String readFdf4 = "C:\\Users\\Administrator\\Desktop\\新建文本文档 (3).txt";
//		File file5 = new File(readFdf4);
//		String readTxt = readTxtFile(file5);
//		System.out.println(readTxt);
//		readTxt = readTxt.replaceAll("null", "");
//		readTxt = readTxt.replaceAll("\"", "");
//		readTxt = readTxt.replaceAll("\r|\n", ",");
//		readTxt = readTxt.replaceAll(",,", ",");
//		System.out.println(readTxt);
//		String[] txt = readTxt.split("\n");
//		String classOne = null;
//		if(txt.length>0){
//			System.out.println(txt.length);
//			for(int i = 0;i <txt.length;i++){
//				String[] c = txt[i].split(" ");
//				System.out.println(c.length);
//				String city = c[0].replaceAll("\r|\n", "").replaceAll(" ", "");
//				String areaCode = c[1].replaceAll("\r|\n", "").replaceAll(" ", "");
//				if(areaCode.contains("电话号码")){
//					classOne = city;
//					continue;
//				}
//				System.out.println(city);
//				System.out.println(areaCode);
//				System.out.println(classOne);
////				AreaPhone area = new AreaPhone();
////				area.setCity(city);
////				area.setAreaCode(areaCode);
////				areaPhoneRepository.save(area);
//			}
//		}
	}
	
	public static String phone(String p) throws Exception{
		String url = "https://haoma.baidu.com/query";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage1= webClient.getPage(webRequest);
		String html = searchPage1.getWebResponse().getContentAsString();
//		System.err.println(html);
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#queryForm > input");
		Elements ele1 = doc.select("#id_captcha_0");
		Elements ele2 = doc.select("#id_cid");
		String csrfmiddlewaretoken = null;
		String captcha_0 = null;
		String cid = null;
		if(ele.size()>0&&ele1.size()>0&&ele2.size()>0){
			csrfmiddlewaretoken = ele.get(0).attr("value").trim();
			captcha_0 = ele1.attr("value").trim();
			cid = ele2.attr("value").trim();
		}
//		System.err.println(csrfmiddlewaretoken);
//		System.err.println(captcha_0);
//		System.err.println(cid);
		String url1 = "https://haoma.baidu.com/user_code_info?cid=fac89bf697dce97c8fda971ae8af8fbd6a92bc0f";
		WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage searchPage2= webClient.getPage(webRequest2);
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest1.setAdditionalHeader("Connection", "keep-alive");
		webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest1.setAdditionalHeader("Host", "haoma.baidu.com");
		webRequest1.setAdditionalHeader("Origin", "https://haoma.baidu.com");
		webRequest1.setAdditionalHeader("Referer", "https://haoma.baidu.com/query");
		webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests:", "1");
		webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "csrfmiddlewaretoken="+csrfmiddlewaretoken+"&phone="+p+"&captcha_0="+captcha_0+"&captcha_1=1&cid=fac89bf697dce97c8fda971ae8af8fbd6a92bc0f";
		webRequest1.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest1);
		String html4 = searchPage.getWebResponse().getContentAsString();
		Document doc1 = Jsoup.parse(html4);
		Elements ele3 = doc1.select("div.category h2");
		Elements ele4 = doc1.select("div.category > span");
		String type = null;
		String phone = null;
		String mark = null;
		String markType = null;
		if(ele3.size()>0){
			
			if(ele4.size()==0){
				type = ele3.text().trim();
			}else if(ele4.size()==1){
				type = ele3.text().trim();
				phone = ele4.text().trim();
			}else if(ele4.size()==2){
				markType = ele3.text().trim();
				mark = ele4.get(0).text().trim();
				phone = ele4.get(1).text().trim();
			}else if(ele4.size()==3){
				markType = ele3.text().trim();
				mark = ele4.get(0).text().trim();
				phone = ele4.get(1).text().trim();
				type = ele4.get(2).text().trim();
			}else{
				ele3 = doc1.select("div.category h2");
				ele4 = doc1.select("div.category  span");
				markType = ele3.text().trim();
				type = ele4.text().trim();
			}
			System.err.println("type :"+type);
			System.err.println("markType :"+markType);
			System.err.println("mark :"+mark);
			System.err.println("phone :"+phone);
//			System.out.println(ssp);
//			System.out.println(sspp);
		}
//	    System.out.println(searchPage.getWebResponse().getContentAsString());
		return html4;
	}
	
	public static  String readTxtFile(File fileName)throws Exception{  
//		List<String> lines=new ArrayList<String>();
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"GBK"));
		String line = null;
		try{  
			String read=null;  
			while((read=br.readLine())!=null){  
				line=line+read+"\r\n";  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}  
		br.close();


		return line;  
	} 

	
public static WebDriver openloginCmbChina()throws Exception{ 
		
		//driver.manage().window().maximize();
	System.out.println("launching chrome browser");
	System.setProperty("webdriver.chrome.driver", "D:\\zhaohui\\chromedriver.exe");
	WebDriver driver = null;
	try {
	
	ChromeOptions chromeOptions = new ChromeOptions();
	chromeOptions.addArguments("disable-gpu"); 

	driver = new ChromeDriver(chromeOptions);
	 
	driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
	
	
    driver.manage().window().maximize();
		
		return driver;
	} catch (Exception e) {
		System.out.println("网络超时");
		
	}
	return driver;
	
}
}
