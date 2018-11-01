package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@Component
public class QrcodeService {

	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	public static final String FORMAT = "png";
	public static final String CHARTSET = "utf-8";

	/**
	 * 
	 * @Title:getQRresult
	 * @Description:读取二维码
	 * @param filePath
	 * @return
	 * @author doubledumbao
	 * @修改时间：2018年2月26日 上午9:45:19
	 * @修改内容：创建
	 */
	public static Result getQRresult(String filePath) {
		/**
		 * 如果用的jdk是1.9，需要配置下面这一行。
		 */
		// System.setProperty("java.specification.version", "1.9");
		Result result = null;
		try {
			File file = new File(filePath);
			BufferedImage bufferedImage = ImageIO.read(file);
			BinaryBitmap bitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
			HashMap hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
			result = new MultiFormatReader().decode(bitmap, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	
	/**   
	  *    
	  * 项目名称：common-microservice-ocr  
	  * 所属包名：app.service
	  * 类描述：   
	  * 创建人：hyx 
	  * 创建时间：2018年8月28日 
	  * @version 1  
	  * 返回值    Result
	  */
	public static Result getQRresult(BufferedImage bufferedImage) {
		/**
		 * 如果用的jdk是1.9，需要配置下面这一行。
		 */
		// System.setProperty("java.specification.version", "1.9");
		Result result = null;

		try {

			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
			HashMap hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
			result = new MultiFormatReader().decode(bitmap, hints);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
