package text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;    
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;    
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;    
import java.net.URL;   
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
public class login2 {    
	public static void readFdf(String file) throws Exception {    
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
	/**   
	 * @param args   
	 */   
	public static void main(String[] args) {    
		// TODO Auto-generated method stub    
		try {    
			// 取得F盘下的SpringGuide.pdf的内容   
		//	saveUrlAs("https://223.100.185.98/wt-web/person/jcmx?yhkh=2000-01-01,2018-06-30", "f:\\a.pdf", "GET");
			readFdf("f:\\file\\a.pdf");
			File file = new File("f:\\file\\a.txt");
			String readTxtFile = readTxtFile(file);
			System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile);
			
			String[] split = readTxtFile.split("\n");
			for(int i=3;i<split.length-2;i++){
				String liushuihao = null;
				String jiefan = null;
				String daifan = null;
				String yu = null;
				String zai1 = null;
				String ri = null;
				
				String string = split[i];
				String[] split2 = string.split(" ");
				if(split2.length>5){
					if(split2.length!=7){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					jiefan = split2[2];//借方金额
					daifan = split2[3];//贷方金额
					yu = split2[4];//余额
					zai1 = split2[5];//摘要
					if(split2.length>6){
						zai1 = zai1 + split2[split2.length-1].toString();
					}
					String[] split3 = split2[1].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					}
				}
				if(split2.length<6){
					if(split2.length!=4&&split2.length!=5){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					jiefan = split2[1];//借方金额
					daifan = split2[2];//贷方金额
					yu = split2[3];//余额
					zai1 = split2[4];//摘要
					String[] split3 = split2[0].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					}
					if(split2.length==4){
						//[11006170101_002658462010-06-17, 0.00, 550.00, 11,568.69补缴201001至201005公积金]
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[0].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[1];//借方金额
						daifan = split2[2];//贷方金额
						String string3 = split2[3];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);
					}
					if(split2.length==5&&split2[0].length()<27){
						//[11605050101_0045219520, 6-05-05, 1,540.68, 0.00, 56,417.36部分提取(逐月提取公积金]	
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[1].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[2];
						daifan = split2[3];
						String string3 = split2[4];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);
						
					}
					if(split2.length==5&&split2[0].length()>27){
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String substring = split2[0].substring(20, split2[0].length());
						String[] split3 = substring.split("-");
						ri = split3[1]+ "-" + split3[2].toString();//日期
						jiefan = split2[1];
						daifan = split2[2];
						yu=split2[3];
						zai1=split2[4];
					}
				}
				//10903170101_000508922 0 -03-17 0.00 604.00 31,125.33 汇缴200902公积金

				if(split2.length==7){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					String[] split3 = split2[2].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					jiefan = split2[3];//借方金额
					daifan = split2[4];//贷方金额
					yu = split2[5];//余额
					zai1 = split2[6];//摘要
				}
				
				System.out.println(">>>"+i+".读取出来的文件内容是："
				+"\r\n业务流水号:"+liushuihao+"\n 日期:"+ri+"\n借方金额:"+jiefan+"\n贷方金额:"+daifan+"\n余额:"+yu+"\n摘要:"+zai1);
			}
			
			
			
			
		} catch (Exception e) {   
			e.printStackTrace();    
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



	public static File saveUrlAs(String url,String filePath,String method){  
		//System.out.println("fileName---->"+filePath);  
		//创建不同的文件夹目录  
		File file=new File(filePath);  
		//判断文件夹是否存在  
		if (!file.exists())  
		{  
			//如果文件夹不存在，则创建新的的文件夹  
			file.mkdirs();  
		}  
		FileOutputStream fileOut = null;  
		HttpURLConnection conn = null;  
		InputStream inputStream = null;  
		try  
		{  
			// 建立链接  
			URL httpUrl=new URL(url);  
			conn=(HttpURLConnection) httpUrl.openConnection();  
			//以Post方式提交表单，默认get方式  
			conn.setRequestMethod(method);  
			conn.setDoInput(true);    
			conn.setDoOutput(true);  
			// post方式不能使用缓存   
			conn.setUseCaches(false);  
			//连接指定的资源   
			conn.connect();  
			//获取网络输入流  
			inputStream=conn.getInputStream();  
			BufferedInputStream bis = new BufferedInputStream(inputStream);  
			//判断文件的保存路径后面是否以/结尾  
			if (!filePath.endsWith("/")) {  

				filePath += "/";  

			}  
			//写入到文件（注意文件保存路径的后面一定要加上文件的名称）  
			fileOut = new FileOutputStream(filePath+"b.pdf");  
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);  

			byte[] buf = new byte[4096];  
			int length = bis.read(buf);  
			//保存文件  
			while(length != -1)  
			{  
				bos.write(buf, 0, length);  
				length = bis.read(buf);  
			}  
			bos.close();  
			bis.close();  
			conn.disconnect();  
		} catch (Exception e)  
		{  
			e.printStackTrace();  
			System.out.println("抛出异常！！");  
		}  

		return file;  

	}  
}
