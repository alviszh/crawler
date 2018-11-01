package app.test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;

/**
 * @description:  分辨率
 * @author: sln 
 * @date: 2017年10月11日 下午4:49:39 
 */
public class Mainpx {
	public static void main(String[] args) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int width = (int)screensize.getWidth(); 
		int height = (int)screensize.getHeight(); 
		System.out.println(width); 
		System.out.println(height);
	}
}
