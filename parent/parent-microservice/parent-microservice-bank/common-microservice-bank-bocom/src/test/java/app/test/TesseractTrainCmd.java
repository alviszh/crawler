package app.test;

import java.io.IOException;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

/**   
*    
* 项目名称：common-microservice-bank-bocom   
* 类名称：TesseractTrainCmd   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月16日 上午10:12:17   
* @version        
*/
public class TesseractTrainCmd {

	

	public static void main(String[] args) throws Exception {
		String cmdString = "D://Tesseract-OCR//tesseract.exe D:\\home\\img\\tr\\langyp.fontyp.exp0.tif  D:\\home\\img\\tr\\langyp.fontyp.exp0 batch.nochop makebox";
	
		String cmdString1 = "D://Tesseract-OCR//tesseract.exe D:\\home\\img\\tr\\bocom.tif D:\\home\\img\\tr\\bocom nobatch box.train";
		
		String cmdString2 = "D://Tesseract-OCR//unicharset_extractor.exe D:\\home\\img\\tr\\bocom.box";

		String cmdString3 = "D://Tesseract-OCR//mftraining -F font_properties.exe  -U  D:\\home\\img\\tr\\unicharset D:\\home\\img\\tr\\bocom.tr";
		
		String cmdString4 = "D://Tesseract-OCR//cntraining D:\\home\\img\\tr\\bocom.tr";

		String cmdString5 = "D://Tesseract-OCR//combine_tessdata D:\\home\\img\\tr\\bocom.";
		
		
		Runtime r = Runtime.getRuntime();
		
		System.out.println(cmdString);
		Process p = r.exec(cmdString);
		System.out.println(p.waitFor());
	}
}
