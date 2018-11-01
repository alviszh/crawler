package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import app.util.ContentExtractor;
public class AppTest {
	public static void main(String[] args) {
		
		
		
		try {
			// String url =
			// "http://news.gxnews.com.cn/staticpages/20180413/newgx5ad05c54-17234424.shtml?pcview=1";
			// String url = "http://news.ifeng.com/a/20180412/57513050_0.shtml";
			// String url =
			// "http://newpaper.dahe.cn/hnsb/html/2018-04/13/content_237906.htm";
			// String url =
			// "http://yulin.gxorg.com/jinrong/2018/0413/6792.html";
			 String url = "http://www.xjche365.com/cssh/20180413/514.html";
//			 String url = "http://news.ifeng.com/a/20180412/57497659_0.shtml";
//			 String url =
//			 "http://gov.hnr.cn/zt/zzzxzx/zxxx/20180412/371409/index.html";
//			 String url = "http://epaper.anhuinews.com/html/ahfzb/20180412/article_3655083.shtml";
//			 String url = "http://www.ln.chinanews.com/news/2018/0412/137099.html";
//			String url = "https://www.rong360.com/news/2018/04/11/155440.html";
			// String url = "http://bd.kuwo.cn/yinyue/26369771?from=baidu";
			// String url = "http://www.360kan.com/m/gqfoYkYoRnL7UB.html";
			// String url = "http://baike.wdzj.com/doc-view-3256.html";
			Document doc = Jsoup.connect(url).get();
			
			ContentExtractor contentExtractor = new ContentExtractor(doc);
	    	Element contentElement = contentExtractor.getContentElement();
	    	String time = contentExtractor.getTime(contentElement);
			
	    	System.out.println("时间："+time);
	    	
			Elements select = doc.select(
					"div[id*=ontent],div[class*=ontent],div[id*=rticle],div[class*=rticle],div[class*=main],div[class*=exttit_m1],div[class*=text],div[class*=Text],div[class*=rtM],div[class*=font],div[class*=wz],div[class*=Art],div[class*=mt10],div[id*=rtibody],div[class=post_text]");
			// 存放p标签的数量，方便取出数量最多的p标签的父级标签
			List<Integer> intList = new ArrayList<Integer>();
			for (int i = 0; i < select.size(); i++) {
				Element element = select.get(i);
				String p = element.getElementsByTag("p") + "";
				if (!"".equals(p)) {
					// p标签的数量
					Elements ps = element.getElementsByTag("p");
					int size = ps.size();
					intList.add(size);
				}
			}
			Collections.sort(intList);
			int q = 0;
			for (int i = 0; i < intList.size() - 1; i++) {
				int int1 = intList.get(i);
				System.out.println("界面中p标签的个数---" + int1);
				q += int1;
			}
			System.out.println("除了最大的其余数量的和-----" + q);
			// 如果p标签最大的个数等于其他个数的和，那么取数量第二的p标签
			if (intList.get(intList.size() - 1) == q) {
				System.out.println("最大p标签的个数        是      其他p标签的个数的和");
				for (int i = 0; i < select.size(); i++) {
					Element element = select.get(i);
					// 判断是否存在p标签
					String p = element.getElementsByTag("p") + "";
					if (!"".equals(p)) {
						// p标签的数量
						Elements ps = element.getElementsByTag("p");
						int size = ps.size();
						// 锁定p标签的位置
						if (size == intList.get(intList.size() - 2)) {
							System.out.println("最终选择的p标签的个数---" + size);
							System.out.println("最终的结果div---" + element + "");

							String end = "";
							// 处理对应所有标签中的属性
							Elements children = element.children();
							String allP = "";
							for (int j = 0; j < children.size(); j++) {
								// 所有的p标签
								Element element2 = children.get(j);
								Elements elementsImg = element2.getElementsByTag("img");
								// 判断p标签中是否存在img标签
								String img = "";
								if (elementsImg.size() == 0) {
								} else {
									System.out.println("p标签中      存在      img标签");
									for (int k = 0; k < elementsImg.size(); k++) {
										String src = elementsImg.get(k).attr("src");
										// 判断是否是全路径
										if (src.contains("//")) {
											System.out.println("图片-----绝对路径");
											String srcAll = "<img src = " + src + "></img>";
											img += srcAll;
										} else {
											System.out.println("图片-----相对路径");
											// 截取url得到域名，进行拼接图片地址
											String[] split = url.split("//");
											String[] split2 = split[1].split("/");
											String yuming = split2[0].trim() + "/";
											System.out.println("域名-----" + yuming);
											String srcAll = "<img src = " + yuming + src + "></img>";
											img += srcAll;
										}
									}
									System.out.println("最终图片结果----" + img);
								}
								String text = element2.text();
								String pp = "<p>" + img + text + "</p>";
								allP += pp;
							}
							end = "<div>" + allP + "</div>";
							String trim = end.trim();
							System.out.println("最终的结果-----" + trim);
						}
					}
				}
			} else {
				System.out.println("最大p标签的个数      不是      其他p标签的个数的和");
				for (int i = 0; i < select.size(); i++) {
					Element element = select.get(i);
					// 判断是否存在p标签
					String p = element.getElementsByTag("p") + "";
					if (!"".equals(p)) {
						// p标签的数量
						Elements ps = element.getElementsByTag("p");
						int size = ps.size();
						// 锁定p标签的位置
						if (size == intList.get(intList.size() - 1)) {
							System.out.println("最终选择的p标签的个数---" + size);
							System.out.println("最终的结果div---" + element + "");

							String end = "";
							// 处理对应所有标签中的属性
							Elements children_p = element.getElementsByTag("p");
							String allP = "";
							for (int j = 0; j < children_p.size(); j++) {
								Element element2 = children_p.get(j);
								Elements elementsImg = element2.getElementsByTag("img");
								// 判断p标签中是否存在img标签
								String img = "";
								if (elementsImg.size() == 0) {
								} else {
									System.out.println("p标签中      存在      img标签");
									for (int k = 0; k < elementsImg.size(); k++) {
										String src = elementsImg.get(k).attr("src");
										// 判断是否是全路径
										if (src.contains("//")) {
											System.out.println("图片-----绝对路径");
											String srcAll = "<img src = " + src + "></img>";
											img += srcAll;
										} else {
											System.out.println("图片-----相对路径");
											// 截取url得到域名，进行拼接图片地址
											String[] split = url.split("//");
											String[] split2 = split[1].split("/");
											String yuming = split2[0].trim() + "/";
											System.out.println("域名-----" + yuming);
											String srcAll = "<img src = " + yuming + src + "></img>";
											img += srcAll;
										}
									}
									System.out.println("最终图片结果----" + img);
								}
								// 所有的p标签
								String text = element2.text();
								String pp = "<p>" + img + text + "</p>";
								allP += pp;
							}
							end = "<div>" + allP + "</div>";
							String trim = end.trim();
							System.out.println("最终的结果-----" + trim);
						}
					}
				}
			}
			// FileWriter fw = null;
			// File f = new File("url.txt");
			// try {
			// if (!f.exists()) {
			// f.createNewFile();
			// }
			// fw = new FileWriter(f);
			// BufferedWriter out = new BufferedWriter(fw);
			// out.write(contentAsString, 0, contentAsString.length() - 1);
			// out.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

		} catch (Exception e) {
		}
	}
	
}
