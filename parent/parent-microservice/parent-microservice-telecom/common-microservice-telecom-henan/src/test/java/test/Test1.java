package test;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Test1 {

	public static void main(String[] args) throws Exception {
		
		String xmlStr = "	 <!--后台返回的限制消息结果，请等待十分钟后在发送-->"
		 +"	 <!--后台返回的剩余时间-->"
		 +"<randInfo>"
		+"<flag>0</flag>"
			+"<msg></msg>"
		    +"<resultMessage>请等待30分钟后在发送</resultMessage>"
		    +"<surplusTime>24</surplusTime>"
		+"</randInfo>";
		
		Document document = DocumentHelper.parseText(xmlStr);
		Element rootElement = document.getRootElement();
		Element resultMessage = rootElement.element("resultMessage");
		String text = resultMessage.getText();
		System.out.println(text);
	}
}
