package test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.ChaoJiYingUtils;

import app.service.ChaoJiYingOcrService;

public class Test {
	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String url = "C:\\Users\\Administrator\\git\\strong-auth-crawler-zh\\parent\\parent-microservice\\parent-microservice-bank\\common-microservice-bank-spdb-creditcard\\snapshot\\201805.PDF";
		String url = "C:\\Users\\Administrator\\Downloads\\李世雄先生201804账单.PDF";
		String readFdf4 = readFdf(url);
		File file5 = new File(readFdf4);
		String readTxt = readTxtFile(file5);
//		String a = readTxt.substring(readTxt.indexOf("Charges")+8);
//		a = a.substring(0,a.indexOf("本期账务明细")).replaceAll("\r|\n", "");;
//		String[] c = a.split(" ");
//		for(int i = 0;i<c.length;i++){
//			System.out.println("c："+c[i]);  
//		}
//		System.out.println("a："+c.length);  
//		String b = readTxt.substring(readTxt.lastIndexOf("Due")+4);
//		b = b.substring(0,b.indexOf("当前欠款"));
//		System.out.println("b："+b);  
//		System.out.println("读取出来的文件内容是："+"\r\n"+readTxt);  
		readTxt = readTxt.substring(readTxt.indexOf("Amount")+6);
		readTxt = readTxt.substring(0,readTxt.indexOf("本期积分情况"));
//		System.out.println("读取出来的文件内容是11111："+"\r\n"+readTxt);  
		String[] txt = readTxt.split("\n");
//		System.out.println("读取出来的文件内容是11111："+"\r\n"+txt[68]);  
//		if(txt[68].contains("账单分期")){
//			String[] txt1 = txt[68].toString().split(" ");
//			String s = txt[68].substring(txt[68].indexOf(" ")).trim();
//			s = s.substring(s.indexOf(" ")).trim();
//			s = s.substring(0,s.lastIndexOf(" "));
//			s = s.substring(0,s.lastIndexOf(" ")).replace(" ", "");
//			String ss = s.substring(s.indexOf("第"));
//			ss = ss.substring(1,ss.indexOf("期"));
//			String sss = s.substring(s.indexOf("共"));
//			sss = sss.substring(1,sss.indexOf("期"));
//			System.out.println("11111："+txt1[0]);
//			System.out.println("11111："+txt1[1]);
//			System.out.println("11111："+s);
//			System.out.println("11111："+txt1[txt1.length-2]);
//			System.out.println("11111："+txt1[txt1.length-1]);
//			System.out.println("11111："+ss);
//			System.out.println("11111："+sss);
//		}
		if(txt.length>0){
			for(int i = 1;i <txt.length;i++){
				
				try {
					String[] txt1 = txt[i].toString().split(" ");
					String s = txt[i].substring(txt[i].indexOf(" ")).trim();
					s = s.substring(s.indexOf(" ")).trim();
					s = s.substring(0, s.lastIndexOf(" "));
					s = s.substring(0, s.lastIndexOf(" ")).replace(" ", "");
				} catch (Exception e) {
					System.out.println("读取出来的文件内容是11111："+"\r\n"+txt[i]); 
					String txt1 = txt[i].toString().replace(" ", "");
					String a = txt1.substring(0,8);
					String b = txt1.substring(8,16);
					String c = txt1.substring(16,txt1.lastIndexOf("¥")-4);
					String g = txt1.substring(16,txt1.lastIndexOf("¥"));
					String d = g.substring(g.length()-4);
					String f = txt1.substring(txt1.lastIndexOf("¥"));
					System.out.println("11111："+a);
					System.out.println("11111："+b);
					System.out.println("11111："+c);
					System.out.println("11111："+d);
					System.out.println("11111："+f);
					
				}
				
			}
		}
//			
//			
//		
//		System.out.println("读取出来的文件内容是11111："+"\r\n"+txt[71]);
//		for(int i = 0;i<=18;i++){
//			String month=getBeforeMonth(i);
//			System.out.println(month);
//			
//		}
		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		// 图片请求
////		String loginurl = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action";
////		WebRequest webRequest = new WebRequest(new URL(loginurl), HttpMethod.GET);
////		Page html = webClient.getPage(webRequest);
//		String codeurl = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/validateCode";
//		WebRequest webRequest = new WebRequest(new URL(codeurl), HttpMethod.GET);
//		Page html = webClient.getPage(webRequest);
//		String path = getPathBySystem();
//		String imgagePath=getImagePath(html,path);
//		System.out.println(imgagePath);
//		String verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1004"); 
//		System.out.println(verifycode);
	}
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

//	/**
//	 * @Description: 调用超级鹰服务(本地)
//	 * @param  imgPath
//	 * @param  codeType
//	 * @return String 
//	 * @throws
//	 */
//	public String callChaoJiYingService(String imgPath, String codeType){
//		Gson gson = new GsonBuilder().create();
//		long starttime = System.currentTimeMillis();
//		String chaoJiYingResult = super.getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);
//		
//		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);		
//		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
//			tracer.addTag("errNo ======>>",errNo);
//			return ChaoJiYingUtils.getErrMsg(errNo);
//		}
//		
//		tracer.addTag("ChaoJiYingResult [CODETYPE={}]: {}", chaoJiYingResult);
//		long endtime = System.currentTimeMillis();
//		tracer.addTag("超级鹰识别耗时 callChaoJiYingService",(endtime-starttime)+"ms");
//		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//		
//	}
	/**
	 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
	 * 利用IO流保存验证码成功后，将完整路径信息一并返回
	 * 
	 * @param page
	 * @param imagePath
	 * @return
	 * @throws Exception
	 */
	public static  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) {  
	        int temp = 0;  
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}
	
	
	//从当日期开始往前推n月
    public static String getBeforeMonth(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -n);
        String beforeMonth = f.format(c.getTime());
        return beforeMonth;
    }
    
    public static String readFdf(String file) throws Exception {    
		// 是否排序    
		boolean sort = false;
		// pdf文件名    
		String pdfFile = file;
		// 输入文本文件名称    
		String textFile = null;
		// 编码方式    
		String encoding = "UTF-8";    
		// 开始提取页数    
		int startPage = 1;    
		// 结束提取页数    
		int endPage = Integer.MAX_VALUE;    
		// 文件输入流，生成文本文件    
		Writer output = null;    
		// 内存中存储的PDF Document    
		PDDocument document = null;    
		try {    
			try {    
				// 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件    
				URL url = new URL(pdfFile);    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				System.out.println("document"+document);
				// 获取PDF的文件名    
				String fileName = url.getFile();    
				// 以原来PDF的名称来命名新产生的txt文件    
				if (fileName.length() > 4) {    
					File outputFile = new File(fileName.substring(0, fileName    
							.length() - 4)    
							+ ".txt");
					textFile = outputFile.getName();    
				}    
			} catch (MalformedURLException e) {    
				// 如果作为URL装载得到异常则从文件系统装载    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				if (pdfFile.length() > 4) {    
					textFile = pdfFile.substring(0, pdfFile.length() - 4)    
							+ ".txt";    
				}    
			}    
			// 文件输入流，写入文件倒textFile    
			output = new OutputStreamWriter(new FileOutputStream(textFile),    
					encoding);    
			// PDFTextStripper来提取文本    
			PDFTextStripper stripper = null;    
			stripper = new PDFTextStripper();    
			// 设置是否排序    
			stripper.setSortByPosition(sort);    
			// 设置起始页    
			stripper.setStartPage(startPage);    
			// 设置结束页    
			stripper.setEndPage(endPage);    
			// 调用PDFTextStripper的writeText提取并输出文本    
			stripper.writeText(document, output);    
			System.out.println(textFile);
			return textFile;
		} finally {    
			if (output != null) {    
				// 关闭输出流    
				output.close();    
			}    
			if (document != null) {    
				// 关闭PDF Document    
				document.close();    
			}    
		}    
	}
    
    public static String readTxtFile(File fileName)throws Exception{  
		String result=null;  
		FileReader fileReader=null;  
		BufferedReader bufferedReader=null;  
		try{  
			fileReader=new FileReader(fileName);  
			bufferedReader=new BufferedReader(fileReader);  
			try{  
				String read=null;  
				while((read=bufferedReader.readLine())!=null){  
					result=result+read+"\r\n";  
				}  
			}catch(Exception e){  
				e.printStackTrace();  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			if(bufferedReader!=null){  
				bufferedReader.close();  
			}  
			if(fileReader!=null){  
				fileReader.close();  
			}  
		}  

		return result;  
	} 
    
    
}
