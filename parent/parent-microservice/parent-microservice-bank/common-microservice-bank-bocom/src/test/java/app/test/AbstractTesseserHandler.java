/**
 * 
 */
package app.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kingly
 * @date 2016年1月5日
 * 
 */
public class AbstractTesseserHandler {
	private static final String TESSERACT_ROOT = "D:/Tesseract-OCR"; // "D:/Tesseract-OCR";
	public static final String TESSERACT_LANGUAGE = "num";
	private long lastUpdateTime;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTesseserHandler.class);

	/**
	 * @param imageFile
	 *            图片
	 * @param resultFile
	 *            结果result.txt文件
	 * @return 识别字符串结果
	 * @throws Exception
	 */
	public static String getVerifycode(File imageFile) throws Exception {
		File resultFile = new File(imageFile.getAbsolutePath()
				.replace(imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1), "txt"));
		// 文件存在则记录上一次文件更新时间
		/*
		 * if (resultFile.exists()) { lastUpdateTime =
		 * resultFile.lastModified(); }
		 */

		// 根据系统的不同，执行相应系统的命令调用tesseract-OCR
		final String system = System.getProperty("os.name").toLowerCase();
		String resultFileName = resultFile.getAbsolutePath().replace(".txt", "");
		if (system.contains("linux")) {
			String[] cmdA = { "/bin/sh", "-c", TESSERACT_ROOT + "/tesseract " + imageFile.getAbsolutePath() + " "
					+ resultFileName + " -l " + TESSERACT_LANGUAGE };
			Process process = Runtime.getRuntime().exec(cmdA);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			} // System.out.println(sb.toString());
		} else if (system.contains("windows")) {
			Runtime r = Runtime.getRuntime();
			
			  
			//String command = TESSERACT_ROOT + "/tesseract.exe " + "D:\\home\\img\\tr\\bocom.tif  D:\\home\\img\\tr\\bocom batch.nochop makebox";
			String command = TESSERACT_ROOT + "/tesseract.exe " + imageFile.getAbsolutePath() + " " + resultFileName
					+ " -l fontyp"  ;
			System.out.println(command);
			Process p = r.exec(command);
			long startTime = System.currentTimeMillis();
			System.out.println(p.waitFor());
		}

		// 只要result.txt文件不存在 或 result.txt文件没有更新 则等待，等待时间超过5s则break！
		
	/*	while (!resultFile.exists()) {
			System.out.println("验证码识别中...");
			Thread.sleep(1000);
			if (System.currentTimeMillis() - startTime > 5000) {
				System.out.println("验证码识别超时！");
				break;
			}
		}*/

		String result = FileUtils.readFileToString(resultFile, "UTF-8");
		result = replaceBlank(result).trim();
		return result;
	}
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * String command =
		 * "E://Tesseract-OCR//tesseract.exe D:\\Tesseract-OCR\\03.png 4 -l";
		 * Runtime r = Runtime.getRuntime(); System.out.println(command);
		 * r.exec(command);
		 */

	/*	Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("D://Tesseract-OCR//tesseract.exe D:\\home\\img\\tr\\03.png D:\\home\\img\\tr\\4 -l");
	
		System.out.println(p.waitFor());
		p.exitValue();*/
		String path = "D:\\home\\img\\tr\\langyp.fontyp.exp0.tif";
		getVerifycode(new File(path));
	}

}
