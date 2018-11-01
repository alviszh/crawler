package app.parser.pbccrc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zmy on 2018/1/2.
 */
public abstract class AbstractParser {
    public static List<String> getSubStringByRegex(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 获取两个日期的月份差
     * @param str1 (2016.5.6)
     * @param str2 (2036.6.6)
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String str1, String str2)
            throws ParseException {
        int result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(str1));
        aft.setTime(sdf.parse(str2));
        int month = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int year = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        result =month + year;
        return result == 0 ? 1 : Math.abs(result);
    }

    /**
     *  将日期格式 yyyy-MM 转化成 yyyy-MM-dd
     * @param dateStr
     * @return
     */
    public String toDateFormat(String dateStr, String start, String end){
        String result = "";
        try {
            Date date = StringToDate(dateStr, start);
            result = DateToString(date, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
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

    //字段对应字典值
    /** 征信数据信息 - 婚姻状况 */
    public String getMarriage(String marriage) {
        if (marriage == null)
            return "";
        if (marriage.equals("未婚")) {
            return "0";
        } else if (marriage.equals("已婚")) {
            return "1";
        } else if (marriage.equals("离异")) {
            return "2";
        } else if (marriage.equals("丧偶")) {
            return "3";
        } else if (marriage.equals("未知")) {
            return "4";
        } else {
            return marriage;
        }
    }

    /** 信贷记录信息 - 账户类型 */
    public String getCreditType(String creditType) {
        if (creditType == null) {
            return "";
        }
        if (creditType.equals("信用卡")) {
            return "1";
        } else if (creditType.equals("住房贷款")) {
            return "2";
        } else if (creditType.equals("其他贷款")) {
            return "3";
        } else {
            return creditType;
        }
    }
    /** 信用卡 - 账户状态 */
    public String getStatus(String status) {
        if (status == null) {
            return "";
        }
        if (status.equals("正常")) {
            return "1";
        } else if (status.equals("销户")) {
            return "2";
        } else if (status.equals("未激活")) {
            return "3";
        } else if (status.equals("止付")) {
            return "4";
        } else if (status.equals("冻结")) {
            return "5";
        } else if (status.equals("呆账")) {
            return "6";
        } else if (status.equals("逾期")) {
            return "7";
        } else {
            return status;
        }
    }
    /** 账户类型（币种） */
    public String getAccountType(String accountType) {
        if (accountType == null) {
            return "";
        }
        if (accountType.contains("人民币")) {
            return "1";
        } else if (accountType.contains("美元")) {
            return "2";
        } else if (accountType.contains("日元")) {
            return "3";
        } else if (accountType.contains("港元")) {
            return "4";
        } else if (accountType.contains("欧元")) {
            return "5";
        } else if (accountType.contains("英镑")) {
            return "6";
        } else if (accountType.contains("澳大利亚元")) {
            return "7";
        } else if (accountType.contains("加拿大元")) {
            return "8";
        } else if (accountType.contains("其他")) {
            return "9";
        } else if (accountType.contains("澳门元")) {
            return "10";
        } else if (accountType.contains("瑞士法郎")) {
            return "11";
        } else {
            return accountType;
        }
    }
    /** 信用卡类型 */
    public String getCardType(String cardType){
        if (cardType == null)
            return "";
        if (cardType.equals("贷记卡")) {
            return "1";
        } else if (cardType.equals("准贷记卡")) {
            return "2";
        } else {
            return cardType;
        }
    }
    /** 贷款明细  账户状态*/
    public String getLoanStatus(String status){
        if (status == null)
            return "";
        if (status.equals("结清")) {
            return "1";
        } else if (status.equals("逾期")) {
            return "2";
        } else if (status.equals("转出")) {
            return "3";
        } else if (status.equals("呆账")) {
            return "4";
        } else if (status.equals("正常")) {
            return "5";
        } else {
            return status;
        }
    }
    /** 贷款明细 贷款种类 */
    public String getLoanType(String loanType){
        if (loanType == null)
            return "";
        if (loanType.equals("个人住房贷款")) {
            return "0";
        } else if (loanType.equals("个人经营性贷款")) {
            return "1";
        } else if (loanType.equals("个人汽车贷款")) {
            return "2";
        } else if (loanType.equals("个人住房公积金贷款")) {
            return "3";
        } else if (loanType.contains("包括商住两用")) {//个人商住房（包括商住两用）贷款
            return "4";
        } else if (loanType.equals("农户贷款")) {
            return "5";
        } else if (loanType.equals("个人助学贷款")) {
            return "6";
        } else if (loanType.equals("个人消费贷款")) {
            return "7";
        } else if (loanType.equals("其他贷款")) {
            return "8";
        } else {
            return loanType;
        }
    }
    /** 查询信息 查询原因 */
    public String getQueryReason(String queryReason){
        if (queryReason == null)
            return "";
        if (queryReason.equals("信用卡审批")) {
            return "1";
        } else if (queryReason.equals("贷后管理")) {
            return "2";
        } else if (queryReason.equals("担保资格审批")) {
            return "3";
        } else if (queryReason.equals("贷款审批")) {
            return "4";
        } else if (queryReason.equals("异议核查")) {
            return "5";
        } else if (queryReason.equals("特约商户实名审查")) {
            return "6";
        } else if (queryReason.equals("本人查询")) {
            return "7";
        } else if (queryReason.equals("公积金提取复核")) {
            return "8";
        } else if (queryReason.equals("招商投标")) {
            return "9";
        } else if (queryReason.equals("身份信息在线更新")) {
            return "10";
        } else if (queryReason.equals("股指期货开户")) {
            return "11";
        } else if (queryReason.equals("保前审查")) {
            return "12";
        } else if (queryReason.equals("保后管理")) {
            return "13";
        } else if (queryReason.equals("业务审批")) {
            return "14";
        } else if (queryReason.contains("本人查询")) {
            if (queryReason.contains("临柜")) { //本人查询(临柜)
                return "15";
            } else if (queryReason.contains("互联网个人信用信息服务平台")) {//本人查询(互联网个人信用信息服务平台)
                return "16";
            } else {
                return queryReason;
            }
//        } else if (queryReason.equals("法人代表、负责人、高管等资信审查")) {
        } else if (queryReason.contains("资信审查")) {
            return "17";
        } else if (queryReason.equals("客户准入资格审查")) {
            return "18";
        } else if (queryReason.equals("融资审批")) {
            return "19";
        } else if (queryReason.equals("担保资格审查")) {
            return "20";
        } else {
            return queryReason;
        }
    }

    /** 0:否; 1:是 */
    public String parserFlag(String flag){
        if (flag == null)
            return "0";
        if (flag.equals("true")) {
            return "1";
        } else {
            return "0";
        }
    }

    public static void main(String[] args) {

            String dateStr = "2017-3-11";
            try {
                Date date = StringToDate(dateStr, "yyyy-MM-dd");
                String result = DateToString(date, "yyyy-MM-dd");
                System.out.println("result="+result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }
}
