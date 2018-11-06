package com.test;

import java.io.FileReader;

import javax.script.Invocable;

import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;

/** * Java调用并执行js文件，传递参数，并活动返回值 * * @author manjushri */

public class ScriptEngineTest3 {

	public static void main(String[] args) throws Exception { 
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String jsFileRSA = "D:\\img\\allJs.js"; // 读取js文件
		FileReader reader1 = new FileReader(jsFileRSA); // 执行指定脚本
		engine.eval(reader1); 
		final Invocable invocable = (Invocable) engine;

		 
		Object result = invocable.invokeFunction("encryptedString","18092316191");
		 
		System.out.println(result); 
		 
	}

}
