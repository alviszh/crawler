package test.ocr.swagger;

public class OCRutil {
	
	/**
	 * 根据一个文件路径获取文件扩展名
	 * @param path   	可以是文件全名、文件相对位置、文件绝对位置   
	 * @return String 
	 * @throws 
	 */
	public static String getDocType(String path){
		if(path == null || path.length() ==0){
			return null;
		}
    	int charAt = path.lastIndexOf(".");
    	if(charAt > 0){
    		return path.substring(charAt+1, path.length());
    	}
    	return "";
        
	}

}
