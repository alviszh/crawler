package test.pbccrc;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.IdEntity;
import com.microservice.dao.entity.crawler.pbccrc.CreditBaseInfo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/27.
 */
public class test1 {

    public static void main(String[] args) {
        List<String> matchResultList = null;
        String matchResultStr = "";
       /* String detailItem = "2015年7月21日中国邮政储蓄银行安徽合肥市分行发放的490,000元（人民币）个人住房贷款，2045年7月21日到期。" +
                "截至2016年9月，余额481,693。最近5年内有1个月处于逾期状态，没有发生过90天以上逾期。2016年8月已转出";

        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u8f6c\u51fa");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74", "-").replace("\u6708\u5df2\u8f6c\u51fa", "");
        }
        System.out.println(matchResultStr);*/

//        String detailItem = "2011年6月17日交通银行北京市分行发放的1,260,000元（人民币）个人商用房（包括商住两用）贷款，2015年5月已结清。最近5年内有1个月处于逾期状态，没有发生过90天以上逾期。";
        String detailItem = "2001年12月19日中国工商银行北京市分行发放的370,000元（人民币）个人住房贷款，2009年12月已结清。";
        matchResultList = getSubStringByRegex(detailItem, "\uff08(?!.*\u5143\uff08)[\u4e00-\u9fa5].*?\uff09");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\uff08|\uff09", "");
        }
        System.out.println(matchResultStr);

        matchResultList = getSubStringByRegex(detailItem, "\uff09\\D+?\uff0c");
        if (matchResultList.size()>0) {
            String  loanItem = matchResultList.get(0).replaceFirst("\uff09", "").replaceAll("\uff0c","");
            System.out.println(loanItem);
        }
    }

    public static List<String> getSubStringByRegex(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public void gson(){
        //忽悠字段：id、createtime（类：IdEntity）
        ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().equals("id") || fa.getName().equals("createtime"); // <---
            }
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

        };
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(myExclusionStrategy)
                .setPrettyPrinting().create();

        CreditBaseInfo creditBaseInfo = new CreditBaseInfo();
        creditBaseInfo.setMarital_status("已婚");
        creditBaseInfo.setQuery_time("11");
        creditBaseInfo.setMapping_id("121212");
        String json = gson.toJson(creditBaseInfo);
        System.out.println(json);
    }
}
