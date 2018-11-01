package org.common.microservice.eureka.china.unicom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

/**   
*    
* 项目名称：common-microservice-eureka-china-unicom   
* 类名称：cookiereasss   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年5月22日 下午1:08:02   
* @version        
*/
public class cookiereasss {

	
	private static Set<Cookie> cookies;
	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;
	
	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream("C:/Users/Administrator/Desktop/运营商字段说明/tel.xls");
		wb = new HSSFWorkbook(is);
		System.out.println("list中的数据打印出来");
		sheet = wb.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("rowNum"+ rowNum);
		System.out.println("colNum"+ colNum);
		
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		Set<Cookie> cookies = new HashSet<>();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++) {
			row = sheet.getRow(i);
			Cookie cookie = new Cookie("iservice.10010.com", row.getCell(0)+"",  row.getCell(1)+"");
			
			cookies.add(cookie);
		}
		
		is = new FileInputStream("C:/Users/Administrator/Desktop/运营商字段说明/tel2.xls");
		wb = new HSSFWorkbook(is);
		System.out.println("list中的数据打印出来");
		sheet = wb.getSheetAt(0);
		 rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		 colNum = row.getPhysicalNumberOfCells();
		System.out.println("rowNum2"+ rowNum);
		System.out.println("colNum2"+ colNum);
		
		 webClient = WebCrawler.getInstance().getNewWebClient();
		
		Set<Cookie> cookies2 = new HashSet<>();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++) {
			row = sheet.getRow(i);
			Cookie cookie = new Cookie("iservice.10010.com", row.getCell(0)+"",  row.getCell(1)+"");
			
			cookies2.add(cookie);
		}
		
		for(Cookie cookie : cookies){
			for(Cookie cookie2 : cookies2){
				if(cookie2.getName().indexOf(cookie.getName().trim())!=-1){
					if(!cookie2.getValue().equals(cookie.getValue().trim())){
						System.out.println("cookie  ::"+cookie.getName()+":::::::"+cookie.getValue());
						System.out.println("cookie2  ::"+cookie2.getName()+":::::::"+cookie2.getValue());
					}
				}
			}
		}
		
	}
}
