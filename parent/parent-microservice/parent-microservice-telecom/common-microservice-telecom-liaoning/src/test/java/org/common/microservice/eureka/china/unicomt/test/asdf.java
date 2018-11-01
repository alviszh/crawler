//package org.common.microservice.eureka.china.unicomt.test;
//
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//
//public class asdf {
//
//	public static void main(String[] args) {
//		login();
//	}
//	
//	public static void login(){
//		 WebDriver webDriver = new FirefoxDriver();
//		   webDriver.manage().window().maximize();
//		   //与浏览器同步非常重要，必须等待浏览器加载完毕
//		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
//		   webDriver.get("http://login.189.cn/login");
//		   System.out.println(webDriver.getPageSource());
//	}
//}
