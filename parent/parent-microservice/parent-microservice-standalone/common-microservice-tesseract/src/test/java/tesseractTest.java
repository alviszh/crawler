import app.domain.ImageHandler;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/15.
 */
public class tesseractTest {
    public static void main(String[] args) throws IOException {
           File imageFile = new File("G:\\img\\f155be74-6c69-4edd-bd3c-e2bf1b1709bd.png");
           ImageHandler.imagePreHandle(imageFile, null);
           ITesseract instance = new Tesseract();
           instance.setDatapath("C:\\Program Files (x86)\\tessdata");
           // 默认是英文（识别字母和数字），如果要识别中文(数字 + 中文），需要制定语言包
//           instance.setLanguage("eng");
           instance.setLanguage("chi_sim");
        long start = System.currentTimeMillis();
           try{
                   String result = instance.doOCR(imageFile);
                   System.out.println(result);
               }catch(TesseractException e){
                   System.out.println(e.getMessage());
               }
        long end = System.currentTimeMillis();
        System.out.println((end-start)+":ms");
    }

}
