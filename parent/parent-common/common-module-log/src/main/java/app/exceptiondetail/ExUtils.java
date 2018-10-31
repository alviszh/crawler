package app.exceptiondetail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
/**
 * 该方法用于将异常信息详细记录到日志中，效果同：e.printStackTrace();
 * 期初开发的时候将这个工具类作为组件，后发现总是注入很是繁琐，故启用原方法，改为静态直接调用
 * 注入的方式依旧保存
 * 
 * @author sln
 *
 */
public class ExUtils {
	public static String getEDetail(Exception e){
		ExceptionUtils.getStackTrace(e);
		StringWriter sw = new StringWriter(); 
		e.printStackTrace(new PrintWriter(sw, true)); 
		String eToString = sw.toString(); 
		String[] split = eToString.split("\\)");  //需要转义
		StringBuffer buf=new StringBuffer();
		buf.append("<!DOCTYPE html><html><head></head><body>");
		for (String string : split) {
			buf.append(string+")"+"<br/>");
		}
		buf.append("</body></html>");
		return buf.toString();
	}
}
