package app.webchange;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.monitor.MonitorHtmlAfterTreat;
import com.microservice.dao.entity.crawler.monitor.MonitorJsAfterTreat;

import app.utils.PatternUtils;

/**
 * @description:用于处理html或者js源码，排除公共部分
 * 
 * 
 * 			有的网页，需要去除的地方太多，用切割的方式不好处理，即减法不好用，就用加法
 * @author: sln 
 * @date: 2018年3月26日 下午3:48:45 
 */
@Component
public class HtmlOrJsContentAfterTreatService {
	//方法一：根据元素定位并且移除
	public String removeByElementLocate(Document doc,List<MonitorHtmlAfterTreat> list){
		String keyValue;
		for(MonitorHtmlAfterTreat treatDetail:list){
			keyValue=treatDetail.getElementselect().trim();
			doc.select("["+keyValue+"]").remove();
		}
		String html = doc.html();
		html=CutAllNoteSymbolService.replaceAllNoteSymbol(html);
		return html;
	}
	//方法二：根据元素定位，只留取这部分(例如大庆社保)  id=link116
	public String onlyRemainByElementLocate(Document doc,List<MonitorHtmlAfterTreat> list){
		String html="";
		for(MonitorHtmlAfterTreat treatDetail : list){ 
	        String keyValue = treatDetail.getElementselect().trim();
	        html=doc.select("["+keyValue+"]").html();
		}
		return html;
	}
	//方法三：根据元素定位，将该元素中每次都变化的属性值置空
	public String setEmptyByElementLocate( Document doc,List<MonitorHtmlAfterTreat> list){
		String keyValue;  
        String attribute;  
        for(MonitorHtmlAfterTreat treatDetail : list){  
        	keyValue = treatDetail.getElementselect().trim();
        	attribute = treatDetail.getAttribute().trim();
        	doc.select("["+keyValue+"]").attr(""+attribute+"","");  //将属性值设置为空
        }  
        String html = doc.html();
    	html=CutAllNoteSymbolService.replaceAllNoteSymbol(html);  //返回结果之前先将源码的注释全去掉（内蒙古省直社保）
		return html;
	}
	//===================================================================
	//方法四：部分网站html无法通过元素定位，故用如下方式（相加的方式，来获取不变化的部分）适用于所有网站——是否用如下方式取决于表格维护者填写了哪种方案
	public String remainSourceCodeByAdd(String html,List<MonitorHtmlAfterTreat> list){
		String strStart;  
		String strEnd;  
		String tempPartHtml;
		StringBuffer htmlBuffer=new StringBuffer();
		for(MonitorHtmlAfterTreat treatDetail:list){  
			strStart = treatDetail.getStrstart().trim();
			strEnd = treatDetail.getStrend().trim();
			tempPartHtml=PatternUtils.splitData(html,strStart,strEnd);
			htmlBuffer.append(tempPartHtml);
		} 
		return htmlBuffer.toString();
	}
	//同方法四：将js中不变的内容进行拼接
	public String remainJsSourceCodeByAdd(String jsContent, List<MonitorJsAfterTreat> list) {
		String strStart;  
		String strEnd;  
		String tempPartHtml;
		StringBuffer htmlBuffer=new StringBuffer();
		for(MonitorJsAfterTreat treatDetail:list){  
			strStart = treatDetail.getStrstart().trim();
			strEnd = treatDetail.getStrend().trim();
			tempPartHtml=PatternUtils.splitData(jsContent,strStart,strEnd);
			htmlBuffer.append(tempPartHtml);
		} 
		return htmlBuffer.toString();
	}
	/////////////////////////////////////////////////
	//方法五：将注释部分去除(如下代码不起作用)(将此方法独立作为一个类——CutAllNoteSymbolService)
	/**
	  *	部分html或者js,前后两次返回的内容中，针对同一部分，有的是有注释的，有的是没有注释的，对于这种情况，决定进行完如上操作之后，将
	  *	注释相关的注释符号替换掉
	 */
//		public String replaceAllNoteSymbol(String sourceCode){
//			sourceCode=sourceCode.replaceAll("<!--", "");
//			sourceCode=sourceCode.replaceAll("-->", "");
//			sourceCode=sourceCode.replaceAll("/*", "");
//			sourceCode=sourceCode.replaceAll("\\*/", "");   //此处要将以*开头的进行转义，否则报错：java.util.regex.PatternSyntaxException: Dangling meta character '*' near index 0	*/
//			sourceCode=sourceCode.replaceAll("//", "");
//			return sourceCode;
//		}
	////////////////////////////////////////////////////////////////////////////////////////////
	//截取指定位置(不包含截取点)
	public String splitData(String str, String strStart, String strEnd) { 
		String tempStr;  
		if(strStart.equals("sourceCodeStart")){
			tempStr = str.substring(0, str.indexOf(strEnd));  
		}else if(strEnd.equals("sourceCodeEnd")){
			tempStr = str.substring(str.indexOf(strStart),str.length());  
		}else{
			tempStr = str.substring(str.indexOf(strStart)+strStart.length(), str.indexOf(strEnd));  
		}
        return tempStr;  
	}
	//方法六：String：如下方法适用于无法通过具体元素定位，或者是用元素定位繁琐的情况，根据指定位置进行切除
	public String setEmptyBySplitAndReplace(String html,List<MonitorHtmlAfterTreat> list){
		String strStart;  
		String strEnd;  
		String splitData;
		String tempHtml=html;
		for(MonitorHtmlAfterTreat treatDetail:list){  
			strStart = treatDetail.getStrstart().trim();
			strEnd = treatDetail.getStrend().trim();
			splitData=PatternUtils.splitData(tempHtml,strStart,strEnd);
			tempHtml=tempHtml.replaceAll(splitData,"");
		}
		return tempHtml;
		
	}
	//方法七:本方法适用于随记的数据可已通过元素定位，但是随机数据不光出现在元素中，还出现在html
	public String replaceAllByElementLocate(String html,Document doc,List<MonitorHtmlAfterTreat> list){
		String keyValue;
		String attribute;
		String attrValue; //属性值
		String tempHtml=html;
		for(MonitorHtmlAfterTreat treatDetail:list){
			keyValue=treatDetail.getElementselect().trim();
			attribute=treatDetail.getAttribute().trim();
			attrValue=doc.select("["+keyValue+"]").attr(attribute);
			tempHtml=tempHtml.replaceAll(attrValue,"");
		}
		tempHtml=CutAllNoteSymbolService.replaceAllNoteSymbol(tempHtml);
		return tempHtml;
	}
}
