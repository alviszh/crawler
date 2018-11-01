package Test;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TestJs {

	public static void main(String[] args) throws Exception {
		TestJs rsaTest = new TestJs();
//		String mobilNbr="17718194181";
//		String effDate ="2017-08-21";
//		String expDate ="2017-09-26";
//		String serviceNbr="119110";
//		String operListID="";
//		String isPrepay="";
//		String randomCode="";
//		String macCode="";
//		String params = "currentPage=&pageSize=10&effDate=" + effDate + "&expDate=" + expDate + "&serviceNbr=" + serviceNbr + "&operListID=" + operListID + "&isPrepay=" + isPrepay + "&pOffrType=481&random="+randomCode+ "&macCode=" + macCode;

		
		//String mobilNbr ="17718194181";
//		String _key = mobilNbr.substring(1,2)+mobilNbr.substring(3,4)+mobilNbr.substring(6,7)+mobilNbr.substring(8,10);
//		String params = "validCode="+"3BBk"+"&phone=" + mobilNbr+"&key="+_key;
		
//		String _key = mobilNbr.substring(1, 2) + mobilNbr.substring(3, 4) + mobilNbr.substring(6, 7)+ mobilNbr.substring(8, 10);
//		String params = "mobileNum=" + mobilNbr + "&key=" + _key;
		
		
		//String params ="serviceNbr=17718194181";
		
		
		//String params = "currentPage=&pageSize=10&effDate=2017-09-01&expDate=2017-09-30&serviceNbr=17718194181&operListID=&isPrepay=0&pOffrType=481&random=273136&macCode=88f176659da018b1a1f656def37c7e58"; 
		//String params = "currentPage=&pageSize=10&effDate=2017-09-01&expDate=2017-09-30&serviceNbr=17718194181&operListId=5";
		
		//String params = "currentPage=&pageSize=10&effDate=2017-09-01&expDate=2017-09-30&uuid=703781e89a44121e14e9601ce3f3c4616be11c41aaa62cb6b17e999be4d3b497ceb9ca4cd05f12ca8af106e59a998eae9e053a5771ff207cf1d3f4f3e588aa46&operListId=";
			//!!!!!!!!
		//String params = "currentPage=&pageSize=10&effDate=2017-10-01&expDate=2017-10-31&serviceNbr=17718194181&operListID=2&isPrepay=0&pOffrType=481&random=138978&macCode=15783d00c67fa0c504e777576fe472f9";
		//String params = "uuid=18134539679&currentMonth=201708&currentLatnId=551";
		String params="111111";
		String str = rsaTest.encryptedPhone(params);
		System.out.println(str);
	}
//8c2fec02dc6b441ea193f905c1cd97a955a572a32d0417cb03996924be6e8b87df7236c97e1f810407b30a3b031a935f0d6b49a0a55f57e85d76a8e5bc289f0c
	public String encryptedPhone(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",phonenum);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
}
