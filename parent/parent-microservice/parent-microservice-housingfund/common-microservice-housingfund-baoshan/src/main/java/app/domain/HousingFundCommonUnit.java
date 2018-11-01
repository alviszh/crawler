package app.domain;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zmy on 2018/5/16.
 */
@Component
public class HousingFundCommonUnit {
    public static String getPresentDate(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }
    public static String getTwoYearAgoDate(){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -2);
        c.add(Calendar.MONTH, 0);
        c.add(Calendar.DAY_OF_MONTH, 0);
        String yearAgo = f.format(c.getTime());
        return yearAgo;
    }
    public WebClient addcookie(TaskHousing taskHousing) {
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
        Iterator<Cookie> i = cookies.iterator();
        while (i.hasNext()) {
            webClient.getCookieManager().addCookie(i.next());
        }
        return webClient;
    }
    //摘要
    public static String getSummary(String summaryCode){
        String summary="汇缴";
        if(summaryCode.equals("1219")){
            summary="汇缴";
        }else if(summaryCode.equals("1001")){
            summary="单位开户";
        }else if(summaryCode.equals("1002")){
            summary="单位转移(转入)";
        }else if(summaryCode.equals("1003")){
            summary="单位转移(转出)";
        }else if(summaryCode.equals("1011")){
            summary="单位销户";
        }else if(summaryCode.equals("1012")){
            summary="零余额销户";
        }else if(summaryCode.equals("1118")){
            summary="个人开户";
        }else if(summaryCode.equals("1201")){
            summary="个人转移（转出）";
        }else if(summaryCode.equals("1202")){
            summary="个人转移(转入)";
        }else if(summaryCode.equals("1228")){
            summary="正常提取";
        }else if(summaryCode.equals("1231")){
            summary="结息";
        }else if(summaryCode.equals("1233")){
            summary="销户提取";
        }else if(summaryCode.equals("1234")){
            summary="异地转出";
        }else{
            summary="其他";
        }
        return summary;
    }
    //个人状态
    public static String getAccState(String code){
        String accState = code;
        if(code.equals("0")){
            accState="正常";
        }else if(code.equals("1")){
            accState="封存";
        }else if(code.equals("2")){
            accState="空账";
        }else if(code.equals("9")){
            accState="销户";
        }
        return accState;
    }

    //婚姻状况（未出现的先保存code）
    public static String getMarstatus(String code){
        String marstatus = code;
        if (code.equals("1")) {
            marstatus = "未婚";
        } else if (code.equals("2")) {
            marstatus = "已婚";
        } else if (code.equals("3")) {
            marstatus = "丧偶";
        } else if (code.equals("4")) {
            marstatus = "离婚";
        } else if (code.equals("9")) {
            marstatus = "未说明的婚姻状况";
        }
        return marstatus;
    }

    //职业（未出现的先保存code）
    public static String getOccupation(String code){
        String occupation = code;
        if (code.equals("1")) {
            occupation = "国家公务人员";
        } else if (code.equals("2")) {
            occupation = "专业技术人员";
        } else if (code.equals("3")) {
            occupation = "职员";
        } else if (code.equals("4")) {
            occupation = "企业管理人员";
        } else if (code.equals("5")) {
            occupation = "工人";
        } else if (code.equals("6")) {
            occupation = "农民";
        } else if (code.equals("7")) {
            occupation = "学生";
        } else if (code.equals("8")) {
            occupation = "现役军人";
        } else if (code.equals("9")) {
            occupation = "自由职业者";
        } else if (code.equals("A")) {
            occupation = "个体经营者";
        } else if (code.equals("B")) {
            occupation = "无业人员";
        } else if (code.equals("C")) {
            occupation = "退（离）休人员";
        } else if (code.equals("Z")) {
            occupation = "其他";
        }
        return occupation;
    }

    //是否已认证
    public static String getUserLevel(String code){
        String userLevel = code;
        if (code.equals("2")) {
            userLevel = "是";
        } else if (code.equals("1")) {
            userLevel = "否";
        }
        return userLevel;
    }

    //账户机构
    public static String getAccinstcode(String code){
        String accinstcode = code;
        if (code.equals("08750001")) {
            accinstcode = "隆阳管理部";
        } else if (code.equals("08750002")) {
            accinstcode = "腾冲县管理部";
        } else if (code.equals("08750003")) {
            accinstcode = "昌宁县管理部";
        } else if (code.equals("08750004")) {
            accinstcode = "龙陵县管理部";
        } else if (code.equals("08750005")) {
            accinstcode = "施甸县管理部";
        }
        return accinstcode;
    }

    //
    public static String getBankcode(String code){
        String bankcode = code;
        if (code.equals("0001")) {
            bankcode = "工行";
        } else if (code.equals("0002")) {
            bankcode = "建行";
        } else if (code.equals("0003")) {
            bankcode = "农行";
        } else if (code.equals("0004")) {
            bankcode = "中行";
        } else if (code.equals("0005")) {
            bankcode = "富滇";
        } else if (code.equals("0006")) {
            bankcode = "农信社";
        } else if (code.equals("0007")) {
            bankcode = "农发行";
        }
        return bankcode;
    }

    //是否签约（提取收款是否签约、个人托收是否签约）
    public static String getCheckid(String code){
        String userLevel = code;
        if (code.equals("1")) {
            userLevel = "是";
        } else if (code.equals("2")) {
            userLevel = "否";
        }
        return userLevel;
    }
}
