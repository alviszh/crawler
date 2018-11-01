package com.test;

import java.io.FileReader;
import javax.script.Invocable;

import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;

/** * Java调用并执行js文件，传递参数，并活动返回值 * * @author manjushri */

public class ScriptEngineTest {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");

		String jsFileRSA = "D:\\img\\RSA.js"; // 读取js文件
		String jsFileBigInt = "D:\\img\\RSA.js"; // 读取js文件

		FileReader reader1 = new FileReader(jsFileRSA); // 执行指定脚本
		FileReader reader2 = new FileReader(jsFileBigInt); // 执行指定脚本
		engine.eval(reader1);
		engine.eval(reader2);

		if (engine instanceof Invocable) {
			Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数

			// c = merge(2, 3);

			Integer c = (Integer) invoke.invokeFunction("biFromHex",
					"a5aeb8c636ef1fda5a7a17a2819e51e1ea6e0cceb24b95574ae026536243524f322807df2531a42139389674545f4c596db162f6e6bbb26498baab074c036777");

			System.out.println("c = " + c);
		}

		reader1.close();
		reader2.close();
		
		
		
	}

}
