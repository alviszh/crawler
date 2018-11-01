package app.parser;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.*;
import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanBasicBean;
import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
public class InsuranceZhoushanParser<T extends InsuranceZhoushanBasicBean> {

    private String taskid;

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public HtmlImage parseImg(HtmlPage page) throws IOException {

        HtmlImage img = page.getFirstByXPath("//*[@id=\"text\"]/form/table/tbody/tr[3]/td[2]/img");

        String userHome = System.getProperty("user.home");
        File imgHomeFile = new File(userHome + File.separatorChar + "img");
        if (!imgHomeFile.exists()) {
            imgHomeFile.mkdirs();
        }
        img.saveAs(new File(imgHomeFile.getAbsolutePath() + File.separatorChar + UUID.randomUUID() + ".jpg"));
        return img;
    }

    public String parseQuerySQL(String jsonStr) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
        String pageGridParamsStr = jsonObject.getAsJsonObject("data").getAsJsonObject("hashMap")
                .get("pageGridParams").getAsString();
        pageGridParamsStr = pageGridParamsStr.replaceAll("\\\\\"", "\"");
        JsonObject pageGridParams = jsonParser.parse(pageGridParamsStr).getAsJsonArray().get(0).getAsJsonObject();
        return pageGridParams.get("querySQL").getAsString();
    }

    public List<T> parseInsurance(String dataJson, Class<? extends InsuranceZhoushanBasicBean> clazz) {
        try {
            List<T> list = new ArrayList<T>();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(dataJson).getAsJsonObject();
            JsonArray dataList = jsonObject.getAsJsonArray("data");
            Gson gson = new Gson();
            for (JsonElement jsonElement : dataList) {
                T insurance = (T) gson.fromJson(jsonElement, clazz);//对于javabean直接给出class实例
                insurance.setTaskid(this.taskid);
                list.add(insurance);
                System.out.println(insurance);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InsuranceZhoushanUserInfo> parseUserInfo(String dataJson) {
        try {

            List<InsuranceZhoushanUserInfo> userInfoList = new ArrayList<InsuranceZhoushanUserInfo>();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(dataJson).getAsJsonObject();
            JsonObject hashMap = jsonObject.getAsJsonObject("data").getAsJsonObject("hashMap");
            String div1 = hashMap.get("div_1").getAsString();
            String div2 = hashMap.get("div_2").getAsString();
            Gson gson = new Gson();
            System.out.println(div1);
            System.out.println(div2);
            JsonObject div1JsonObject = jsonParser.parse(div1).getAsJsonArray().get(0).getAsJsonObject();
            JsonArray div2JsonArray = jsonParser.parse(div2).getAsJsonArray();
            for (JsonElement jsonElement : div2JsonArray) {
                JsonObject div2Json = jsonElement.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : div2Json.entrySet()) {
                    div1JsonObject.addProperty(entry.getKey(), entry.getValue().getAsString());
                }
                InsuranceZhoushanUserInfo userInfo = gson.fromJson(div1JsonObject, InsuranceZhoushanUserInfo.class);
                userInfo.setTaskid(this.taskid);
                userInfoList.add(userInfo);
            }
            return userInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}