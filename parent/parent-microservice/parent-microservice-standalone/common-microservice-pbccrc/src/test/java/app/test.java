package app;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/8.
 */
public class test {

    public static void main(String[] args) throws ParseException {

        // yyyy是年，MM是月，dd是日，	HH是(24小时制)时，hh是(12小时制)时，mm是分，ss是秒
        Date date = test.StringToDate("", "yyyy-MM");
        System.out.println(date);

        String string = test.DateToString(date, "yyyy-MM-dd");
        System.out.println("++"+string+"==");
    }

        /**
         * 字符串转换为日期
         * @param string
         * @param pattern
         * @return
         * @throws ParseException
         */
    public static Date StringToDate(String string, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        if (!"".equals(string) && string !=null) {
            date = simpleDateFormat.parse(string);
        }
        return date;
    }

    /**
     * 日期转换为字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String DateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String string = "";
        if (date !=null) {
            string = simpleDateFormat.format(date);
        }
        return string;
    }

    public void parseFile() throws Exception {

        String html="";
        String encoding = "UTF-8";
        File file = new File("G:\\pbccrc.html");
        if (file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
// System.out.println(lineTxt);
                html+=lineTxt;
            }
            read.close();
            System.out.println("最终："+html);
//            html = StringEscapeUtils.unescapeJson(html);
            Document htmlDoc = Jsoup.parse(html);
            String result = htmlDoc.select("div[align=center]").toString();
            System.out.println("处理之后的html是："+ result);
        } else {
            System.out.println("找不到指定的文件");
        }
    }

}
