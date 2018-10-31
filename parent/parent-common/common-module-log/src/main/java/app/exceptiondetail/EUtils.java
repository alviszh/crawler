package app.exceptiondetail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
/**
 * 该方法用于将异常信息详细记录到日志中，效果同：e.printStackTrace();
 * 
 * @author sln
 *
 */
@Component
public class EUtils {
	public String getEDetail(Exception e){
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
