package test.ocr.swagger;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestOcr {
	
	 /** 
     * 文件位置放在，项目同一路径  OCR程序
     */  
    public static String tessPath = "D:\\Prj\\Tesseract-OCR\\tesseract.exe";
    /**
	 * 语言选项
	 */
	private final String LANG_OPTION = "-l";
	/**
	 * 输出文本选项
	 */
	private final String PAGESEGMODE_OPTION = "-psm";
	
	
	public static void main(String[] args) {
		String filepath = "D:\\Prj\\tesstemp\\show.jpg";
		File file = new File(filepath);
		String ocrTxt = new TestOcr().getOcrTxt(file);
		System.out.println(ocrTxt);
	}
	
	/**
	 * 将图片解析成ocr文本
	  * @Description
	  * @param imageFile
	  * @return
	  * @author dengyong
	  * @date 2018年1月18日 下午3:18:51
	 */
	private String getOcrTxt(File imageFile){
		/** 
         * 采用
         * 设置输出txt文件的文件名称
         */
    	String imageFileName = imageFile.getName();
    	String ocrTxtName = "op_"+getName(imageFileName);//+System.currentTimeMillis();
    	
    	//输出的文件路径
        File txtFile = new File(imageFile.getParentFile(), ocrTxtName);
        List<String> cmd = new ArrayList<String>();
        // OCR程序路径
        cmd.add(tessPath);
        cmd.add("");
        cmd.add(txtFile.getName());
        // 语言选项
        cmd.add(LANG_OPTION);
        // 中文简体
        cmd.add("chi_sim");
        // 输出模式
        cmd.add(PAGESEGMODE_OPTION);  
        cmd.add("3");
        
        String content = "";
        
        try {
        	//构造命令行
            ProcessBuilder pb = new ProcessBuilder();
            //执行的目录
            pb.directory(imageFile.getParentFile());
            //待转换的图片jpg tif等
            cmd.set(1, imageFileName);  
            pb.command(cmd);
            //[D:\Prj\Tesseract-OCR\tesseract.exe, show.jpg, op_show.jpg, -l, chi_sim, -psm, 3]
            pb.redirectErrorStream(true);
            Process process = pb.start();
            // tesseract.exe 1.jpg 1 -l chi_sim  
            // Runtime.getRuntime().exec("tesseract.exe 22.jpg txt -l chi_sim");
            // 0代表正常退出
           
//            {"url":"D:\\Prj\\tesstemp\show.jpg"}
//            {"url":" file:///D://Prj//tesstemp//show.jpg"}
            //tesseract.exe show.jpg resu
//{"url":" http://img.blog.csdn.net/20130714093408843"}
            
            int w = process.waitFor();  
            if (w == 0)
            {
            	File file = new File(txtFile.getAbsolutePath() + ".txt");
            	content = readTxtFile(file);
            } else  
            {  
                String msg;  
                switch (w)  
                {  
                case 1:  
                    msg = "Errors accessing files. There may be spaces in your image's filename.";  
                    break;
                case 29:  
                    msg = "Cannot recognize the image or its selected region.";  
                    break;  
                case 31:  
                    msg = "Unsupported image format.";  
                    break;
                default:  
                    msg = "Errors occurred.";  
                }
                throw new RuntimeException(msg);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				//对临时文件进行删除
				new File(txtFile.getAbsolutePath() + ".txt").delete();
			}catch(Exception e){
			}
		}
        return content;
	}
	/**
	 * 读取txt内容
	 * @param file
	 * @return
	 */
    public static String readTxtFile(File file){
    	StringBuffer sbstr = new StringBuffer("");
        try {
            String encoding="utf-8";
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = "";
			while((lineTxt = bufferedReader.readLine()) != null){
				sbstr.append(lineTxt).append("\n");
            }
			bufferedReader.close();
            read.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return sbstr.toString();
    }
    /**
   	 * 根据一个文件路径获取名称，不包含扩展名
   	 * @param path   	文件的物理路径       
   	 * @return String        
   	 * @throws 
   	 */
   	public static String getName(String path){
   		if(path==null || path.length()==0)
       		return null;
   		if(path.equals("/")){
   			return path;
   		}
           int i=path.lastIndexOf("/");
           int j=path.lastIndexOf(".");
           if(i>=0)
               return path.substring(i+1 , j);
           else
               return path;
   	}
}
