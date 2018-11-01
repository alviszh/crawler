package org.common.microservice.insurance.sz.hunan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParser {
	public static void main(String[] args) {
		/*String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("E:\\crawler\\changsha\\hunanUserinfo.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		int year = Integer.parseInt(date)-1;
		System.out.println(year);// new Date()为获取当前系统时间
		
		Document document = Jsoup.parse(txt);
		Element personInfo = document.getElementById("personInfo");
		if(null != personInfo){
			String idNum = getNextLabelByKeyword(document, "td", "证件号码");
			String name = getNextLabelByKeyword(document, "td", "姓名");
			String gender = getNextLabelByKeyword(document, "td", "性别");
			String nation = getNextLabelByKeyword(document, "td", "民族");
			String birthday = getNextLabelByKeyword(document, "td", "出生日期");
			
			System.out.println(idNum+"-*-*-"+name+"-*-*-"+gender+"-*-*-"+nation+"-*-*-"+birthday);
		}*/
		try {
			parserInsuranceInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void parserInsuranceInfo() throws Exception{
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("E:\\crawler\\changsha\\insruanceInfo.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		Document document = Jsoup.parse(txt);
		Element divTabs = document.getElementById("divTabs");
		if(null != divTabs){
			Element pensionDiv = document.getElementById("divTab1");
			Element tbody1 = pensionDiv.select("tbody").first();
			Elements trs1 = tbody1.select("tr");
			String className1 = trs1.get(0).className();
			System.out.println(className1);
			if(!className1.equals("empty")){
				for (Element tr : trs1) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					System.out.println("--------|");
					System.out.println(str.toString());
				}
			}
			
			Element pensionDiv2 = document.getElementById("divTab2");
			Element tbody2 = pensionDiv2.select("tbody").first();
			Elements trs2 = tbody1.select("tr");
			String className2 = trs2.get(0).className();
			System.out.println(className2);
			if(!className2.equals("empty")){
				for (Element tr : trs2) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					System.out.println("--------|");
					System.out.println(str.toString());
				}
			}
			
			Element pensionDiv3 = document.getElementById("divTab3");
			Element tbody3 = pensionDiv3.select("tbody").first();
			Elements trs3 = tbody3.select("tr");
			String className3 = trs3.get(0).className();
			System.out.println(className3);
			if(!className3.equals("empty")){
				for (Element tr : trs3) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					System.out.println("--------|");
					System.out.println(str.toString());
				}
			}
			
			Element pensionDiv4 = document.getElementById("divTab4");
			Element tbody4 = pensionDiv4.select("tbody").first();
			Elements trs4 = tbody4.select("tr");
			String className4 = trs4.get(0).className();
			System.out.println(className4);
			if(!className4.equals("empty")){
				for (Element tr : trs4) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					System.out.println("--------|");
					System.out.println(str.toString());
				}
			}
			
			Element pensionDiv5 = document.getElementById("divTab5");
			Element tbody5 = pensionDiv5.select("tbody").first();
			Elements trs5 = tbody5.select("tr");
			String className5 = trs5.get(0).className();
			System.out.println(className5);
			if(!className5.equals("empty")){
				for (Element tr : trs5) {
					Elements tds = tr.children();
					List<String> str = new ArrayList<String>();
					for (Element td : tds) {
						String text = td.text();
						str.add(text);
					}
					System.out.println("--------|");
					System.out.println(str.toString());
				}
			}
		}
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.child(0).val();
			}
		}
		return null;
	}
}
