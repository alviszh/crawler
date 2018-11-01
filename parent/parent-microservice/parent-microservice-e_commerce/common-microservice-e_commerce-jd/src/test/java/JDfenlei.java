
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDFenLeiBean;

import app.bean.Data;
import app.bean.JDFenLeiJsonRootBean;
import app.bean.Leibie;




/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：jdfenlei 类描述： 创建人：hyx
 * 创建时间：2018年8月28日 下午5:08:23
 * 
 * @version
 */
public class JDfenlei {



	public static void main(String[] args) throws Exception {
		String url = "https://dc.3.cn/category/get?&callback=getCategoryCallback";

		Document doc = Jsoup.parse(new URL(url).openStream(), "GBK", url);

		// System.out.println(doc.body().text());

		String txt = "";
		Pattern pattern = Pattern.compile("getCategoryCallback\\((.*?)\\)");
		Matcher matcher = pattern.matcher(doc.body().text());
		while (matcher.find()) {
			txt = matcher.group(1);
		}

		Gson gs = new Gson();
		Type type = new TypeToken<JDFenLeiJsonRootBean>() {
		}.getType();
		JDFenLeiJsonRootBean jDFenLeiJsonRootBean = gs.fromJson(txt, type);

		System.out.println(jDFenLeiJsonRootBean.toString());

		List<Data> datas = jDFenLeiJsonRootBean.getData();
		List<JDFenLeiBean> jdFenLeiBeans = new ArrayList<>();
		for (Data data : datas) {
			// System.out.println("====" + data.toString());
			List<Leibie> fenleis = data.getS();
			for (Leibie leibie : fenleis) {
//				System.out.println("====" + leibie.toString());

				// System.out.println("=一级标签="+getChinese(leibie.getN()));
				List<Leibie> fenlei2 = leibie.getS();

				for (Leibie leibie2S : fenlei2) {

					// System.out.println("=二级标签=="+getChinese(leibie2S.getN()).trim());

					// System.out.println("=二级=="+leibie2S.toString());

					List<Leibie> fenlei3 = leibie2S.getS();

					for (Leibie leibie3s : fenlei3) {
						JDFenLeiBean jdFenLeiBean = new JDFenLeiBean();
						jdFenLeiBean.setLevelone(getChinese(leibie.getN()));
						jdFenLeiBean.setLeveltwo(getChinese(leibie2S.getN()));
						jdFenLeiBean.setLevelthree(getChinese(leibie3s.getN()).trim());
						
						// System.out.println("=三级标签==="+getChinese(leibie3s.getN()).trim());

						String str_num = leibie3s.getN();

						String[] str_nums = str_num.split("\\|");

						// System.out.println("=三级标签编号==="+str_nums[0].replaceAll("-",
						// ","));
						
						String levelnum = getNum(str_nums[0].split("\\&")[0].replaceAll("-", ","));
						jdFenLeiBean.setLevelnum(levelnum);

						System.out.println("==========="+str_nums[0].replaceAll("-", ","));
						System.out.println(jdFenLeiBean.toString());

						// System.out.println("=三级标签编号="+(getNum().trim()));

						// System.out.println("=三级=="+leibie3s.toString());
						jdFenLeiBeans.add(jdFenLeiBean);
					}
				}
			}

		}
		
//		jdFenLeiBeanRepository.saveAll(jdFenLeiBeans);
	}

	private static String getChinese(String str) {

		String reg = "[^\u4e00-\u9fa5]";

		str = str.replaceAll(reg, "");

		return str;
	}

	private static String getNum(String str) {

		String value = "";
		Pattern p = Pattern.compile("(\\d{2})(.+)(\\d{2})");// 这个2是指连续数字的最少个数
		Matcher m = p.matcher(str);
		while (m.find()) {
			value += m.group();
		}

		return value;
	}
}
