package app.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */
public class NatureParser {

    private static String genderString = "[{\"id\":\"1\",\"name\":\"男性\",\"py\":\"nx\"},{\"id\":\"2\",\"name\":\"女性\",\"py\":\"nx\"},{\"id\":\"3\",\"name\":\"未说明的性别\",\"py\":\"wsmdxb\"}]";
    private static String nationString = "[{\"id\":\"01\",\"name\":\"汉族\",\"py\":\"hz\"},{\"id\":\"02\",\"name\":\"蒙古族\",\"py\":\"mgz\"},{\"id\":\"03\",\"name\":\"回族\",\"py\":\"hz\"},{\"id\":\"04\",\"name\":\"藏族\",\"py\":\"cz\"},{\"id\":\"05\",\"name\":\"维吾尔族\",\"py\":\"wwez\"},{\"id\":\"06\",\"name\":\"苗族\",\"py\":\"mz\"},{\"id\":\"07\",\"name\":\"彝族\",\"py\":\"yz\"},{\"id\":\"08\",\"name\":\"壮族\",\"py\":\"zz\"},{\"id\":\"09\",\"name\":\"布依族\",\"py\":\"byz\"},{\"id\":\"10\",\"name\":\"朝鲜族\",\"py\":\"cxz\"},{\"id\":\"11\",\"name\":\"满族\",\"py\":\"mz\"},{\"id\":\"12\",\"name\":\"侗族\",\"py\":\"dz\"},{\"id\":\"13\",\"name\":\"瑶族\",\"py\":\"yz\"},{\"id\":\"14\",\"name\":\"白族\",\"py\":\"bz\"},{\"id\":\"15\",\"name\":\"土家族\",\"py\":\"tjz\"},{\"id\":\"16\",\"name\":\"哈尼族\",\"py\":\"hnz\"},{\"id\":\"17\",\"name\":\"哈萨克族\",\"py\":\"hskz\"},{\"id\":\"18\",\"name\":\"傣族\",\"py\":\"dz\"},{\"id\":\"19\",\"name\":\"黎族\",\"py\":\"lz\"},{\"id\":\"20\",\"name\":\"傈傈族\",\"py\":\"llz\"},{\"id\":\"21\",\"name\":\"佤族\",\"py\":\"*z\"},{\"id\":\"22\",\"name\":\"畲族\",\"py\":\"*z\"},{\"id\":\"23\",\"name\":\"高山族\",\"py\":\"gsz\"},{\"id\":\"24\",\"name\":\"拉祜族\",\"py\":\"l*z\"},{\"id\":\"25\",\"name\":\"水族\",\"py\":\"sz\"},{\"id\":\"26\",\"name\":\"东乡族\",\"py\":\"dxz\"},{\"id\":\"27\",\"name\":\"纳西族\",\"py\":\"nxz\"},{\"id\":\"28\",\"name\":\"景颇族\",\"py\":\"jpz\"},{\"id\":\"29\",\"name\":\"柯尔克孜族\",\"py\":\"kekzz\"},{\"id\":\"30\",\"name\":\"土族\",\"py\":\"tz\"},{\"id\":\"31\",\"name\":\"达翰尔族\",\"py\":\"dhez\"},{\"id\":\"32\",\"name\":\"仫佬族\",\"py\":\"*lz\"},{\"id\":\"33\",\"name\":\"羌族\",\"py\":\"qz\"},{\"id\":\"34\",\"name\":\"布朗族\",\"py\":\"blz\"},{\"id\":\"35\",\"name\":\"撒拉族\",\"py\":\"slz\"},{\"id\":\"36\",\"name\":\"毛南族\",\"py\":\"mnz\"},{\"id\":\"37\",\"name\":\"仡佬族\",\"py\":\"*lz\"},{\"id\":\"38\",\"name\":\"锡伯族\",\"py\":\"xbz\"},{\"id\":\"39\",\"name\":\"阿昌族\",\"py\":\"acz\"},{\"id\":\"40\",\"name\":\"普米族\",\"py\":\"pmz\"},{\"id\":\"41\",\"name\":\"塔吉克族\",\"py\":\"tjkz\"},{\"id\":\"42\",\"name\":\"怒族\",\"py\":\"nz\"},{\"id\":\"43\",\"name\":\"乌孜别克族\",\"py\":\"wzbkz\"},{\"id\":\"44\",\"name\":\"俄罗斯族\",\"py\":\"elsz\"},{\"id\":\"45\",\"name\":\"鄂温克族\",\"py\":\"ewkz\"},{\"id\":\"46\",\"name\":\"德昂族\",\"py\":\"daz\"},{\"id\":\"47\",\"name\":\"保安族\",\"py\":\"baz\"},{\"id\":\"48\",\"name\":\"裕固族\",\"py\":\"ygz\"},{\"id\":\"49\",\"name\":\"京族\",\"py\":\"jz\"},{\"id\":\"50\",\"name\":\"塔塔尔族\",\"py\":\"ttez\"},{\"id\":\"51\",\"name\":\"独龙族\",\"py\":\"dlz\"},{\"id\":\"52\",\"name\":\"鄂伦春族\",\"py\":\"elcz\"},{\"id\":\"53\",\"name\":\"赫哲族\",\"py\":\"hzz\"},{\"id\":\"54\",\"name\":\"门巴族\",\"py\":\"mbz\"},{\"id\":\"55\",\"name\":\"珞巴族\",\"py\":\"*bz\"},{\"id\":\"56\",\"name\":\"基诺族\",\"py\":\"jnz\"},{\"id\":\"99\",\"name\":\"其他\",\"py\":\"qt\"}]";
    private static String staffStausString = "[{\"id\":\"1\",\"name\":\"在职\",\"py\":\"zz\"},{\"id\":\"2\",\"name\":\"退休\",\"py\":\"tx\"},{\"id\":\"3\",\"name\":\"死亡\",\"py\":\"sw\"},{\"id\":\"4\",\"name\":\"离休\",\"py\":\"lx\"},{\"id\":\"5\",\"name\":\"二乙\",\"py\":\"ey\"}]";
    private static String shenfenString = "[{\"id\":\"11\",\"name\":\"国家公务员\",\"py\":\"gjgwy\"},{\"id\":\"13\",\"name\":\"专业技术人员\",\"py\":\"zyjsry\"},{\"id\":\"17\",\"name\":\"职员\",\"py\":\"zy\"},{\"id\":\"21\",\"name\":\"企业管理人员\",\"py\":\"qyglry\"},{\"id\":\"24\",\"name\":\"工人\",\"py\":\"gr\"},{\"id\":\"27\",\"name\":\"农民\",\"py\":\"nm\"},{\"id\":\"31\",\"name\":\"学生\",\"py\":\"xs\"},{\"id\":\"37\",\"name\":\"现役军人\",\"py\":\"xyjr\"},{\"id\":\"51\",\"name\":\"自由职业者\",\"py\":\"zyzyz\"},{\"id\":\"54\",\"name\":\"个体经营者\",\"py\":\"gtjyz\"},{\"id\":\"70\",\"name\":\"无业人员\",\"py\":\"wyry\"},{\"id\":\"80\",\"name\":\"退(离)休人员\",\"py\":\"t(l)xry\"},{\"id\":\"90\",\"name\":\"其他\",\"py\":\"qt\"}]";
    private static String yonggongStyle = "[{\"id\":\"1\",\"name\":\"原固定职工\",\"py\":\"ygdzg\"},{\"id\":\"2\",\"name\":\"城镇合同制职工\",\"py\":\"czhtzzg\"},{\"id\":\"3\",\"name\":\"农民合同制工人\",\"py\":\"nmhtzgr\"},{\"id\":\"4\",\"name\":\"临时工\",\"py\":\"lsg\"},{\"id\":\"9\",\"name\":\"其他\",\"py\":\"qt\"}]";
    private static String peasantWorker = "[{\"id\":\"0\",\"name\":\"非农民工\",\"py\":\"fnmg\"},{\"id\":\"1\",\"name\":\"农民工\",\"py\":\"nmg\"}]";
    private static String insuranceType = "[{\"id\":\"03\",\"name\":\"基本医疗保险\",\"py\":\"jbylbx\"},{\"id\":\"04\",\"name\":\"工伤保险\",\"py\":\"gsbx\"},{\"id\":\"05\",\"name\":\"生育保险\",\"py\":\"sybx\"},{\"id\":\"06\",\"name\":\"意外伤害险\",\"py\":\"ywshx\"},{\"id\":\"07\",\"name\":\"大病医疗\",\"py\":\"dbyl\"},{\"id\":\"08\",\"name\":\"公务员医疗\",\"py\":\"gwyyl\"},{\"id\":\"11\",\"name\":\"城镇居民医疗\",\"py\":\"czjmyl\"}]";
    private static String payStatus = "[{\"id\":\"0\",\"name\":\"未参保\",\"py\":\"wcb\"},{\"id\":\"1\",\"name\":\"参保缴费\",\"py\":\"cbjf\"},{\"id\":\"2\",\"name\":\"暂停缴费(中断)\",\"py\":\"ztjf(zd)\"},{\"id\":\"3\",\"name\":\"终止缴费\",\"py\":\"zzjf\"},{\"id\":\"4\",\"name\":\"恢复缴费\",\"py\":\"hfjf\"}]";
    private static String workerPersonType = "[{\"id\":\"030\",\"name\":\"基本医疗保险普通人员\",\"py\":\"jbylbxptry\"},{\"id\":\"031\",\"name\":\"基本医疗保险离休二乙人员-特殊记账企业\",\"py\":\"jbylbxlxeyry-tsjzqy\"},{\"id\":\"032\",\"name\":\"基本医疗保险离休二乙人员-市属机关、市属特困企业\",\"py\":\"jbylbxlxeyry-ssjg*sstkqy\"},{\"id\":\"033\",\"name\":\"基本医疗保险离休二乙人员-省属特困企业\",\"py\":\"jbylbxlxeyry-sstkqy\"},{\"id\":\"040\",\"name\":\"工伤保险普通人员\",\"py\":\"gsbxptry\"},{\"id\":\"050\",\"name\":\"生育保险普通人员\",\"py\":\"sybxptry\"},{\"id\":\"060\",\"name\":\"意外伤害保险普通人员\",\"py\":\"ywshbxptry\"},{\"id\":\"070\",\"name\":\"大额医疗保险普通人员\",\"py\":\"deylbxptry\"},{\"id\":\"080\",\"name\":\"公务员补助普通人员\",\"py\":\"gwybzptry\"},{\"id\":\"110\",\"name\":\"城居医疗保险普通人员\",\"py\":\"cjylbxptry\"}]";
    private static String juminPersonType = "[{\"id\":\"101\",\"name\":\"普通成年人\",\"py\":\"ptcnr\"},{\"id\":\"102\",\"name\":\"成年重残\",\"py\":\"cnzc\"},{\"id\":\"103\",\"name\":\"成年低保\",\"py\":\"cndb\"},{\"id\":\"104\",\"name\":\"成年低收入\",\"py\":\"cndsr\"},{\"id\":\"105\",\"name\":\"普通未成年人\",\"py\":\"ptwcnr\"},{\"id\":\"106\",\"name\":\"未成年重残\",\"py\":\"wcnzc\"},{\"id\":\"107\",\"name\":\"未成年低保\",\"py\":\"wcndb\"},{\"id\":\"108\",\"name\":\"未成年无财政补助\",\"py\":\"wcnwczbz\"},{\"id\":\"201\",\"name\":\"部属高校普通\",\"py\":\"bsgxpt\"},{\"id\":\"202\",\"name\":\"部属高校低保\",\"py\":\"bsgxdb\"},{\"id\":\"203\",\"name\":\"部属高校重残\",\"py\":\"bsgxzc\"},{\"id\":\"204\",\"name\":\"省属高校普通\",\"py\":\"ssgxpt\"},{\"id\":\"205\",\"name\":\"省属高校低保\",\"py\":\"ssgxdb\"},{\"id\":\"206\",\"name\":\"省属高校重残\",\"py\":\"ssgxzc\"},{\"id\":\"207\",\"name\":\"市属高校普通\",\"py\":\"ssgxpt\"},{\"id\":\"208\",\"name\":\"市属高校低保\",\"py\":\"ssgxdb\"},{\"id\":\"209\",\"name\":\"市属高校重残\",\"py\":\"ssgxzc\"},{\"id\":\"210\",\"name\":\"部属民办普通\",\"py\":\"bsmbpt\"},{\"id\":\"211\",\"name\":\"部属民办低保\",\"py\":\"bsmbdb\"},{\"id\":\"212\",\"name\":\"部属民办重残\",\"py\":\"bsmbzc\"},{\"id\":\"213\",\"name\":\"省属民办普通\",\"py\":\"ssmbpt\"},{\"id\":\"214\",\"name\":\"省属民办低保\",\"py\":\"ssmbdb\"},{\"id\":\"215\",\"name\":\"省属民办重残\",\"py\":\"ssmbzc\"},{\"id\":\"216\",\"name\":\"市属民办普通\",\"py\":\"ssmbpt\"},{\"id\":\"217\",\"name\":\"市属民办低保 \",\"py\":\"ssmbdb \"},{\"id\":\"218\",\"name\":\"市属民办重残\",\"py\":\"ssmbzc\"}]";
    private static String payType = "[{\"id\":\"01\",\"name\":\"正常缴费\",\"py\":\"zcjf\"},{\"id\":\"02\",\"name\":\"补缴\",\"py\":\"bj\"},{\"id\":\"03\",\"name\":\"个体缴费\",\"py\":\"gtjf\"},{\"id\":\"04\",\"name\":\"补收\",\"py\":\"bs\"},{\"id\":\"05\",\"name\":\"基数补差\",\"py\":\"jsbc\"},{\"id\":\"07\",\"name\":\"补年限\",\"py\":\"bnx\"},{\"id\":\"08\",\"name\":\"退休补划帐户\",\"py\":\"txbhzh\"},{\"id\":\"09\",\"name\":\"特权补收\",\"py\":\"tqbs\"},{\"id\":\"10\",\"name\":\"个人账户特殊划入\",\"py\":\"grzhtshr\"},{\"id\":\"11\",\"name\":\"划入大额医疗\",\"py\":\"hrdeyl\"},{\"id\":\"12\",\"name\":\"离休缺口补收\",\"py\":\"lxqkbs\"},{\"id\":\"13\",\"name\":\"重复缴费退款划账户\",\"py\":\"zfjftkhzh\"},{\"id\":\"14\",\"name\":\"退款\",\"py\":\"tk\"},{\"id\":\"50\",\"name\":\"07年前风险金\",\"py\":\"07nqfxj\"}]";
    private static String peopleStatus = "[{\"id\":\"1\",\"name\":\"在职\",\"py\":\"zz\"},{\"id\":\"2\",\"name\":\"退休\",\"py\":\"tx\"},{\"id\":\"3\",\"name\":\"死亡\",\"py\":\"sw\"},{\"id\":\"4\",\"name\":\"离休\",\"py\":\"lx\"},{\"id\":\"5\",\"name\":\"二乙\",\"py\":\"ey\"}]";

    private static List<JSONObject> genderJson = JSON.parseArray(genderString).toJavaList(JSONObject.class);
    private static List<JSONObject> nationJson = JSON.parseArray(nationString).toJavaList(JSONObject.class);
    private static List<JSONObject> staffStausJson = JSON.parseArray(staffStausString).toJavaList(JSONObject.class);
    private static List<JSONObject> shenfenJson = JSON.parseArray(shenfenString).toJavaList(JSONObject.class);
    private static List<JSONObject> yonggongJson = JSON.parseArray(yonggongStyle).toJavaList(JSONObject.class);
    private static List<JSONObject> peasantJson = JSON.parseArray(peasantWorker).toJavaList(JSONObject.class);
    private static List<JSONObject> insuranceTypeJson = JSON.parseArray(insuranceType).toJavaList(JSONObject.class);
    private static List<JSONObject> payStatusJson = JSON.parseArray(payStatus).toJavaList(JSONObject.class);
    private static List<JSONObject> workerPersonTypeJson = JSON.parseArray(workerPersonType).toJavaList(JSONObject.class);
    private static List<JSONObject> juminPersonTypeJson = JSON.parseArray(juminPersonType).toJavaList(JSONObject.class);
    private static List<JSONObject> payTypeJson = JSON.parseArray(payType).toJavaList(JSONObject.class);
    private static List<JSONObject> peopleStatusJson = JSON.parseArray(peopleStatus).toJavaList(JSONObject.class);


    public static void main(String[] args) {
        System.out.println(genderJson);
    }

    //获取性别
    public static String getGender(String genderId){
        if(genderId == null)
            return null;
        for(JSONObject json:genderJson){
            if(json.getString("id").equals(genderId))
                return json.getString("name");
        }
        return genderId;
    }
    //获取国籍
    public static String getNation(String nationId){
        if(nationId == null)
            return null;
        for(JSONObject json:nationJson){
            if(json.getString("id").equals(nationId))
                return json.getString("name");
        }
        return nationId;
    }
    //获取人员状态
    public static String getStaffStaus(String statusId){
        if(statusId == null)
            return null;
        for(JSONObject json:staffStausJson){
            if(json.getString("id").equals(statusId))
                return json.getString("name");
        }
        return statusId;
    }
    //获取身份
    public static String getShenFen(String shenfenId){
        if(shenfenId == null)
            return null;
        for(JSONObject json:shenfenJson){
            if(json.getString("id").equals(shenfenId))
                return json.getString("name");
        }
        return shenfenId;
    }
    //获取用工形式
    public static String getYongGong(String yonggongId){
        if(yonggongId == null)
            return null;
        for(JSONObject json:yonggongJson){
            if(json.getString("id").equals(yonggongId))
                return json.getString("name");
        }
        return yonggongId;
    }
    //是否农民工
    public static String getPeasant(String peasantId){
        if(peasantId == null)
            return null;
        for(JSONObject json:peasantJson){
            if(json.getString("id").equals(peasantId))
                return json.getString("name");
        }
        return peasantId;
    }

    //获取保险类型
    public static String getInsuranceType(String insuranceTypeId){
        if(insuranceTypeId == null)
            return null;
        for(JSONObject json:insuranceTypeJson){
            if(json.getString("id").equals(insuranceTypeId))
                return json.getString("name");
        }
        return insuranceTypeId;
    }
    //获取人员缴费状态
    public static String getPayStatus(String payStatusId){
        if(payStatusId == null)
            return null;
        for(JSONObject json:payStatusJson){
            if(json.getString("id").equals(payStatusId))
                return json.getString("name");
        }
        return payStatusId;
    }
    //获取参保人类别(职工)
    public static String getWorkerPersonType(String workerPersonTypeId){
        if(workerPersonTypeId == null)
            return null;
        for(JSONObject json:workerPersonTypeJson){
            if(json.getString("id").equals(workerPersonTypeId))
                return json.getString("name");
        }
        return workerPersonTypeId;
    }
    //获取参保人类别(居民)
    public static String getJuminPersonType(String juminPersonTypeId){
        if(juminPersonTypeId == null)
            return null;
        for(JSONObject json:juminPersonTypeJson){
            if(json.getString("id").equals(juminPersonTypeId))
                return json.getString("name");
        }
        return juminPersonTypeId;
    }
    //获取缴费类型
    public static String getPayType(String payTypeId){
        if(payTypeId == null)
            return null;
        for(JSONObject json:payTypeJson){
            if(json.getString("id").equals(payTypeId))
                return json.getString("name");
        }
        return payTypeId;
    }
    //获取人员状态
    public static String getPeopleStatus(String peopleStatusId){
        if(peopleStatusId == null)
            return null;
        for(JSONObject json:peopleStatusJson){
            if(json.getString("id").equals(peopleStatusId))
                return json.getString("name");
        }
        return peopleStatusId;
    }
}
