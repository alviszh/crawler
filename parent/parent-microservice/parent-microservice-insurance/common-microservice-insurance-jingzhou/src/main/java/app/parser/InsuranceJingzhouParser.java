package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsuranceJingzhouBasicBean;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouBaseInfo;


@Component
public class InsuranceJingzhouParser<T extends InsuranceJingzhouBasicBean> {

    private String taskid;

    public InsuranceJingzhouParser() {
    }

    public InsuranceJingzhouParser(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskid() {
        return taskid;
    }


    public String checkLoginStatus(String html) {
        Pattern pattern = Pattern.compile("<div.*?id=[\"|']user_func_menu[\"|'][^>]*?>([^<]+?)<");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return "SUCCESS___" + matcher.group(1).trim();
        } else {
            pattern = Pattern.compile("Base\\.alert\\(['|\"](.+?)['|\"]");
            matcher = pattern.matcher(html);
            if (matcher.find()) {
                return matcher.group(1).trim();
            } else {
                return "ERROR";
            }
        }
    }

    public String getCookies(CookieManager cookieManager) {

        StringBuffer cookieBuffer = new StringBuffer();
        Set<Cookie> cookies = cookieManager.getCookies();

        for (Cookie cookie : cookies) {
            String cookeStr = cookie.toString();
            String[] cookieArr = cookeStr.split(";");

            for (String cookieElement : cookieArr) {
                if (cookieElement.startsWith("JSESSIONID") || cookieElement.startsWith("POSTID")) {
                    cookieBuffer.append(cookieElement).append("; ");
                }
            }
        }
        return cookieBuffer.toString();
    }

    public List<T> parseInsurance(String dataJson, String jsonType, Class<? extends InsuranceJingzhouBasicBean> clazz) {
        List<T> list = new ArrayList<T>();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(dataJson).getAsJsonObject();
        JsonObject lists = jsonObject.getAsJsonObject("lists");
        JsonObject typeList = lists.getAsJsonObject("list" + jsonType);
        JsonArray dataList = typeList.getAsJsonArray("list");

        Gson gson = new Gson();
        for (JsonElement jsonElement : dataList) {
            T insurance = (T) gson.fromJson(jsonElement, clazz);//对于javabean直接给出class实例
            insurance.setTaskid(this.taskid);
            list.add(insurance);
        }
        return list;
    }

    public InsurancejingzhouBaseInfo parserUserInfo(String dataJson) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(dataJson).getAsJsonObject();
        JsonObject fieldData = jsonObject.getAsJsonObject("fieldData");
        Gson gson = new Gson();
        InsurancejingzhouBaseInfo baseInfo = gson.fromJson(fieldData, InsurancejingzhouBaseInfo.class);
        baseInfo.setTaskid(this.taskid);
        return baseInfo;
    }
}