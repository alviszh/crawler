package testLogin;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

public class TestLogon {
	
	
	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";
	
	static Boolean headless = false;

	public static void main(String[] args) throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		driver.manage().window().fullscreen();
		String baseUrl = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		driver.get(baseUrl);  
		Thread.sleep(1100); 
		
//		((JavascriptExecutor)driver).executeScript("document.getElementById(\"dt_notice\").style ='width: 100%;';");
		WebElement slide = driver.findElement(By.id("WAP_login_password_slide"));
		String attribute = slide.getAttribute("style");
		System.out.println("属性是="+attribute);
		if(attribute.contains("display: none;")){
			System.out.println("98754没有滑动验证码");
		}else{
			System.out.println("12356有滑动验证码");
			driver.navigate().refresh();
			Thread.sleep(1000);
		}
		WebElement numLogin = driver.findElement(By.name("WAP_login_message_paslog"));
		WebElement loginBtn = driver.findElement(By.name("WAP_login_password_logsubmit"));
		WebElement smsLogin = driver.findElement(By.name("WAP_login_password_meslog"));
		numLogin.click();
		
		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		System.out.println("开始输入,账号密码");
		username.sendKeys("17600325986");
		Thread.sleep(1000);
		password.sendKeys("wang1992");
		Thread.sleep(1000);										
		WebElement slide2 = driver.findElement(By.id("WAP_login_password_slide"));
		String attribute2 = slide2.getAttribute("style");
		
		System.out.println("属性是="+attribute2);
		if(attribute2.contains("display: none;")){
			System.out.println("账号密码登录没有滑动验证码");
		}else{
			WebElement siller2 = slide2.findElement(By.id("siller2"));
			WebElement knob = siller2.findElement(By.className("dt_child_content_knob"));
			knob.click();
			int a = 0;
			for (int i = 0; i < 3; i++) {
				if(!siller2.getText().contains("验证通过")){
					knob.click();
					WebElement source = siller2.findElement(By.className("dt_child_content_knob_move_back"));
				    Actions action = new Actions(driver);
				    for (int j = 0; j < 150; j++) {
				    	action.clickAndHold(source).moveByOffset(10+(int)(Math.random()), (int)(Math.random()));
			            Thread.sleep(18);
					}
				    Thread.sleep((int)(Math.random())*10+1000);
		            //拖动完释放鼠标
		            action.moveToElement(source).release();
			        //组织完这些一系列的步骤，然后开始真实执行操作
			        Action actions = action.build();
			        actions.perform();
					
					/*
//					WebElement moveSlider = driver.findElement(By.xpath("//*[@id=\"siller1_dt_child_content_containor\"]/div[3]"));
					
					Point location = knob.getLocation();
					System.out.println(location.getX()+","+location.getY());
					Robot robot = new Robot();
					robot.mouseMove(10+location.getX(), location.getY()+60);
					robot.mousePress(KeyEvent.BUTTON1_MASK);
					Thread.sleep(1500);
					for (int b = 0; b < 160; b++) {
						location = siller2.findElement(By.className("dt_child_content_knob")).getLocation();
						robot.mouseMove((int)(Math.random() )+20+location.getX(), location.getY()+60+(int)(Math.random()));
						Thread.sleep(20);
					}
					Thread.sleep(1500);
//					robot.mousePress(KeyEvent.BUTTON3_MASK);
					robot.mouseRelease(KeyEvent.BUTTON1_MASK);*/
					
			        Thread.sleep(2000);
			        siller2 = slide2.findElement(By.id("siller2"));
			        if(siller2.getText().contains("验证通过")){
			        	loginBtn.click();
			        	break;
			        }else{
			        	a++;
			        }
			        Thread.sleep(1500);
				}else{
					loginBtn.click();
				}
			}
			System.out.println(a+"");
//		    (int)(Math.random()*5)+
			/*//确保每次拖动的像素不同，故而使用随机数
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);*/
            
		}
		
	}
	
	
	
	
	
	
	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

}
