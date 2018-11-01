package test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) throws AWTException {
        StringSelection stringSelection = new StringSelection("copy");
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);

        Robot robot = new Robot();
        //System.setProperty("java.awt.headless", "true");
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}