package test;

import java.io.IOException;


/**
 * @description:
 * @author: sln 
 * @date: 2017年11月23日 上午11:25:14 
 */
public class KillProcess {
	public static void main(String[] args) throws IOException {
//		WindowsUtils.killByName("iexplore.exe");
//		WindowsUtils.killPID("7972");
		Runtime runTime = Runtime.getRuntime();
	    runTime.exec("TASKKILL /F /IM iexplore.exe");
        runTime.exec("TASKKILL /F /IM IEDriverServer.exe");
	}
}
