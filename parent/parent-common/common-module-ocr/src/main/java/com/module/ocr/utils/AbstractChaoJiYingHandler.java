package com.module.ocr.utils;

import java.io.File;

public abstract class AbstractChaoJiYingHandler implements IVerifycodeHandler {
	//  codetype 参考：       http://www.chaojiying.com/price.html
	private static final String USERNAME = "harmonycredit";		//"md19841002";
	private static final String PASSWORD = "HcPcPt20160919";	//"xhhcxy168";		//"12qwaszx"; 
	private static final String SOFTID = "892052";				//"891477"; 
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static String getVerifycodeByChaoJiYing(String codetype, String len_min, String time_add, String str_debug, String filePath) {
		return ChaoJiYingUtils.PostPic(USERNAME, PASSWORD, SOFTID, codetype, len_min, time_add, str_debug, filePath);
	}
	
	public String getVerifycodeByChaoJiYingByByte(String codetype, String len_min, String time_add, String str_debug, byte[] byteArr) {
		return ChaoJiYingUtils.PostPic(USERNAME, PASSWORD, SOFTID, codetype, len_min, time_add, str_debug, byteArr);
	}
	
	public static String getVerifycodeByChaoJiYing(String codetype,  String filePath) {
		return ChaoJiYingUtils.PostPic(USERNAME, PASSWORD, SOFTID, codetype, LEN_MIN, TIME_ADD, STR_DEBUG, filePath);
	}
	

	@Override
	public String getVerifycode(File file) throws Exception {
		return null;
	}

	@Override
	public String getVerifycode(File file, String codeType) throws Exception {
		return null;
	}
	
}
