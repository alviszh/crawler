package app.parser;

import com.gargoylesoftware.htmlunit.html.*;
import com.microservice.dao.entity.crawler.insurance.dandong.InsuranceDandongUserInfo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class InsuranceDandongParser {

    public HtmlImage parseImg(HtmlPage page) throws IOException {
        DomElement yzm = page.getElementById("yzm");
        HtmlImage img = (HtmlImage) yzm.getNextElementSibling();

        String userHome = System.getProperty("user.home");
        File imgHomeFile = new File(userHome + File.separatorChar + "img");
        if (!imgHomeFile.exists()) {
            imgHomeFile.mkdirs();
        }
        img.saveAs(new File(imgHomeFile.getAbsolutePath() + File.separatorChar + UUID.randomUUID() + ".jpg"));
        return img;
    }

    public InsuranceDandongUserInfo parserUserData(HtmlPage htmlPage) {
        DomElement title = htmlPage.getElementsByTagName("title").get(0);
        String titleText = title.asText();
        if (titleText.indexOf("社保卡查询结果") == -1) {
            throw new RuntimeException("Insurance Dandong Verification Code Error!");
        }

        DomElement formElement = htmlPage.getElementById("zc1form");

        String formText = formElement.asText();

        if (formText.indexOf("姓名有误") != -1) {
            throw new RuntimeException("Insurance Dandong User Name Error!");
        }

        if (formText.indexOf("证件号有误") != -1) {
            throw new RuntimeException("Insurance Dandong User ID Card Num Error!");
        }

        String formHtml = formElement.asXml();

        InsuranceDandongUserInfo userInfo = new InsuranceDandongUserInfo();

        Pattern pattern = Pattern.compile("姓.*?名：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        Matcher matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setUserNama(matcher.group(1).trim());
        }

        pattern = Pattern.compile("身份证号：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setUserIdNum(matcher.group(1).trim());
        }

        pattern = Pattern.compile("参保状态：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setInsuranceStatus(matcher.group(1).trim());
        }

        pattern = Pattern.compile("照片状态：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setPhotoStatus(matcher.group(1).trim());
        }

        pattern = Pattern.compile("数据审验状态：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setDataStatus(matcher.group(1).trim());
        }

        pattern = Pattern.compile("照片信息：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setPhotoInfo(matcher.group(1).trim());
        }

        pattern = Pattern.compile("制卡状态：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setCardStatus(matcher.group(1).trim());
        }

        pattern = Pattern.compile("社保卡号：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setInsuranceCardNum(matcher.group(1).trim());
        }

        pattern = Pattern.compile("卡地址：[\\s\\S]*?<td[^>]*?>([^<]+?)</td>");
        matcher = pattern.matcher(formHtml);
        if (matcher.find()) {
            userInfo.setCardAddress(matcher.group(1).trim());
        }
        return userInfo;
    }

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        File imgHomeFile = new File(userHome + File.separatorChar + "img");
        if (!imgHomeFile.exists()) {
            imgHomeFile.mkdirs();
        }

        System.out.println(imgHomeFile.getAbsolutePath());
    }
}
