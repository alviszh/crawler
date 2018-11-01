package com.test;

import java.io.FileReader;

import javax.script.Compilable;
import javax.script.Invocable;

import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

/** * Java调用并执行js文件，传递参数，并活动返回值 * * @author manjushri */

public class ScriptEngineTest2 {

	public static void main(String[] args) throws Exception {
		//ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		String jsFileRSA = "D:\\img\\allJs.js"; // 读取js文件
		FileReader reader1 = new FileReader(jsFileRSA); // 执行指定脚本
		engine.eval(reader1);
		final Compilable compilable = (Compilable) engine;
		final Invocable invocable = (Invocable) engine;

		//Object result = invocable.invokeFunction("new RSAKeyPair('10001','','a5aeb8c636ef1fda5a7a17a2819e51e1ea6e0cceb24b95574ae026536243524f322807df2531a42139389674545f4c596db162f6e6bbb26498baab074c036777')");
		
		Object result = invocable.invokeFunction("encryptedString","18995154123");
		
		System.out.println(result);
		System.out.println(result.getClass());
		 

		// c = merge(2, 3);

		//Object c =invocable.invokeMethod(new Object(), "RSAKeyPair","10001","","a5aeb8c636ef1fda5a7a17a2819e51e1ea6e0cceb24b95574ae026536243524f322807df2531a42139389674545f4c596db162f6e6bbb26498baab074c036777");

		//System.out.println(c);
	}

}
