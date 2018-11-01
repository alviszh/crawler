package app.crawler.htmlparse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.daqing.HousingDaQingPay;

import app.bean.JsonRootBean;

/**
 * 
 * 项目名称：common-microservice-housingfund-daqing 类名称：HousingDQParse 类描述： 创建人：hyx
 * 创建时间：2017年11月7日 下午2:05:24
 * 
 * @version
 */
public class HousingDQParse {

	private static Gson gs = new Gson();

	public static JsonRootBean userinfo_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		JsonRootBean jsonObject = gs.fromJson(html, JsonRootBean.class);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static List<HousingDaQingPay> tranflowPDF_parse(String path) throws Exception {
		File pdfFile = new File(path);
		PDDocument document = null;

		// 方式二：
		document = PDDocument.load(pdfFile);

		// 获取页码
		int pages = document.getNumberOfPages();

		// 读文本内容
		PDFTextStripper stripper = new PDFTextStripper();
		// 设置按顺序输出
		stripper.setSortByPosition(true);
		stripper.setStartPage(1);
		stripper.setEndPage(pages);
		String content = stripper.getText(document);
		// System.out.println(content);

		BufferedReader bre = null;
		String str = "";
		bre = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));

		List<HousingDaQingPay> result_list = new ArrayList<>();
		while ((str = bre.readLine()) != null) {
			String[] array = str.split(" ");

			if (array.length < 5 || array.toString().indexOf("序号")!=-1) {
				continue;
			}
			HousingDaQingPay housingDaQingPay = new HousingDaQingPay();
			housingDaQingPay.setXuhao(array[0]);
			housingDaQingPay.setStartdate(array[1]);
			housingDaQingPay.setAbstracttxt(array[2]);
			housingDaQingPay.setDrawnum(array[3]);
			housingDaQingPay.setIncomenum(array[4]);
			housingDaQingPay.setBalance(array[5]);
			
			result_list.add(housingDaQingPay);
		}
		
		return result_list;

	}

}
