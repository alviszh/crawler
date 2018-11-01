package com.text.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class STest {

	public static void main(String[] args) {
		/*int m = 201;
		for(int y = 1;y <= m; y += 200){
			System.out.println("y ="+y);
			if(m == 601){
				break;
			}
			m+=200;			
		}*/
		
		STest st = new STest();
		st.getJsonSize();
		
	}
	 
	public int getJsonSize(){
		File file = new File("C:/home/明细1.txt");
        String json = txt2String(file);
         Document doc = Jsoup.parse(json);
         Elements inputs = doc.select("[name^=pageUpList]");
         List<String> names = new ArrayList<>();
         List<String> values = new ArrayList<>();
		 for(Element input : inputs){
//			 System.out.println(input.attr("name")+":"+input.attr("value"));
			 names.add(input.attr("name"));
			 values.add(input.attr("value"));
		 }
		
		 System.out.println("name = "+URLEncoder.encode(names.get(0)));
		 System.out.println("name = "+names.get(1));
		 System.out.println("name = "+names.get(2));
		 System.out.println("name = "+names.get(3));
		 System.out.println("name = "+names.get(4));
		 
		 System.out.println("value = "+values.get(0));
		 System.out.println("value = "+values.get(1));
		 System.out.println("value = "+values.get(2));
		 System.out.println("value = "+values.get(3));
		 System.out.println("value = "+values.get(4));
		return 1;
	}

	
	/**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
    
}
