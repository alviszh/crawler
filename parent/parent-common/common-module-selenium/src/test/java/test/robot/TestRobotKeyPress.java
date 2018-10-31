package test.robot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class TestRobotKeyPress {

	public static void main(String[] args) throws AWTException, IOException {
		Robot robot = new Robot();
		// 设置Robot产生一个动作后的休眠时间,否则执行过快
		robot.setAutoDelay(1000);
		
		 //获取屏幕分辨率
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(d);
        
        Runtime.getRuntime().exec("notepad"); 
        robot.keyPress(KeyEvent.VK_I);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_L);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyPress(KeyEvent.VK_E);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_Y);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyPress(KeyEvent.VK_U);
        
        

	}

}
