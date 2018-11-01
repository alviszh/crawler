package org.common.microservice.bank.cebchina;

import java.io.IOException;

public class KillTest {
	public static void main(String[] args) throws IOException {
		Runtime runTime = Runtime.getRuntime();
		runTime.exec("TASKKILL /F /IM iexplore.exe"); 
//		runTime.exec("TASKKILL /F /IM IEDriverServer.exe");
	}
}
