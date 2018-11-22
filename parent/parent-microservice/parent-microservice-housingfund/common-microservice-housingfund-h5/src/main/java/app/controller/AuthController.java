package app.controller;

import app.commontracerlog.TracerLog;
import com.crawler.housingfund.json.TaskHousingfund;
import app.client.fund.HousingfundTaskClient;
import com.crawler.housingfund.json.HousingFundJsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/h5/fund")
public class AuthController {
    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    private TracerLog tracer;

    public static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private HousingfundTaskClient housingfundTaskClient;


    /**
     * 认证首页
     * @param model
     * @param themeColor
     * @param isTopHide
     * @return
     */
    @RequestMapping(value = {"","/auth"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String auth( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide) {
//        themeColor = "#" + themeColor;
        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        System.out.println("************* themeColor="+themeColor + ", isTopHide="+isTopHide+",topHide="+topHide);
        return "auth";
    }

    /**
     * 进入显示所有城市的页面
     * @param model
     * @param housingfundJsonBean
     * @param themeColor
     * @param topHide
     * @return
     */
    @RequestMapping(value = "/citys",  method = {RequestMethod.GET, RequestMethod.POST})
    public String citys(Model model, HousingFundJsonBean housingfundJsonBean,String themeColor, String topHide) {
        System.out.println("******citys******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        model.addAttribute("housingfundJsonBean", housingfundJsonBean);
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);
        return "citys";
    }

    @RequestMapping(value = "/housingfund",method = {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model,HousingFundJsonBean housingfundJsonBean ,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                       @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                       @RequestParam(name = "city") String city,
                       @RequestParam(name = "idnum") String idnum,
                       @RequestParam(name = "username") String username) {
        /*创建task*/
        TaskHousingfund taskHousingfund= housingfundTaskClient.createTask(housingfundJsonBean);
        tracer.addTag("taskid", taskHousingfund.getTaskid());
        /*TaskHousingfund taskHousingfund = new TaskHousingfund();
        taskHousingfund.setTaskid("cbb472a8-4dbd-4552-9308-18233baa8857");*/
        model.addAttribute("taskHousingfund", taskHousingfund);
        log.info("taskHousingfund==================>" +taskHousingfund);
        log.info("-----------公积金登录页面------------" + taskHousingfund.getTaskid());
//        themeColor = "#" + themeColor;
        model.addAttribute("taskId",taskHousingfund.getTaskid());
        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);
        model.addAttribute("city", city);
        model.addAttribute("idnum", idnum);
        model.addAttribute("name", username);
        System.out.println("******housingfund******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        if (city.equals("北京市")) {
            return "auth_beijing";
        } else if (city.equals("佛山市")) {
            return "auth_foshan";
        } else if (city.equals("重庆市")) {
            return "auth_chongqing";
        } else if (city.equals("南昌市")||city.equals("台州市")) {
            return "auth_nanchang";
        } else if (city.equals("石家庄市")) {
            return "auth_nanchang";
        } else if (city.equals("青岛市")) {
            return "auth_qingdao";
        } else if (city.equals("宁波市")||city.equals("牡丹江市")||city.equals("齐齐哈尔市")||city.equals("呼伦贝尔市")||city.equals("苏州市")||city.equals("广州市")||city.equals("淄博市")) {
            return "auth_ningbo";
        } else if (city.equals("合肥市")||city.equals("滨州市")||city.equals("红河哈尼族彝族自治州")||city.equals("株洲市")) {
            return "auth_hefei";
        } else if (city.equals("天津市")) {
            return "auth_tianjin";
        } else if (city.equals("无锡市")||city.equals("商丘市")) {
            return "auth_wuxi";
        } else if (city.equals("哈尔滨市")||city.equals("宜宾市")||city.equals("湖州市")) {
            return "auth_haerbin";
        } else if (city.equals("杭州市")) {
            return "auth_hangzhou";
        } else if (city.equals("沈阳市")) {
            return "auth_shenyang";
        } else if (city.equals("郑州市")||city.equals("海口市")||city.equals("楚雄市")||city.equals("德州市")||city.equals("呼和浩特市")||city.equals("沧州市")||city.equals("西安市")||city.equals("大理市")) {
            return "auth_zhengzhou";
        } else if (city.equals("贵阳市")) {
            return "auth_guiyang";
        } else if (city.equals("温州市")) {
            return "auth_wenzhou";
        } else if (city.equals("厦门市")) {
            return "auth_xiamen";
        } else if (city.equals("上海市")) {
            return "auth_shanghai";
        } else if (city.equals("成都市")) {
            return "auth_chengdu";
        } else if (city.equals("东莞市")||city.equals("曲靖市")) {
            return "auth_dongguan";
        } else if (city.equals("深圳市")||city.equals("徐州市")) {
            return "auth_shenzhen";
        } else if (city.equals("烟台市")||city.equals("玉溪市")||city.equals("乌海市")) {
            return "auth_yantai";
        } else if (city.equals("太原市")) {
            return "auth_taiyuan";
        } else if (city.equals("长春市")||city.equals("日照市")||city.equals("宝鸡市")||city.equals("绵阳市")||city.equals("聊城市")||city.equals("延边朝鲜族自治州")||city.equals("黑河市")||city.equals("盘锦市")||city.equals("衡水市")||city.equals("邢台市")||city.equals("济南市")||city.equals("南充市")||city.equals("义乌市")||city.equals("濮阳市")||city.equals("凉山彝族自治州")||city.equals("丹东市")||city.equals("丽水市")||city.equals("昆明市")||city.equals("临沂市")||city.equals("佳木斯市")||city.equals("宜春市")||city.equals("桂林市")||city.equals("柳州市")||city.equals("抚州市")||city.equals("赤峰市")||city.equals("吉林市")||city.equals("通化市")||city.equals("潍坊市")||city.equals("保定市")||city.equals("驻马店市")||city.equals("乐山市")||city.equals("榆林市")||city.equals("梧州市")||city.equals("焦作市")||city.equals("赣州市")||city.equals("贵港市")||city.equals("上饶市")||city.equals("义乌市")||city.equals("文山壮族苗族自治州")||city.equals("泸州市")||city.equals("遂宁市")||city.equals("济宁市")||city.equals("湛江市")||city.equals("蚌埠市")) {
            return "auth_changchun";
        } else if (city.equals("长沙市")||city.equals("泰安市")) {
            return "auth_changsha";
        } else if (city.equals("大庆市")||city.equals("白山市")||city.equals("襄阳市")||city.equals("酒泉市")) {
            return "auth_daqing";
        } else if (city.equals("南京市")) {
            return "auth_nanjing";
        } else if (city.equals("安庆市")||city.equals("金华市")||city.equals("宿迁市")) {
            return "auth_anqing";
        } else if (city.equals("大连市")) {
            return "auth_dalian";
        } else if (city.equals("铁岭市")) {
            return "auth_tieling";
        } else if (city.equals("洛阳市")||city.equals("大同市")) {
            return "auth_luoyang";
        } else if (city.equals("许昌市")||city.equals("长治市")) {
            return "auth_xuchang";
        } else if (city.equals("咸阳市")) {
            return "auth_xianyang";
        } else if (city.equals("延安市")||city.equals("四平市")) {
            return "auth_yanan";
        } else if (city.equals("新乡市")) {
            return "auth_xinxiang";
        } else if (city.equals("扬州市")) {
            return "auth_yangzhou";
        } else if (city.equals("威海市")) {
            return "auth_weihai";
        } else if (city.equals("济源市")) {
            return "auth_jiyuan";
        } else if (city.equals("随州市")||city.equals("肇庆市")) {
            return "auth_suizhou";
        } else if (city.equals("唐山市")) {
            return "auth_tangshan";
        } else if (city.equals("漯河市")) {
            return "auth_luohe";
        } else if (city.equals("通辽市")||city.equals("钦州市")||city.equals("西宁市")||city.equals("武汉市")) {
            return "auth_tongliao";
        } else if (city.equals("银川市")) {
            return "auth_yinchuan";
        } else if (city.equals("昭通市")) {
            return "auth_zhaotong";
        } else if (city.equals("枣庄市")) {
            return "auth_zaozhuang";
        } else if (city.equals("新余市")) {
            return "auth_xinyu";
        }
        else {
            return "default_login";
        }
    }

    /**
     * 跳转到采集成功页
     * @param model
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public String success( Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                           @RequestParam(name = "taskId") String taskId,
                           @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                           @RequestParam(name = "city") String city) {
        log.info("-----------数据采集成功------------" + taskId);
//        themeColor = "#" + themeColor;
        model.addAttribute("taskId",taskId);
        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        model.addAttribute("city",city);
        return "success";
    }

    @RequestMapping("/test")
    public String test(Model model){
        return "test";
    }
}
