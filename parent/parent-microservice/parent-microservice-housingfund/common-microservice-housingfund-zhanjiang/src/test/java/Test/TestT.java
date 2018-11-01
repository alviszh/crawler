package Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestT {

	public static void main(String[] args) throws UnsupportedEncodingException { 
			
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
					"org.apache.commons.logging.impl.NoOpLog"); 
					java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
					java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
			
					
					
					String decode = URLDecoder.decode("%E6%90%9C%E7%B4%A2%E5%85%B3%E9%94%AE%E5%AD%97", "UTF-8");
					System.out.println(decode);
	}
}
