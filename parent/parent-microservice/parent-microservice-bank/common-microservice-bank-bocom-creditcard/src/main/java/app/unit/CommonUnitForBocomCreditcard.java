package app.unit;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.bank.json.BankJsonBean;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.ImageHandler;

/**
 * 
 * 项目名称：common-microservice-bank-bocom-creditcard 类名称：CommonUnit 类描述： 创建人：hyx
 * 创建时间：2017年11月22日 下午5:56:13
 * 
 * @version
 */
public class CommonUnitForBocomCreditcard {

	public static final Set<Integer> EXCLUDED_COLOR_RGBS = new HashSet<Integer>();

	static {
		EXCLUDED_COLOR_RGBS.add(new Color(147, 196, 236).getRGB());
		EXCLUDED_COLOR_RGBS.add(new Color(110, 110, 110).getRGB());
	}

	public static final HashMap<String, String> map = new HashMap<String, String>();

	static {
		map.put("q", "10");
		map.put("w", "11");
		map.put("e", "12");
		map.put("r", "13");
		map.put("t", "14");
		map.put("y", "15");
		map.put("u", "16");
		map.put("i", "17");
		map.put("o", "18");
		map.put("p", "19");
		map.put("a", "20");
		map.put("s", "21");
		map.put("d", "22");
		map.put("f", "23");
		map.put("g", "24");
		map.put("h", "25");
		map.put("j", "26");
		map.put("k", "27");
		map.put("l", "28");
		map.put("z", "29");
		map.put("x", "30");
		map.put("c", "31");
		map.put("v", "32");
		map.put("b", "33");
		map.put("n", "34");
		map.put("m", "35");
		map.put("Q", "36");
		map.put("W", "37");
		map.put("E", "38");
		map.put("R", "39");
		map.put("T", "40");
		map.put("Y", "41");
		map.put("U", "42");
		map.put("I", "43");
		map.put("O", "44");
		map.put("P", "45");
		map.put("A", "46");
		map.put("S", "47");
		map.put("D", "48");
		map.put("F", "49");
		map.put("G", "50");
		map.put("H", "51");
		map.put("J", "52");
		map.put("K", "53");
		map.put("L", "54");
		map.put("Z", "55");
		map.put("X", "56");
		map.put("C", "57");
		map.put("V", "58");
		map.put("B", "59");
		map.put("N", "60");
		map.put("M", "61");
		map.put("\"", "62");
		map.put(";", "63");
		map.put("~", "64");
		map.put("'", "66");
		map.put(".", "67");
		map.put("@", "68");
		map.put("+", "69");
		map.put("\\", "70");
		map.put("$", "71");
		map.put(",", "72");
		map.put("&", "73");
		map.put("/", "74");
		map.put(">", "75");
		map.put("{", "76");
		map.put("^", "77");
		map.put("*", "78");
		map.put("(", "79");
		map.put("!", "80");
		map.put("`", "81");
		map.put("-", "82");
		map.put("_", "83");
		map.put("}", "84");
		map.put("?", "85");
		map.put("%", "86");
		map.put("|", "87");
		map.put("[", "88");
		map.put(":", "89");
		map.put(" ", "90");
		map.put("=", "91");
		map.put("#", "92");
		map.put("<", "93");
		map.put(")", "94");
	}

	public static String getPot(String potcode, String password, int k) {
		String pot = "";
		char[] strs = potcode.toCharArray();

		for (int i = 0; i < strs.length; i++) {
			map.put(strs[i] + "", i + "");
			System.out.println(strs[i] + "----" + i);
		}

		char[] passwds = password.toCharArray();

		for (char passwd : passwds) {
			String mapValue = map.get(passwd + "");
			System.out.println("密码：" + passwd + "-----mapValue----下标：" + mapValue);
			pot += mapValue + ",";
		}

		if (pot.endsWith(",")) {
			pot = pot.substring(0, pot.length() - 1);
		}

		if (pot.length() <= 0) {
			throw new RuntimeException("Tesseract 解析密码错误 code 长度不能小于或等于0位：" + potcode);

		}
		return pot;
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.unit 类描述：
	 * 根据dom节点截取图片 根据系统不同区分存储路径 创建人：hyx 创建时间：2017年11月27日
	 * 
	 * @version 1 返回值 String
	 */
	public static String saveImgXY(WebDriver driver, By startSelector, By endSelector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement startEle = driver.findElement(startSelector);
		WebElement endEle = driver.findElement(endSelector);
		Point startPoint = startEle.getLocation();
		Point endPoint = endEle.getLocation();
		// Get width and height of the element
		int eleWidth = startEle.getSize().getWidth();
		int eleHeight = startEle.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("startPoint.getX()-------" + startPoint.getX());
		System.out.println("startPoint.getY()-------" + startPoint.getY());
		System.out.println("endPoint.getX()-------" + endPoint.getX());
		int width = endPoint.getX() - startPoint.getX() + endEle.getSize().getHeight();
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		System.out.println("width-------" + width);
		BufferedImage eleScreenshot = fullImg.getSubimage(startPoint.getX(), startPoint.getY(), width, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = WebDriverUnit.getImageLocalPathBySystem();
		FileUtils.copyFile(screenshot, file);

		ImageHandler.imagePreHandle(file, EXCLUDED_COLOR_RGBS);
		return file.getAbsolutePath();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static boolean isSixNum(String mobiles) {
		Pattern p = Pattern.compile("\\d{6}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断是否为邮箱
	 * 
	 * @author zp
	 * @param mobiles
	 * @return
	 */
	public static boolean isEmailNO(String emails) {
		String reg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(emails);
		return m.matches();
	}

	public static void main(String[] args) {
		String mobiles = "wl1321";

//		System.out.println(isMobileNO(mobiles));
		System.out.println(isSixNum(mobiles));
	}

}
