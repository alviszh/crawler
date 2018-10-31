package com.module.jna.webdriver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class WebDriverUnit extends AbstractChaoJiYingHandler {

	//private static final String OCR_FILE_PATH = "/home/img";

	public static File getImageCustomPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;

	}

/*	public static File getImageLocalPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}*/

	/**
	 * 
	 * 项目名称：common-module-selenium 所属包名：com.module.jna.webdriver 类描述：
	 * 根据系统的不同获取保存路径并创建文件夹 创建人：hyx 创建时间：2017年11月27日
	 * 
	 * @version 1 返回值 File
	 */
	public static File getImageLocalPathBySystem() throws Exception {
		String path = getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;
	}

	// 全屏截取图片
	public static String saveScreenshotByPath(WebDriver driver) throws Exception {
		// 获取当前jar的绝对路径
		String path = WebDriverUnit.class.getProtectionDomain().getCodeSource().getLocation().toString();
		System.out.println("saveScreenshotByPath-------" + path);
		saveScreenshotByPath(driver, path);
		return path;
	}

	// 全屏截取图片
	public static String saveScreenshotByPath(WebDriver driver, String path) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File file = getImageCustomPath(path);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	// 全屏截取图片 获取当前目录下的路径
	public static String saveScreenshotByPath(WebDriver driver, Class<?> clas) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// String path =
		// clas.getProtectionDomain().getCodeSource().getLocation().getPath()+"snapshot/";
		// String path = System.getProperty("user.dir") + "/snapshot/";
		String path = getPathBySystem();
		System.out.println("截图保存路径： " + path);
		File file = getImageCustomPath(path);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

/*	// 全屏截取图片
	public static String saveImg(WebDriver driver) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}*/

/*	// 根据dom节点截取图片
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}*/

	/**
	 * 
	 * 项目名称：common-module-selenium 所属包名：com.module.jna.webdriver 类描述：
	 * 根据dom节点截取图片 自定义存放路径 创建人：hyx 创建时间：2017年11月23日
	 * 
	 * @version 1 返回值 String
	 */
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		String path = getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File file = getImageCustomPath(path);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}
	
	
	/**
	 * 
	 * 项目名称：common-module-selenium 所属包名：com.module.jna.webdriver 类描述：
	 * 根据dom节点截取图片 自定义存放路径 创建人：zz
	 * 增加x,y
	 * @version 1 返回值 String
	 */
	public static String saveImg(WebDriver driver, By selector, int pX, int pY) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		String path = getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File file = getImageCustomPath(path);
		BufferedImage eleScreenshot = fullImg.getSubimage(pX+point.getX(), pY+point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}
	/**
	 *
	 * 项目名称：common-module-selenium 所属包名：com.module.jna.webdriver 类描述：
	 * 根据dom节点截取图片 自定义存放路径 创建人：zz
	 * 增加x,y
	 * @version 1 返回值 String
	 */
	public static String saveImg(WebDriver driver, By selector, int pX, int pY, String imgType) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		String path = getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File file = getImageCustomPath(path);
		BufferedImage eleScreenshot = fullImg.getSubimage(pX+point.getX(), pY+point.getY(), eleWidth-pX, eleHeight-pY);
		ImageIO.write(eleScreenshot, imgType, screenshot);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}
	

	// 根据dom节点截取图片 自定义存放路径
	public static String saveImg(WebDriver driver, By selector, File file) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	/*// 自定义起始焦点的基础上增加自定义的坐标
	public static String saveImg(WebDriver driver, By selector, int addX, int addY) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX() + addX, point.getY() + addY, eleWidth,
				eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}*/

	/*// 自定义起始焦点的基础上增加自定义的坐标
	public static String saveImg(WebDriver driver, String parentFrameName, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(By.name(parentFrameName));
		Point point = ele.getLocation();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		int pX = point.getX();
		int pY = point.getY();
		driver.switchTo().frame(parentFrameName);

		WebElement targetEle = driver.findElement(selector);
		Point targetPoint = targetEle.getLocation();
		int targetWidth = targetEle.getSize().getWidth();
		int targetHeight = targetEle.getSize().getHeight();

		BufferedImage eleScreenshot = fullImg.getSubimage(pX + targetPoint.getX(), pY + targetPoint.getY(), targetWidth,
				targetHeight);

		ImageIO.write(eleScreenshot, "jpg", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}*/
	// 自定义起始焦点的基础上增加自定义的坐标
		public static String saveImg(WebDriver driver, String parentFrameName, By selector) throws Exception {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullImg = ImageIO.read(screenshot);
			// Get the location of element on the page
			WebElement ele = driver.findElement(By.name(parentFrameName));
			Point point = ele.getLocation();
			// Crop the entire page screenshot to get only element screenshot
			System.out.println("point.getX()-------" + point.getX());
			System.out.println("point.getY()-------" + point.getY());
			int pX = point.getX();
			int pY = point.getY();
			driver.switchTo().frame(parentFrameName);

			WebElement targetEle = driver.findElement(selector);
			Point targetPoint = targetEle.getLocation();
			int targetWidth = targetEle.getSize().getWidth();
			int targetHeight = targetEle.getSize().getHeight();

			BufferedImage eleScreenshot = fullImg.getSubimage(pX + targetPoint.getX(), pY + targetPoint.getY(), targetWidth,
					targetHeight);

			ImageIO.write(eleScreenshot, "jpg", screenshot);
			File file = getImageLocalPathBySystem();
			FileUtils.copyFile(screenshot, file);
			return file.getAbsolutePath();
		}
	

	/*// 自定义起始焦点和宽、高
	public static String saveImg(WebDriver driver, By selector, int pX, int pY, int eW, int eH) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + pX);
		System.out.println("point.getY()-------" + pY);
		System.out.println("eleWidth-------" + eW);
		System.out.println("eleHeight-------" + eH);
		BufferedImage eleScreenshot = fullImg.getSubimage(pX, pY, eW, eH);
		ImageIO.write(eleScreenshot, "jpg", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}*/

	/**
	 * 
	 * 项目名称：common-module-selenium 所属包名：com.module.jna.webdriver 类描述：
	 * 根据环境不同获取不同的 创建人：hyx 创建时间：2017年11月23日
	 * 
	 * @version 1 返回值 String
	 */
	public static String getPathBySystem() {

		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			System.out.println("path:"+path);
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			System.out.println("path:"+path);
			return path;
		}

	}

	public static String getPathBySystem(String folderName) {
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/"+folderName+"/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/"+folderName+"/";
			return path;
		}

	}

	public static void main(String[] args) {
		System.out.println("java_vendor:" + System.getProperty("java.vendor"));
		System.out.println("java_vendor_url:" + System.getProperty("java.vendor.url"));
		System.out.println("java_home:" + System.getProperty("java.home"));
		System.out.println("java_class_version:" + System.getProperty("java.class.version"));
		System.out.println("java_class_path:" + System.getProperty("java.class.path"));
		System.out.println("os_name:" + System.getProperty("os.name"));
		System.out.println("os_arch:" + System.getProperty("os.arch"));
		System.out.println("os_version:" + System.getProperty("os.version"));
		System.out.println("user_name:" + System.getProperty("user.name"));
		System.out.println("user_home:" + System.getProperty("user.home"));
		System.out.println("user_dir:" + System.getProperty("user.dir"));
		System.out.println("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
		System.out.println("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
		System.out.println("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
		System.out.println("java_vm_version:" + System.getProperty("java.vm.version"));
		System.out.println("java_vm_vendor:" + System.getProperty("java.vm.vendor"));
		System.out.println("java_vm_name:" + System.getProperty("java.vm.name"));
		System.out.println("java_ext_dirs:" + System.getProperty("java.ext.dirs"));
		System.out.println("file_separator:" + System.getProperty("file.separator"));
		System.out.println("path_separator:" + System.getProperty("path.separator"));
		System.out.println("line_separator:" + System.getProperty("line.separator"));
		System.out.println("os.name:" + System.getProperty("os.name"));

	}
}
