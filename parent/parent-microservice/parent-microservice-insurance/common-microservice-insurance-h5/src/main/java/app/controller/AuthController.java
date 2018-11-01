package app.controller;

import app.commontracerlog.TracerLog;
import com.crawler.insurance.json.TaskInsurance;
import app.client.insur.InsurBeiJingClient;
import app.client.insur.InsuranceTaskClient;
import com.crawler.insurance.json.InsuranceJsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/h5/insur")
public class AuthController {
    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    private TracerLog tracer;

    public static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private InsuranceTaskClient insuranceTaskClient;

    @Autowired
    private InsurBeiJingClient insurBeiJingClient;

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
     * @param insuranceJsonBean
     * @param themeColor
     * @param topHide
     * @return
     */
    @RequestMapping(value = "/citys",  method = {RequestMethod.GET, RequestMethod.POST})
    public String citys(Model model, InsuranceJsonBean insuranceJsonBean,String themeColor, String topHide) {
        System.out.println("******citys******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        model.addAttribute("insuranceJsonBean", insuranceJsonBean);
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);
        /*创建task*/
        TaskInsurance taskInsurance = insuranceTaskClient.createTask(insuranceJsonBean);
        tracer.addTag("taskid", taskInsurance.getTaskid());
        /*TaskInsurance taskInsurance = new TaskInsurance();
        taskInsurance.setTaskid("cbb472a8-4dbd-4552-9308-18233baa8857");*/
        model.addAttribute("taskInsurance", taskInsurance);
        log.info("taskInsurance==================>" +taskInsurance);
        return "citys";
    }

    @RequestMapping(value = "/insurance",  method = {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                       @RequestParam(name = "taskId") String taskId,
                       @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                       @RequestParam(name = "city") String city,
                       @RequestParam(name = "idNum") String idNum,
                       @RequestParam(name = "name") String name) {
        log.info("-----------社保登录页面------------" + taskId);
//        themeColor = "#" + themeColor;
        model.addAttribute("taskId",taskId);
        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);
        model.addAttribute("city", city);
        model.addAttribute("idNum", idNum);
        model.addAttribute("name", name);
        System.out.println("******insurance******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        if (city.equals("北京市")) {
            return "auth_beijing";
        } else if (city.equals("上海市")) {
            return "auth_shanghai";
        } else if (city.equals("长春市")||city.equals("泰安市")||city.equals("滨州市")||city.equals("德州市")||city.equals("临沂市")||city.equals("兰州市")||city.equals("日照市")||city.equals("桂林市")||city.equals("内蒙古自治区")||city.equals("济南市")||city.equals("南宁市")||city.equals("贵港市")||city.equals("宿迁市")||city.equals("淮安市")) {
            return "auth_changchun";
        } else if (city.equals("深圳市")) {
            return "auth_shenzhen";
        } else if (city.equals("广州市")) {
            return "auth_guangzhou";
        } else if (city.equals("石家庄市")) {
            return "auth_shijiazhuang";
        } else if (city.equals("宁波市")) {
            return "auth_ningbo";
        } else if (city.equals("佛山市")) {
            return "auth_foshan";
        } else if (city.equals("成都市")) {
            return "auth_chengdu";
        } else if (city.equals("烟台市")) {
            return "auth_yantai";
        } else if (city.equals("青岛市")) {
            return "auth_qingdao";
        } else if (city.equals("淄博市")) {
            return "auth_zibo";
        } else if (city.equals("天津市")) {
            return "auth_tianjin";
        } else if (city.equals("东莞市")) {
            return "auth_dongguan";
        } else if (city.equals("重庆市")) {
            return "auth_chongqing";
        } else if (city.equals("苏州市")) {
            return "auth_suzhou";
        } else if (city.equals("沈阳市")) {
            return "auth_shenyang";
        } else if (city.equals("厦门市")) {
            return "auth_xiamen";
        } else if (city.equals("哈尔滨市")) {
            return "auth_haerbin";
        } else if (city.equals("温州市")||city.equals("浙江省")||city.equals("安徽省")) {
            return "auth_wenzhou";
        } else if (city.equals("郑州市")) {
            return "auth_zhengzhou";
        } else if (city.equals("大庆市")) {
            return "auth_daqing";
        } else if (city.equals("南昌市")||city.equals("南京市")||city.equals("商丘市")||city.equals("吉林省")||city.equals("湖北省")||city.equals("绵阳市")||city.equals("玉林市")||city.equals("绍兴市")||city.equals("扬州市")||city.equals("泰州市")||city.equals("福建省")) {
            return "auth_nanchang";
        } else if (city.equals("太原市")) {
            return "auth_taiyuan";
        } else if (city.equals("无锡市")) {
            return "auth_wuxi";
        } else if (city.equals("泉州市")) {
            return "auth_quanzhou";
        } else if (city.equals("贵阳市")) {
            return "auth_guiyang";
        } else if (city.equals("湖南省")) {
            return "auth_hunan";
        } else if (city.equals("合肥市")||city.equals("柳州市")||city.equals("湖州市")||city.equals("蚌埠市")) {
            return "auth_hefei";
        } else if (city.equals("武汉市")) {
            return "auth_wuhan";
        } else if (city.equals("杭州市")) {
            return "auth_hangzhou";
        } else if (city.equals("徐州市")) {
            return "auth_xuzhou";
        } else if (city.equals("衡水市")) {
            return "auth_hengshui";
        } else if (city.equals("铁岭市")||city.equals("珠海市")) {
            return "auth_tieling";
        } else if (city.equals("焦作市")||city.equals("济源市")||city.equals("洛阳市")) {
            return "auth_jiaozuo";
        } else if (city.equals("鞍山市")) {
            return "auth_anshan";
        } else if (city.equals("大连市")) {
            return "auth_dalian";
        } else if (city.equals("威海市")) {
            return "auth_weihai";
        } else if (city.equals("盘锦市")) {
            return "auth_panjin";
        } else if (city.equals("济宁市")) {
            return "auth_jining";
        } else if (city.equals("潍坊市")) {
            return "auth_weifang";
        } else if (city.equals("漯河市")||city.equals("四川省")||city.equals("凉山彝族自治州")||city.equals("海口市")) {
            return "auth_luohe";
        } else if (city.equals("常州市")) {
            return "auth_changzhou";
        }
        else if (city.equals("镇江市")) {
            return "auth_zhenjiang";
        }
        else if (city.equals("聊城市")) {
            return "auth_liaocheng";
        }
        else if (city.equals("内蒙古自治区")) {
            return "auth_neimenggu";
        }
        else if (city.equals("山西省")) {
            return "auth_shanxi";
        }
        else if (city.equals("陕西省")||city.equals("新疆维吾尔自治区")) {
            return "auth_shanxi3";
        }
        else if (city.equals("黑龙江省")) {
            return "auth_heilongjiang";
        }
        else if (city.equals("云南省")) {
            return "auth_yunnan";
        }
        else if (city.equals("泸州市")) {
            return "auth_luzhou";
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

    /**
     * 北京社保 （断路器）
     * @param model
     * @param themeColor
     * @param taskId
     * @param isTopHide
     * @param city
     * @return
     */
    @RequestMapping(value = "/beijing/hystrix",  method = {RequestMethod.GET, RequestMethod.POST})
    public String hystrix(Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                          @RequestParam(name = "taskId") String taskId,
                          @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                          @RequestParam(name = "city") String city) {
        log.info("-----------北京社保 断路器------------");
//        themeColor = "#" + themeColor;
        model.addAttribute("taskId",taskId);
        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);
        model.addAttribute("city", city);
        String result = insurBeiJingClient.hystrix();
        log.info("-----------hystrix------------" + result);
        if ("SUCCESS".equals(result)) {
            return "auth_beijing";
        }
        return "hystrix";
    }

    @RequestMapping("/test")
    public String test(Model model){
        return "test";
    }
}
