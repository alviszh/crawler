package text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class POIUnit {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		readExcelWithoutTitle("C:\\Users\\Administrator\\Desktop\\tel.xls"); 
	}
	
	public static WebClient addCookie(WebClient webClient,String cookiepath,String domain) throws Exception{
		List<List<String>> lists =  readExcelWithoutTitle(cookiepath); 
		
		for(List<String> list:lists){
			String key = list.get(0);
			String value = list.get(1);
			System.out.println("domain---->"+domain);
			System.out.println("key---->"+key);
			System.out.println("value---->"+value); 
			webClient.getCookieManager().addCookie(new Cookie(domain, key, value)); 
		}
		
		return webClient;
	}
	
	public static List<List<String>> readExcelWithoutTitle(String filepath) throws Exception{
	    String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
	    InputStream is = null;
	    Workbook wb = null;
	    List<List<String>> sheetList = new ArrayList<List<String>>();//对应sheet页
	    try {
	        is = new FileInputStream(filepath);
	         
	        if (fileType.equals("xls")) {
	            wb = new HSSFWorkbook(is);
	        } else {
	            throw new Exception("读取的不是excel文件");
	        } 
	        int sheetSize = wb.getNumberOfSheets();
	        System.out.println("sheetSize-------"+sheetSize);
	        if(sheetSize>0){
	        	Sheet sheet = wb.getSheetAt(0);  
	            int rowSize = sheet.getLastRowNum() + 1; 
	           // System.out.println("rowSize-------"+rowSize); 
	            for (int j = 0; j < rowSize; j++) {//遍历行
	                Row row = sheet.getRow(j);
	                if (row == null) {//略过空行
	                    continue;
	                }
	                int cellSize = row.getLastCellNum();//行中有多少个单元格，也就是有多少列
	                //System.out.println("cellSize-------"+cellSize);
	                List<String> rowList = new ArrayList<String>();//对应一个数据行
	                for (int k = 0; k < cellSize; k++) {
	                    Cell cell = row.getCell(k);
	                    String value = null;
	                    if (cell != null) {
	                        value = cell.toString();
	                    }
	                   // System.out.println("value-------"+value);
	                    rowList.add(value);
	                }
	                sheetList.add(rowList);
	            } 
		       
		        }
	    } catch (FileNotFoundException e) {
	        throw e;
	    } finally {
	        if (wb != null) {
	            wb.close();
	        }
	        if (is != null) {
	            is.close();
	        }
	    }
	    
	    return sheetList;
	}

}
