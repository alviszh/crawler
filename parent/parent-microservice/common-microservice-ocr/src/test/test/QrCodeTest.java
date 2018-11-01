package test;

import com.google.zxing.Result;

import app.service.QrcodeService;

public class QrCodeTest {
	
	static String filePath = "D:\\img\\show.png";

	public static void main(String[] args) {
		Result result = QrcodeService.getQRresult(filePath);
		
		String text = result.getText();
		
		System.out.println("text---"+text);

	}

}
